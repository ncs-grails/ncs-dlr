<%@ page contentType="text/html;charset=UTF-8" %>

<!-- FORM: ADDING NEW REPORTED EFFORT -->
<g:form name="reportedEffort-create" method="post" controller="reportedEffort" action="save">
        
  <g:hiddenField name="assignedEffort.id" value="${reportedEffortInstance?.assignedEffort?.id}" />

  <div class="clearCenterPadding">
    
    <!-- CONTROLS: STUDY ACTIVITY, STUDY TASK, & EFFORT -->    
    <div class="effortSelection">
      
      <!--Study Activity -->
      <span class="controlBox">Study Activity
        <span class="value ${hasErrors(bean: reportedEffortInstance, field: 'activity', 'errors')}">
          <g:select class="basic"
            name="activity.id"
            from="${studyActivityList}"
            optionKey="id"
            optionValue="name"
            value="${reportedEffortInstance?.activity?.id}" 
            noSelection="${['0':'Choose ...']}"/>
        </span>
      </span>

      <!--Study Task -->
      <span class="controlBox">Study Task
        <span class="value ${hasErrors(bean: reportedEffortInstance, field: 'task', 'errors')}">
          <g:select class="basic"
            name="task.id"
            from="${studyTaskList}"
            optionKey="id"
            optionValue="name"
            value="${reportedEffortInstance?.task?.id}" 
            noSelection="${['0':'Choose ...']}"/>
        </span>
      </span>

      <!-- Effort -->
      <span class="controlBox">
        <span class="value ${hasErrors(bean: reportedEffortInstance, field: 'percentEffort', 'errors')}">Effort
          <g:textField 
            name="percentEffort" 
            class="textfieldBasic" 
            style="text-align:right;"
            size="1" 
            value="${reportedEffortInstance?.percentEffort}"
          />%
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
  
  <!-- CONTROLS: ADD & CANCEL -->
  <div class="clearCenterPadding">
    
    <g:submitToRemote 
      class="buttonBasic" 
      url="${[controller:'reportedEffort',action:'save']}" 
      update="addOrEditForm" 
      onSuccess="loadAssignedEffort();" 
      value="ADD"
    />
    <button 
      class="buttonBasic" 
      onClick="return resetForm();">
      CANCEL
    </button>
    
  </div>



        
</g:form>      
