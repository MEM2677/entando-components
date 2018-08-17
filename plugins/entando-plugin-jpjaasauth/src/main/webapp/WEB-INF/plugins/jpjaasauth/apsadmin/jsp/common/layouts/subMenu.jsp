<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li><a href="<s:url namespace="/do/jpjaasauth/Mapping" action="list" />" ><s:text name="jpjaasauth.title.mappingManagement" /></a></li>
	<li><a href="<s:url namespace="/do/jpjaasauth/Config" action="edit" />" ><s:text name="jpjaasauth.title.jaasManagement" /></a></li>
</wp:ifauthorized>
