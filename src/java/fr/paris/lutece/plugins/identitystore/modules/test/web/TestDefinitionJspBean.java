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
import fr.paris.lutece.plugins.identitystore.modules.test.business.TestDefinition;
import fr.paris.lutece.plugins.identitystore.modules.test.business.TestDefinitionHome;

/**
 * This class provides the user interface to manage TestDefinition features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageTestDefinitions.jsp", controllerPath = "jsp/admin/plugins/identitystore/modules/test/", right = "IDENTITYSTORE_TEST_MANAGEMENT" )
public class TestDefinitionJspBean extends AbstractManageTesterJspBean<Integer, TestDefinition>
{
    // Templates
    private static final String TEMPLATE_MANAGE_TESTDEFINITIONS = "/admin/plugins/identitystore/modules/test/manage_testdefinitions.html";
    private static final String TEMPLATE_CREATE_TESTDEFINITION = "/admin/plugins/identitystore/modules/test/create_testdefinition.html";
    private static final String TEMPLATE_MODIFY_TESTDEFINITION = "/admin/plugins/identitystore/modules/test/modify_testdefinition.html";

    // Parameters
    private static final String PARAMETER_ID_TESTDEFINITION = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TESTDEFINITIONS = "module.identitystore.test.manage_testdefinitions.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TESTDEFINITION = "module.identitystore.test.modify_testdefinition.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_TESTDEFINITION = "module.identitystore.test.create_testdefinition.pageTitle";

    // Markers
    private static final String MARK_TESTDEFINITION_LIST = "testdefinition_list";
    private static final String MARK_TESTDEFINITION = "testdefinition";

    private static final String JSP_MANAGE_TESTDEFINITIONS = "jsp/admin/plugins/identitystore/modules/test/ManageTestDefinitions.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_TESTDEFINITION = "module.identitystore.test.message.confirmRemoveTestDefinition";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "module.identitystore.test.model.entity.testdefinition.attribute.";

    // Views
    private static final String VIEW_MANAGE_TESTDEFINITIONS = "manageTestDefinitions";
    private static final String VIEW_CREATE_TESTDEFINITION = "createTestDefinition";
    private static final String VIEW_MODIFY_TESTDEFINITION = "modifyTestDefinition";

    // Actions
    private static final String ACTION_CREATE_TESTDEFINITION = "createTestDefinition";
    private static final String ACTION_MODIFY_TESTDEFINITION = "modifyTestDefinition";
    private static final String ACTION_REMOVE_TESTDEFINITION = "removeTestDefinition";
    private static final String ACTION_CONFIRM_REMOVE_TESTDEFINITION = "confirmRemoveTestDefinition";

    // Infos
    private static final String INFO_TESTDEFINITION_CREATED = "module.identitystore.test.info.testdefinition.created";
    private static final String INFO_TESTDEFINITION_UPDATED = "module.identitystore.test.info.testdefinition.updated";
    private static final String INFO_TESTDEFINITION_REMOVED = "module.identitystore.test.info.testdefinition.removed";

    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";

    // Session variable to store working values
    private TestDefinition _testdefinition;
    private List<Integer> _listIdTestDefinitions;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_TESTDEFINITIONS, defaultView = true )
    public String getManageTestDefinitions( HttpServletRequest request )
    {
        _testdefinition = null;

        if ( request.getParameter( AbstractPaginator.PARAMETER_PAGE_INDEX ) == null || _listIdTestDefinitions.isEmpty( ) )
        {
            _listIdTestDefinitions = TestDefinitionHome.getIdTestDefinitionsList( );
        }

        Map<String, Object> model = getPaginatedListModel( request, MARK_TESTDEFINITION_LIST, _listIdTestDefinitions, JSP_MANAGE_TESTDEFINITIONS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_TESTDEFINITIONS, TEMPLATE_MANAGE_TESTDEFINITIONS, model );
    }

    /**
     * Get Items from Ids list
     * 
     * @param listIds
     * @return the populated list of items corresponding to the id List
     */
    @Override
    List<TestDefinition> getItemsFromIds( List<Integer> listIds )
    {
        List<TestDefinition> listTestDefinition = TestDefinitionHome.getTestDefinitionsListByIds( listIds );

        // keep original order
        return listTestDefinition.stream( ).sorted( Comparator.comparingInt( notif -> listIds.indexOf( notif.getId( ) ) ) ).collect( Collectors.toList( ) );
    }

    /**
     * reset the _listIdTestDefinitions list
     */
    public void resetListId( )
    {
        _listIdTestDefinitions = new ArrayList<>( );
    }

    /**
     * Returns the form to create a testdefinition
     *
     * @param request
     *            The Http request
     * @return the html code of the testdefinition form
     */
    @View( VIEW_CREATE_TESTDEFINITION )
    public String getCreateTestDefinition( HttpServletRequest request )
    {
        _testdefinition = ( _testdefinition != null ) ? _testdefinition : new TestDefinition( );

        Map<String, Object> model = getModel( );
        model.put( MARK_TESTDEFINITION, _testdefinition );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_TESTDEFINITION ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_TESTDEFINITION, TEMPLATE_CREATE_TESTDEFINITION, model );
    }

    /**
     * Process the data capture form of a new testdefinition
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_TESTDEFINITION )
    public String doCreateTestDefinition( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _testdefinition, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_TESTDEFINITION ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _testdefinition, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_TESTDEFINITION );
        }

        TestDefinitionHome.create( _testdefinition );
        addInfo( INFO_TESTDEFINITION_CREATED, getLocale( ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTDEFINITIONS );
    }

    /**
     * Manages the removal form of a testdefinition whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_TESTDEFINITION )
    public String getConfirmRemoveTestDefinition( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTDEFINITION ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_TESTDEFINITION ) );
        url.addParameter( PARAMETER_ID_TESTDEFINITION, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_TESTDEFINITION, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a testdefinition
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage testdefinitions
     */
    @Action( ACTION_REMOVE_TESTDEFINITION )
    public String doRemoveTestDefinition( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTDEFINITION ) );

        TestDefinitionHome.remove( nId );
        addInfo( INFO_TESTDEFINITION_REMOVED, getLocale( ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTDEFINITIONS );
    }

    /**
     * Returns the form to update info about a testdefinition
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_TESTDEFINITION )
    public String getModifyTestDefinition( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_TESTDEFINITION ) );

        if ( _testdefinition == null || ( _testdefinition.getId( ) != nId ) )
        {
            Optional<TestDefinition> optTestDefinition = TestDefinitionHome.findByPrimaryKey( nId );
            _testdefinition = optTestDefinition.orElseThrow( ( ) -> new AppException( ERROR_RESOURCE_NOT_FOUND ) );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_TESTDEFINITION, _testdefinition );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_TESTDEFINITION ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_TESTDEFINITION, TEMPLATE_MODIFY_TESTDEFINITION, model );
    }

    /**
     * Process the change form of a testdefinition
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_TESTDEFINITION )
    public String doModifyTestDefinition( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _testdefinition, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_TESTDEFINITION ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _testdefinition, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_TESTDEFINITION, PARAMETER_ID_TESTDEFINITION, _testdefinition.getId( ) );
        }

        TestDefinitionHome.update( _testdefinition );
        addInfo( INFO_TESTDEFINITION_UPDATED, getLocale( ) );
        resetListId( );

        return redirectView( request, VIEW_MANAGE_TESTDEFINITIONS );
    }
}
