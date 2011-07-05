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

    def cancel = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > CANCEL -------------------"                
        println "PRINTLN ReportedEffortController.cancel.params: ${params}"   
        
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)        

        render(view: "/assignedEffort/show", model:[assignedEffortInstance: assignedEffortInstance] )

    } 
    
    // when ADD button is pressed on AssignedEffort > show.gsp
    def create = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > CREATE -------------------"                
        println "PRINTLN ReportedEffortController.create.params: ${params}"                
 
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
        println "PRINTLN ReportedEffortController.create.assignedEffortInstance: ${assignedEffortInstance}"

        def reportedEffortInstance
        if ( assignedEffortInstance ) {            
            reportedEffortInstance = new ReportedEffort()
            reportedEffortInstance.assignedEffort = assignedEffortInstance            
        }               
        println "PRINTLN ReportedEffortController.create.reportedEffortInstance: ${reportedEffortInstance}"        
                
        [ reportedEffortInstance: reportedEffortInstance ]
 
    } 

    def save = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > SAVE ---------------------"                
        println "PRINTLN ReportedEffortController.save.params: ${params}"        
        
        // REPORTED EFFORT
        def reportedEffortInstance = new ReportedEffort(params)
        def reportedEffortConvertedVal = reportedEffortInstance.percentEffortConverted

        println "PRINTLN ReportedEffortController.save.reportedEffortInstance: ${reportedEffortInstance}"                    
        println "PRINTLN ReportedEffortController.save.reportedEffortConvertedVal: ${reportedEffortConvertedVal}"

        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort?.id)
        def assignedEffortConverted = assignedEffortInstance.assignedEffortConverted

        println "PRINTLN ReportedEffortController.save.assignedEffortInstance: ${assignedEffortInstance}"                                
        println "PRINTLN ReportedEffortController.save.assignedEffortConverted}: ${assignedEffortConverted}"

        // COMBINED REPORTED EFFORT
        def c = ReportedEffort.createCriteria()
        def sumReportedPercentEffort = c.get {
            eq("assignedEffort", assignedEffortInstance)
            projections {
                sum("percentEffort")
            }
        }
        println "PRINTLN ReportedEffortController.save.sumReportedPercentEffort: ${sumReportedPercentEffort}"        

        def sumReportedPercentEffortConverted 
        if ( sumReportedPercentEffort ) {
            sumReportedPercentEffortConverted = sumReportedPercentEffort * 100                    
        }
        println "PRINTLN ReportedEffortController.save.sumReportedPercentEffortConverted: ${sumReportedPercentEffortConverted}"
        
        def combineReportedEffortConverted 
        if ( sumReportedPercentEffortConverted && reportedEffortConvertedVal ) {
            combineReportedEffortConverted = sumReportedPercentEffortConverted + reportedEffortConvertedVal
        }
        println "PRINTLN ReportedEffortController.save.combineReportedEffortConverted: ${combineReportedEffortConverted}"

        // REPORTED EFFORT ENTRY VALIDATION
        def err = false 
        def errMessage
                
        if ( reportedEffortConvertedVal ) {
            
            // EFFORT ENTRY VALIDATION
            def pRange = 0.0..100.0

            // entry is not a valid percent effort
            if ( !(pRange.containsWithinBounds(reportedEffortConvertedVal.toBigDecimal())) ) {
            
                err = true
                errMessage = "The effort you entered is not a valid percent for effort reporting."                                    
            
            // entry is is greater than what is assigned
            } else if ( reportedEffortConvertedVal.toBigDecimal() >  assignedEffortConverted.toBigDecimal() ) {

                err = true
                errMessage = "The effort you entered is greater than what has been assigned to you."

            } else if ( combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() > assignedEffortConverted.toBigDecimal() ) {                
                
                err = true
                errMessage = "The effort you entered, plus what has already been reported, is greater than what is assigned to you."
                
            }

            println "PRINTLN ReportedEffortController.save.errMessage: ${errMessage}"
            
        }
        
        // USER who entered effort
        def principal = authenticateService.principal()                         
        reportedEffortInstance.userCreated = principal.getUsername()
        println "PRINTLN ReportedEffortController.save.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}"        

        // SAVE
        if ( err == false && reportedEffortInstance.validate() && reportedEffortInstance.save(flush: true)) {

            println "SAVE SUCCESSFULLY"

            render(view: "/assignedEffort/show", model: [assignedEffortInstance: assignedEffortInstance] )
                        
        } else {

            println "SAVE FAILED"        
            // reportedEffortInstance.errors.each{ println it }
                        
            render(view: "create", model: [reportedEffortInstance: reportedEffortInstance, errMessage: errMessage])
                       
        } 
        
    } //def save

    def delete = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > DELETE -------------------"                
        println "PRINTLN ReportedEffortController.delete.params: ${params}"        
        
        // ASSIGNED & REPORTED EFFORT
        def reportedEffortInstance = ReportedEffort.read(params?.reportedEffort?.id)

        def assignedEffortInstance 
        if ( reportedEffortInstance ) {
            assignedEffortInstance = reportedEffortInstance.assignedEffort            
        }

        println "PRINTLN ReportedEffortController.delete.reportedEffortInstance: ${reportedEffortInstance}"        
        println "PRINTLN ReportedEffortController.delete.assignedEffortInstance: ${assignedEffortInstance}"        
 
        // DELETE
        def errMessage

        if ( reportedEffortInstance ) {
            try {
                reportedEffortInstance.delete(flush: true)
                //flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                errMessage = "Reported Effort could not be deleted"
                //flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            }
        } else {
            
            errMessage = "Must select a Reported Effort to delete."
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
        }        
        
        render(view: "/assignedEffort/show", model: [assignedEffortInstance: assignedEffortInstance, errMessage: errMessage] )
       
    } //def delete
    
    def edit = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > EDIT --------------------"                
        println "PRINTLN ReportedEffortController.edit.params: ${params}"
        
        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
        println "PRINTLN ReportedEffortController.edit.assignedEffortInstance: ${assignedEffortInstance}"     
        
        // REPORTED EFFORT 
        def reportedEffortInstance
        if ( params?.reportedEffort?.id ) {
            reportedEffortInstance = ReportedEffort.get(params?.reportedEffort?.id)            
        }
        println "PRINTLN ReportedEffortController.edit.reportedEffortInstance: ${reportedEffortInstance}"
        
        if ( reportedEffortInstance ) {
            
            return [reportedEffortInstance: reportedEffortInstance]
            
        } else {
            
            def errMessage = "Must select a Reported Effort to edit"            
            def model = [
                assignedEffortInstance: assignedEffortInstance, 
                reportedEffortInstance: reportedEffortInstance,
                errMessage: errMessage
            ]
            
            render(view: "/assignedEffort/show", model: model )
            
        }
        
    } //def edit 
    
   def editSave = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > EDIT-SAVE ---------------------"                
        println "PRINTLN ReportedEffortController.save.params: ${params}"        
        
        def reportedEffortInstance = new ReportedEffort(params)
        println "PRINTLN ReportedEffortController.save.reportedEffortInstance: ${reportedEffortInstance}"
        
        

        /*

        if (reportedEffortInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
            redirect(action: "show", id: reportedEffortInstance.id)
        }
        else {
            render(view: "create", model: [reportedEffortInstance: reportedEffortInstance])
        }

        // REPORTED EFFORT
        def reportedEffortInstance = new ReportedEffort(params)
        def reportedEffortConvertedVal = reportedEffortInstance.percentEffortConverted

        println "PRINTLN ReportedEffortController.save.reportedEffortInstance: ${reportedEffortInstance}"                    
        println "PRINTLN ReportedEffortController.save.reportedEffortConvertedVal: ${reportedEffortConvertedVal}"

        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort?.id)
        def assignedEffortConverted = assignedEffortInstance.assignedEffortConverted

        println "PRINTLN ReportedEffortController.save.assignedEffortInstance: ${assignedEffortInstance}"                                
        println "PRINTLN ReportedEffortController.save.assignedEffortConverted}: ${assignedEffortConverted}"

        // COMBINED REPORTED EFFORT
        def c = ReportedEffort.createCriteria()
        def sumReportedPercentEffort = c.get {
            eq("assignedEffort", assignedEffortInstance)
            projections {
                sum("percentEffort")
            }
        }
        println "PRINTLN ReportedEffortController.save.sumReportedPercentEffort: ${sumReportedPercentEffort}"        

        def sumReportedPercentEffortConverted 
        if ( sumReportedPercentEffort ) {
            sumReportedPercentEffortConverted = sumReportedPercentEffort * 100                    
        }
        println "PRINTLN ReportedEffortController.save.sumReportedPercentEffortConverted: ${sumReportedPercentEffortConverted}"
        
        def combineReportedEffortConverted 
        if ( sumReportedPercentEffortConverted && reportedEffortConvertedVal ) {
            combineReportedEffortConverted = sumReportedPercentEffortConverted + reportedEffortConvertedVal
        }
        println "PRINTLN ReportedEffortController.save.combineReportedEffortConverted: ${combineReportedEffortConverted}"

        // REPORTED EFFORT ENTRY VALIDATION
        def err = false 
        def errMessage
                
        if ( reportedEffortConvertedVal ) {
            
            // EFFORT ENTRY VALIDATION
            def pRange = 0.0..100.0

            // entry is not a valid percent effort
            if ( !(pRange.containsWithinBounds(reportedEffortConvertedVal.toBigDecimal())) ) {
            
                err = true
                errMessage = "The effort you entered is not a valid percent for effort reporting."                                    
            
            // entry is is greater than what is assigned
            } else if ( reportedEffortConvertedVal.toBigDecimal() >  assignedEffortConverted.toBigDecimal() ) {

                err = true
                errMessage = "The effort you entered is greater than what has been assigned to you."

            } else if ( combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() > assignedEffortConverted.toBigDecimal() ) {                
                
                err = true
                errMessage = "The effort you entered, plus what has already been reported, is greater than what is assigned to you."
                
            }

            println "PRINTLN ReportedEffortController.save.errMessage: ${errMessage}"
            
        }
        
        // USER who entered effort
        def principal = authenticateService.principal()                         
        reportedEffortInstance.userCreated = principal.getUsername()
        println "PRINTLN ReportedEffortController.save.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}"        

        // SAVE
        if ( err == false && reportedEffortInstance.validate() && reportedEffortInstance.save(flush: true)) {

            println "SAVE SUCCESSFULLY"

            render(view: "/assignedEffort/show", model: [assignedEffortInstance: assignedEffortInstance] )
                        
        } else {

            println "SAVE FAILED"        
            // reportedEffortInstance.errors.each{ println it }
                        
            render(view: "create", model: [reportedEffortInstance: reportedEffortInstance, errMessage: errMessage])
                       
        } 
*/        
    } //def editSave
    
    def commit = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > COMMIT -------------------"                
        println "PRINTLN ReportedEffortController.commit.params: ${params}"
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
        println "PRINTLN ReportedEffortController.commit.assignedEffortInstance: ${assignedEffortInstance}"     
        
        if ( assignedEffortInstance ) {
            
            def assignedPercentEffort = assignedEffortInstance.assignedEffort
            println "PRINTLN ReportedEffortController.commit.assignedPercentEffort: ${assignedPercentEffort}"
            
            def sumOfReportedPercentEffort = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
            println "PRINTLN ReportedEffortController.commit.sumOfReportedPercentEffort: ${sumOfReportedPercentEffort}"     
            
            def errMessage
            
            if ( sumOfReportedPercentEffort.toBigDecimal() < assignedPercentEffort.toBigDecimal() ) {
                
                errMessage = "Cannot COMMIT your reported effort because it is less than what has been assigned to you."
                
                def model  = [
                    assignedEffortInstance: assignedEffortInstance, 
                    errMessage: errMessage
                ]
                
                render(view: "/assignedEffort/show", model: model)
                
                
            } else if ( sumOfReportedPercentEffort.toBigDecimal() == assignedPercentEffort.toBigDecimal() ) {
                
                //Date dateCommitted
                //ReportingStaff commitingStaff


/*
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
 */
                
                
                render(view: "/main/show")
                
            }
    
        }

    } //def commit 
    
    
    
    /*******************************************************************************
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [reportedEffortInstanceList: ReportedEffort.list(params), reportedEffortInstanceTotal: ReportedEffort.count()]
    }

    def create = {
        def reportedEffortInstance = new ReportedEffort()
        reportedEffortInstance.properties = params
        return [reportedEffortInstance: reportedEffortInstance]
    }

    def save = {
        def reportedEffortInstance = new ReportedEffort(params)
        if (reportedEffortInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), reportedEffortInstance.id])}"
            redirect(action: "show", id: reportedEffortInstance.id)
        }
        else {
            render(view: "create", model: [reportedEffortInstance: reportedEffortInstance])
        }
    }

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

    *******************************************************************************/

} //class ReportedEffortController