package edu.umn.ncs
import org.joda.time.*
//import org.joda.time.format.*
//import java.text.DateFormat


// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class MainController {

    def authenticateService
    def laborService

    def index = {
        
        println "PRINTLN MAIN CONTROLLER > INDEX -------------------------------"        
        println "PRINTLN MainController.index.params: ${params}"        
        
        // create REPORTING STAFF, REPORTING PERIOD, & ASSIGNED EFFORT instance
        def principal = authenticateService.principal()                         
        def reportingStaffInstance = laborService.getReportingStaff(principal)
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        def assignedEffortInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)

        println "PRINTLN MainController.index.reportingStaffInstance: ${reportingStaffInstance}"            
        println "PRINTLN MainController.index.reportingPeriodInstance: ${reportingPeriodInstance}"
        println "PRINTLN MainController.index.assignedEffortInstance: ${assignedEffortInstance}"            
        println "PRINTLN MainController.index.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"            
        
        [
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance
        ]

    } 
            
} 
