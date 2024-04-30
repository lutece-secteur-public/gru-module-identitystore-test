<jsp:useBean id="managetestTestIdentityAttribute" scope="session" class="fr.paris.lutece.plugins.identitystore.modules.test.web.TestIdentityAttributeJspBean" />
<% String strContent = managetestTestIdentityAttribute.processController ( request , response ); %>

<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../../AdminFooter.jsp" %>
