<%@ page contentType="text/html;charset=UTF-8" %>
<g:form name="reportedEffort-create" method="post" controller="reportedEffort">
        

  <div class="clearCenterPadding">
    
    <div class="effortSelection">

      <!--Study Activity -->
      Study Activity
        <g:select class="basic"
          name="studyActivityInstance.id"
          from="${studyActivityList}"
          optionKey="id"
          optionValue="name"
          value="${reportedEffortInstance?.activity?.id}" 
          noSelection="${['0':'Choose ...']}"/>&nbsp;&nbsp;

      <!--Study Task -->
      Study Task
        <g:select class="basic"
          name="studyTaskInstance.id"
          from="${studyTaskList}"
          optionKey="id"
          optionValue="name"
          value="${reportedEffortInstance?.task?.id}" 
          noSelection="${['0':'Choose ...']}"/>&nbsp;&nbsp;

      <!-- Effort -->
      <span class="nowrap">Effort
        <g:textField 
          name="reportedEffort" 
          class="textfieldBasic" 
          style="text-align:right;"
          size="1" 
          value="${reportedEffortInstance?.percentEffort}"
        />&nbsp;%
      </span>
      
      <div id="failedAddDiv">
        <g:if test="${flash.message}">
          ${flash.message}
        </g:if>
      </div>

    </div>

  </div>
    
  <div class="clearCenterPadding">
    <g:submitToRemote class="buttonBasic" url="${[controller:'reportedEffort',action:'save']}" update="[failure:'failedAddDiv']" value="ADD"/>
    <!--
    <g:actionSubmit class="buttonBasic" value="ADD" constructor="reportedEffort" action="save" />          
    -->
    &nbsp;&nbsp;&nbsp;
    <button class="buttonBasic" onClick="return resetForm();">CANCEL</button>
  </div>



        
</g:form>      
