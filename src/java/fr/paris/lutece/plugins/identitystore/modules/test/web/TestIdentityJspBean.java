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
 
package fr.paris.lutece.plugins.identitystore.modules.test.web;

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.html.AbstractPaginator;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.identitystore.modules.test.business.TestIdentity;
import fr.paris.lutece.plugins.identitystore.modules.test.business.TestIdentityHome;

/**
 * This class provides the user interface to manage TestIdentity features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageTestIdentitys.jsp", controllerPath = "jsp/admin/plugins/identitystore/modules/test/", right = "IDENTITYSTORE_TEST_MANAGEMENT" )
public class TestIdentityJspBean extends AbstractManageTesterJspBean <Integer, TestIdentity>
{
    // Templates
    private static final String TEMPLATE_MANAGE_TESTIDENTITYS = "/admin/plugins/identitystore/modules/test/manage_testidentitys.html";
    private static final String TEMPLATE_CREATE_TESTIDENTITY = "/admin/plugins/identitystore/modules/test/create_testidentity.html";
    private static final String TEMPLATE_MODIFY_TESTIDENTITY = "/admin/plugins/identitystore/modules/test/modify_testidentity.html";

    // Parameters
    private static final String PARAMETER_ID_TESTIDENTITY = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TESTIDENTITYS = "module.identitystore.test.manage_testidentitys.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TESTIDENTITY = "module.identitystore.test.modify_testidentity.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TESTIDENTITY = "module.identitystore.test.create_testidentity.pageTitle";

    // Markers
    private static final String MARK_TESTIDENTITY_LIST = "testidentity_list";
    private static final String MARK_TESTIDENTITY = "testidentity";

    private static final String JSP_MANAGE_TESTIDENTITYS = "jsp/admin/plugins/identitystore/modules/test/ManageTestIdentitys.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_TESTIDENTITY = "module.identitystore.test.message.confirmRemoveTestIdentity";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.identitystore.test.model.entity.testidentity.attribute.";

    // Views
    private static final String VIEW_MANAGE_TESTIDENTITYS = "manageTestIdentitys";
    private static final String VIEW_CREATE_TESTIDENTITY = "createTestIdentity";
    private static final String VIEW_MODIFY_TESTIDENTITY = "modifyTestIdentity";

    // Actions
    private static final String ACTION_CREATE_TESTIDENTITY = "createTestIdentity";
    private static final String ACTION_MODIFY_TESTIDENTITY = "modifyTestIdentity";
    private static final String ACTION_REMOVE_TESTIDENTITY = "removeTestIdentity";
    private static final String ACTION_CONFIRM_REMOVE_TESTIDENTITY = "confirmRemoveTestIdentity";

    // Infos
    private static final String INFO_TESTIDENTITY_CREATED = "module.identitystore.test.info.testidentity.created";
    private static final String INFO_TESTIDENTITY_UPDATED = "module.identitystore.test.info.testidentity.updated";
    private static final String INFO_TESTIDENTITY_REMOVED = "module.identitystore.test.info.testidentity.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private TestIdentity _testidentity;
    private List<Integer> _listIdTestIdentitys;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_TESTIDENTITYS, defaultView = true )
    public String getManageTestIdentitys( HttpServletRequest request )
    {
        _testidentity = null;
        
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX) == null || _listIdTestIdentitys.isEmpty( ) )
        {
        	_listIdTestIdentitys = TestIdentityHome.getIdTestIdentitysList(  );
        }
        
        Map<String, Object> model = getPaginatedListModel( request, MARK_TESTIDENTITY_LIST, _listIdTestIdentitys, JSP_MANAGE_TESTIDENTITYS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_TESTIDENTITYS, TEMPLATE_MANAGE_TESTIDENTITYS, model );
    }

	/**
     * Get Items from Ids list
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
	@Override
	List<TestIdentity> getItemsFromIds( List<Integer> listIds ) 
	{
		List<TestIdentity> listTestIdentity = TestIdentityHome.getTestIdentitysListByIds( listIds );
		
		// keep original order
        return listTestIdentity.stream()
                 .sorted(Comparator.comparingInt( notif -> listIds.indexOf( notif.getId())))
                 .collect(Collectors.toList());
	}
    
    /**
    * reset the _listIdTestIdentitys list
    */
    public void resetListId( )
    {
    	_listIdTestIdentitys = new ArrayList<>( );
    }

    /**
     * Returns the form to create a testidentity
     *
     * @param request The Http request
     * @return the html code of the testidentity form
     */
    @View( VIEW_CREATE_TESTIDENTITY )
    public String getCreateTestIdentity( HttpServletRequest request )
    {
        _testidentity = ( _testidentity != null ) ? _testidentity : new TestIdentity(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_TESTIDENTITY, _testidentity );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_TESTIDENTITY ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_TESTIDENTITY, TEMPLATE_CREATE_TESTIDENTITY, model );
    }

    /**
     * Process the data capture form of a new testidentity
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_TESTIDENTITY )
    public String doCreateTestIdentity( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _testidentity, request, getLocale( ) );
        

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_TESTIDENTITY ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _testidentity, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_TESTIDENTITY );
        }

        TestIdentityHome.create( _testidentity );
        addInfo( INFO_TESTIDENTITY_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTIDENTITYS );
    }

    /**
     * Manages the removal form of a testidentity whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_TESTIDENTITY )
    public String getConfirmRemoveTestIdentity( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTIDENTITY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_TESTIDENTITY ) );
        url.addParameter( PARAMETER_ID_TESTIDENTITY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TESTIDENTITY, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a testidentity
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage testidentitys
     */
    @Action( ACTION_REMOVE_TESTIDENTITY )
    public String doRemoveTestIdentity( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTIDENTITY ) );
        
        
        TestIdentityHome.remove( nId );
        addInfo( INFO_TESTIDENTITY_REMOVED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTIDENTITYS );
    }

    /**
     * Returns the form to update info about a testidentity
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_TESTIDENTITY )
    public String getModifyTestIdentity( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTIDENTITY ) );

        if ( _testidentity == null || ( _testidentity.getId(  ) != nId ) )
        {
            Optional<TestIdentity> optTestIdentity = TestIdentityHome.findByPrimaryKey( nId );
            _testidentity = optTestIdentity.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }


        Map<String, Object> model = getModel(  );
        model.put( MARK_TESTIDENTITY, _testidentity );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_TESTIDENTITY ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_TESTIDENTITY, TEMPLATE_MODIFY_TESTIDENTITY, model );
    }

    /**
     * Process the change form of a testidentity
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_TESTIDENTITY )
    public String doModifyTestIdentity( HttpServletRequest request ) throws AccessDeniedException
    {   
        populate( _testidentity, request, getLocale( ) );
		
		
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_TESTIDENTITY ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _testidentity, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_TESTIDENTITY, PARAMETER_ID_TESTIDENTITY, _testidentity.getId( ) );
        }

        TestIdentityHome.update( _testidentity );
        addInfo( INFO_TESTIDENTITY_UPDATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTIDENTITYS );
    }
}
