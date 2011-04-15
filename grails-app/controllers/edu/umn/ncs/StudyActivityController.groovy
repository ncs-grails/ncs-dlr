package edu.umn.ncs

class StudyActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def scaffold = true

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [studyActivityInstanceList: StudyActivity.list(params), studyActivityInstanceTotal: StudyActivity.count()]
    }

    def create = {
        def studyActivityInstance = new StudyActivity()
        studyActivityInstance.properties = params
        return [studyActivityInstance: studyActivityInstance]
    }

    def save = {
        def studyActivityInstance = new StudyActivity(params)
        if (studyActivityInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), studyActivityInstance.id])}"
            redirect(action: "show", id: studyActivityInstance.id)
        }
        else {
            render(view: "create", model: [studyActivityInstance: studyActivityInstance])
        }
    }

    def show = {
        def studyActivityInstance = StudyActivity.get(params.id)
        if (!studyActivityInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), params.id])}"
            redirect(action: "list")
        }
        else {
            [studyActivityInstance: studyActivityInstance]
        }
    }

    def edit = {
        def studyActivityInstance = StudyActivity.get(params.id)
        if (!studyActivityInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [studyActivityInstance: studyActivityInstance]
        }
    }

    def update = {
        def studyActivityInstance = StudyActivity.get(params.id)
        if (studyActivityInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (studyActivityInstance.version > version) {
                    
                    studyActivityInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'studyActivity.label', default: 'StudyActivity')] as Object[], "Another user has updated this StudyActivity while you were editing")
                    render(view: "edit", model: [studyActivityInstance: studyActivityInstance])
                    return
                }
            }
            studyActivityInstance.properties = params
            if (!studyActivityInstance.hasErrors() && studyActivityInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), studyActivityInstance.id])}"
                redirect(action: "show", id: studyActivityInstance.id)
            }
            else {
                render(view: "edit", model: [studyActivityInstance: studyActivityInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def studyActivityInstance = StudyActivity.get(params.id)
        if (studyActivityInstance) {
            try {
                studyActivityInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), params.id])}"
            redirect(action: "list")
        }
    }
}
