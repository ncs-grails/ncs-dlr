package edu.umn.ncs

class StudyTaskOdeController {

	def debug = true
	//def debug = true
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [studyTaskOdeInstanceList: StudyTaskOde.list(params), studyTaskOdeInstanceTotal: StudyTaskOde.count()]
    }

    def create = {
        def studyTaskOdeInstance = new StudyTaskOde()
        studyTaskOdeInstance.properties = params
        return [studyTaskOdeInstance: studyTaskOdeInstance]
    }

    def save = {
        def studyTaskOdeInstance = new StudyTaskOde(params)
        if (studyTaskOdeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'studyTaskOde.label', default: 'StudyTaskOde'), studyTaskOdeInstance.id])}"
            redirect(action: "show", id: studyTaskOdeInstance.id)
        }
        else {
            render(view: "create", model: [studyTaskOdeInstance: studyTaskOdeInstance])
        }
    }

    def show = {
        def studyTaskOdeInstance = StudyTaskOde.get(params.id)
        if (!studyTaskOdeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTaskOde.label', default: 'StudyTaskOde'), params.id])}"
            redirect(action: "list")
        }
        else {
            [studyTaskOdeInstance: studyTaskOdeInstance]
        }
    }

    def edit = {
        def studyTaskOdeInstance = StudyTaskOde.get(params.id)
        if (!studyTaskOdeInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTaskOde.label', default: 'StudyTaskOde'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [studyTaskOdeInstance: studyTaskOdeInstance]
        }
    }

    def update = {
        def studyTaskOdeInstance = StudyTaskOde.get(params.id)
        if (studyTaskOdeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (studyTaskOdeInstance.version > version) {
                    
                    studyTaskOdeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'studyTaskOde.label', default: 'StudyTaskOde')] as Object[], "Another user has updated this StudyTaskOde while you were editing")
                    render(view: "edit", model: [studyTaskOdeInstance: studyTaskOdeInstance])
                    return
                }
            }
            studyTaskOdeInstance.properties = params
            if (!studyTaskOdeInstance.hasErrors() && studyTaskOdeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'studyTaskOde.label', default: 'StudyTaskOde'), studyTaskOdeInstance.id])}"
                redirect(action: "show", id: studyTaskOdeInstance.id)
            }
            else {
                render(view: "edit", model: [studyTaskOdeInstance: studyTaskOdeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTaskOde.label', default: 'StudyTaskOde'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def studyTaskOdeInstance = StudyTaskOde.get(params.id)
        if (studyTaskOdeInstance) {
            try {
                studyTaskOdeInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'studyTaskOde.label', default: 'StudyTaskOde'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'studyTaskOde.label', default: 'StudyTaskOde'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTaskOde.label', default: 'StudyTaskOde'), params.id])}"
            redirect(action: "list")
        }
    }
}
