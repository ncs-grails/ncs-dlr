<%@ page contentType="text/html;charset=UTF-8" %>
<g:form name="reportedEffort-create" method="post" controller="reportedEffort" action="save">
        
  <div class="clearCenterPadding">
    
    <div class="effortSelection">
      
      <g:hiddenField name="assignedEffort.id" value="${reportedEffortInstance?.assignedEffort?.id}" />

      <!--Study Activity -->
      Study Activity
      <span class="value ${hasErrors(bean: reportedEffortInstance, field: 'activity', 'errors')}">
        <g:select class="basic"
          name="activity.id"
          from="${studyActivityList}"
          optionKey="id"
          optionValue="name"
          value="${reportedEffortInstance?.activity?.id}" 
          noSelection="${['0':'Choose ...']}"/>
      </span>

      <!--Study Task -->
      Study Task
      <span class="value ${hasErrors(bean: reportedEffortInstance, field: 'task', 'errors')}">
        <g:select class="basic"
          name="task.id"
          from="${studyTaskList}"
          optionKey="id"
          optionValue="name"
          value="${reportedEffortInstance?.task?.id}" 
          noSelection="${['0':'Choose ...']}"/>&nbsp;&nbsp;
      </span>

      <!-- Effort -->
      <span class="value ${hasErrors(bean: reportedEffortInstance, field: 'percentEffort', 'errors')}">
        <span class="nowrap">Effort
          <g:textField 
            name="percentEffort" 
            class="textfieldBasic" 
            style="text-align:right;"
            size="1" 
            value="${reportedEffortInstance?.percentEffort}"
          />&nbsp;%
        </span>
      </span>
      
      <g:if test="${flash.message}">
        ${flash.message}
      </g:if>

      <g:hasErrors bean="${reportedEffortInstance}">
        <div class="errors">
          <g:renderErrors bean="${reportedEffortInstance}" as="list" />
        </div>
      </g:hasErrors>
      
    </div>

  </div>
    
  <div class="clearCenterPadding">
    
    <g:submitToRemote 
      class="buttonBasic" 
      url="${[controller:'reportedEffort',action:'save']}" 
      update="[success:'addOrEditForm', failure:'addOrEditForm']" 
      onSuccess="reloadAssignedEffort();" 
      value="ADD"
    />
    
    &nbsp;&nbsp;&nbsp;
    
    <button class="buttonBasic" onClick="return resetForm();">CANCEL</button>
    
  </div>



        
</g:form>      
