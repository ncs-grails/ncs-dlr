<%@ page contentType="text/html;charset=UTF-8" %>

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="layout" content="ncs" />
		<link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
		<title>Effort Assignment</title>    
	</head>

	<body>
      
		<!-- navigation -->
		<p class="breadcrumbs">
			<g:link controller="main" action="show">DLR Home</g:link>
			&nbsp;&nbsp;&gt&nbsp;&nbsp;
			<g:link controller="applicationManagement" action="index">Application Management</g:link>
			&nbsp;&nbsp;&gt&nbsp;&nbsp;
			<span class="fontMaroon">Effort Assignment</span>
		</p>

	    <h1>Effort Assignment</h1>

	    <g:form>

			<!-- PERIOD SELECTION CONTROLS -->
			<div class="clearBoth"			
				<div class="floatLeft">
					<div class="spacing">
					
						<span class="controlBackground">Reporting Month
							<g:select 
								class="basic"
								name="reportingPeriodInstance.id"
								from="${periodSelectiontList}"
								optionKey="id"
								optionValue="name"
								value="${reportingPeriodInstance.id}" 
							/>&nbsp;
							
						  <g:actionSubmit class="buttonBasic" value="GO" constructor="" action="show" />
						            
						</span>
					</div>
				</div>
				<div class="floatRight">
					<div class="spacing">
						<g:actionSubmit class="buttonBasic" value="SUBMIT ASSGINED EFFORT" action="update"/>
					</div>
				</div>
			</div>

			<!-- STAFF EFFORT-->
			<table>

				<thead>
					<tr>
						<th class="basic" colspan="11">
							<strong>
								<g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" />&nbsp;
								(Period Id: ${reportingPeriodInstance?.id})								
							</strong>
						</th>
					</tr>				
					<tr>
						<th class="basic" rowspan="3"></th>
						<th class="basic" rowspan="2" colspan="2">Staff</th>
						<th class="basic" colspan="5">Effort</th>
						<th class="basic" colspan="2">Email Notification</th>
					</tr>
					<tr>
						<th class="basic" colspan="3">ASSIGNED</th>
						<th class="basic" rowspan="2">REPORTED</th>
						<th class="basic" rowspan="2">COMMITTED<br/>(date)</th>
						<th class="basic" rowspan="2">Dates Email Sent</th>
						<th class="basic" rowspan="2">Send<br />Now</th>
					</tr>
					<tr>
						<th class="basic">Name</th>
						<th class="basic">Id</th>
						<th class="basic">Previous</th>
						<th class="basic">Copy Over</th>
						<th class="basic">Current</th>
					</tr>
				</thead>

				<tbody>

					<g:each var="ea" in="${effortAssignmentList}" >

						<!-- logic for highlighting table rows -->
						<g:if test="${!ea.thisPeriodAssignedEffort}" >
							<tr class="backgroundColorGainsboro">		
						</g:if>
						<g:elseif test="${ea.thisPeriodAssignedEffort && !ea.isCommitted}">
							<tr class="backgroundColorOrange">							
						</g:elseif>
						<g:else>
							<tr>
						</g:else>

							<!-- row number -->
							<td class="basic" style="text-align:right;"> ${ea.rowNum}</td>
	
							<!-- staff name & id -->
							<td class="basic"><div class="nowrap">${ea.fullName}</div></td>
							<td class="basic" style="text-align:right;"><div class="nowrap">${ea.staffId}</div></td>
	
	              			<!-- previous effort -->
							<td class="basic" style="text-align:right;">
								<div class="nowrap">                  
									<g:formatNumber number="${ea.previousPeriodEffort}" type="percent" maxFractionDigits="2"/>
									<g:hiddenField name="staff-${ea.staffId}.previousPeriodEffort" value="${ea.previousPeriodEffort}" />
							  	</div>
							</td>
	
							<!-- checkbox: copy previous to current -->
							<td class="basic" style="text-align:center;">
							  	<g:if test="${ea.previousPeriodEffort && !ea.dateCommitted}">
							    	<input type="checkbox" name="staff-${ea.staffId}.copyPreviousToCurrent"/>
							  	</g:if>
							</td>
	
							<!-- textbox: this period's effort -->
							<td class="basic" style="text-align:right;">
	                			<div class="nowrap">          
	                  				<!-- if current period's assigned effort is already committed, do not display textbox. Otherwise, display textbox -->
	                  				<g:if test="${ea.isCommitted}" >
	                    				<g:hiddenField name="staff-${ea.staffId}.thisPeriodAssignedEffort" value="${g.formatNumber(number:(ea.thisPeriodAssignedEffort ?: 0) * 100, maxFractionDigits:2)}" />
	                    				<g:formatNumber number='${ea.thisPeriodAssignedEffort}' type='percent' />             
	                  				</g:if>
	                  				<g:elseif test="${!ea.isCommitted && ea.thisPeriodAssignedEffort < ea.currentPeriodReportedEffort}">
										<g:textField 
											name="staff-${ea.staffId}.thisPeriodAssignedEffort" 
											class="textfieldBasicRed" 
											style="text-align:right;"
											size="3" 
											value="${g.formatNumber(number:(ea.thisPeriodAssignedEffort ?: 0) * 100, maxFractionDigits:2)}"
										/> %
									</g:elseif>
	                  				<g:else test="${!ea.isCommitted}">
	                    				<g:textField 
											name="staff-${ea.staffId}.thisPeriodAssignedEffort" 
											class="textfieldBasic" 
											style="text-align:right;"
											size="3" 
											value="${g.formatNumber(number:(ea.thisPeriodAssignedEffort ?: 0) * 100, maxFractionDigits:2)}"
										/> %
	                  				</g:else>
	                			</div>
	              			</td>
	
	              			<!-- effort reported -->
	              			<td class="basic" style="text-align:right;">
	                			<div class="nowrap">                                    
	                  				<g:formatNumber number="${ea.currentPeriodReportedEffort}" type="percent" maxFractionDigits="2"/>
	                			</div>
	              			</td>
	
							<!-- date effort committed -->
							<td class="basic" style="text-align:center;">
								<div class="nowrap">                                    
							  		<g:formatDate date="${ea.dateCommitted}" format="MM-dd-yyyy"/>
							  	</div>
							</td>
	
	              			<!-- dates email sent -->
	              			<td class="basic">
	                			<div class="topAlign">${ea.datesEmailsSent?.join(', ')}</div>
	              			</td>
	
	              			<!-- checkbox: send email -->
	              			<td class="basic" style="text-align:center;">
	                			<g:if test="${!ea.isCommitted}" >
	                  				<input type="checkbox" name="staff-${ea.staffId}.sendNow"/>
	                			</g:if>
	              			</td>

          				</tr>

					</g:each>

				</tbody>

			</table>
		    <div class="tableFooterNote">Table row highlighting scheme: 
		    	<span class="backgroundColorGainsboro">not assigned effort</span>, 
		    	<span class="backgroundColorOrange">assigned effort, but not committed</span>.
		    </div>

		</g:form>
      
		<div class="pageSpacing"> </div>
      
	</body>

</html>
