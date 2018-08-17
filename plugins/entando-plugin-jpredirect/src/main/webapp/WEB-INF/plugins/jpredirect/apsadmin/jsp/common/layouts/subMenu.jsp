<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li><a href="<s:url namespace="/do/jpredirect/Redirect" action="list" />" ><s:text name="jpredirect.title.redirectManagement" /></a></li>
</wp:ifauthorized>
