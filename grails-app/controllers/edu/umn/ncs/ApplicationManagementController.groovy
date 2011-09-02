package edu.umn.ncs
import org.joda.time.*
import edu.umn.ncs.dlr.ExportController
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class ApplicationManagementController {

    def authenticateService
    def laborService
	def debug = grailsApplication.config.console.debugging
	
    def index = {
		
		if (debug) {
	        println "APPLICATION MANAGEMENT CONTROLLER > INDEX ---------------------"
	        println "=> params: ${params}"
		}
		
		redirect(action: "list", params: params)
		
    }

    def list = {    
		 
		if (debug) {
	        println "APPLICATION MANAGEMENT CONTROLLER > LIST ----------------------"
	        println "=> params: ${params}"
		}
		                
    }
    
	def report = {
        
		if (debug) {
		    println "APPLICATION MANAGEMENT CONTROLLER > REPORT ---------------------"
	        println "=> params: ${params}"
		}

		// REPORTING PERIOD
		def currentReportingPeriodInstance = laborService.getCurrentReportingPeriod()
		if (debug) {println "=> currentReportingPeriodInstance: ${currentReportingPeriodInstance}"}
			
		def c = ReportingPeriod.createCriteria()
		def periodList = c.list {		
			le("periodDate",currentReportingPeriodInstance.periodDate)
			isNotNull("preparedDate")
			order("periodDate", "desc")
		} 
        if (debug) { println "=> periodList: ${periodList}" }
		
		def reportingPeriodInstanceList = []
        periodList.each{
            reportingPeriodInstanceList.add(['reporting_period_id':it.id, name:g.formatDate(date:it.periodDate, format:'MMMM yyyy')] )
        }
		//if (debug) { println "=> reportingPeriodInstanceList: ${reportingPeriodInstanceList}" }
		
		// REPORT TYPES
		def cR = ReportType.createCriteria()
		def reportTypeList = cR.list {
			eq("obsolete", false)
			order("name", "desc")
		}
		if (debug) { println "=> reportTypeList: ${reportTypeList}" }
		
		def reportTypeInstanceList = []
		reportTypeList.each{
			reportTypeInstanceList.add(['report_type_id':it.id, name:it.name] )
		}
		if (debug) { println "=> reportTypeInstanceList: ${reportTypeInstanceList}" }

		// REPORT FORMAT		
		def reportFormatList = ExportController.allowedFormats
        if (debug) { println "=> reportFormatList: ${reportFormatList}" }
		
		[ 
            reportingPeriodInstanceList: reportingPeriodInstanceList,
			reportTypeInstanceList: reportTypeInstanceList,
			reportFormatList: reportFormatList 
        ]
		
	} 

} //class ApplicationManagementController

