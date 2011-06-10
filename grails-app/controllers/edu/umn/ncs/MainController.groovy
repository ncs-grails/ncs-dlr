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
        redirect(action:'show')        

    }

    def show = {
        
        println "PRINTLN MAIN CONTROLLER > SHOW --------------------------------"
        println "PRINTLN MainController.show.params: ${params}"        

        // REPORTING STAFF
        def principal = authenticateService.principal()                         
        def reportingStaffInstance = laborService.getReportingStaff(principal)
        println "PRINTLN MainController.show.reportingStaffInstance: ${reportingStaffInstance}"

        // REPORTING PERIOD
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        println "PRINTLN MainController.show.reportingPeriodInstance: ${reportingPeriodInstance}"

        // ASSIGNED EFFORT FOR PERIOD, if it exists
        def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, reportingStaffInstance)
        println "PRINTLN MainController.show.assignedEffortInstance: ${assignedEffortInstance}"
        
        [
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance
        ]
       
    } //def show
            
} //class MainController
