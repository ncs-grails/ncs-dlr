package edu.umn.ncs

class StudyActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def debug = true

    def index = {
        if (debug) { log.debug "=> StudyActivityController.index.params: ${params}}" }
        redirect(action: "list", params: params)
    }

    def list = {
        
        if (debug) { log.debug "=> StudyActivityController.list.params: ${params}}" }
        if (debug) { log.debug "=> StudyActivityController.list.params.max: ${params.max}" }        
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (debug) { log.debug "=> StudyActivityController.list.params.max: ${params.max}" }
                
        if (debug) { log.debug "=> StudyActivityController.list.StudyActivity.list(): ${StudyActivity.list()}" }
        if (debug) { log.debug "=> StudyActivityController.list.StudyActivity.list(params): ${StudyActivity.list(params)}" }

        if (debug) { log.debug "=> StudyActivityController.list.StudyActivity.count(): ${StudyActivity.count()}" }

        [
            studyActivityInstanceList: StudyActivity.list(params), 
            studyActivityInstanceTotal: StudyActivity.count()
        ]
    }

    def create = {
                
        if (debug) { log.debug "=> StudyActivityController.create.params: ${params}" }
        
        def studyActivityInstance = new StudyActivity()
        //if (debug) { log.debug "=> StudyActivityController.create.studyActivityInstance.properties: ${studyActivityInstance.properties}" }        
        if (debug) { log.debug "=> StudyActivityController.create.studyActivityInstance: ${studyActivityInstance}" }

        studyActivityInstance.properties = params
        //if (debug) { log.debug "=> StudyActivityController.create.studyActivityInstance.properties: ${studyActivityInstance.properties}" }
        if (debug) { log.debug "=> StudyActivityController.create.studyActivityInstance: ${studyActivityInstance}" }
        
        return [studyActivityInstance: studyActivityInstance]
        
    }

    def save = {
                
        if (debug) { log.debug "=> StudyActivityController.save.params: ${params}" }
        
        def studyActivityInstance = new StudyActivity(params)
        if (debug) { log.debug "=> StudyActivityController.save.new studyActivityInstance: ${studyActivityInstance}" }
        
        if (studyActivityInstance.save(flush: true)) {
            if (debug) { log.debug "=> StudyActivityController.save.studyActivityInstance.save = true" }
            
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), studyActivityInstance.id])}"
            redirect(
                action: "show", 
                id: studyActivityInstance.id
            )
        }
        else {
            if (debug) { log.debug "=> StudyActivityController.save.studyActivityInstance.save = false" }
            if (debug) { log.debug "=> StudyActivityController.save.studyActivityInstance: ${studyActivityInstance}" }
            render(view: "create", model: [studyActivityInstance: studyActivityInstance])
        }
        
    }

    def show = {

        if (debug) { log.debug "=> StudyActivityController.show.params: ${params}}" }
        if (debug) { log.debug "=> StudyActivityController.show.params.id: ${params.id}}" }
        
        def studyActivityInstance = StudyActivity.get(params.id)
        if (debug) { log.debug "=> StudyActivityController.show.studyActivityInstance: ${studyActivityInstance}" }
                
        if (!studyActivityInstance) {
            if (debug) { log.debug "=> StudyActivityController.show.(!studyActivityInstance) = true" }
            
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), params.id])}"
            redirect(action: "list")
        }
        else {
            if (debug) { log.debug "=> StudyActivityController.show.(!studyActivityInstance) = false" }
            [studyActivityInstance: studyActivityInstance]
        }
        
    }

    def edit = {
        
        if (debug) { log.debug "=> StudyActivityController.edit.params: ${params}}" }
        if (debug) { log.debug "=> StudyActivityController.edit.params.id: ${params.id}}" }

        def studyActivityInstance = StudyActivity.get(params.id)
        if (debug) { log.debug "=> StudyActivityController.edit.studyActivityInstance: ${studyActivityInstance}" }
        
        if (!studyActivityInstance) {
            if (debug) { log.debug "=> StudyActivityController.show.(!studyActivityInstance) = true" } 
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyActivity.label', default: 'StudyActivity'), params.id])}"
            redirect(action: "list")
        }
        else {
            if (debug) { log.debug "=> StudyActivityController.show.(!studyActivityInstance) = false" }
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
        
    } //def delete 
    
}
