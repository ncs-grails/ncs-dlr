<%@ page import="edu.umn.ncs.StudyActivity" %>
<html>
  
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
    <g:set var="entityName" value="${message(code: 'studyActivity.label', default: 'StudyActivity')}" />
    <title>
      <g:message code="default.edit.label" args="[entityName]" />
    </title>
  </head>
  
  <body>
    
    <div class="nav">
      <span class="menuButton">
        <a class="home" href="${createLink(uri: '/')}">
          <g:message code="default.home.label"/>
        </a>
      </span>
      <span class="menuButton">
        <g:link class="list" action="list">
          <g:message code="default.list.label" args="[entityName]" />
        </g:link>
      </span>
      <span class="menuButton">
        <g:link class="create" action="create">
          <g:message code="default.new.label" args="[entityName]" />
        </g:link>
      </span>
    </div>
    
    <div class="body">
      
        <h1>
          <g:message code="default.edit.label" args="[entityName]" />
        </h1>
      
        <g:if test="${flash.message}">
          <div class="message">${flash.message}</div>
        </g:if>
      
        <g:hasErrors bean="${studyActivityInstance}">          
          <div class="errors">
            <g:renderErrors bean="${studyActivityInstance}" as="list" />
          </div>
        </g:hasErrors>
      
        <g:form method="post" >
          
          <g:hiddenField name="id" value="${studyActivityInstance?.id}" />
          <g:hiddenField name="version" value="${studyActivityInstance?.version}" />
            
          <div class="dialog">
            
            <table>
              
              <tbody>

                <tr class="prop">
                  <td valign="top" class="name">
                    <label for="name"><g:message code="studyActivity.name.label" default="Name" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: studyActivityInstance, field: 'name', 'errors')}">
                    <g:textArea name="name" cols="40" rows="5" value="${studyActivityInstance?.name}" />
                  </td>
                </tr>

                <tr class="prop">
                  <td valign="top" class="name">
                    <label for="obsolete"><g:message code="studyActivity.obsolete.label" default="Obsolete" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: studyActivityInstance, field: 'obsolete', 'errors')}">
                    <g:checkBox name="obsolete" value="${studyActivityInstance?.obsolete}" />
                  </td>
                </tr>

                <tr class="prop">
                  <td valign="top" class="name">
                    <label for="userCreated"><g:message code="studyActivity.userCreated.label" default="User Created" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: studyActivityInstance, field: 'userCreated', 'errors')}">
                    <g:textField name="userCreated" value="${studyActivityInstance?.userCreated}" />
                  </td>
                </tr>

                <tr class="prop">
                  <td valign="top" class="name">
                    <label for="appCreated"><g:message code="studyActivity.appCreated.label" default="App Created" /></label>
                  </td>
                  <td valign="top" class="value ${hasErrors(bean: studyActivityInstance, field: 'appCreated', 'errors')}">
                    <g:textField name="appCreated" value="${studyActivityInstance?.appCreated}" />
                  </td>
                </tr>

              </tbody>
              
            </table>
            
          </div>
          
          <div class="buttons">
            <span class="button">
              <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
            </span>
            <span class="button">
              <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </span>
          </div>
          
        </g:form>
        
    </div>
    
  </body>
  
</html>
