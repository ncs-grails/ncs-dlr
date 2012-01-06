package edu.umn.ncs

class StudyTaskController {

	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def debug = grailsApplication.config.console.debugging
	
	def index = {
		redirect(action: "list", params: params)
	}

	def list = {

		if (debug) {
			println "STUDY TASK CONTROLLER > INDEX ---------------------"
			println "=> params: ${params}"
		}

		params.max = Math.min(params.max ? params.int('max') : 10, 100)

		[
			studyTaskInstanceList: StudyTask.list(params), 
			//studyTaskInstanceList: StudyTask.list(params), 
			studyTaskInstanceTotal: StudyTask.count()

		]

	}
/*
	def create = {
		def studyTaskInstance = new StudyTask()
		studyTaskInstance.properties = params
		return [studyTaskInstance: studyTaskInstance]
	}

	def save = {
		def studyTaskInstance = new StudyTask(params)
		if (studyTaskInstance.save(flush: true)) {
		    flash.message = "${message(code: 'default.created.message', args: [message(code: 'studyTask.label', default: 'StudyTask'), studyTaskInstance.id])}"
		    redirect(action: "show", id: studyTaskInstance.id)
		} else {
		    render(view: "create", model: [studyTaskInstance: studyTaskInstance])
		}
	}

	def show = {
		def studyTaskInstance = StudyTask.get(params.id)
		if (!studyTaskInstance) {
		    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTask.label', default: 'StudyTask'), params.id])}"
		    redirect(action: "list")
		} else {
		    [studyTaskInstance: studyTaskInstance]
		}
	}

	def edit = {
		def studyTaskInstance = StudyTask.get(params.id)
		if (!studyTaskInstance) {
		    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTask.label', default: 'StudyTask'), params.id])}"
		    redirect(action: "list")
		} else {
		    return [studyTaskInstance: studyTaskInstance]
		}
	}

	def update = {
		def studyTaskInstance = StudyTask.get(params.id)
		if (studyTaskInstance) {
		    if (params.version) {
			def version = params.version.toLong()
			if (studyTaskInstance.version > version) {
		    
			    studyTaskInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'studyTask.label', default: 'StudyTask')] as Object[], "Another user has updated this StudyTask while you were editing")
			    render(view: "edit", model: [studyTaskInstance: studyTaskInstance])
			    return
			}
		    }
		    studyTaskInstance.properties = params
		    if (!studyTaskInstance.hasErrors() && studyTaskInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.updated.message', args: [message(code: 'studyTask.label', default: 'StudyTask'), studyTaskInstance.id])}"
			redirect(action: "show", id: studyTaskInstance.id)
		    } else {
			render(view: "edit", model: [studyTaskInstance: studyTaskInstance])
		    }
		}
		else {
		    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTask.label', default: 'StudyTask'), params.id])}"
		    redirect(action: "list")
		}
	}

	def delete = {
		def studyTaskInstance = StudyTask.get(params.id)
		if (studyTaskInstance) {
		    try {
			studyTaskInstance.delete(flush: true)
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'studyTask.label', default: 'StudyTask'), params.id])}"
			redirect(action: "list")
		    }
		    catch (org.springframework.dao.DataIntegrityViolationException e) {
			flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'studyTask.label', default: 'StudyTask'), params.id])}"
			redirect(action: "show", id: params.id)
		    }
		} else {
		    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studyTask.label', default: 'StudyTask'), params.id])}"
		    redirect(action: "list")
		}
	}
*/

}
