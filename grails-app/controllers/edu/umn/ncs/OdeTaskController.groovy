package edu.umn.ncs

class OdeTaskController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [odeTaskInstanceList: OdeTask.list(params), odeTaskInstanceTotal: OdeTask.count()]
    }

    def create = {
        def odeTaskInstance = new OdeTask()
        odeTaskInstance.properties = params
        return [odeTaskInstance: odeTaskInstance]
    }

    def save = {
        def odeTaskInstance = new OdeTask(params)
        if (odeTaskInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'odeTask.label', default: 'OdeTask'), odeTaskInstance.id])}"
            redirect(action: "show", id: odeTaskInstance.id)
        }
        else {
            render(view: "create", model: [odeTaskInstance: odeTaskInstance])
        }
    }

    def show = {
        def odeTaskInstance = OdeTask.get(params.id)
        if (!odeTaskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'odeTask.label', default: 'OdeTask'), params.id])}"
            redirect(action: "list")
        }
        else {
            [odeTaskInstance: odeTaskInstance]
        }
    }

    def edit = {
        def odeTaskInstance = OdeTask.get(params.id)
        if (!odeTaskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'odeTask.label', default: 'OdeTask'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [odeTaskInstance: odeTaskInstance]
        }
    }

    def update = {
        def odeTaskInstance = OdeTask.get(params.id)
        if (odeTaskInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (odeTaskInstance.version > version) {
                    
                    odeTaskInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'odeTask.label', default: 'OdeTask')] as Object[], "Another user has updated this OdeTask while you were editing")
                    render(view: "edit", model: [odeTaskInstance: odeTaskInstance])
                    return
                }
            }
            odeTaskInstance.properties = params
            if (!odeTaskInstance.hasErrors() && odeTaskInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'odeTask.label', default: 'OdeTask'), odeTaskInstance.id])}"
                redirect(action: "show", id: odeTaskInstance.id)
            }
            else {
                render(view: "edit", model: [odeTaskInstance: odeTaskInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'odeTask.label', default: 'OdeTask'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def odeTaskInstance = OdeTask.get(params.id)
        if (odeTaskInstance) {
            try {
                odeTaskInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'odeTask.label', default: 'OdeTask'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'odeTask.label', default: 'OdeTask'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'odeTask.label', default: 'OdeTask'), params.id])}"
            redirect(action: "list")
        }
    }
}
