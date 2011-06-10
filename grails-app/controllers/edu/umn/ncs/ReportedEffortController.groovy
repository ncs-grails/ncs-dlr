package edu.umn.ncs

class ReportedEffortController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def authenticateService
    def laborService

    def index = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > INDEX --------------------"                
        println "PRINTLN ReportedEffortController.index.params: ${params}"                
        redirect(action: "main", params: params)
        
    }

    def main = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > MAIN ---------------------"                
        println "PRINTLN ReportedEffortController.main.params: ${params}"                
        
        // get REPORTING STAFF, REPORTING PERIOD, and ASSIGNED EFFORT  instances
        def reportingStaffId = params?.reportingStaffId
        def reportingPeriodId = params?.reportingPeriodId
        def assignedEffortId = params?.assignedEffortId

        println "PRINTLN ReportedEffortController.main.reportingStaffId: ${reportingStaffId}"                        
        println "PRINTLN ReportedEffortController.main.reportingPeriodId: ${reportingPeriodId}"                
        println "PRINTLN ReportedEffortController.main.assignedEffortId: ${assignedEffortId}"                
        
        def reportingStaffInstance 
        def reportingPeriodInstance 
        def assignedEffortInstance 
        
        if ( reportingStaffId ) {
            reportingStaffInstance = ReportingStaff.findById(reportingStaffId)
        }
        println "PRINTLN ReportedEffortController.main.reportingStaffInstance: ${reportingStaffInstance}"                    
        if ( reportingPeriodId ) {
            reportingPeriodInstance = ReportingPeriod.findById(reportingPeriodId)            
        }
        println "PRINTLN ReportedEffortController.main.reportingPeriodInstance: ${reportingPeriodInstance}"        
        if ( assignedEffortId ) {
            assignedEffortInstance = AssignedEffort.findById(assignedEffortId)
        }
        println "PRINTLN ReportedEffortController.main.assignedEffortInstance: ${assignedEffortInstance}"                    
              
        [            
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance
        ]        
        
    } // def main

    
    def create = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > CREATE -------------------"                
        println "PRINTLN ReportedEffortController.create.params: ${params}"                
        
        // get REPORTING STAFF, REPORTING PERIOD, and REPORTED EFFORT parameters                 
        def reportingStaffId = params?.reportingStaff.id
        def reportingPeriodId = params?.reportingPeriod.id
        def assignedEffortId = params?.assignedEffort.id
        
        println "PRINTLN ReportedEffortController.create.params.reportingStaffId: ${reportingStaffId}"
        println "PRINTLN ReportedEffortController.create.params.reportingPeriodId: ${reportingPeriodId}"
        println "PRINTLN ReportedEffortController.create.params.assignedEffortId: ${assignedEffortId}"

        // get REPORTING STAFF, REPORTING PERIOD, and ASSIGNED EFFORT instances 
        def reportingStaffInstance
        if ( reportingStaffId ) {
            reportingStaffInstance = ReportingStaff.findById(params?.reportingStaff.id)            
        }
        def reportingPeriodInstance 
        if ( reportingPeriodId ) {
            reportingPeriodInstance = ReportingPeriod.findById(params?.reportingPeriod.id)
        }
        def assignedEffortInstance
        if ( assignedEffortId ) {
            assignedEffortInstance = AssignedEffort.findById(assignedEffortId)            
        }

        println "PRINTLN ReportedEffortController.create.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.create.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.create.assignedEffortInstance: ${assignedEffortInstance}"        

        // get REPORTED EFFORT instance
        def reportedEffortInstance = new ReportedEffort()
        reportedEffortInstance.assignedEffort = assignedEffortInstance
        println "PRINTLN ReportedEffortController.create.reportedEffortInstance: ${reportedEffortInstance}"        
               
        def studyActivityList = laborService.getActiveStudyActivityList()
        def studyTaskList =  laborService.getActiveStudyTaskList()
                
        [ 
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance:reportingPeriodInstance, 
            assignedEffortInstance: assignedEffortInstance,
            reportedEffortInstance: reportedEffortInstance, 
            studyActivityList: studyActivityList, 
            studyTaskList: studyTaskList 
        ]

    } //def create

    def save = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > SAVE ---------------------"                
        println "PRINTLN ReportedEffortController.save.params: ${params}"        
        
        // get parameters 
        def reportingStaffId = params?.reportingStaff.id
        def reportingPeriodId = params?.reportingPeriod.id
        def assignedEffortId = params?.assignedEffort.id
        
        println "PRINTLN ReportedEffortController.save.reportingStaffId: ${reportingStaffId}"
        println "PRINTLN ReportedEffortController.save.reportingPeriodId: ${reportingPeriodId}"
        println "PRINTLN ReportedEffortController.save.assignedEffortId: ${assignedEffortId}"

        // get REPORTING STAFF, REPORTING PERIOD, and ASSIGNED EFFORT instances 
        def reportingStaffInstance
        if ( reportingStaffId ) {
            reportingStaffInstance = ReportingStaff.findById(params?.reportingStaff.id)            
        }
        def reportingPeriodInstance 
        if ( reportingPeriodId ) {
            reportingPeriodInstance = ReportingPeriod.findById(params?.reportingPeriod.id)
        }
        def assignedEffortInstance
        if ( assignedEffortId ) {
            assignedEffortInstance = AssignedEffort.findById(assignedEffortId)            
        }

        println "PRINTLN ReportedEffortController.save.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.save.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.save.assignedEffortInstance: ${assignedEffortInstance}"        
        
        // get REPORTED EFFORT entered
        def reportedEffortInstance = new ReportedEffort(params)
        println "PRINTLN ReportedEffortController.save.reportedEffortInstance: ${reportedEffortInstance}"        
        
        // convert Reported Effort to decimal number for insert into db
        if ( reportedEffortInstance.percentEffort ) {
            reportedEffortInstance.percentEffort = reportedEffortInstance.percentEffort / 100.0            
            println "PRINTLN ReportedEffortController.save.reportedEffortInstance.percentEffort: ${reportedEffortInstance.percentEffort}"        
        }
        
        // get USERNAME of person reporting effort
        def username = reportingStaffInstance.username
        println "PRINTLN ReportedEffortController.save.username: ${username}"                
        
        reportedEffortInstance.userCreated = username
        println "PRINTLN ReportedEffortController.save.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}"        
        
        if (reportedEffortInstance.save(flush: true)) {
            
            println "SAVE SUCCESSFULLY"            
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
            
            redirect(controller: 'main', action: "show")
            //redirect(uri:"/main/show")
        
        } else {
            
            println "SAVE FAILED"
            
            // get STUDY ACTIVITY & TASK list for form controls
            def studyActivityList = laborService.getActiveStudyActivityList()
            def studyTaskList =  laborService.getActiveStudyTaskList()
            
            def model = [
                reportingStaffInstance: reportingStaffInstance, 
                reportingPeriodInstance: reportingPeriodInstance, 
                assignedEffortInstance: assignedEffortInstance,
                reportedEffortInstance: reportedEffortInstance,
                studyActivityList: studyActivityList, 
                studyTaskList: studyTaskList
            ]
                
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"

            render(view: "create", model: model)
            
        } //if (reportedEffortInstance.save(flush: true))
        
    } //def save
    
    
    def delete = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > DELTE --------------------"                
        println "PRINTLN ReportedEffortController.delete.params: ${params}"        
        
        // get parameters 
        def reportingStaffId = params?.reportingStaff.id
        def reportingPeriodId = params?.reportingPeriod.id
        def assignedEffortId = params?.assignedEffort.id
        
        params.each{
            
            if ( it.key =~ /^reportedEffortId-[0-9]*$/) {
                
                def reportedEffortId = Integer.parseInt(it.key.replace('staff-', ''))
                println "PRINTLN ReportedEffortController.delete.params.each.reportedEffortId: ${reportedEffortId}"
                
            }

        }

        println "PRINTLN ReportedEffortController.delete.reportingStaffId: ${reportingStaffId}"
        println "PRINTLN ReportedEffortController.delete.reportingPeriodId: ${reportingPeriodId}"
        println "PRINTLN ReportedEffortController.delete.assignedEffortId: ${assignedEffortId}"

        // get REPORTING STAFF, REPORTING PERIOD, and ASSIGNED EFFORT instances 
        def reportingStaffInstance
        if ( reportingStaffId ) {
            reportingStaffInstance = ReportingStaff.findById(params?.reportingStaff.id)            
        }
        def reportingPeriodInstance 
        if ( reportingPeriodId ) {
            reportingPeriodInstance = ReportingPeriod.findById(params?.reportingPeriod.id)
        }
        def assignedEffortInstance
        if ( assignedEffortId ) {
            assignedEffortInstance = AssignedEffort.findById(assignedEffortId)            
        }

        println "PRINTLN ReportedEffortController.delete.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.delete.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.delete.assignedEffortInstance: ${assignedEffortInstance}"        
        
        

        /*
        def reportedEffortInstance = ReportedEffort.get(params.id)
        println "PRINTLN ReportedEffortController.delete.ReportedEffort.get(params.id): ${ReportedEffort.get(params.id)}"        
        

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
