<%@ page contentType="text/html"%><html>
  
  <head>
    <title>NCS Direct Labor Reporting Notification Email</title>
  </head>
  
  <body>
    
    Dear ${reportingStaffInstance.firstName} ${reportingStaffInstance.lastName}:

    <p>Please submit your EFFORT for work done in 
      <strong><g:formatDate date="${reportingPeriodInstance.periodDate}" format="MMMM yyyy" /></strong>
      for the <em>National Children's Study</em>, using the University of Minnesota - National Children's Study system.
    </p>
    
    To access this system:
    
    <ol>
      <li>Go to the <em>secure</em> <strong>University of Minnesota - National Children's Study</strong> login page (<strong>https://secure.ncs.umn.edu/ncs-dlr/</strong>).</li>
      <li>Login with your University of Minnesota Internet ID (x.500 username) <strong>${reportingStaffInstance.username}</strong> and password.</li>
    </ol>
    
    USER SUPPORT<br/>
    phone: 612-625.3453<br/>
    email: ncs_user_support@ncs.umn.edu<br/>
        
  </body>

</html>
