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

package fr.paris.lutece.plugins.identitystore.modules.test.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;
import java.util.Optional;

/**
 * This class provides instances management methods (create, find, ...) for TestIdentity objects
 */
public final class TestIdentityHome
{
    // Static variable pointed at the DAO instance
    private static ITestIdentityDAO _dao = SpringContextService.getBean( "identitystore-test.testIdentityDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "identitystore-test" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private TestIdentityHome( )
    {
    }

    /**
     * Create an instance of the testIdentity class
     * 
     * @param testIdentity
     *            The instance of the TestIdentity which contains the informations to store
     * @return The instance of testIdentity which has been created with its primary key.
     */
    public static TestIdentity create( TestIdentity testIdentity )
    {
        _dao.insert( testIdentity, _plugin );

        return testIdentity;
    }

    /**
     * Update of the testIdentity which is specified in parameter
     * 
     * @param testIdentity
     *            The instance of the TestIdentity which contains the data to store
     * @return The instance of the testIdentity which has been updated
     */
    public static TestIdentity update( TestIdentity testIdentity )
    {
        _dao.store( testIdentity, _plugin );

        return testIdentity;
    }

    /**
     * Remove the testIdentity whose identifier is specified in parameter
     * 
     * @param nKey
     *            The testIdentity Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a testIdentity whose identifier is specified in parameter
     * 
     * @param nKey
     *            The testIdentity primary key
     * @return an instance of TestIdentity
     */
    public static Optional<TestIdentity> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the testIdentity objects and returns them as a list
     * 
     * @return the list which contains the data of all the testIdentity objects
     */
    public static List<TestIdentity> getTestIdentitysList( )
    {
        return _dao.selectTestIdentitysList( _plugin );
    }

    /**
     * Load the id of all the testIdentity objects and returns them as a list
     * 
     * @return the list which contains the id of all the testIdentity objects
     */
    public static List<Integer> getIdTestIdentitysList( )
    {
        return _dao.selectIdTestIdentitysList( _plugin );
    }

    /**
     * Load the data of all the testIdentity objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the testIdentity objects
     */
    public static ReferenceList getTestIdentitysReferenceList( )
    {
        return _dao.selectTestIdentitysReferenceList( _plugin );
    }

    /**
     * Load the data of all the avant objects and returns them as a list
     * 
     * @param listIds
     *            liste of ids
     * @return the list which contains the data of all the avant objects
     */
    public static List<TestIdentity> getTestIdentitysListByIds( List<Integer> listIds )
    {
        return _dao.selectTestIdentitysListByIds( _plugin, listIds );
    }

}
