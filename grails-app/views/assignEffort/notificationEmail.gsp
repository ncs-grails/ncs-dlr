<%@ page contentType="text/html"%><html>

	<head>
		<title>**REVISED/NEW** INSTRUCTIONS FOR NCS DIRECT LABOR REPORTING FOR NEW/JOFOC CONTRACT EFFECTIVE 9/28/12</title>
	</head>

	<body>

		Dear ${reportingStaffInstance.firstName} ${reportingStaffInstance.lastName}:

		<p>Please submit your EFFORT for work done in <g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /> 
			for the National Children's Study, using the University of Minnesota - National Children's Study system. 
			<ul>
				<li>
					<strong>All effort should be under the "NCS Follow-on Contract" activity, using as few "tasks" as feasible to describe your effort</strong>
					(no more than 3)
				</li>
				<li>
					Please contact your NCS supervisor if you have questions regarding your effort.
				</li>
			<ul/>
			
		</p>

		To access this system:
		<ol>
			<li>Open Mozilla Firefox browser.</li>
			<li>Go to the <em>secure</em> <strong>University of Minnesota - National Children's Study</strong> 
				login page (<strong><a href="https://secure.ncs.umn.edu/ncs-dlr/">https://secure.ncs.umn.edu/ncs-dlr/</a></strong>).
			</li>
			<li>Login with your University of Minnesota Internet ID (x.500 username) <strong>${reportingStaffInstance.username}</strong> and password.</li>
		</ol>

		USER SUPPORT<br/>
		phone: 612-626-8160 (ask for NCS IT support)<br/>
		email: help@ncs.umn.edu<br/>

	</body>

</html>
