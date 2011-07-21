package edu.umn.ncs

class SfrReportController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [sfrReportInstanceList: SfrReport.list(params), sfrReportInstanceTotal: SfrReport.count()]
    }

    def create = {
        def sfrReportInstance = new SfrReport()
        sfrReportInstance.properties = params
        return [sfrReportInstance: sfrReportInstance]
    }

    def save = {
        def sfrReportInstance = new SfrReport(params)
        if (sfrReportInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'sfrReport.label', default: 'SfrReport'), sfrReportInstance.id])}"
            redirect(action: "show", id: sfrReportInstance.id)
        }
        else {
            render(view: "create", model: [sfrReportInstance: sfrReportInstance])
        }
    }

    def show = {
        def sfrReportInstance = SfrReport.get(params.id)
        if (!sfrReportInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sfrReport.label', default: 'SfrReport'), params.id])}"
            redirect(action: "list")
        }
        else {
            [sfrReportInstance: sfrReportInstance]
        }
    }

    def edit = {
        def sfrReportInstance = SfrReport.get(params.id)
        if (!sfrReportInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sfrReport.label', default: 'SfrReport'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [sfrReportInstance: sfrReportInstance]
        }
    }

    def update = {
        def sfrReportInstance = SfrReport.get(params.id)
        if (sfrReportInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (sfrReportInstance.version > version) {
                    
                    sfrReportInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'sfrReport.label', default: 'SfrReport')] as Object[], "Another user has updated this SfrReport while you were editing")
                    render(view: "edit", model: [sfrReportInstance: sfrReportInstance])
                    return
                }
            }
            sfrReportInstance.properties = params
            if (!sfrReportInstance.hasErrors() && sfrReportInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'sfrReport.label', default: 'SfrReport'), sfrReportInstance.id])}"
                redirect(action: "show", id: sfrReportInstance.id)
            }
            else {
                render(view: "edit", model: [sfrReportInstance: sfrReportInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sfrReport.label', default: 'SfrReport'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def sfrReportInstance = SfrReport.get(params.id)
        if (sfrReportInstance) {
            try {
                sfrReportInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'sfrReport.label', default: 'SfrReport'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'sfrReport.label', default: 'SfrReport'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sfrReport.label', default: 'SfrReport'), params.id])}"
            redirect(action: "list")
        }
    }
}
