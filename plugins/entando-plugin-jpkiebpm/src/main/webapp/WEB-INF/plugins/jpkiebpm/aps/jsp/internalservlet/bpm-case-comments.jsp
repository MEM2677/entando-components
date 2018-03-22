<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="<wp:resourceURL />plugins/jpkiebpm/static/css/jbpm-widget-ext.css" rel="stylesheet">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jquery-ui.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>
<script src="<wp:resourceURL />plugins/jpkiebpm/static/js/jbpm-component.js"></script>


<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">

        <form action="<wp:action path="/ExtStr2/do/bpm/FrontEnd/CaseInstanceComments/postComment.action"/>" method="post" class="form-horizontal">
            <div class="form-group">
                <label class="control-label col-xs-4" for="processPath">Select a Case Instance</label>
                <div class="col-xs-6">
                    <s:select list="cases" id="casePath" name="casePath">
                    </s:select>
                </div
            </div>
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="control-label col-xs-4" for="commentInput">Comments:</label>
                    <div class="col-xs-6">
                        <wpsf:textfield name="commentInput"/>
                    </div>
                    <div class="col-xs-2">
                        <wpsf:submit type="button" value="Submit" cssClass="btn btn-primary pull-right" />
                    </div>
                </div>
            </div>
        </form>

        <s:property value="comments" escapeHtml="false" escapeJavaScript="false"/>

    </div>
</div>