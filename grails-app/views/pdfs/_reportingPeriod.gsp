<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
  
		<title>NCS Study Center Technical Direct Labor Form </title>
      
		<style type="text/css">
        	body { font-size: 8pt; }
			h1 { text-align: center; }
			h2 { text-align: center; }
        	table { border-collapse: collapse; }
        	th td { border:.05em solid black; padding:0.25em; vertical-align:top; }
        	@page { size: 11in 8.5in; }
        </style>
      
	</head>
  
	<body>
  
		<h2>NCS Study Center Technical Direct Labor Form for <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></h2>
		<h2>University of Minnesota - Ramsey County</h2>

    	<hr/>
  
			<dl>
			
				<!-- CONTRACT INFO -->
				<dt>1. Contract Information</dt>
					<dd>1.1. Contract Number: <strong>${projectInfoInstance.contractNumber}</strong></dd>
					<dd>1.2. Contract Period: <strong>${projectInfoInstance.contractPeriod}</strong></dd>
					<dd>1.3. Date Prepared: <strong>${reportingPeriodInstance.preparedDate}</strong></dd>
					<dd>1.4. Reference Invoice Number: <strong>SFR 2706 - ${reportingPeriodInstance.referenceInvoiceNumber}</strong></dd>
					<dd>1.5. Principal Investigator: <strong>${projectInfoInstance.principalInvestigator}</strong></dd>
					
				<!-- STAFF DETAIL -->
				<dt>2. Staff Detail</dt>
					<table>
						<thead>
							<tr>
								<th></th>
								<th>Staff Name</th>
								<th>Labor Category</th>
								<th>Study Activity</th>
								<th>Task (specify))</th>
								<th>Percent Effort Applied</th>
							</tr>
						</thead>
						<tbody>
							<g:each status="d" var="ae" in="${assignedEffortInstanceList}">
								<tr>
									<td rowspan="${ae.reportedEffort.size()}">2.${d + 1}</td>
									<td rowspan="${ae.reportedEffort.size()}">${ae.reportingStaff}</td>
									<td rowspan="${ae.reportedEffort.size()}">${ae.laborCategory}</td>
									<g:each status="i" var="re" in="${ae.reportedEffort}">
										<g:if test="${i > 0}">
											<!-- If this is the second row, let's shift it in. -->
											${"</tr><tr>"}
										</g:if>
										<td>${fieldValue(bean: re, field: "activity")}</td>
										<td>${fieldValue(bean: re, field: "task")}</td>
										<td align="right"><g:formatNumber number="${re.percentEffort}" type="percent" /></td>
									</g:each>
									<g:if test="${ ! ae.reportedEffort}">
										<td/><td/><td/>
									</g:if>
								</tr>
							</g:each>
						</tbody>
					</table>
					
			</dl>
			
	</body>
  
</html>