<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="ncs" />
    <link rel="stylesheet" type="text/css" href="${resource(dir:'css',file:'custom.css')}" />
    <title>NCS DLR Application Management</title>
  </head>

  <body>
      <h1>DLR Application Management</h1>

      <p class="breadcrumbs">
        <g:link controller="main" action="index">DLR Home</g:link>
        &nbsp;&nbsp;&gt&nbsp;&nbsp;
        <span class="fontMaroon">Application Management</span>
        <!--
        &nbsp;&nbsp;&gt&nbsp;&nbsp;
        Staff
        &nbsp;&nbsp;::&nbsp;&nbsp;
        <g:link controller="assignEffort" action="show">Effort Assignment</g:link>
        &nbsp;&nbsp;::&nbsp;&nbsp;
        Report Generation
        &nbsp;&nbsp;::&nbsp;&nbsp;
        Report Archive   
        -->
      </p>
      
      <dl>
        <dt>Staff</dt>
          <dd>Enter staff information</dd>
        <dt><g:link controller="assignEffort" action="show">Effort Assignment</g:link></dt>
          <dd>Enter assigned effort</dd>
        <dt>Report Generation</dt>
          <dd>Generate reports for submission</dd>
        <dt>Report Archive</dt>
          <dd>View past reported effort</dd>
      </dl>

      <div class="pageSpacing"> </div>

  </body>

</html>
