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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.paris.lutece.plugins.identitystore.service.indexer.elastic.index.service.IdentityIndexer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import javax.sql.DataSource;

public abstract class IdentityStoreBDDAndESTestCase extends AbstractIdentityStoreTestCase
{
    protected final String CURRENT_INDEX_ALIAS = "identities-alias";
    protected final String CURRENT_INDEX = "identities-index";

    protected ElasticsearchContainer elasticsearchContainer;

    public PostgreSQLContainer postgreSQLContainer;

    protected HikariDataSource dataSource;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void startContainers( )
    {
        elasticsearchContainer = new ElasticsearchContainer(
                "docker.elastic.co/elasticsearch/elasticsearch:".concat( IdentityStoreTestContext.ELASTICSEARCH_VERSION ) )
                        .withEnv( "xpack.security.enabled", "false" ).withNetworkAliases( "localhost" ).withReuse( false );
        elasticsearchContainer.start( );
        this.esUrl = elasticsearchContainer.getHttpHostAddress( );

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer( "postgres:".concat( IdentityStoreTestContext.POSTGRES_VERSION ) )
                .withDatabaseName( "idstore" ).withUsername( "idstore" ).withPassword( "idstore" ).withInitScript( "db/init.sql" ).withReuse( false );
        postgreSQLContainer.start( );
        this.dbUrl = postgreSQLContainer.getJdbcUrl( );
    }

    @Override
    protected void shutDownContainers( )
    {
        if ( elasticsearchContainer != null && elasticsearchContainer.isRunning( ) )
        {
            elasticsearchContainer.stop( );
            elasticsearchContainer = null;
        }

        if ( postgreSQLContainer != null && postgreSQLContainer.isRunning( ) )
        {
            postgreSQLContainer.stop( );
            postgreSQLContainer = null;
        }
    }

    @Override
    protected void preInitApplication( ) throws Exception
    {
        // do nothing
    }

    @Override
    protected void postInitApplication( ) throws Exception
    {
        // Init elastic index
        final IdentityIndexer identityIndexer = new IdentityIndexer( "http://" + this.esUrl );
        identityIndexer.initIndex( CURRENT_INDEX );
        identityIndexer.addAliasOnIndex( CURRENT_INDEX, CURRENT_INDEX_ALIAS );
    }

    protected DataSource getDataSource()
    {
        if(dataSource == null)
        {
            final HikariConfig hikariConfig = new HikariConfig( );
            hikariConfig.setJdbcUrl( postgreSQLContainer.getJdbcUrl( ) );
            hikariConfig.setUsername( postgreSQLContainer.getUsername( ) );
            hikariConfig.setPassword( postgreSQLContainer.getPassword( ) );
            hikariConfig.setDriverClassName( postgreSQLContainer.getDriverClassName( ) );
            hikariConfig.setMaximumPoolSize(1000);
            hikariConfig.setMinimumIdle(10);
            dataSource = new HikariDataSource(hikariConfig);
        }
        return dataSource;
    }
}
