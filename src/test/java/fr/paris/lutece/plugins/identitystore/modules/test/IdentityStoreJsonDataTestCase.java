/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.identitystore.modules.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.paris.lutece.plugins.identitystore.business.rules.duplicate.DuplicateRule;
import fr.paris.lutece.plugins.identitystore.business.rules.duplicate.DuplicateRuleAttributeTreatment;
import fr.paris.lutece.plugins.identitystore.modules.test.data.IdentityStoreTest;
import fr.paris.lutece.plugins.identitystore.modules.test.data.TestAttribute;
import fr.paris.lutece.plugins.identitystore.modules.test.data.TestDefinition;
import fr.paris.lutece.plugins.identitystore.modules.test.data.TestDuplicateRule;
import fr.paris.lutece.plugins.identitystore.modules.test.data.TestIdentity;
import fr.paris.lutece.plugins.identitystore.modules.test.util.FileNameAlphanumericComparator;
import fr.paris.lutece.plugins.identitystore.modules.test.util.StringAlphanumericComparator;
import fr.paris.lutece.plugins.identitystore.service.attribute.IdentityAttributeService;
import fr.paris.lutece.plugins.identitystore.service.identity.IdentityAttributeNotFoundException;
import fr.paris.lutece.plugins.identitystore.service.identity.IdentityService;
import fr.paris.lutece.plugins.identitystore.service.indexer.elastic.index.service.IdentityIndexer;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AttributeDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AttributeTreatmentType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.crud.IdentityChangeResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.SearchAttribute;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.SearchDto;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class IdentityStoreJsonDataTestCase extends IdentityStoreBDDAndESTestCase
{
    private static final String PROTOCOL = "http://";
    protected final Map<String, Pair<Boolean, String>> results = new HashMap<>( );
    protected final Set<File> testDefinitions = new HashSet<>( );
    protected final ObjectMapper mapper = new ObjectMapper( )
            .enable( SerializationFeature.INDENT_OUTPUT )
            .disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
    protected final List<String> inputsFilter = Arrays.stream(System.getProperty("inputs", "").split(",")).filter(StringUtils::isNotEmpty).collect(Collectors.toList());

    @Override
    protected void preInitApplication( ) throws Exception
    {
        final Path inputsPath = Paths.get( basePath, this.getTestDataPath( ), "definition" );
        final Path configPath = Paths.get( this.getTestDataPath( ), "config/identitystore.properties" );
        this.propertiesTemplatePath = configPath.toString( );

        final PathMatcher jsonMatcher = FileSystems.getDefault( ).getPathMatcher( "glob:**/*.json" );
        Files.walkFileTree( inputsPath, new SimpleFileVisitor<Path>( )
        {
            @Override
            public FileVisitResult visitFile( Path path, BasicFileAttributes attrs )
            {
                if ( !Files.isDirectory( path ) && jsonMatcher.matches( path ) && (inputsFilter.isEmpty() || inputsFilter.contains(path.getFileName().toString())) )
                {
                    testDefinitions.add( path.toFile( ) );
                }
                return FileVisitResult.CONTINUE;
            }
        } );
    }

    protected abstract String getTestDataPath( );

    public void test( ) throws Exception {
        this.beforeTest();
        if ( !testDefinitions.isEmpty() )
        {
            testDefinitions.stream( ).sorted( FileNameAlphanumericComparator.createStringComparator( ) ).forEach(file -> {
                try
                {
                    final IdentityStoreTest identityStoreTest = mapper.readValue( file, IdentityStoreTest.class );
                    if ( identityStoreTest != null && identityStoreTest.getTestDefinition() != null )
                    {
                        System.out.println( "----------------------------------------------------------------------" );
                        System.out.println( "----- Running test definition: " + identityStoreTest.getTestDefinition().getName( ) + " -----" );
                        System.out.println( "----------------------------------------------------------------------" );
                        System.out.println();
                        System.out.println( "Description: " + identityStoreTest.getTestDefinition().getDescription( )  );
                        System.out.println();
                        System.out.println( "----- Init test data -----" );
                        identityStoreTest.getTestDefinition().getInputs( ).stream( )
                            .map( testIdentity -> new ImmutablePair<>( testIdentity.getName( ), this.toIdentityChangeRequest( testIdentity ) ) ).forEach(pair -> {
                                try
                                {
                                    System.out.println( "[Create identity " + pair.getLeft() + "]" );
                                    System.out.println(pair.getRight().getIdentity().getAttributes().stream().map(a -> a.getKey() + "=" + a.getValue( ) ).collect( Collectors.joining( ", " ) ));
                                    final IdentityChangeResponse response = new IdentityChangeResponse( );
                                    IdentityService.instance( ).create( pair.getRight( ), this.getAuthor( ), IdentityStoreTestContext.SAMPLE_APPCODE, response );
                                    System.out.println("Status: " + response.getStatus().getHttpCode() + " - " + response.getStatus().getType());
                                    System.out.println("CUID: " + response.getCustomerId());
                                    System.out.println("Message: " + response.getStatus().getMessage());
                                    System.out.println();
                                }
                                catch( IdentityStoreException e )
                                {
                                    throw new RuntimeException( e );
                                }
                            } );
                        final List<TestIdentity> result = this.runDefinition(identityStoreTest.getTestDefinition());
                        results.put( identityStoreTest.getTestDefinition().getName( ), this.getTestResult( result, identityStoreTest.getTestDefinition() ) );
                        System.out.println();
                        System.out.println( "----- Clear test data -----" );
                        this.clearData( );
                        System.out.println();
                    }
                    else
                    {
                        throw new RuntimeException( "ERROR " + file.getName( ) + " : JSON is empty or malformed" );
                    }
                }
                catch( Exception e )
                {
                    throw new RuntimeException( e );
                }
            } );
            System.out.println( "----- Global test Results -----" );
            results.keySet( ).stream( ).sorted(StringAlphanumericComparator.createStringComparator()).forEach( this::displayResult );
            assertTrue( results.values( ).stream( ).allMatch( Pair::getLeft ) );
        }
    }

    protected abstract void beforeTest() throws Exception;

    private void displayResult( final String result ) {
        final String trace = result + " :: " + (results.get(result).getLeft() ? "OK" : "KO") + "\n" + results.get(result).getRight() + "\n";
        System.out.println(trace);
    }

    protected Pair<Boolean, String> getTestResult( final List<TestIdentity> results, final TestDefinition testDefinition )
    {
        // Transform expected identities names to TestIdentity objects
        final List<TestIdentity> expectedTestIdentities = testDefinition.getExpected().stream()
                .map(expected -> testDefinition.getInputs().stream().filter(input -> input.getName().equals(expected)).findFirst().orElse(null))
                .filter(Objects::nonNull)
                .peek( testIdentity -> testIdentity.getAttributes( ).sort( Comparator.comparing( TestAttribute::getKey ) ) )
                .collect(Collectors.toList());

        // Get result names from inputs and sort attributes by key name
        testDefinition.getInputs( ).forEach( testIdentity -> testIdentity.getAttributes( ).sort( Comparator.comparing( TestAttribute::getKey ) ) );
        results.forEach( testIdentity -> {
            testIdentity.getAttributes( ).sort( Comparator.comparing( TestAttribute::getKey ) );
            testDefinition.getInputs( ).stream( ).filter( input -> input.equals( testIdentity ) ).forEach( input -> testIdentity.setName( input.getName( ) ) );
        } );

        // Build result message to compare inputs and expected results
        final StringBuilder message = new StringBuilder("Liste des identités testées : " + testDefinition.getInputs( ).stream( ).map( TestIdentity::getName ).collect(Collectors.joining(", ")));
        message.append("\nListe des identités attendues pour la réussite du test : ")
                .append(String.join(", ", testDefinition.getExpected()))
                .append("\nListe des identités retournées par le test : ")
                .append(results.stream().map(result -> this.getNameFromInputs(result, testDefinition.getInputs())).sorted().collect(Collectors.joining(", ")));

        // If there is more results than expected by the test definition, calculate how much more there is
        if ( results.size( ) > testDefinition.getExpected( ).size( ) )
        {
            results.removeAll( expectedTestIdentities );
            message.append("\nLe résultat du test contient ").append(results.size()).append(" identité(s) de plus que la liste des identités attendues pour la réussite du test.");
            return new MutablePair<>( false, message.toString() );
        }
        else
            // If there is less, calculate how much
            if ( results.size( ) < testDefinition.getExpected( ).size( ) )
            {
                expectedTestIdentities.removeAll( results );
                message.append("\nLa liste des identités attendues pour la réussite du test contient ").append(expectedTestIdentities.size()).append(" identité(s) qui n'ont pas été retournée(s) par le test: ").append( expectedTestIdentities.stream( ).map( TestIdentity::getName ).sorted( ).collect( Collectors.joining( ", " ) ) );
                return new MutablePair<>( false, message.toString() );
            }
            else
            { // Finally, if the sizes are equal, check if the lists are the same (there can be differences)
                final List<TestIdentity> expectedCopy = new ArrayList<>( expectedTestIdentities );
                final List<TestIdentity> resultCopy = new ArrayList<>( results );
                expectedTestIdentities.removeAll( results );
                if ( expectedTestIdentities.isEmpty( ) ) // means that the two lists contains the same identities, our test is OK then :)
                {
                    message.append("\nLe résultat du test correspond à la liste des identités attendues pour la réussite du test.");
                    return new MutablePair<>( true, message.toString() );
                }
                else // Same size but different identities, our test is KO :(
                {
                    message.append("\nLa liste des identités attendues pour la réussite du test contient ").append(expectedTestIdentities.size()).append(" identité(s) qui n'ont pas été retournée(s) par le test: ").append( expectedTestIdentities.stream( ).map( TestIdentity::getName ).sorted( ).collect( Collectors.joining( ", " ) ) );
                    resultCopy.removeAll( expectedCopy );
                    if ( !resultCopy.isEmpty( ) )
                    {
                        message.append("\nLe résultat du test contient ").append(resultCopy.size()).append(" identité(s) non définie(s) dans la liste des identités attendues pour la réussite du test.");
                    }
                    return new MutablePair<>( false, message.toString() );
                }
            }
    }

    private String getNameFromInputs( final TestIdentity result, final List<TestIdentity> inputs ) {
        return inputs.stream().filter(expected -> expected.equals(result)).map(TestIdentity::getName).findFirst().orElse("not found");
    }

    protected abstract List<TestIdentity> runDefinition(TestDefinition testDefinition ) throws Exception;

    protected void clearData( ) throws Exception
    {
        /* Clean BDD tables */
        System.out.println( "----- Truncate BDD tables -----" );
        final DataSource ds = getDataSource( );
        try ( final Connection connection = ds.getConnection() ) {
            final String sql = "truncate table identitystore_identity, identitystore_identity_history, identitystore_identity_attribute, identitystore_identity_attribute_certificate, identitystore_identity_attribute_history, identitystore_index_action;";
            System.out.println("[Request]\n" + sql + "\n");
            connection.prepareStatement(sql).executeUpdate( );
        }

        /* Clean ES index */
        System.out.println( "----- Delete ES Index -----" );
        final IdentityIndexer identityIndexer = new IdentityIndexer( PROTOCOL + elasticsearchContainer.getHttpHostAddress( ) );
        final String indexBehindAlias = identityIndexer.getIndexBehindAlias( CURRENT_INDEX_ALIAS );
        identityIndexer.deleteIndex( indexBehindAlias );
        identityIndexer.initIndex( CURRENT_INDEX );
        identityIndexer.addAliasOnIndex( CURRENT_INDEX, CURRENT_INDEX_ALIAS );
    }

    protected TestIdentity toTestIdentity( final IdentityDto identityDto )
    {
        final TestIdentity testIdentity = new TestIdentity( );
        testIdentity.setConnectionId( identityDto.getConnectionId( ) );
        testIdentity.setCustomerId( identityDto.getCustomerId( ) );
        identityDto.getAttributes( ).forEach( certifiedAttribute -> {
            TestAttribute testAttribute = new TestAttribute( );
            testIdentity.getAttributes( ).add( testAttribute );
            testAttribute.setCertificationDate( certifiedAttribute.getCertificationDate( ) );
            testAttribute.setCertifier( certifiedAttribute.getCertifier( ) );
            testAttribute.setType( certifiedAttribute.getType( ) );
            testAttribute.setKey( certifiedAttribute.getKey( ) );
            testAttribute.setValue( certifiedAttribute.getValue( ) );
            testAttribute.setCertificationLevel( certifiedAttribute.getCertificationLevel( ) );

        } );
        return testIdentity;
    }

    protected IdentityDto toIdentityDto( final TestIdentity testIdentity ) {
        final IdentityDto identity = new IdentityDto( );
        identity.setConnectionId( testIdentity.getConnectionId( ) );
        identity.setCustomerId( testIdentity.getCustomerId( ) );
        testIdentity.getAttributes( ).forEach( testAttribute -> {
            final AttributeDto certifiedAttribute = new AttributeDto( );
            identity.getAttributes( ).add( certifiedAttribute );
            certifiedAttribute.setValue( testAttribute.getValue( ) );
            certifiedAttribute.setKey( testAttribute.getKey( ) );
            certifiedAttribute.setCertificationDate( testAttribute.getCertificationDate( ) );
            certifiedAttribute.setCertifier( testAttribute.getCertifier( ) );
        } );
        return identity;
    }

    protected IdentityChangeRequest toIdentityChangeRequest(final TestIdentity testIdentity )
    {
        final IdentityChangeRequest identityChangeRequest = new IdentityChangeRequest( );
        identityChangeRequest.setIdentity( this.toIdentityDto(testIdentity) );
        return identityChangeRequest;
    }

    protected DuplicateRule toDuplicateRule(final TestDuplicateRule testDuplicateRule ) {
        final DuplicateRule duplicateRule = new DuplicateRule();
        duplicateRule.setName( testDuplicateRule.getName( ) );
        duplicateRule.setCode( testDuplicateRule.getCode() );
        duplicateRule.setDescription( testDuplicateRule.getName() );
        duplicateRule.setDaemon( testDuplicateRule.isDaemon() );
        duplicateRule.setActive( testDuplicateRule.isActive() );
        duplicateRule.setDetectionLimit( -1 );
        duplicateRule.setNbEqualAttributes( testDuplicateRule.getNbEqualAttributes( ) );
        duplicateRule.setNbFilledAttributes( testDuplicateRule.getNbFilledAttributes( ) );
        duplicateRule.setNbMissingAttributes( testDuplicateRule.getNbMissingAttributes( ) );
        duplicateRule.setCheckedAttributes(testDuplicateRule.getCheckedAttributes().stream().map(key -> {
            try {
                return IdentityAttributeService.instance().getAttributeKey(key);
            } catch (IdentityAttributeNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        duplicateRule.setAttributeTreatments(testDuplicateRule.getAttributeTreatments().stream().map(treatment -> {
            final DuplicateRuleAttributeTreatment attributeTreatment = new DuplicateRuleAttributeTreatment( );
            attributeTreatment.setAttributes(treatment.getAttributeKeys().stream().map(key -> {
                try {
                    return IdentityAttributeService.instance().getAttributeKey(key);
                } catch (IdentityAttributeNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList()));
            attributeTreatment.setType(AttributeTreatmentType.valueOf(treatment.getType()));
            return attributeTreatment;

        }).collect(Collectors.toList()));

        return duplicateRule;
    }

    protected IdentitySearchRequest toIdentitySearchRequest(final TestIdentity testIdentity, final boolean withSearchType )
    {
        final IdentitySearchRequest identitySearchRequest = new IdentitySearchRequest( );
        final SearchDto search = new SearchDto( );
        search.setAttributes( new ArrayList<>( ) );
        identitySearchRequest.setSearch( search );
        testIdentity.getAttributes( ).forEach( testAttribute -> {
            final SearchAttribute searchAttributeDto = new SearchAttribute( );
            search.getAttributes( ).add( searchAttributeDto );
            searchAttributeDto.setKey( testAttribute.getKey( ) );
            searchAttributeDto.setValue( testAttribute.getValue( ) );
            if( withSearchType && testAttribute.getSearchType() != null )
            {
                searchAttributeDto.setTreatmentType(testAttribute.getSearchType());
            }
            else
            {
                final boolean strict = !StringUtils.equalsAny( testAttribute.getKey( ), "first_name", "family_name", "preferred_username" );
                searchAttributeDto.setTreatmentType( strict ? AttributeTreatmentType.STRICT : AttributeTreatmentType.APPROXIMATED );
            }
        } );
        return identitySearchRequest;
    }
}
