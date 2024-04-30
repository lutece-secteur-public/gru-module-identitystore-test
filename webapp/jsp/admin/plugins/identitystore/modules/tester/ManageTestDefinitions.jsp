<jsp:useBean id="managetestTestDefinition" scope="session" class="fr.paris.lutece.plugins.identitystore.modules.test.web.TestDefinitionJspBean" />
<% String strContent = managetestTestDefinition.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
