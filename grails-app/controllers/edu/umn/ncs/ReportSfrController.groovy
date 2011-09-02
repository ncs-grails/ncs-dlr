package edu.umn.ncs

class ReportSfrController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def debug = grailsApplication.config.console.debugging
	//def debug = true
		
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [reportSfrInstanceList: ReportSfr.list(params), reportSfrInstanceTotal: ReportSfr.count()]
    }

    def create = {
        def reportSfrInstance = new ReportSfr()
        reportSfrInstance.properties = params
        return [reportSfrInstance: reportSfrInstance]
    }

    def save = {
        def reportSfrInstance = new ReportSfr(params)
        if (reportSfrInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportSfr.label', default: 'ReportSfr'), reportSfrInstance.id])}"
            redirect(action: "show", id: reportSfrInstance.id)
        }
        else {
            render(view: "create", model: [reportSfrInstance: reportSfrInstance])
        }
    }

    def show = {
        def reportSfrInstance = ReportSfr.get(params.id)
        if (!reportSfrInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportSfr.label', default: 'ReportSfr'), params.id])}"
            redirect(action: "list")
        }
        else {
            [reportSfrInstance: reportSfrInstance]
        }
    }

    def edit = {
        def reportSfrInstance = ReportSfr.get(params.id)
        if (!reportSfrInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportSfr.label', default: 'ReportSfr'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [reportSfrInstance: reportSfrInstance]
        }
    }

    def update = {
        def reportSfrInstance = ReportSfr.get(params.id)
        if (reportSfrInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (reportSfrInstance.version > version) {
                    
                    reportSfrInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'reportSfr.label', default: 'ReportSfr')] as Object[], "Another user has updated this ReportSfr while you were editing")
                    render(view: "edit", model: [reportSfrInstance: reportSfrInstance])
                    return
                }
            }
            reportSfrInstance.properties = params
            if (!reportSfrInstance.hasErrors() && reportSfrInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reportSfr.label', default: 'ReportSfr'), reportSfrInstance.id])}"
                redirect(action: "show", id: reportSfrInstance.id)
            }
            else {
                render(view: "edit", model: [reportSfrInstance: reportSfrInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportSfr.label', default: 'ReportSfr'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def reportSfrInstance = ReportSfr.get(params.id)
        if (reportSfrInstance) {
            try {
                reportSfrInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'reportSfr.label', default: 'ReportSfr'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'reportSfr.label', default: 'ReportSfr'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportSfr.label', default: 'ReportSfr'), params.id])}"
            redirect(action: "list")
        }
    }
}
