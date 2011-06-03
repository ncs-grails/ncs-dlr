package edu.umn.ncs

class ReportedEffortController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "create", params: params)
    }

    def create = {
        
        println "PRINTLN ReportedEffortController.create.params: ${params}"        
        
        def reportedEffortInstance = new ReportedEffort()
        reportedEffortInstance.properties = params
        //println "PRINTLN ReportedEffortController.create.reportedEffortInstance.properties: ${reportedEffortInstance.properties}"        
        
        def csa = StudyActivity.createCriteria()        
        def studyActivityList = csa.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //println "PRINTLN ReportedEffortController.create.studyActivityList: ${studyActivityList}"        
        
        def cst = StudyTask.createCriteria()        
        def studyTaskList = cst.list{
            eq("obsolete", false) 
            order("name", "asc")
        }
        //println "PRINTLN ReportedEffortController.create.studyTaskList: ${studyTaskList}"        
        
        return [
            studyActivityList: studyActivityList, 
            studyTaskList: studyTaskList,
            reportedEffortInstance: reportedEffortInstance            
        ]
        
    } //def create

    def save = {
        
        println "start SAVE"
        println "PRINTLN ReportedEffortController.save.params: ${params}"        
        
        //TODO: replace with spring security code
        def username = 'sqv'
        
        def reportedEffortInstance = new ReportedEffort(params)
        println "PRINTLN ReportedEffortController.save.reportedEffortInstance: ${reportedEffortInstance}"        
        
        if ( reportedEffortInstance.percentEffort ) {
            reportedEffortInstance.percentEffort = reportedEffortInstance.percentEffort / 100.0            
        }
        
        reportedEffortInstance.userCreated = username
        
        if (reportedEffortInstance.save(flush: true)) {
            println "successful save"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
            redirect(action: "create")
        }
        else {
            
            println "failed save"
            
            def csa = StudyActivity.createCriteria()        
            def studyActivityList = csa.list{
                eq("obsolete", false) 
                order("name", "asc")
            }

            def cst = StudyTask.createCriteria()        
            def studyTaskList = cst.list{
                eq("obsolete", false) 
                order("name", "asc")
            }
            
            def model = [
                studyActivityList: studyActivityList, 
                studyTaskList: studyTaskList,
                reportedEffortInstance: reportedEffortInstance
            ]
                
            render(view: "create", model: model)
        }
        
    } //def save
    
    
    def delete = {
        
        println "PRINTLN ReportedEffortController.delete.params.id: ${params.id}"        

        def reportedEffortInstance = ReportedEffort.get(params.id)
        println "PRINTLN ReportedEffortController.delete.ReportedEffort.get(params.id): ${ReportedEffort.get(params.id)}"        
        
        /*
        if (reportedEffortInstance) {
            try {
                reportedEffortInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            redirect(action: "list")
        }
        */

    } //def delete
    
            
    def show = {
        def reportedEffortInstance = ReportedEffort.get(params.id)
        if (!reportedEffortInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            redirect(action: "list")
        }
        else {
            [reportedEffortInstance: reportedEffortInstance]
        }
    }

    def edit = {
        def reportedEffortInstance = ReportedEffort.get(params.id)
        if (!reportedEffortInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [reportedEffortInstance: reportedEffortInstance]
        }
    }

    def update = {
        def reportedEffortInstance = ReportedEffort.get(params.id)
        if (reportedEffortInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (reportedEffortInstance.version > version) {
                    
                    reportedEffortInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'reportedEffort.label', default: 'ReportedEffort')] as Object[], "Another user has updated this ReportedEffort while you were editing")
                    render(view: "edit", model: [reportedEffortInstance: reportedEffortInstance])
                    return
                }
            }
            reportedEffortInstance.properties = params
            if (!reportedEffortInstance.hasErrors() && reportedEffortInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
                redirect(action: "show", id: reportedEffortInstance.id)
            }
            else {
                render(view: "edit", model: [reportedEffortInstance: reportedEffortInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            redirect(action: "list")
        }
    }
    
}
