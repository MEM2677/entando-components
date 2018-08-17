<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />"><s:text name="jpredirect.title.redirectManagement" /></a>
        &#32;/&#32;
        <s:if test="getStrutsAction() == 1">
            <s:text name="jpredirect.redirect.label.new" />
        </s:if>
        <s:elseif test="getStrutsAction() == 2">
            <s:text name="jpredirect.redirect.label.edit" />
        </s:elseif>
    </span>
</h1>
<s:form action="save" cssClass="form-horizontal">
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable fade in">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
        </div>
    </s:if>
    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable fade in">
            <button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul class="margin-base-top">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    
    <p class="sr-only">
        <wpsf:hidden name="strutsAction" />
        <s:if test="getStrutsAction() == 2">
            <wpsf:hidden name="id" />
        </s:if>
    </p>
    
    <%-- pagecode --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['pagecode']}" />
    <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <div class="col-xs-12">
            <label for="redirect_pagecode"><s:text name="label.pagecode" /></label>
<%--            
            <wpsf:textfield name="pagecode" id="redirect_pagecode" cssClass="form-control" />
--%>        
            <wpsf:select list="pages" name="pagecode" id="redirect_pagecode" cssClass="form-control" />
            
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
            </s:if>
        </div>
    </div>
    <%-- groupname --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['groupname']}" />
    <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <div class="col-xs-12">
            <label for="redirect_groupname"><s:text name="label.groupname" /></label>
<%--            
            <wpsf:textfield name="groupname" id="redirect_groupname" cssClass="form-control" />
--%>
            <s:select list="groups" name="groupname" headerKey="" headerValue="-" id="redirect_groupname" cssClass="form-control"/>        
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
            </s:if>
        </div>
    </div>
    <%-- rolename --%>
    <s:set var="fieldFieldErrorsVar" value="%{fieldErrors['rolename']}" />
    <s:set var="fieldHasFieldErrorVar" value="#fieldFieldErrorsVar != null && !#fieldFieldErrorsVar.isEmpty()" />
    <s:set var="controlGroupErrorClassVar" value="%{#fieldHasFieldErrorVar ? ' has-error' : ''}" />
    <div class="form-group<s:property value="#controlGroupErrorClassVar" />">
        <div class="col-xs-12">
            <label for="redirect_rolename"><s:text name="label.rolename" /></label>
<%--            
            <wpsf:textfield name="rolename" id="redirect_rolename" cssClass="form-control" />
--%>
            <s:select list="roles" name="rolename" headerKey="" headerValue="-" id="redirect_rolename" cssClass="form-control" />
            <s:if test="#fieldHasFieldErrorVar">
                <span class="help-block text-danger">
                    <s:iterator value="%{#fieldFieldErrorsVar}"><s:property />&#32;</s:iterator>
                    </span>
            </s:if>
        </div>
    </div>
    
    <%-- save button --%>
    <div class="form-group">
        <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
            <wpsf:submit type="button" action="save" cssClass="btn btn-primary btn-block">
                <span class="icon fa fa-floppy-o"></span>&#32;
                <s:text name="label.save" />
            </wpsf:submit>
        </div>
    </div>
    
</s:form>

</div>
