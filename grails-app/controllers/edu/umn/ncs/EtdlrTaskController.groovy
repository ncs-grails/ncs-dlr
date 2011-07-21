package edu.umn.ncs

class EtdlrTaskController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [etdlrTaskInstanceList: EtdlrTask.list(params), etdlrTaskInstanceTotal: EtdlrTask.count()]
    }

    def create = {
        def etdlrTaskInstance = new EtdlrTask()
        etdlrTaskInstance.properties = params
        return [etdlrTaskInstance: etdlrTaskInstance]
    }

    def save = {
        def etdlrTaskInstance = new EtdlrTask(params)
        if (etdlrTaskInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'etdlrTask.label', default: 'EtdlrTask'), etdlrTaskInstance.id])}"
            redirect(action: "show", id: etdlrTaskInstance.id)
        }
        else {
            render(view: "create", model: [etdlrTaskInstance: etdlrTaskInstance])
        }
    }

    def show = {
        def etdlrTaskInstance = EtdlrTask.get(params.id)
        if (!etdlrTaskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'etdlrTask.label', default: 'EtdlrTask'), params.id])}"
            redirect(action: "list")
        }
        else {
            [etdlrTaskInstance: etdlrTaskInstance]
        }
    }

    def edit = {
        def etdlrTaskInstance = EtdlrTask.get(params.id)
        if (!etdlrTaskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'etdlrTask.label', default: 'EtdlrTask'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [etdlrTaskInstance: etdlrTaskInstance]
        }
    }

    def update = {
        def etdlrTaskInstance = EtdlrTask.get(params.id)
        if (etdlrTaskInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (etdlrTaskInstance.version > version) {
                    
                    etdlrTaskInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'etdlrTask.label', default: 'EtdlrTask')] as Object[], "Another user has updated this EtdlrTask while you were editing")
                    render(view: "edit", model: [etdlrTaskInstance: etdlrTaskInstance])
                    return
                }
            }
            etdlrTaskInstance.properties = params
            if (!etdlrTaskInstance.hasErrors() && etdlrTaskInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'etdlrTask.label', default: 'EtdlrTask'), etdlrTaskInstance.id])}"
                redirect(action: "show", id: etdlrTaskInstance.id)
            }
            else {
                render(view: "edit", model: [etdlrTaskInstance: etdlrTaskInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'etdlrTask.label', default: 'EtdlrTask'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def etdlrTaskInstance = EtdlrTask.get(params.id)
        if (etdlrTaskInstance) {
            try {
                etdlrTaskInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'etdlrTask.label', default: 'EtdlrTask'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'etdlrTask.label', default: 'EtdlrTask'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'etdlrTask.label', default: 'EtdlrTask'), params.id])}"
            redirect(action: "list")
        }
    }
}
