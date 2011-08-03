<g:ifAnyGranted role="ROLE_NCS_IT,ROLE_NCS_DLR_MANAGE,ROLE_NCS_DLR">

	<g:form name="assignedEffort" method="post">
	    
		<g:hiddenField name="id" value="${assignedEffortInstance.id}" />
		
		<!-- CURRENT EFFORT REPORTINGPERIOD STATUS -->
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
			<g:submitToRemote 
				class="buttonBasic" 
				value="COMMIT" 
				url="${[controller:'assignedEffort',action:'commit' ]}" 
				update="remoteFormContainer" 
			/>
		</div>
	
	</g:form>
	
</g:ifAnyGranted>
	