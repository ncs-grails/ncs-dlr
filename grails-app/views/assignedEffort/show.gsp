<g:form name="assignedEffort" method="post" controller="assignedEffort" action="commit">
    
	<g:hiddenField name="id" value="${assignedEffortInstance.id}" />

  <!-- CURRENT REPORTED EFFORT STATUS (AJZ: must have assignedEffortInstance.id here, NOT reportedIstance.assignedEffort.id) -->
  <g:include 
    controller="assignedEffort" 
    action="showCurrent" 
    id="${assignedEffortInstance.id}" 
    params="${[isForm: true]}" 
  />

  <!-- display ERROR MESSAGES -->       
  <g:if test="${errMessage}">
    <div class="errors">${errMessage}</div>        
  </g:if>
  <g:else>
    <g:hasErrors bean="${reportedEffortInstance}">
      <div class="errors">
        <g:renderErrors bean="${reportedEffortInstance}" as="list" />
      </div>
    </g:hasErrors>
  </g:else>
  
  <!-- BUTTON CONTROLS -->
  <div class="clearCenterPadding">
  	<g:if test="${!hideAddButton}">
	    <g:submitToRemote 
	      class="buttonBasic" 
	      value="ADD" 
	      url="${[controller:'reportedEffort',action:'create' ]}" 
	      update="remoteFormContainer" 
	    />
	</g:if>
    <g:submitToRemote 
      class="buttonBasic" 
      value="DELETE" 
      url="${[controller:'reportedEffort',action:'delete' ]}" 
      update="remoteFormContainer" 
    />
    <g:submitToRemote 
      class="buttonBasic" 
      value="EDIT" 
      url="${[controller:'reportedEffort',action:'edit' ]}" 
      update="remoteFormContainer" 
    />
	<g:submitButton 
      class="buttonBasic"
      name="commit" 
      value="COMMIT" 
    />
  </div>

</g:form>
