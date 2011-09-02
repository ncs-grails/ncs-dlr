package edu.umn.ncs

class StudyTaskEtdlrController {

	def debug = grailsApplication.config.console.debugging
	//def debug = true
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [studyTaskEtdlrInstanceList: StudyTaskEtdlr.list(params), studyTaskEtdlrInstanceTotal: StudyTaskEtdlr.count()]
    }

    def create = {
        def studyTaskEtdlrInstance = new StudyTaskEtdlr()
        studyTaskEtdlrInstance.properties = params
        return [studyTaskEtdlrInstance: studyTaskEtdlrInstance]
    }

    def save = {
        def studyTaskEtdlrInstance = new StudyTaskEtdlr(params)
        if (studyTaskEtdlrInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr'), studyTaskEtdlrInstance.id])}"
            redirect(action: "show", id: studyTaskEtdlrInstance.id)
        }
        else {
            render(view: "create", model: [studyTaskEtdlrInstance: studyTaskEtdlrInstance])
        }
    }

    def show = {
        def studyTaskEtdlrInstance = StudyTaskEtdlr.get(params.id)
        if (!studyTaskEtdlrInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr'), params.id])}"
            redirect(action: "list")
        }
        else {
            [studyTaskEtdlrInstance: studyTaskEtdlrInstance]
        }
    }

    def edit = {
        def studyTaskEtdlrInstance = StudyTaskEtdlr.get(params.id)
        if (!studyTaskEtdlrInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [studyTaskEtdlrInstance: studyTaskEtdlrInstance]
        }
    }

    def update = {
        def studyTaskEtdlrInstance = StudyTaskEtdlr.get(params.id)
        if (studyTaskEtdlrInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (studyTaskEtdlrInstance.version > version) {
                    
                    studyTaskEtdlrInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr')] as Object[], "Another user has updated this StudyTaskEtdlr while you were editing")
                    render(view: "edit", model: [studyTaskEtdlrInstance: studyTaskEtdlrInstance])
                    return
                }
            }
            studyTaskEtdlrInstance.properties = params
            if (!studyTaskEtdlrInstance.hasErrors() && studyTaskEtdlrInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr'), studyTaskEtdlrInstance.id])}"
                redirect(action: "show", id: studyTaskEtdlrInstance.id)
            }
            else {
                render(view: "edit", model: [studyTaskEtdlrInstance: studyTaskEtdlrInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def studyTaskEtdlrInstance = StudyTaskEtdlr.get(params.id)
        if (studyTaskEtdlrInstance) {
            try {
                studyTaskEtdlrInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTaskEtdlr.label', default: 'StudyTaskEtdlr'), params.id])}"
            redirect(action: "list")
        }
    }
}
