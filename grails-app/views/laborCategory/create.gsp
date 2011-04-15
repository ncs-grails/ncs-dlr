

<%@ page import="edu.umn.ncs.LaborCategory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ncs" />
        <g:set var="entityName" value="${message(code: 'laborCategory.label', default: 'LaborCategory')}" />
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
            <g:hasErrors bean="${laborCategoryInstance}">
            <div class="errors">
                <g:renderErrors bean="${laborCategoryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="laborCategory.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: laborCategoryInstance, field: 'name', 'errors')}">
                                    <g:textArea name="name" cols="40" rows="5" value="${laborCategoryInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="obsolete"><g:message code="laborCategory.obsolete.label" default="Obsolete" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: laborCategoryInstance, field: 'obsolete', 'errors')}">
                                    <g:checkBox name="obsolete" value="${laborCategoryInstance?.obsolete}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="userCreated"><g:message code="laborCategory.userCreated.label" default="User Created" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: laborCategoryInstance, field: 'userCreated', 'errors')}">
                                    <g:textField name="userCreated" value="${laborCategoryInstance?.userCreated}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="appCreated"><g:message code="laborCategory.appCreated.label" default="App Created" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: laborCategoryInstance, field: 'appCreated', 'errors')}">
                                    <g:textField name="appCreated" value="${laborCategoryInstance?.appCreated}" />
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
