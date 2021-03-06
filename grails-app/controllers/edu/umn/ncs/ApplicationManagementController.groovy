package edu.umn.ncs
import org.joda.time.*
import edu.umn.ncs.dlr.ExportController
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class ApplicationManagementController {

	def springSecurityService
	def laborService
	def debug = true
		
	def index = {
		
		log.debug ""
		log.debug "APPLICATION MANAGEMENT CONTROLLER > INDEX ---------------------"
		log.debug "=> params: ${params}"
		
		redirect(action: "list", params: params)
		
	}

	def list = {    
	 
		log.debug ""
		log.debug "APPLICATION MANAGEMENT CONTROLLER > LIST ----------------------"
		log.debug "=> params: ${params}"
			
	}
    
	def report = {
        
		log.debug ""
		log.debug "APPLICATION MANAGEMENT CONTROLLER > REPORT ---------------------"
		log.debug "=> params: ${params}"

		// REPORTING PERIOD
		def currentReportingPeriodInstance = ReportingPeriod.findById(76)
		//def currentReportingPeriodInstance = laborService.getCurrentReportingPeriod()
		log.debug "=> currentReportingPeriodInstance: ${currentReportingPeriodInstance}"
			
		def c = ReportingPeriod.createCriteria()
		def periodList = c.list {		
			le("periodDate",currentReportingPeriodInstance.periodDate)
			isNotNull("preparedDate")
			order("periodDate", "desc")
		} 
		//log.debug "=> periodList: ${periodList}" 
		
		def reportingPeriodInstanceList = []
		periodList.each{
			reportingPeriodInstanceList.add(['reporting_period_id':it.id, name:g.formatDate(date:it.periodDate, format:'MMMM yyyy')] )
		}
		log.debug "=> reportingPeriodInstanceList: ${reportingPeriodInstanceList}" 
		
		// REPORT TYPES
		def cR = ReportType.createCriteria()
		def reportTypeList = cR.list {
			eq("obsolete", false)
			order("name", "desc")
		}
		log.debug "=> reportTypeList: ${reportTypeList}" 
		
		def reportTypeInstanceList = []
		reportTypeList.each{ reportTypeInstanceList.add(['report_type_id':it.id, name:it.name] ) }
		if (debug) { log.debug "=> reportTypeInstanceList: ${reportTypeInstanceList}" }

		// REPORT FORMAT		
		def reportFormatList = ExportController.allowedFormats
		if (debug) { log.debug "=> reportFormatList: ${reportFormatList}" }
		
		[ 
			reportingPeriodInstanceList: reportingPeriodInstanceList,
			reportTypeInstanceList: reportTypeInstanceList,
			reportFormatList: reportFormatList 
		]
		
	} // def report 

}
