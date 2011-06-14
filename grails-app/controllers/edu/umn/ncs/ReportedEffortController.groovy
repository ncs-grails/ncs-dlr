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
                             
        // get parameters
        def reportingStaffInstance 
        def reportingPeriodInstance
        def assignedEffortInstance 

        reportingStaffInstance = ReportingStaff.read(params?.reportingStaff?.id)
        reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod?.id)
        assignedEffortInstance = AssignedEffort.read(params?.assignedEffort?.id)            
        
        println "PRINTLN MainController.show.reportingStaffInstance: ${reportingStaffInstance}"
        println "PRINTLN MainController.show.reportingPeriodInstance: ${reportingPeriodInstance}"
        println "PRINTLN MainController.show.assignedEffortInstance: ${assignedEffortInstance}"        
                              
        [            
            reportingStaffInstance: reportingStaffInstance,
            reportingPeriodInstance: reportingPeriodInstance,
            assignedEffortInstance: assignedEffortInstance
        ]        

    } // def main
    
    def add = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > ADD -------------------"                
        println "PRINTLN ReportedEffortController.add.params: ${params}"                
        
        // get REPORTING STAFF, REPORTING PERIOD, and REPORTED EFFORT parameters                 
        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)
        
        println "PRINTLN ReportedEffortController.add.params.reportingStaffInstance: ${reportingStaffInstance}"
        println "PRINTLN ReportedEffortController.add.params.reportingPeriodInstance: ${reportingPeriodInstance}"
        println "PRINTLN ReportedEffortController.add.params.assignedEffortInstance: ${assignedEffortInstance}"
       
        // create REPORTED EFFORT instance
        def reportedEffortInstance = new ReportedEffort()
        reportedEffortInstance.assignedEffort = assignedEffortInstance
        println "PRINTLN ReportedEffortController.add.reportedEffortInstance: ${reportedEffortInstance}"        
               
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

    } //def add

    def save = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > SAVE ---------------------"                
        println "PRINTLN ReportedEffortController.save.params: ${params}"        
        
        // create REPORTING STAFF, REPORTING PERIOD, & ASSIGNED EFFORT instances from parameters
        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)
                
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
            
            def model = [
                'reportingStaff.id': reportingStaffInstance.id, 
                'reportingPeriod.id': reportingPeriodInstance.id, 
                'assignedEffort.id': assignedEffortInstance.id
            ]
    
            println "PRINTLN ReportedEffortController.save.model: ${model}"        

            redirect(controller: 'reportedEffort', action: "main", params: model)
        
        } else {
            
            println "SAVE FAILED"
            
            // get error message if save failes
            if ( ! reportedEffortInstance.save()) {
                reportedEffortInstance.errors.each{ println it }
            }

            
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
        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)
        
        println "PRINTLN ReportedEffortController.delete.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.delete.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.delete.assignedEffortInstance: ${assignedEffortInstance}"        

        params.each{
            
            if ( it.key =~ /^reportedEffortId-[0-9]*$/) {
                
                def reportedEffortId = Integer.parseInt(it.key.replace('reportedEffortId-', ''))
                println "PRINTLN ReportedEffortController.delete.params.each.reportedEffortId: ${reportedEffortId}"
                
            }

        }
        

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
