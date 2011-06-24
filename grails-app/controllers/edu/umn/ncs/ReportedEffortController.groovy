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
                             
        // get REPORTING STAFF, REPORTING PERIOD, and ASSIGNED EFFORT parameters                 
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
        println "PRINTLN ReportedEffortController.add.params.reportingStaffInstance: ${reportingStaffInstance}"

        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        println "PRINTLN ReportedEffortController.add.params.reportingPeriodInstance: ${reportingPeriodInstance}"

        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)        
        println "PRINTLN ReportedEffortController.add.params.assignedEffortInstance: ${assignedEffortInstance}"
       
        // create REPORTED EFFORT instance
        def reportedEffortInstance = new ReportedEffort()
        reportedEffortInstance.assignedEffort = assignedEffortInstance
        println "PRINTLN ReportedEffortController.add.reportedEffortInstance: ${reportedEffortInstance}"        
                               
        // create STUDY ACTIVITY & TASK list for form controls
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

    def addSave = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > ADD-SAVE ---------------------"                
        println "PRINTLN ReportedEffortController.addSave.params: ${params}"        
        
        // get REPORTING STAFF, REPORTING PERIOD, & ASSIGNED EFFORT parameters        

        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)                

        println "PRINTLN ReportedEffortController.addSave.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.addSave.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.addSave.assignedEffortInstance: ${assignedEffortInstance}"        
        
        // get STUDY ACTIVITY, STUDY TASK, & PERCENT EFFORT entered by user

        def studyActivityInstance
        def studyTaskInstance
        def percentEffort 
        def reportedEffortInstance 

        if ( params?.activity.id ) {
            studyActivityInstance = StudyActivity.read(params?.activity.id)
        }
        println "PRINTLN ReportedEffortController.addSave.studyActivityInstance: ${studyActivityInstance}"
        
        if ( params?.task.id ) {
            studyTaskInstance = StudyTask.read(params?.task.id)
        }
        println "PRINTLN ReportedEffortController.addSave.studyTaskInstance: ${studyTaskInstance}"

        if ( params?.percentEffort ) {
            percentEffort = params?.percentEffort            
        }
        println "PRINTLN ReportedEffortController.addSave.percentEffort: ${percentEffort}"
        
        reportedEffortInstance = new ReportedEffort(params)
        println "PRINTLN ReportedEffortController.addSave.reportedEffortInstance: ${reportedEffortInstance}"                    

        // create variables for error checking

        def assignedPercentEffort = assignedEffortInstance.assignedEffort * 100
        println "PRINTLN ReportedEffortController.addSave.assignedPercentEffort: ${assignedPercentEffort}"                    
                
        def c = ReportedEffort.createCriteria()
        def sumReportedPercentEffort_dec = c.get {
            eq("assignedEffort", assignedEffortInstance)
            projections {
                sum("percentEffort")
            }
        }
        println "PRINTLN ReportedEffortController.addSave.sumReportedPercentEffort_dec: ${sumReportedPercentEffort_dec}"
        
        def sumReportedPercentEffort 
        if ( sumReportedPercentEffort_dec ) {
            sumReportedPercentEffort = sumReportedPercentEffort_dec * 100
        } else {
            sumReportedPercentEffort = 0
        }
        println "PRINTLN ReportedEffortController.addSave.sumReportedPercentEffort: ${sumReportedPercentEffort}"
        
        def combinedReportedEffort = percentEffort.toBigDecimal() + sumReportedPercentEffort
        println "PRINTLN ReportedEffortController.addSave.combinedReportedEffort: ${combinedReportedEffort}"
        
        def pRange = 0.0..100.0
        //println "PRINTLN ReportedEffortController.addSave.pRange: ${pRange}"                    
        
        // create FLASH MESSAGESif any of the REPORTED EFFORT provided by user is missing or invalid.  Otherwise, save reported effort.
        if ( !studyActivityInstance ) {            
            
            flash.message = "Must select a STUDY ACTIVITY."                        
            
        } else if ( !studyTaskInstance ) {
            
            flash.message = "Must select a TASK."                        
            
        } else if ( !percentEffort ) {
            
            flash.message = "Must enter a PERCENT EFFORT."                        
            
        } else if ( !(pRange.containsWithinBounds(percentEffort.toBigDecimal())) ) {
            
            flash.message = "The effort you entered is not a valid percent for effort reporting."                                    
            
        // reported percent effort just entered is greater than what is assigned
        } else if ( percentEffort.toBigDecimal() >  assignedPercentEffort ) {
            
            flash.message = "The effort you entered is greater than what has been assigned to you."
        
        // reported percent effort just entered plus what has already been reported is great than what is assigned
        } else if ( combinedReportedEffort.toBigDecimal() > assignedPercentEffort ) {
            
            flash.message = "The combined effort reported thus far is greated than what has been assigned to you."
            
        } else {
            
            // convert Reported Effort to decimal number for insert into db
            if ( reportedEffortInstance ) {
                reportedEffortInstance.percentEffort = reportedEffortInstance.percentEffort / 100.0            
            }
            println "PRINTLN ReportedEffortController.addSave.reportedEffortInstance.percentEffort: ${reportedEffortInstance.percentEffort}"        

            // get USER CREATED
            def username = reportingStaffInstance.username
            println "PRINTLN ReportedEffortController.addSave.username: ${username}"                
            
            reportedEffortInstance.userCreated = username
            println "PRINTLN ReportedEffortController.addSave.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}"        

            if (reportedEffortInstance.save(flush: true)) {
                println "SAVE SUCCESSFULLY"            
                //flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
            } else {
                
                println "SAVE FAILED"

                flash.message = "Your reported effort did not save."
                //flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"

                // print error message to command line if SAVE fails
                if ( ! reportedEffortInstance.save()) {
                    reportedEffortInstance.errors.each{ println it }
                }
                
            } //if (reportedEffortInstance.save(flush: true))

        } //if ( !studyActivityInstance )        
        
        // if error occured, direct user to ADD page
        if ( flash.message ) {

            println "flash.message = ${flash.message}"
            
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
            //println "PRINTLN ReportedEffortController.addSave.model: ${model}"        
            
            render(view: "add", model: model)            

        // if no error occured, direct user to main effort reporting page
        } else {

            println "flash.message = ${flash.message}"

            def model = [
                'reportingStaff.id': reportingStaffInstance.id, 
                'reportingPeriod.id': reportingPeriodInstance.id, 
                'assignedEffort.id': assignedEffortInstance.id
            ]
            //println "PRINTLN ReportedEffortController.addSave.model: ${model}"        

            redirect(controller: 'reportedEffort', action: "main", params: model)
            
        } //if ( flash.message )

    } //def save

    def cancel = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > CANCEL ------------------"                
        println "PRINTLN ReportedEffortController.cancel.params: ${params}"   
        
        // get parameters 
        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)

        println "PRINTLN ReportedEffortController.cancel.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.cancel.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.cancel.assignedEffortInstance: ${assignedEffortInstance}"        

        flash.message = null                        
        
        def model = [
                'reportingStaff.id': reportingStaffInstance.id, 
                'reportingPeriod.id': reportingPeriodInstance.id, 
                'assignedEffort.id': assignedEffortInstance.id
        ]

        redirect(action: "main", params:model)
        
    } //def cancel
    
    def delete = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > DELETE -------------------"                
        println "PRINTLN ReportedEffortController.delete.params: ${params}"        
        
        // get parameters 
        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)
        
        println "PRINTLN ReportedEffortController.delete.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.delete.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.delete.assignedEffortInstance: ${assignedEffortInstance}"        

        // determine REPORTED EFFORT to delete
        def reportedeffortId
        def reportedEffortInstance 
        
        params.each{
            if ( it.key =~ /^reportedEffort.id*$/) {
                reportedeffortId = params?.reportedEffort.id
            }
        }
        println "PRINTLN ReportedEffortController.edit.reportedeffortId: ${reportedeffortId}"
        
        if ( reportedeffortId ) {
            reportedEffortInstance = ReportedEffort.read(params?.reportedEffort.id)                        
        }                
        println "PRINTLN ReportedEffortController.edit.reportedEffortInstance: ${reportedEffortInstance}"        

        def model = [
                'reportingStaff.id': reportingStaffInstance.id, 
                'reportingPeriod.id': reportingPeriodInstance.id, 
                'assignedEffort.id': assignedEffortInstance.id
        ]
        println "PRINTLN ReportedEffortController.delete.model: ${model}"        

        if ( reportedEffortInstance ) {
            
            println "if ( reportedEffortInstance ) = TRUE"
            
            reportedEffortInstance.delete(flush: true)
            println "DELETE SUCCESSFUL"
            
        } else {
            
            println "if ( reportedEffortInstance ) = FALSE"
            println "DELETE FAILED"                    
            flash.message = "Must select a reported effort to DELETE."
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortId])}"
            
        } //if (reportedEffortInstance)

        redirect(action: "main", params:model)
        
    } //def delete
    
    def edit = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > EDIT --------------------"                
        println "PRINTLN ReportedEffortController.edit.params: ${params}"        
        
        // get parameters 
        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        println "PRINTLN ReportedEffortController.edit.reportingStaffInstance: ${reportingStaffInstance}"        

        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        println "PRINTLN ReportedEffortController.edit.reportingPeriodInstance: ${reportingPeriodInstance}"        

        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)
        println "PRINTLN ReportedEffortController.edit.assignedEffortInstance: ${assignedEffortInstance}"     
        
        def reportedEffortInstance 
        
        params.each{
            if ( it.key =~ /^reportedEffort.id*$/) {
                reportedEffortInstance = ReportedEffort.read(params?.reportedEffort.id)
            }
        }
        println "PRINTLN ReportedEffortController.edit.reportedEffortInstance: ${reportedEffortInstance}"
        
        // reported effort is selected for edit
        if ( reportedEffortInstance ) {            
                        
            def studyActivityList = laborService.getActiveStudyActivityList()
            def studyTaskList =  laborService.getActiveStudyTaskList()
                
            [
                reportingStaffInstance: reportingStaffInstance, 
                reportingPeriodInstance: reportingPeriodInstance, 
                assignedEffortInstance: assignedEffortInstance,
                reportedEffortInstance: reportedEffortInstance, 
                studyActivityList: studyActivityList, 
                studyTaskList: studyTaskList 
            ]
            
        // reported effort is NOT selected for edit
        } else {
            
            def model = [
                    'reportingStaff.id': reportingStaffInstance.id, 
                    'reportingPeriod.id': reportingPeriodInstance.id, 
                    'assignedEffort.id': assignedEffortInstance.id
            ]
            flash.message = "Must select a reported effort to EDIT."
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedeffortId])}"
            redirect(action: "main", params:model)
            
        } //if ( reportedEffortInstance ) {

    } //def edit 
    
    def editSave = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > SAVE ---------------------"                
        println "PRINTLN ReportedEffortController.save.params: ${params}"        
        
        // create REPORTING STAFF, REPORTING PERIOD, & ASSIGNED EFFORT instances from parameters
        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)
                
        println "PRINTLN ReportedEffortController.save.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.save.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.save.assignedEffortInstance: ${assignedEffortInstance}"        
        
        /*
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
        */

    } //def editSave
    

    /*******************************************************************************
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
    *******************************************************************************/

} //class ReportedEffortController