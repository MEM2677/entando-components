<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations"/></li>
    <li><s:text name="breadcrumb.integrations.components"/></li>
    <li><s:text name="jpjaasauth.admin.menu"/></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <h1 class="page-title-container">
                <s:text name="jpjaasauth.admin.menu" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="title.jaasManagement.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
    </div>
</div>
<br/>
<div class="mb-20">

<%--
    <div class="panel panel-default">
        <div class="panel-body">
            <s:text name="label.jpmail.intro" />
        </div>
    </div>
 --%>

    <div id="messages">
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
    </div>

    <s:form id="configurationForm" name="configurationForm" method="post" action="save" cssClass="form-horizontal">
        <legend><s:text name="legend.generalSettings" /></legend>
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="label.active" />
            </label>
            <div class="col-sm-10">
                <div class="checkbox">
                    <wpsf:checkbox name="enabled" id="active" cssClass=" bootstrap-switch" />
                </div>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">
                <s:text name="label.privileged" />
            </label>
            <div class="col-sm-10">
                <div class="checkbox">
                    <wpsf:checkbox name="privileged" id="privileged" cssClass=" bootstrap-switch" />
                </div>
            </div>
        </div>

        <legend><s:text name="legend.connection" /></legend>
        <%-- smtpHost --%>
        <s:set var="fieldErrorsVar" value="%{fieldErrors['smtpHost']}" />
        <s:set var="hasFieldErrorVar" value="#fieldErrorsVar != null && !#fieldErrorsVar.isEmpty()" />
        <s:set var="controlGroupErrorClass" value="%{#hasFieldErrorVar ? ' has-error' : ''}" />
        <div class="form-group<s:property value="#controlGroupErrorClass" />">
            <label class="col-sm-2 control-label" for="smtpHost">
                <s:text name="label.realms" />
                <i class="fa fa-asterisk required-icon"></i>
            </label>
            <div class="col-sm-10">
                <wpsf:textfield name="realmscsv" id="realmscsv" cssClass="form-control" />
                <s:if test="#hasFieldErrorVar">
                    <span class="help-block text-danger">
                        <s:iterator value="%{#fieldErrorsVar}"><s:property />&#32;</s:iterator>
                        </span>
                </s:if>
            </div>
        </div>

        <div class="form-group">
            <div class="col-xs-12">
                <div class="pull-right">
                    <wpsf:submit name="save" type="button" action="save" cssClass="btn btn-primary" onclick="overrideSubmit('saveSmtp')">
                        <s:text name="%{getText('label.save')}"/>
                    </wpsf:submit>
                </div>
            </div>
        </div>

    </s:form>
</div>
