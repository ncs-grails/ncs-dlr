package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR-MANAGE'])
class ReportingStaffController {

    def authenticateService

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        
        println "PRINTLN REPORTING STAFF CONTROLLER > INDEX --------------------"                
        println "PRINTLN ReportingStaffController.index.params: ${params}"
		                
        redirect(action: "list", params: params)
		
    } //def index

    def list = {
		
        println "PRINTLN REPORTING STAFF CONTROLLER > LIST REPORTS -------------"                
        println "PRINTLN params: ${params}"
		                
        //params.max = Math.min(params.max ? params.int('max') : 10, 100)
		//println "PRINTLN ReportingStaffController.list.params.max: ${params.max}"
		
		//def reportsEffortStaffInstanceList = ReportingStaff.list(params)		
		//println "reportsEffortStaffInstanceList:  ${reportsEffortStaffInstanceList}"
		
		def c = ReportingStaff.createCriteria()
		def reportsEffortStaffInstanceList = c.list{
			eq("reportsEffort", true)
			order("lastName", "asc")
			order("firstName", "asc")
			order("middleInit", "asc")
        }
			
		def c2 = ReportingStaff.createCriteria()
		def reportsEffortStaffInstanceCount = c2.list{
			eq("reportsEffort", true)
			projections {
				rowCount()
			}
        }
		println "reportsEffortStaffInstanceCount:  ${reportsEffortStaffInstanceCount}"
		
		[
			reportsEffortStaffInstanceList: reportsEffortStaffInstanceList,
			reportsEffortStaffInstanceCount: reportsEffortStaffInstanceCount 
		]
		
    } //def list

    def listDoesNotReport = {
		
        println "PRINTLN REPORTING STAFF CONTROLLER > LIST DOES NOT REPORT -----"                
        println "PRINTLN params: ${params}"
		                
        //params.max = Math.min(params.max ? params.int('max') : 10, 100)
		//println "PRINTLN ReportingStaffController.list.params.max: ${params.max}"
		
		//def reportsEffortStaffInstanceList = ReportingStaff.list(params)		
		//println "reportsEffortStaffInstanceList:  ${reportsEffortStaffInstanceList}"
				
		def c3 = ReportingStaff.createCriteria()
		def doesNotReportEffortStaffInstanceList = c3.list{
			eq("reportsEffort", false)
			order("lastName", "asc")
			order("firstName", "asc")
			order("middleInit", "asc")
        }
				
		//println "doesNotReportEffortStaffInstanceList:  ${doesNotReportEffortStaffInstanceList}"
		
		def c4 = ReportingStaff.createCriteria()
		def doesNotReportEffortStaffInstanceCount = c4.list{
			eq("reportsEffort", false)
			projections {
				rowCount()
			}
        }
		println "doesNotReportEffortStaffInstanceCount: ${doesNotReportEffortStaffInstanceCount}"

		[
			doesNotReportEffortStaffInstanceList: doesNotReportEffortStaffInstanceList, 
			doesNotReportEffortStaffInstanceCount: doesNotReportEffortStaffInstanceCount
		]
		
    } //def listDoesNotReport

	def create = {
		
        println "PRINTLN REPORTING STAFF CONTROLLER > CREATE (ADD) -------------"                
        println "PRINTLN params: ${params}"

		// REPORTING STAFF
		def reportingStaffInstance = new ReportingStaff()
        reportingStaffInstance.properties = params
		println "PRINTLN reportingStaffInstance: ${reportingStaffInstance}"		
		
        return [reportingStaffInstance: reportingStaffInstance]
		
    } //def create

	def save = {
		
        println "PRINTLN REPORTING STAFF CONTROLLER > SAVE ---------------------"                
        println "PRINTLN ReportingStaffController.params: ${params}"
		
		// REPORTING STAFF
		def reportingStaffInstance = new ReportingStaff(params)
		println "PRINTLN reportingStaffInstance: ${reportingStaffInstance}"
		println "PRINTLN reportingStaffInstance.id: ${reportingStaffInstance.id}"

		// USER CREATED
		def principal = authenticateService.principal()
		def userCreated = principal.getUsername()
		println "PRINTLN userCreated: ${userCreated}"

		if ( reportingStaffInstance ) {
			println "PRINTLN if(reportingStaffInstance) = TRUE"			
			reportingStaffInstanceK.userCreated = userCreated 
			println "PRINTLN reportingStaffIsntance.userCreated: reportingStaffIsntance.userCreated"			
		}
				
		if (reportingStaffInstance.save(flush: true)) {
			
			println "PRINTLN SAVE SUCCESSFULLY"
			
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), reportingStaffInstance.id])}"
			//redirect(action: "show", id: reportingStaffInstance.id)
			render(view: "create", model: [reportingStaffInstance: reportingStaffInstance])
			
		} else {
		
			println "PRINTLN SAVE FAILED"
			render(view: "create", model: [reportingStaffInstance: reportingStaffInstance])
			
		}
		
	} //def save

	def edit = {
		
        println "PRINTLN REPORTING STAFF CONTROLLER > EDIT ---------------------"                
        println "PRINTLN params: ${params}"

		def reportingStaffInstance = ReportingStaff.get(params.id)
		println "PRINTLN reportingStaffInstance: ${reportingStaffInstance}"
		
		if ( reportingStaffInstance ) {
			println "PRINTLN if(reportingStaffInstance) =  TRUE"
			
			return [reportingStaffInstance: reportingStaffInstance]
			
		} else {
		
			println "PRINTLN if(reportingStaffInstance) =  FALSE"
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), params.id])}"
			redirect(action: "list")
			
		}
		
	} //def edit

	def update = {
		
        println "PRINTLN REPORTING STAFF CONTROLLER > UPDATE -------------------"                
        println "PRINTLN params: ${params}"

		def reportingStaffInstance = ReportingStaff.get(params.id)
		println "PRINTLN reportingStaffInstance: ${reportingStaffInstance}"
		
		if (reportingStaffInstance) {
			
			println "if (reportingStaffInstance) = TRUE"	
			if (params.version) {
				
				def version = params.version.toLong()
				if (reportingStaffInstance.version > version) {					
					reportingStaffInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'reportingStaff.label', default: 'ReportingStaff')] as Object[], "Another user has updated this ReportingStaff while you were editing")
					render(view: "edit", model: [reportingStaffInstance: reportingStaffInstance])
					return
				}
				
			}
			
			reportingStaffInstance.properties = params
			
			if ( !reportingStaffInstance.hasErrors() && reportingStaffInstance.save(flush: true) ) {
				
				println "UPDATES SUCCESSFULLY"	
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), reportingStaffInstance.id])}"
				//redirect(action: "show", id: reportingStaffInstance.id)
				render(view: "edit", model: [reportingStaffInstance: reportingStaffInstance])
				
				
			} else {
			
				println "UPDATE FAILED"	
				render(view: "edit", model: [reportingStaffInstance: reportingStaffInstance])
				
			}
			
		} else {
		
			println "if (reportingStaffInstance) = FALSE"	
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), params.id])}"
			redirect(action: "list")
			
		}
		
	} //def update

		
/*
    def create = {
        def reportingStaffInstance = new ReportingStaff()
        reportingStaffInstance.properties = params
        return [reportingStaffInstance: reportingStaffInstance]
    }


    def show = {
        def reportingStaffInstance = ReportingStaff.get(params.id)
        if (!reportingStaffInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), params.id])}"
            redirect(action: "list")
        }
        else {
            [reportingStaffInstance: reportingStaffInstance]
        }
    }

    def update = {
		
        def reportingStaffInstance = ReportingStaff.get(params.id)
        if (reportingStaffInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (reportingStaffInstance.version > version) {
                    
                    reportingStaffInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'reportingStaff.label', default: 'ReportingStaff')] as Object[], "Another user has updated this ReportingStaff while you were editing")
                    render(view: "edit", model: [reportingStaffInstance: reportingStaffInstance])
                    return
                }
            }
            reportingStaffInstance.properties = params
            if (!reportingStaffInstance.hasErrors() && reportingStaffInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), reportingStaffInstance.id])}"
                redirect(action: "show", id: reportingStaffInstance.id)
            }
            else {
                render(view: "edit", model: [reportingStaffInstance: reportingStaffInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), params.id])}"
            redirect(action: "list")
        }
		
    } //def update

    def delete = {
		
        def reportingStaffInstance = ReportingStaff.get(params.id)
        if (reportingStaffInstance) {
            try {
                reportingStaffInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), params.id])}"
            redirect(action: "list")
        }
		
    } //def delete
    
*/
	    
}
