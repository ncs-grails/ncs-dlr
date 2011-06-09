package edu.umn.ncs

class ReportedEffortController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def authenticateService
    def laborService

    def index = {
        
        println "PRINTLN ReportedEffortController.index.params: ${params}"                
        redirect(action: "main", params: params)
        
    }

    def main = {
        
        println "PRINTLN ReportedEffortController.main.params: ${params}"                

        def principal = authenticateService.principal()                         
        def reportingStaffInstance = laborService.getReportingStaff(principal)

        // REPORTING PERIOD
        def reportingPeriodInstance = laborService.getCurrentReportingPeriod()

        // ASSIGNED EFFORT FOR PERIOD, if it exists
        def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, reportingStaffInstance)
        
        [
            reportingPeriodInstance: reportingPeriodInstance,
            reportingStaffInstance: reportingStaffInstance,
            assignedEffortInstance: assignedEffortInstance
        ]        
    }
    
    def create = {
        
        println "PRINTLN ReportedEffortController.create.params: ${params}"                
        
        // get Reporting Staff instance
        println "PRINTLN ReportedEffortController.create.params.reportingStaff.id: ${params.reportingStaff.id}"                        
        def reportingStaffInstance = ReportingStaff.findById(params.reportingStaff.id)
        println "PRINTLN ReportedEffortController.create.reportingStaffInstance: ${reportingStaffInstance}"        
        
        // get Reporting Period instance
        println "PRINTLN ReportedEffortController.create.params.reportingPeriod.id: ${params.reportingPeriod.id}"                        
        def reportingPeriodInstance = ReportingPeriod.findById(params.reportingPeriod.id)
        println "PRINTLN ReportedEffortController.create.reportingPeriodInstance: ${reportingPeriodInstance}"        

        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort?.id)

        // get Reported Effort instance
        println "PRINTLN ReportedEffortController.create.params.assignedEffort.id: ${params.assignedEffort.id}"                        
        def reportedEffortInstance = new ReportedEffort()
        reportedEffortInstance.assignedEffort = assignedEffortInstance
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
                
        [ reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance:reportingPeriodInstance, 
            reportedEffortInstance: reportedEffortInstance, 
            assignedEffortInstance: assignedEffortInstance,
            studyActivityList: studyActivityList, 
            studyTaskList: studyTaskList ]

    } //def create

    def save = {
        
        println "start SAVE"
        println "PRINTLN ReportedEffortController.save.params: ${params}"        
        
        //TODO: get Reporting Staff
        def principal = authenticateService.principal()                         
        //println "PRINTLN ReportedEffortController.save.principal: ${principal}"
        
        def reportingStaffInstance = laborService.getReportingStaff(principal)
        //println "PRINTLN ReportedEffortController.save.reportingStaffInstance: ${reportingStaffInstance}"

        def username = reportingStaffInstance.username
        println "PRINTLN ReportedEffortController.save.username: ${username}"
        
        def reportedEffortInstance = new ReportedEffort(params)
        println "PRINTLN ReportedEffortController.save.reportedEffortInstance: ${reportedEffortInstance}"        
        
        
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort?.id)

        if ( reportedEffortInstance.percentEffort ) {
            reportedEffortInstance.percentEffort = reportedEffortInstance.percentEffort / 100.0            
            println "PRINTLN ReportedEffortController.save.reportedEffortInstance.percentEffort: ${reportedEffortInstance.percentEffort}"        
        }
        
        reportedEffortInstance.userCreated = username
        println "PRINTLN ReportedEffortController.save.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}"        
        
        if (reportedEffortInstance.save(flush: true)) {
            println "successful save"
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
            redirect(action: "main")
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
                reportedEffortInstance: reportedEffortInstance,
                assignedEffortInstance: assignedEffortInstance
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
