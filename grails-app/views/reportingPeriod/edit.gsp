

<%@ page import="edu.umn.ncs.ReportingPeriod" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'reportingPeriod.label', default: 'ReportingPeriod')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reportingPeriodInstance}">
            <div class="errors">
                <g:renderErrors bean="${reportingPeriodInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${reportingPeriodInstance?.id}" />
                <g:hiddenField name="version" value="${reportingPeriodInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="referenceInvoiceNumber"><g:message code="reportingPeriod.referenceInvoiceNumber.label" default="Reference Invoice Number" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingPeriodInstance, field: 'referenceInvoiceNumber', 'errors')}">
                                    <g:textField name="referenceInvoiceNumber" value="${fieldValue(bean: reportingPeriodInstance, field: 'referenceInvoiceNumber')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="periodDate"><g:message code="reportingPeriod.periodDate.label" default="Period Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingPeriodInstance, field: 'periodDate', 'errors')}">
                                    <g:datePicker name="periodDate" precision="day" value="${reportingPeriodInstance?.periodDate}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="preparedDate"><g:message code="reportingPeriod.preparedDate.label" default="Prepared Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingPeriodInstance, field: 'preparedDate', 'errors')}">
                                    <g:datePicker name="preparedDate" precision="day" value="${reportingPeriodInstance?.preparedDate}" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="completedReportDate"><g:message code="reportingPeriod.completedReportDate.label" default="Completed Report Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingPeriodInstance, field: 'completedReportDate', 'errors')}">
                                    <g:datePicker name="completedReportDate" precision="day" value="${reportingPeriodInstance?.completedReportDate}" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="assignedEfforts"><g:message code="reportingPeriod.assignedEfforts.label" default="Assigned Efforts" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: reportingPeriodInstance, field: 'assignedEfforts', 'errors')}">
                                    
<ul>
<g:each in="${reportingPeriodInstance?.assignedEfforts?}" var="a">
    <li><g:link controller="assignedEffort" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="assignedEffort" action="create" params="['reportingPeriod.id': reportingPeriodInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'assignedEffort.label', default: 'AssignedEffort')])}</g:link>

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
