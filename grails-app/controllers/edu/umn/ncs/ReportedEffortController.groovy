package edu.umn.ncs

class ReportedEffortController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def authenticateService
    def laborService

    def index = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > INDEX --------------------"                
        println "PRINTLN ReportedEffortController.index.params: ${params}"                
        redirect(controller: 'main')
        
    }

    // when ADD button is pressed
    def create = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > CREATE -------------------"                
        println "PRINTLN ReportedEffortController.create.params: ${params}"                
 
        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
        println "PRINTLN ReportedEffortController.create.assignedEffortInstance: ${assignedEffortInstance}"

        // REPORTED EFFORT
        def reportedEffortInstance         
        if ( assignedEffortInstance ) {            
            reportedEffortInstance = new ReportedEffort()
            reportedEffortInstance.assignedEffort = assignedEffortInstance            
        }               
        println "PRINTLN ReportedEffortController.create.reportedEffortInstance: ${reportedEffortInstance}"        
                               
        // create STUDY ACTIVITY & TASK list for form controls
        def studyActivityList = laborService.getActiveStudyActivityList()
        def studyTaskList =  laborService.getActiveStudyTaskList()
                
        [ 
            assignedEffortInstance: assignedEffortInstance,
            reportedEffortInstance: reportedEffortInstance, 
            studyActivityList: studyActivityList, 
            studyTaskList: studyTaskList 
        ]
 
    } //def create

    def save = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > ADD-SAVE ---------------------"                
        println "PRINTLN ReportedEffortController.addSave.params: ${params}"        
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)                
        println "PRINTLN ReportedEffortController.addSave.assignedEffortInstance: ${assignedEffortInstance}"        
        
        def studyActivityInstance
        def studyTaskInstance
        def percentEffort 
        def reportedEffortInstance 

        if ( params?.activity.id ) {
            studyActivityInstance = StudyActivity.read(params?.activity.id)
        }
        println "PRINTLN ReportedEffortController.addSave.studyActivityInstance: ${studyActivityInstance}"
        
        if ( params?.task.id ) {
            studyTaskInstance = StudyTask.read(params?.task?.id)
        }
        println "PRINTLN ReportedEffortController.addSave.studyTaskInstance: ${studyTaskInstance}"

        if ( params?.percentEffort ) {
            def percentEffortVal = params?.percentEffort            
            percentEffort = percentEffortVal.replaceAll(/\.\./, ".")
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

        if ( !studyActivityInstance ) {            
            
            flash.message = "Must select a STUDY ACTIVITY."                        
            
        } else if ( !studyTaskInstance ) {
            
            flash.message = "Must select a TASK."                        
            
        } else if ( !percentEffort ) {
            
            flash.message = "Must enter a PERCENT EFFORT."                        
            
        } else if ( percentEffort.count(".") > 1 ) {

            flash.message = "The effort you entered cannot have more than one decimal."                                    
            
        } else {

            def pRange = 0.0..100.0
            //println "PRINTLN ReportedEffortController.addSave.pRange: ${pRange}"                    

            def combinedReportedEffort
            if ( pRange.containsWithinBounds(percentEffort.toBigDecimal()) ) {
                combinedReportedEffort = percentEffort.toBigDecimal() + sumReportedPercentEffort
            }
            println "PRINTLN ReportedEffortController.addSave.combinedReportedEffort: ${combinedReportedEffort}"
            
            if ( !(pRange.containsWithinBounds(percentEffort.toBigDecimal())) ) {
            
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

            } //} else {

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

            def params = [
                'reportingStaff.id': reportingStaffInstance.id, 
                'reportingPeriod.id': reportingPeriodInstance.id, 
                'assignedEffort.id': assignedEffortInstance.id
            ]
            //println "PRINTLN ReportedEffortController.addSave.model: ${model}"        

            redirect(controller: 'reportedEffort', action: "main", params: params)
            
        } //if ( flash.message )

    } //def save

    def cancel = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > CANCEL ------------------"                
        println "PRINTLN ReportedEffortController.cancel.params: ${params}"   
        
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)
        println "PRINTLN ReportedEffortController.cancel.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.cancel.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.cancel.assignedEffortInstance: ${assignedEffortInstance}"        

        def params = [
                'reportingStaff.id': reportingStaffInstance.id, 
                'reportingPeriod.id': reportingPeriodInstance.id, 
                'assignedEffort.id': assignedEffortInstance.id
        ]

        redirect(action: "main", params: [ 'assignedEffort.id': params?.assignedEffort.id])

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

        def params = [
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

        redirect(action: "main", params: params)
        
    } //def delete
    
    def edit = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > EDIT --------------------"                
        println "PRINTLN ReportedEffortController.edit.params: ${params}"        
        
        // get REPORTING STAFF, REPORTING PERIOD, ASSIGNED EFFORT, and REPORTED EFFORT parameters 
        def reportingStaffInstance = ReportingStaff.read(params?.reportingStaff.id)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod.id)
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)        
        def reportedEffortInstance         
        params.each{
            if ( it.key =~ /^reportedEffort.id*$/) {
                reportedEffortInstance = ReportedEffort.read(params?.reportedEffort.id)
            }
        }
        
        println "PRINTLN ReportedEffortController.edit.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.edit.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.edit.assignedEffortInstance: ${assignedEffortInstance}"     
        println "PRINTLN ReportedEffortController.edit.reportedEffortInstance: ${reportedEffortInstance}"
        
        // reported effort is selected for edit
        if ( reportedEffortInstance ) {     

            def studyActivityList = laborService.getActiveStudyActivityList()
            //println "PRINTLN ReportedEffortController.edit.studyActivityList: ${studyActivityList}"

            def studyTaskList =  laborService.getActiveStudyTaskList()
            //println "PRINTLN ReportedEffortController.edit.studyTaskList: ${studyTaskList}"
            
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
            
            def params = [
                    'reportingStaff.id': reportingStaffInstance.id, 
                    'reportingPeriod.id': reportingPeriodInstance.id, 
                    'assignedEffort.id': assignedEffortInstance.id
            ]
            flash.message = "Must select a reported effort to EDIT."
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedeffortId])}"
            redirect(action: "main", params: params)
            
        } //if ( reportedEffortInstance ) {

    } //def edit 
    
    def editSave = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > EDIT - SAVE ---------------------"                
        println "PRINTLN ReportedEffortController.editSave.params: ${params}"        
        
        // get REPORTING STAFF, REPORTING PERIOD, ASSIGNED EFFORT, & REPORTED EFFORT parameters
        
        def reportedEffortInstance = ReportedEffort.get(params?.id)

        /*
        def assignedEffortInstance = reportedEffortInstance?.assignedEffort
        def reportingPeriodInstance = reportedEffortInstance?.assignedEffort?.period
        def reportingStaffInstance = reportedEffortInstance?.assignedEffort?.reportingStaff
        */
       
        println "PRINTLN ReportedEffortController.editSave.reportingStaffInstance: ${reportingStaffInstance}"        
        println "PRINTLN ReportedEffortController.editSave.reportingPeriodInstance: ${reportingPeriodInstance}"        
        println "PRINTLN ReportedEffortController.editSave.assignedEffortInstance: ${assignedEffortInstance}"        
        println "PRINTLN ReportedEffortController.editSave.reportedEffortInstance: ${reportedEffortInstance}"


        if (reportedEffortInstance) {
            
            def model = [
                reportingStaffInstance: reportingStaffInstance, 
                reportingPeriodInstance: reportingPeriodInstance, 
                assignedEffortInstance: reportedEffortInstance?.assignedEffort, 
                reportedEffortInstance: reportedEffortInstance
            ]
            
            reportedEffortInstance.properties = params

            if (params?.version) {
                
                println "PRINTLN ReportedEffortController.editSave.params?.reportedEffort.version: ${params?.version}"
                
                def version = params?.version.toLong()
                println "PRINTLN ReportedEffortController.editSave.version: ${version}"
                
                if (reportedEffortInstance.version > version) {
                    
                    reportedEffortInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'reportedEffort.label', default: 'ReportedEffort')] as Object[], "Another user has updated this ReportedEffort while you were editing")
                    render(view: "edit", model: model)
                    return
                    
                }
                
            } //if (params.version)
                       
            // if successful save
            if (!reportedEffortInstance.hasErrors() && reportedEffortInstance.save(flush: true)) {
                
                println "PRINTLN SUCCESSFUL SAVE"
                //flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
                render(view: "edit", model: model)
                //redirect(action: "show", id: reportedEffortInstance.id)
                
            }   
            // if saved failed
            else {
                
                println "PRINTLN SAVE FAILED"
                render(view: "edit", model: model)
                
            }
        
        } else {

            def model = [            
                reportingStaffInstance: reportingStaffInstance,
                reportingPeriodInstance: reportingPeriodInstance,
                assignedEffortInstance: assignedEffortInstance
            ]        
            
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            render(view: "main", model: model)
            
        }
        
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