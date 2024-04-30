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
import fr.paris.lutece.plugins.identitystore.modules.test.business.TestIdentityAttribute;
import fr.paris.lutece.plugins.identitystore.modules.test.business.TestIdentityAttributeHome;

/**
 * This class provides the user interface to manage TestIdentityAttribute features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageTestIdentityAttributes.jsp", controllerPath = "jsp/admin/plugins/identitystore/modules/test/", right = "IDENTITYSTORE_TEST_MANAGEMENT" )
public class TestIdentityAttributeJspBean extends AbstractManageTesterJspBean <Integer, TestIdentityAttribute>
{
    // Templates
    private static final String TEMPLATE_MANAGE_TESTIDENTITYATTRIBUTES = "/admin/plugins/identitystore/modules/test/manage_testidentityattributes.html";
    private static final String TEMPLATE_CREATE_TESTIDENTITYATTRIBUTE = "/admin/plugins/identitystore/modules/test/create_testidentityattribute.html";
    private static final String TEMPLATE_MODIFY_TESTIDENTITYATTRIBUTE = "/admin/plugins/identitystore/modules/test/modify_testidentityattribute.html";

    // Parameters
    private static final String PARAMETER_ID_TESTIDENTITYATTRIBUTE = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TESTIDENTITYATTRIBUTES = "module.identitystore.test.manage_testidentityattributes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TESTIDENTITYATTRIBUTE = "module.identitystore.test.modify_testidentityattribute.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TESTIDENTITYATTRIBUTE = "module.identitystore.test.create_testidentityattribute.pageTitle";

    // Markers
    private static final String MARK_TESTIDENTITYATTRIBUTE_LIST = "testidentityattribute_list";
    private static final String MARK_TESTIDENTITYATTRIBUTE = "testidentityattribute";

    private static final String JSP_MANAGE_TESTIDENTITYATTRIBUTES = "jsp/admin/plugins/identitystore/modules/test/ManageTestIdentityAttributes.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_TESTIDENTITYATTRIBUTE = "module.identitystore.test.message.confirmRemoveTestIdentityAttribute";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.identitystore.test.model.entity.testidentityattribute.attribute.";

    // Views
    private static final String VIEW_MANAGE_TESTIDENTITYATTRIBUTES = "manageTestIdentityAttributes";
    private static final String VIEW_CREATE_TESTIDENTITYATTRIBUTE = "createTestIdentityAttribute";
    private static final String VIEW_MODIFY_TESTIDENTITYATTRIBUTE = "modifyTestIdentityAttribute";

    // Actions
    private static final String ACTION_CREATE_TESTIDENTITYATTRIBUTE = "createTestIdentityAttribute";
    private static final String ACTION_MODIFY_TESTIDENTITYATTRIBUTE = "modifyTestIdentityAttribute";
    private static final String ACTION_REMOVE_TESTIDENTITYATTRIBUTE = "removeTestIdentityAttribute";
    private static final String ACTION_CONFIRM_REMOVE_TESTIDENTITYATTRIBUTE = "confirmRemoveTestIdentityAttribute";

    // Infos
    private static final String INFO_TESTIDENTITYATTRIBUTE_CREATED = "module.identitystore.test.info.testidentityattribute.created";
    private static final String INFO_TESTIDENTITYATTRIBUTE_UPDATED = "module.identitystore.test.info.testidentityattribute.updated";
    private static final String INFO_TESTIDENTITYATTRIBUTE_REMOVED = "module.identitystore.test.info.testidentityattribute.removed";
    
    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
    
    // Session variable to store working values
    private TestIdentityAttribute _testidentityattribute;
    private List<Integer> _listIdTestIdentityAttributes;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_TESTIDENTITYATTRIBUTES, defaultView = true )
    public String getManageTestIdentityAttributes( HttpServletRequest request )
    {
        _testidentityattribute = null;
        
        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX) == null || _listIdTestIdentityAttributes.isEmpty( ) )
        {
        	_listIdTestIdentityAttributes = TestIdentityAttributeHome.getIdTestIdentityAttributesList(  );
        }
        
        Map<String, Object> model = getPaginatedListModel( request, MARK_TESTIDENTITYATTRIBUTE_LIST, _listIdTestIdentityAttributes, JSP_MANAGE_TESTIDENTITYATTRIBUTES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_TESTIDENTITYATTRIBUTES, TEMPLATE_MANAGE_TESTIDENTITYATTRIBUTES, model );
    }

	/**
     * Get Items from Ids list
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
	@Override
	List<TestIdentityAttribute> getItemsFromIds( List<Integer> listIds ) 
	{
		List<TestIdentityAttribute> listTestIdentityAttribute = TestIdentityAttributeHome.getTestIdentityAttributesListByIds( listIds );
		
		// keep original order
        return listTestIdentityAttribute.stream()
                 .sorted(Comparator.comparingInt( notif -> listIds.indexOf( notif.getId())))
                 .collect(Collectors.toList());
	}
    
    /**
    * reset the _listIdTestIdentityAttributes list
    */
    public void resetListId( )
    {
    	_listIdTestIdentityAttributes = new ArrayList<>( );
    }

    /**
     * Returns the form to create a testidentityattribute
     *
     * @param request The Http request
     * @return the html code of the testidentityattribute form
     */
    @View( VIEW_CREATE_TESTIDENTITYATTRIBUTE )
    public String getCreateTestIdentityAttribute( HttpServletRequest request )
    {
        _testidentityattribute = ( _testidentityattribute != null ) ? _testidentityattribute : new TestIdentityAttribute(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_TESTIDENTITYATTRIBUTE, _testidentityattribute );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_TESTIDENTITYATTRIBUTE ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_TESTIDENTITYATTRIBUTE, TEMPLATE_CREATE_TESTIDENTITYATTRIBUTE, model );
    }

    /**
     * Process the data capture form of a new testidentityattribute
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_TESTIDENTITYATTRIBUTE )
    public String doCreateTestIdentityAttribute( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _testidentityattribute, request, getLocale( ) );
        

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_TESTIDENTITYATTRIBUTE ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _testidentityattribute, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_TESTIDENTITYATTRIBUTE );
        }

        TestIdentityAttributeHome.create( _testidentityattribute );
        addInfo( INFO_TESTIDENTITYATTRIBUTE_CREATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTIDENTITYATTRIBUTES );
    }

    /**
     * Manages the removal form of a testidentityattribute whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_TESTIDENTITYATTRIBUTE )
    public String getConfirmRemoveTestIdentityAttribute( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTIDENTITYATTRIBUTE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_TESTIDENTITYATTRIBUTE ) );
        url.addParameter( PARAMETER_ID_TESTIDENTITYATTRIBUTE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TESTIDENTITYATTRIBUTE, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a testidentityattribute
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage testidentityattributes
     */
    @Action( ACTION_REMOVE_TESTIDENTITYATTRIBUTE )
    public String doRemoveTestIdentityAttribute( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTIDENTITYATTRIBUTE ) );
        
        
        TestIdentityAttributeHome.remove( nId );
        addInfo( INFO_TESTIDENTITYATTRIBUTE_REMOVED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTIDENTITYATTRIBUTES );
    }

    /**
     * Returns the form to update info about a testidentityattribute
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_TESTIDENTITYATTRIBUTE )
    public String getModifyTestIdentityAttribute( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTIDENTITYATTRIBUTE ) );

        if ( _testidentityattribute == null || ( _testidentityattribute.getId(  ) != nId ) )
        {
            Optional<TestIdentityAttribute> optTestIdentityAttribute = TestIdentityAttributeHome.findByPrimaryKey( nId );
            _testidentityattribute = optTestIdentityAttribute.orElseThrow( ( ) -> new AppException(ERROR_RESOURCE_NOT_FOUND ) );
        }


        Map<String, Object> model = getModel(  );
        model.put( MARK_TESTIDENTITYATTRIBUTE, _testidentityattribute );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_TESTIDENTITYATTRIBUTE ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_TESTIDENTITYATTRIBUTE, TEMPLATE_MODIFY_TESTIDENTITYATTRIBUTE, model );
    }

    /**
     * Process the change form of a testidentityattribute
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_TESTIDENTITYATTRIBUTE )
    public String doModifyTestIdentityAttribute( HttpServletRequest request ) throws AccessDeniedException
    {   
        populate( _testidentityattribute, request, getLocale( ) );
		
		
        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_TESTIDENTITYATTRIBUTE ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _testidentityattribute, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_TESTIDENTITYATTRIBUTE, PARAMETER_ID_TESTIDENTITYATTRIBUTE, _testidentityattribute.getId( ) );
        }

        TestIdentityAttributeHome.update( _testidentityattribute );
        addInfo( INFO_TESTIDENTITYATTRIBUTE_UPDATED, getLocale(  ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTIDENTITYATTRIBUTES );
    }
}
