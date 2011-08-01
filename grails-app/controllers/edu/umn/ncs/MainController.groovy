package edu.umn.ncs
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class MainController {

    def authenticateService
    def laborService

    def index = {
        
        if (debug) { 
			println "MAIN CONTROLLER > INDEX ---------------------------------------"        
			println "=> params: ${params}"
        }        
        
        redirect(action: "show")

    }    
    
    def show = {
        
        if (debug) { 
			println "MAIN CONTROLLER > SHOW ----------------------------------------"        
			println "=> params: ${params}"
        }        
        
        // create REPORTING STAFF, REPORTING PERIOD, & ASSIGNED EFFORT instance
        def principal = authenticateService.principal()                         
        def reportingStaffInstance = laborService.getReportingStaff(principal)        
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()     
        def assignedEffortInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)

        if (debug) { 
			println "=> reportingStaffInstance: ${reportingStaffInstance}"            
			println "=> reportingPeriodInstance: ${reportingPeriodInstance}"
			println "=> assignedEffortInstance: ${assignedEffortInstance}"
			if ( assignedEffortInstance ) {
				println "=> assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"
			}
        }            
        
        [
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance
        ]

    }
		            
	
} 
