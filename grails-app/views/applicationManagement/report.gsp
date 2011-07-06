<%@ page contentType="text/html;charset=UTF-8" %>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS DLR Application Management</title>
  </head>

  <body>

    <!-- NAVIGATION -->
    <p class="breadcrumbs">
      <g:link controller="main" action="index">DLR Home</g:link>
      &nbsp;&nbsp;&gt&nbsp;&nbsp;
      <g:link controller="applicationManagement" action="index">Application Management</g:link>      
    </p>
      
    <h1>Report Generation</h1>

    <fieldset>            
      
      <g:form controller="export" action="reportingPeriod">

        <div class="clearBoth"
            
          <span class="controlBackground">              

            <span class="spacing">
              <label for="id">Reporting Period</label>          
              <g:select class="basic" 
                name="id" 
                from="${reportingPeriodInstanceList}" 
                optionKey="id" 
              />                
            </span>              

            <span class="spacing">
              <label for="format">Output Format</label>
              <g:select class="basic" 
                name="format" 
                from="${reportFormats }" 
              />
            </span>              

            <span class="spacing">
              <g:submitButton class="buttonBasic" name="submit" value="GENERATE" />
            </span>              

          </span>
            
        </div>

        
          
      </g:form>
      
    </fieldset>
    
    <div class="pageSpacing"> </div>

  </body>

</html>