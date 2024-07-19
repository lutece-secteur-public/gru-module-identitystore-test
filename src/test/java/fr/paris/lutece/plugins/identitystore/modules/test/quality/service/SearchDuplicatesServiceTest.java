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
package fr.paris.lutece.plugins.identitystore.modules.test.quality.service;

import fr.paris.lutece.plugins.identitystore.business.rules.duplicate.DuplicateRule;
import fr.paris.lutece.plugins.identitystore.modules.quality.service.SearchDuplicatesService;
import fr.paris.lutece.plugins.identitystore.modules.test.IdentityStoreJsonDataTestCase;
import fr.paris.lutece.plugins.identitystore.modules.test.data.TestDefinition;
import fr.paris.lutece.plugins.identitystore.modules.test.data.TestIdentity;
import fr.paris.lutece.plugins.identitystore.service.duplicate.DuplicateRuleService;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.ResponseStatusType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.DuplicateSearchResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchDuplicatesServiceTest extends IdentityStoreJsonDataTestCase
{

    @Override
    protected String getTestDataPath( )
    {
        return "data/duplicates";
    }

    @Override
    protected void beforeTest() {

    }

    @Override
    protected List<TestIdentity> runDefinition(final TestDefinition testDefinition ) throws Exception
    {
        System.out.println( "[Create duplicate rule " + testDefinition.getDuplicateRule().getCode() + "]" );
        System.out.println("Checked attributes: " + String.join(", ", testDefinition.getDuplicateRule().getCheckedAttributes()));
        System.out.println("Number of filled attributes in the tested identity: " + testDefinition.getDuplicateRule().getNbFilledAttributes());
        System.out.println("Number of attributes that must be equal in the result identities: " + testDefinition.getDuplicateRule().getNbEqualAttributes());
        System.out.println("Number of attributes that can be absent in the result identities: " + testDefinition.getDuplicateRule().getNbMissingAttributes());
        testDefinition.getDuplicateRule().getAttributeTreatments().forEach(attributeTreatment -> {
            System.out.println( "Attribute treatment: " + attributeTreatment.getType() + " - " + String.join("", attributeTreatment.getAttributeKeys()));
        });
        final DuplicateRule duplicateRule = DuplicateRuleService.instance().create(this.toDuplicateRule(testDefinition.getDuplicateRule()));
        if(duplicateRule == null) {
            throw new RuntimeException("Could not create duplicate rule " + testDefinition.getDuplicateRule( ).getName( ) );
        }
        System.out.println();
        System.out.println( "----- Execute duplicate search request -----" );
        System.out.println( "\n[Duplicate search attributes]\n" + testDefinition.getSearchRequest().getAttributes().stream().map(a -> a.getKey() + "=" + a.getValue( ) ).collect( Collectors.joining( ", " ) ) );
        Thread.sleep( 1000 );
        final DuplicateSearchResponse response = SearchDuplicatesService.instance( ).findDuplicates( this.toIdentityDto(testDefinition.getSearchRequest()), Collections.singletonList(testDefinition.getDuplicateRule().getCode()) , Collections.emptyList() );
        System.out.println("\n[Duplicate search response status]\n " + response.getStatus().getHttpCode() + " - " + response.getStatus().getType().name() + " - " + response.getStatus().getMessage( ) );
        if( ResponseStatusType.OK == response.getStatus().getType() )
        {
            System.out.println("\n[Duplicate search response identities]\n" + response.getIdentities().stream().map(identityDto -> identityDto.getCustomerId() + " - " + identityDto.getAttributes().stream().map(attribute -> attribute.getKey() + "=" + attribute.getValue( )).collect(Collectors.joining(", "))).collect(Collectors.joining("\n")));
            System.out.println("\n[Duplicate search response metadata]\n" + response.getMetadata().entrySet().stream().map(entry -> entry.getKey() + " -> " + entry.getValue( ) ).collect( Collectors.joining( "\n" ) ) );
        }
        return response.getIdentities( ).stream( ).map( this::toTestIdentity ).collect( Collectors.toList( ) );
    }
}
