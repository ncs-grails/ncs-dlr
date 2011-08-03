<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="edu.umn.ncs.StudyActivity" %>
<%@ page import="edu.umn.ncs.StudyTask" %>

<g:ifAnyGranted role="ROLE_NCS_IT,ROLE_NCS_DLR_MANAGE,ROLE_NCS_DLR">

	<!-- include for ASSIGNED, REPORTED & COMMITTED message boxes plus REPORTED EFFORT for current period -->      
	<div class="include">
		<g:include 
			controller="assignedEffort" 
			action="showCurrent" 
			id="${reportedEffortInstance?.assignedEffort?.id}" 
			params="${[isForm: false]}" 
		/>
	</div>
	
	<g:form name="reportedEffort-add" method="post">
	
		<g:hiddenField name="assignedEffort.id" value="${reportedEffortInstance?.assignedEffort?.id}" />
	  
		<!-- CONTROLS to enter ACTIVITY, TASK, & EFFORT -->    
		<div class="clearCenterPadding">
			<div class="effortSelection">
				<span class="controlBox">Study Activity
					<span class="value ${hasErrors(bean: reportedEffortInstance, field: 'activity', 'errors')}">
						<g:select class="basic"
							name="activity.id"
							from="${StudyActivity.findAllWhere(obsolete: false)}"
							optionKey="id"
							optionValue="name"
							value="${reportedEffortInstance?.activity?.id}" 
							noSelection="${['null':'Choose ...']}"
						/>
					</span>
				</span>
				<span class="controlBox">Study Task
					<span class="value ${hasErrors(bean: reportedEffortInstance, field: 'task', 'errors')}">
						<g:select class="basic"
							name="task.id"
							from="${StudyTask.findAllWhere(obsolete: false)}"
							optionKey="id"
							optionValue="name"
							value="${reportedEffortInstance?.task?.id}" 
							noSelection="${['null':'Choose ...']}"
						/>
					</span>
				</span>
				<span class="controlBox">
					<span class="value ${hasErrors(bean: reportedEffortInstance, field: 'percentEffort', 'errors')}">Effort
					<g:textField 
						name="percentEffortConverted" 
						class="textfieldBasic" 
						style="text-align:right;"
						size="4" 
						maxlength="6"
						value="${g.formatNumber(number:reportedEffortInstance?.percentEffortConverted, maxFractionDigits:4)}"
					/>%
					</span>
				</span>
			</div>
		</div>
		
		<!-- display ERROR MESSAGES, after save attempt-->       
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
		  
		<!-- BUTTON controls -->
		<div class="clearCenterPadding">
			<g:submitToRemote 
				class="buttonBasic" 
				value="SAVE"
				url="${[controller:'reportedEffort',action:'addSave']}" 
				update="remoteFormContainer"
			/>
			<g:submitToRemote 
				class="buttonBasic" 
				value="CANCEL" 
				url="${[controller:'reportedEffort',action:'cancel' ]}" 
				update="remoteFormContainer" 
			/>
		</div>
	
	</g:form>
	
</g:ifAnyGranted>
	      
   