package edu.umn.ncs
import org.joda.time.*
import edu.umn.ncs.dlr.ExportController
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class ApplicationManagementController {

    def authenticateService
    def laborService
	def debug = false

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
		def currentReportingPeriodIsntance = laborService.getCurrentReportingPeriod()
		if (debug) {println "=> currentReportingPeriodIsntance: ${currentReportingPeriodIsntance}"}
			
		def c = ReportingPeriod.createCriteria()
		def periodList = c.list {		
			le("periodDate",currentReportingPeriodIsntance.periodDate)
			isNotNull("preparedDate")
			order("periodDate", "desc")
		} 
        //if (debug) { println "=> periodList: ${periodList}" }
		
		def reportingPeriodInstanceList = []
        periodList.each{
            reportingPeriodInstanceList.add(['reporting_period_id':it.id, name:g.formatDate(date:it.periodDate, format:'MMMM yyyy')] )
        }
		//if (debug) { println "=> reportingPeriodInstanceList: ${reportingPeriodInstanceList}" }
		
		// REPORT TYPES
		def cR = ReportType.createCriteria()
		def reportsList = cR.list {
			eq("obsolete", false)
			order("name", "desc")
		}
		//if (debug) { println "=> reportsList: ${reportsList}" }
		
		def reportsInstanceList = []
		reportsList.each{
			reportsInstanceList.add(['reports_id':it.id, name:it.name] )
		}
		if (debug) { println "=> reportsInstanceList: ${reportsInstanceList}" }

		// REPORT FORMAT		
		def reportFormats = ExportController.allowedFormats
        if (debug) { println "=> reportFormats: ${reportFormats}" }
		
		[ 
            reportingPeriodInstanceList: reportingPeriodInstanceList,
			reportsInstanceList: reportsInstanceList,
			reportFormats: reportFormats 
        ]
		
	} 

} //class ApplicationManagementController

