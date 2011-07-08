

<%@ page import="edu.umn.ncs.ReportingPeriod" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'reportingPeriod.label', default: 'ReportingPeriod')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${reportingPeriodInstance}">
            <div class="errors">
                <g:renderErrors bean="${reportingPeriodInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
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
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
