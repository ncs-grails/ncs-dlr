package edu.umn.ncs

class ReportingStaffController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        
        println "PRINTLN REPORTING STAFF CONTROLLER > INDEX --------------------"                
        println "PRINTLN ReportingStaffController.index.params: ${params}"
		                
        redirect(action: "list", params: params)
		
    } //def index

    def list = {
		
        println "PRINTLN REPORTING STAFF CONTROLLER > LIST --------------------"                
        println "PRINTLN ReportingStaffController.list.params: ${params}"
		                
        //params.max = Math.min(params.max ? params.int('max') : 10, 100)
		//println "PRINTLN ReportingStaffController.list.params.max: ${params.max}"
		
		def reportsEffortStaffInstanceList = ReportingStaff.list(params)
		
		
		/*
		def c = ReportingStaff.createCriteria()
		def reportsEffortStaffInstanceList = c.list{
			eq("reportsEffort", true)
            //order("lastName", "asc")
            //order("firstName", "asc")
            //order("middleInit", "asc")			
		}
		*/
		//def reportsEffortStaffInstanceList = ReportingStaff.findAllWhere(reportsEffort:true) 
		//def reportsEffortStaffInstanceList = ReportingStaff.list()
		
		/*
		def c2 = ReportingStaff.createCriteria()
		def notReportsEffortStaffInstanceList = c2.list{
			eq("reportsEffort", false)
			order("lastName", "asc")
			order("firstName", "asc")
			order("middleInit", "asc")
		}
		*/
		//def notReportsEffortStaffInstanceList = ReportingStaff.findAllWhere(reportsEffort:false) 
		
        [
			reportsEffortStaffInstanceList: reportsEffortStaffInstanceList
		]
		
    } //def list 
	
    def create = {
        def reportingStaffInstance = new ReportingStaff()
        reportingStaffInstance.properties = params
        return [reportingStaffInstance: reportingStaffInstance]
    }

    def save = {
        def reportingStaffInstance = new ReportingStaff(params)
        if (reportingStaffInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), reportingStaffInstance.id])}"
            redirect(action: "show", id: reportingStaffInstance.id)
        }
        else {
            render(view: "create", model: [reportingStaffInstance: reportingStaffInstance])
        }
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

    def edit = {
		
        def reportingStaffInstance = ReportingStaff.get(params.id)
        if (!reportingStaffInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingStaff.label', default: 'ReportingStaff'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [reportingStaffInstance: reportingStaffInstance]
        }
		
    } //def edit

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
}
