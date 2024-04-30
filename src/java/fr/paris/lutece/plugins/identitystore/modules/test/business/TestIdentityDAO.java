/*
 * Copyright (c) 2002-2023, City of Paris
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


package fr.paris.lutece.plugins.identitystore.modules.test.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class provides Data Access methods for TestIdentity objects
 */
public final class TestIdentityDAO implements ITestIdentityDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_test_identity, name, customer_id, connection_id FROM identitystore_test_test_identity WHERE id_test_identity = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO identitystore_test_test_identity ( name, customer_id, connection_id ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM identitystore_test_test_identity WHERE id_test_identity = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE identitystore_test_test_identity SET name = ?, customer_id = ?, connection_id = ? WHERE id_test_identity = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_test_identity, name, customer_id, connection_id FROM identitystore_test_test_identity";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_test_identity FROM identitystore_test_test_identity";
    private static final String SQL_QUERY_SELECTALL_BY_IDS = "SELECT id_test_identity, name, customer_id, connection_id FROM identitystore_test_test_identity WHERE id_test_identity IN (  ";

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( TestIdentity testIdentity, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++ , testIdentity.getName( ) );
            daoUtil.setString( nIndex++ , testIdentity.getCustomerId( ) );
            daoUtil.setString( nIndex++ , testIdentity.getConnectionId( ) );
            
            daoUtil.executeUpdate( );
            if ( daoUtil.nextGeneratedKey( ) ) 
            {
                testIdentity.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<TestIdentity> load( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	        TestIdentity testIdentity = null;
	
	        if ( daoUtil.next( ) )
	        {
	            testIdentity = new TestIdentity();
	            int nIndex = 1;
	            
	            testIdentity.setId( daoUtil.getInt( nIndex++ ) );
			    testIdentity.setName( daoUtil.getString( nIndex++ ) );
			    testIdentity.setCustomerId( daoUtil.getString( nIndex++ ) );
			    testIdentity.setConnectionId( daoUtil.getString( nIndex ) );
	        }
	
	        return Optional.ofNullable( testIdentity );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( TestIdentity testIdentity, Plugin plugin )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
	        int nIndex = 1;
	        
            	daoUtil.setString( nIndex++ , testIdentity.getName( ) );
            	daoUtil.setString( nIndex++ , testIdentity.getCustomerId( ) );
            	daoUtil.setString( nIndex++ , testIdentity.getConnectionId( ) );
	        daoUtil.setInt( nIndex , testIdentity.getId( ) );
	
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<TestIdentity> selectTestIdentitysList( Plugin plugin )
    {
        List<TestIdentity> testIdentityList = new ArrayList<>(  );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            TestIdentity testIdentity = new TestIdentity(  );
	            int nIndex = 1;
	            
	            testIdentity.setId( daoUtil.getInt( nIndex++ ) );
			    testIdentity.setName( daoUtil.getString( nIndex++ ) );
			    testIdentity.setCustomerId( daoUtil.getString( nIndex++ ) );
			    testIdentity.setConnectionId( daoUtil.getString( nIndex ) );
	
	            testIdentityList.add( testIdentity );
	        }
	
	        return testIdentityList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdTestIdentitysList( Plugin plugin )
    {
        List<Integer> testIdentityList = new ArrayList<>( );
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            testIdentityList.add( daoUtil.getInt( 1 ) );
	        }
	
	        return testIdentityList;
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectTestIdentitysReferenceList( Plugin plugin )
    {
        ReferenceList testIdentityList = new ReferenceList();
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            testIdentityList.addItem( daoUtil.getInt( 1 ) , daoUtil.getString( 2 ) );
	        }
	
	        return testIdentityList;
    	}
    }
    
    /**
     * {@inheritDoc }
     */
	@Override
	public List<TestIdentity> selectTestIdentitysListByIds( Plugin plugin, List<Integer> listIds ) {
		List<TestIdentity> testIdentityList = new ArrayList<>(  );
		
		StringBuilder builder = new StringBuilder( );

		if ( !listIds.isEmpty( ) )
		{
			for( int i = 0 ; i < listIds.size(); i++ ) {
			    builder.append( "?," );
			}
	
			String placeHolders =  builder.deleteCharAt( builder.length( ) -1 ).toString( );
			String stmt = SQL_QUERY_SELECTALL_BY_IDS + placeHolders + ")";
			
			
	        try ( DAOUtil daoUtil = new DAOUtil( stmt, plugin ) )
	        {
	        	int index = 1;
				for( Integer n : listIds ) {
					daoUtil.setInt(  index++, n ); 
				}
	        	
	        	daoUtil.executeQuery(  );
	        	while ( daoUtil.next(  ) )
		        {
		        	TestIdentity testIdentity = new TestIdentity(  );
		            int nIndex = 1;
		            
		            testIdentity.setId( daoUtil.getInt( nIndex++ ) );
				    testIdentity.setName( daoUtil.getString( nIndex++ ) );
				    testIdentity.setCustomerId( daoUtil.getString( nIndex++ ) );
				    testIdentity.setConnectionId( daoUtil.getString( nIndex ) );
		            
		            testIdentityList.add( testIdentity );
		        }
		
		        daoUtil.free( );
		        
	        }
	    }
		return testIdentityList;
		
	}
}
