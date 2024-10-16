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
package fr.paris.lutece.plugins.identitystore.modules.test.identitystore.service.search;

import fr.paris.lutece.plugins.identitystore.modules.test.IdentityStoreJsonDataTestCase;
import fr.paris.lutece.plugins.identitystore.modules.test.IdentityStoreTestContext;
import fr.paris.lutece.plugins.identitystore.modules.test.data.TestDefinition;
import fr.paris.lutece.plugins.identitystore.modules.test.data.TestIdentity;
import fr.paris.lutece.plugins.identitystore.service.contract.ServiceContractNotFoundException;
import fr.paris.lutece.plugins.identitystore.service.identity.IdentityService;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class IdentitySearchRuleTest extends IdentityStoreJsonDataTestCase
{

    @Override
    protected String getTestDataPath( )
    {
        return "data/searchRules";
    }

    @Override
    protected void beforeTest() throws Exception {
        this.clearSearchRulesInBDD();
    }

    @Override
    protected List<TestIdentity> runDefinition(final TestDefinition testDefinition ) throws Exception
    {
        System.out.println( "----- Execute search request -----" );
        Thread.sleep( 1000 );
        final IdentitySearchResponse identitySearchResponse = new IdentitySearchResponse( );
        try
        {
            IdentityService.instance( ).search( this.toIdentitySearchRequest( testDefinition.getSearchRequest( ), true ), this.getAuthor( ), identitySearchResponse,
                    IdentityStoreTestContext.SAMPLE_APPCODE );
            System.out.println( "Response: " + identitySearchResponse );
        }
        catch( ServiceContractNotFoundException e )
        {
            throw new RuntimeException( e );
        }
        return identitySearchResponse.getIdentities( ).stream( ).map( this::toTestIdentity ).collect( Collectors.toList( ) );
    }

    protected void clearSearchRulesInBDD( ) throws Exception
    {
        /* Clean BDD tables */
        System.out.println( "----- Truncate search rules tables -----" );
        final DataSource ds = getDataSource( );
        try (final Connection connection = ds.getConnection()) {
            final String sql = "truncate table identitystore_identity_search_rule_attribute, idstore.public.identitystore_identity_search_rule";
            System.out.println("[Request]\n" + sql + "\n");
            connection.prepareStatement( sql ).execute( );
        }
    }
}
