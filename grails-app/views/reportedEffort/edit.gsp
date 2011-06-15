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

<g:form name="reportedEffort-create" method="post">

  <g:hiddenField name="reportingStaff.id" value="${reportingStaffInstance?.id}" />
  <g:hiddenField name="reportingPeriod.id" value="${reportingPeriodInstance?.id}" />
  <g:hiddenField name="assignedEffort.id" value="${assignedEffortInstance?.id}" />
  <g:hiddenField name="reportedEffort.id" value="${assignedEffortInstance?.id}" />    
  
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
            size="2" 
            value="${g.formatNumber(number:(reportedEffortInstance?.percentEffort ? reportedEffortInstance?.percentEffort*100 : null), maxFractionDigits:4)}"
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

  <div class="clearCenterPadding">

    <!-- ADD button -->
    <g:submitToRemote 
      class="buttonBasic" 
      value="SAVE"
      url="${[controller:'reportedEffort',action:'editSave']}" 
      update="remoteFormContainer"
    />

    <!-- CANCEL button -->
    <g:submitToRemote 
      class="buttonBasic" 
      value="CANCEL" 
      url="${[controller:'reportedEffort',action:'main' ]}" 
      update="remoteFormContainer" 
    />

  </div>

</g:form>      
   