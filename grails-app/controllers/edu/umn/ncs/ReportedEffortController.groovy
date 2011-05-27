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
        
        return [
            studyActivityList: studyActivityList, 
            studyTaskList: studyTaskList,
            reportedEffortInstance: reportedEffortInstance            
        ]
        
    } //def create

    def save = {
        
        println "start SAVE"
        
        def reportedEffortInstance = new ReportedEffort(params)
        if (reportedEffortInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
            println "success"
            redirect(action: "create")
        }
        else {
            
            println "problem saving"
            
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
        
        /*
        def reportedEffortValue = params?.reportedEffort
        if ( reportedEffortValue ) {
            //println "PRINTLN (reportedEffortValue)"        
            if ( reportedEffortValue =~ /[0-9]{1,3}\.?[0-9]{0,2}/ ) {
                //println "PRINTLN ( reportedEffortValue is BigDecimal )"        

                reportedEffortValue = reportedEffortValue.toBigDecimal()
                
                def r = 0.0..100.0
                    
                if ( r.containsWithinBounds(reportedEffortValue) ) {
                    //println "PRINTLN ( within range )"        
                    reportedEffortValue = reportedEffortValue
                } else {
                    //println "PRINTLN ( NOT within range )"        
                    reportedEffortValue = null
                } 
                
            } else {
                //println "PRINTLN ( reportedEffortValue is NOT BigDecimal )"        
                reportedEffortValue = null
            }
        } else {
            //println "PRINTLN (!reportedEffortValue)"        
            reportedEffortValue = null
        }
        println "PRINTLN ReportedEffortController.save.reportedEffortValue: ${reportedEffortValue}"        
       

        // if user data entered is NOT sufficient, direct user back to controller: reportedEffort, action:create
        if ( !reportedEffortValue ) {
            
            if ( studyActivityIdValue == 0 ) {                
                flash.message = "Must select a Study Activity."                                        
            } else if ( studyTaskIdValue == 0 ) {                
                flash.message = "Must select a Task."                                        
            } else if ( !reportedEffortValue ) {                
                flash.message = "Must enter a valid percent effort."                                        
            }
            println "PRINTLN ReportedEffortController.save.flash.message: ${flash.message}"        
                        
            render(
                controller:'main',
                action:'show', 
                model: [
                    studyActivityIdValue: studyActivityIdValue, 
                    studyTaskIdValue: studyTaskIdValue, 
                    reportedEffortValue: reportedEffortValue
                ]
            )
            
        // if user data entered IS sufficient, save data, then direct back to controller:main, action:show       
        } else {
                        
            //TODO: SAVE DATA
            redirect(controller:'main', action:'show')                
            
        }       

*/
    } //def save
            
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

    def delete = {
        def reportedEffortInstance = ReportedEffort.get(params.id)
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
    }
}
