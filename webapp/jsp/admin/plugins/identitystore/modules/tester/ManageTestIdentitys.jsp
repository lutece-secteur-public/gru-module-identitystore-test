<jsp:useBean id="managetestTestIdentity" scope="session" class="fr.paris.lutece.plugins.identitystore.modules.test.web.TestIdentityJspBean" />
<% String strContent = managetestTestIdentity.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
