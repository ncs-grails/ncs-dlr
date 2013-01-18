package edu.umn.ncs
import org.joda.time.*
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE', 'ROLE_NCS_DLR'])
class MainController {

    def springSecurityService
    def laborService
	
    def index = {
        
		log.debug ""
		log.debug "MAIN CONTROLLER > INDEX ---------------------------------------"        
		log.debug "=> params: ${params}"
        
        redirect(action: "show")
    }    
    
    def show = {
		
		log.debug ""
		log.debug "MAIN CONTROLLER > SHOW ----------------------------------------"        
		log.debug "=> params: ${params}"
        
        // REPORTING STAFF
        def principal = springSecurityService.principal                         
        log.debug "=> principal: ${principal}"
		            
        def reportingStaffInstance = laborService.getReportingStaff(principal)
	log.debug "=> reportingStaffInstance: ${reportingStaffInstance}"

        // REPORTING PERIOD
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        log.debug "=> reportingPeriodInstance: ${reportingPeriodInstance}"

        // ASSIGNED EFFORT instance
		def assignedEffortInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)
		log.debug "=> assignedEffortInstance: ${assignedEffortInstance}"
		log.debug "=> assignedEffortInstance.dateCommitted: ${assignedEffortInstance?.dateCommitted}"

        [ reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance ]

    } 
	
} 
