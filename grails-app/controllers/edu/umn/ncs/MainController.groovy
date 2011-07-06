package edu.umn.ncs
import org.joda.time.*

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class MainController {

    def authenticateService
    def laborService

    def index = {
        
        println "PRINTLN MAIN CONTROLLER > INDEX -------------------------------"        
        println "PRINTLN MainController.index.params: ${params}"        
        
        redirect(action: "show")

    }    
    
    def show = {
        
        println "PRINTLN MAIN CONTROLLER > SHOW --------------------------------"        
        println "PRINTLN MainController.show.params: ${params}"        
        
        // create REPORTING STAFF, REPORTING PERIOD, & ASSIGNED EFFORT instance
        def principal = authenticateService.principal()                         
        def reportingStaffInstance = laborService.getReportingStaff(principal)        
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()     
        def assignedEffortInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)

        println "PRINTLN MainController.show.reportingStaffInstance: ${reportingStaffInstance}"            
        println "PRINTLN MainController.show.reportingPeriodInstance: ${reportingPeriodInstance}"
        println "PRINTLN MainController.show.assignedEffortInstance: ${assignedEffortInstance}"            
        if ( assignedEffortInstance ) {
            println "PRINTLN MainController.show.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"                        
        }
        
        [
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance
        ]

    } 
            
} 
