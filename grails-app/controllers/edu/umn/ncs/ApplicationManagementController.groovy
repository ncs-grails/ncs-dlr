package edu.umn.ncs
import org.joda.time.*
import edu.umn.ncs.dlr.ExportController

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ApplicationManagementController {

    def authenticateService
    def laborService

    // display APPLICATION MANAGEMENT page
    def index = {     
        println "PRINTLN ApplicationManagementController.index.params: ${params}"                
    }
    
	def reports = {
		def reportingPeriodInstanceList = ReportingPeriod.list()
		def reportFormats = ExportController.allowedFormats
		
		[ reportingPeriodInstanceList: reportingPeriodInstanceList,
			reportFormats: reportFormats ]
	}

}

