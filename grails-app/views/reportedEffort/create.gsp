<%@ page contentType="text/html;charset=UTF-8" %>

<!-- ASSIGNED, REPORTED & COMMITTED message boxes -->
<!-- REPORTED EFFORT for current period -->      
<div class="include">
    <g:include 
      controller="assignedEffort" 
      action="showCurrent" 
      id="${assignedEffortInstance.id}" 
      params="${[isForm: false]}" 
/>

</div>

<g:form name="reportedEffort-add" method="post">

  <g:hiddenField name="id" value="${assignedEffortInstance?.id}" />
  
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
            size="4" 
            maxlength="6"
            value="${g.formatNumber(number:(reportedEffortInstance?.percentEffort ? reportedEffortInstance?.percentEffort : null), maxFractionDigits:4)}"
          />%
          </span>
      </span>

    </div>

  </div>
  
  <!-- ERROR MESSAGES -->    
  <g:if test="${flash.message}">
    <div class="flashMessage">${flash.message}</div>
  </g:if>

  <g:hasErrors bean="${reportedEffortInstance}">
    <div class="errors">
      <g:renderErrors bean="${reportedEffortInstance}" as="list" />
    </div>
  </g:hasErrors>
  
  <div class="clearCenterPadding">

    <!-- ADD button -->
    <g:submitToRemote 
      class="buttonBasic" 
      value="SAVE"
      url="${[controller:'reportedEffort',action:'save']}" 
      update="remoteFormContainer"
    />

    <!-- CANCEL button -->
    <g:submitToRemote 
      class="buttonBasic" 
      value="CANCEL" 
      url="${[controller:'reportedEffort',action:'cancel' ]}" 
      update="remoteFormContainer" 
    />

  </div>

</g:form>      
   