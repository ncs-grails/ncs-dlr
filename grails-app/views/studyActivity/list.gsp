
<%@ page import="edu.umn.ncs.StudyActivity" %>
<html>
  
  <head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ncs" />
    <g:set var="entityName" value="${message(code: 'studyActivity.label', default: 'StudyActivity')}" />
    <title>
      <g:message code="default.list.label" args="[entityName]" />
    </title>

  </head>
    
  <body>
            
    <!-- navigation -->
    <div class="nav">
      <span class="menuButton">
        <a class="home" href="${createLink(uri: '/')}">
          <g:message code="default.home.label"/>
        </a>
      </span>
      <span class="menuButton">
        <g:link class="create" action="create">
          <g:message code="default.new.label" args="[entityName]" />
        </g:link>
      </span>
    </div>
      
    <div class="body">
          
      var entityName: ${entityName}
          
      <h1>
        <g:message code="default.list.label" args="[entityName]" />
      </h1>
            
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
            
      <div class="list">
              
        <table>
                  
          <thead>
            <tr>
              <g:sortableColumn property="id" title="${message(code: 'studyActivity.id.label', default: 'Id')}" />
              <g:sortableColumn property="name" title="${message(code: 'studyActivity.name.label', default: 'Name')}" />
              <g:sortableColumn property="obsolete" title="${message(code: 'studyActivity.obsolete.label', default: 'Obsolete')}" />
              <g:sortableColumn property="dateCreated" title="${message(code: 'studyActivity.dateCreated.label', default: 'Date Created')}" />
              <g:sortableColumn property="userCreated" title="${message(code: 'studyActivity.userCreated.label', default: 'User Created')}" />
              <g:sortableColumn property="appCreated" title="${message(code: 'studyActivity.appCreated.label', default: 'App Created')}" />
            </tr>                    
          </thead>
          
          <tbody>

            <g:each in="${studyActivityInstanceList}" status="i" var="studyActivityInstance">

              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                <td>
                  <g:link action="show" id="${studyActivityInstance.id}">
                    ${fieldValue(bean: studyActivityInstance, field: "id")}
                  </g:link>
                </td>

                <td>${fieldValue(bean: studyActivityInstance, field: "name")}</td>

                <td>
                  <g:formatBoolean boolean="${studyActivityInstance.obsolete}" />
                </td>

                <td>
                  <g:formatDate date="${studyActivityInstance.dateCreated}" />
                </td>

                <td>${fieldValue(bean: studyActivityInstance, field: "userCreated")}</td>

                <td>${fieldValue(bean: studyActivityInstance, field: "appCreated")}</td>

              </tr>

            </g:each>

          </tbody>
                    
        </table>
              
      </div>
            
      <div class="paginateButtons">
          <g:paginate total="${studyActivityInstanceTotal}" />
      </div>
            
    </div>
      
  </body>
    
</html>
