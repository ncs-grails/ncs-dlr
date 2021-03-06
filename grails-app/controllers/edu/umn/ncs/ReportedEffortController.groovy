package edu.umn.ncs
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE', 'ROLE_NCS_DLR'])
class ReportedEffortController {

    def springSecurityService
    def laborService
	def debug = true
	//def debug = true
		
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        
        if (debug) {
			log.debug "=> REPORTED EFFORT CONTROLLER > INDEX --------------------"                
			log.debug "=> ReportedEffortController.index.params: ${params}"
        }
		                
        redirect(controller: 'main')
        
    }

    def cancel = {
        
        if (debug) {
			log.debug "=> REPORTED EFFORT CONTROLLER > CANCEL -------------------"                
			log.debug "=> ReportedEffortController.cancel.params: ${params}"
        }   
        
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort.id)        

        render(view: "/assignedEffort/show", model:[assignedEffortInstance: assignedEffortInstance] )

    } 
    
    def create = {
        
        if (debug) {
			log.debug "=> REPORTED EFFORT CONTROLLER > CREATE -------------------"                
			log.debug "=> ReportedEffortController.create.params: ${params}"
        }                
 
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
        if (debug) { log.debug "=> ReportedEffortController.create.assignedEffortInstance: ${assignedEffortInstance}" }
		
		// ABOVE CODE SHOULD READ AS FOLLOWS
		// def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort?.id)
		
		
		
        def reportedEffortInstance
        if ( assignedEffortInstance ) {            
            reportedEffortInstance = new ReportedEffort()
            reportedEffortInstance.assignedEffort = assignedEffortInstance            
        }               
        if (debug) { log.debug "=> ReportedEffortController.create.reportedEffortInstance: ${reportedEffortInstance}" }        
                
        [ reportedEffortInstance: reportedEffortInstance ]
 
    } 

    def addSave = {
        
		if (debug) {
	        log.debug "=> REPORTED EFFORT CONTROLLER > ADD-SAVE ---------------------"
	        log.debug "=> ReportedEffortController.addSave.params: ${params}"
		}        
        
        // REPORTED EFFORT
        def reportedEffortInstance = new ReportedEffort(params)
        def reportedEffortConvertedVal = reportedEffortInstance.percentEffortConverted

        if (debug) { log.debug "=> ReportedEffortController.addSave.reportedEffortInstance: ${reportedEffortInstance}" }                    
        if (debug) { log.debug "=> ReportedEffortController.addSave.reportedEffortConvertedVal: ${reportedEffortConvertedVal}" }
				
        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort?.id)
        def assignedEffortConverted = assignedEffortInstance.assignedEffortConverted

        if (debug) { log.debug "=> ReportedEffortController.addSave.assignedEffortInstance: ${assignedEffortInstance}" }                           
        if (debug) { log.debug "=> ReportedEffortController.addSave.assignedEffortConverted}: ${assignedEffortConverted}" }

        // COMBINED REPORTED EFFORT
		def sumReportedPercentEffort = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)		
        if (debug) { log.debug "=> ReportedEffortController.addSave.sumReportedPercentEffort: ${sumReportedPercentEffort}" }        

        def sumOfAlreadyReportedPercentEffortConverted 
        if ( sumReportedPercentEffort ) {
            sumOfAlreadyReportedPercentEffortConverted = sumReportedPercentEffort * 100                    
        }
        if (debug) { log.debug "=> ReportedEffortController.addSave.sumOfAlreadyReportedPercentEffortConverted: ${sumOfAlreadyReportedPercentEffortConverted}" }
        
        def combineReportedEffortConverted = laborService.getCombineReportedEffortConverted(sumOfAlreadyReportedPercentEffortConverted, reportedEffortConvertedVal)
        if (debug) { log.debug "=> ReportedEffortController.addSave.combineReportedEffortConverted: ${combineReportedEffortConverted}" }

        // REPORTED EFFORT ENTRY VALIDATION
        def err = false 
        def errMessage
		def hideAddButton = false
		
        if ( reportedEffortConvertedVal || reportedEffortConvertedVal.toBigDecimal() == 0 ) {
            
            // EFFORT ENTRY VALIDATION
            def pRange = 0.0..100.0

			// entry equals zero
            if ( reportedEffortConvertedVal.toBigDecimal() == 0 ) {
			
                err = true
                errMessage = "Cannot enter zero percent effort."                                    
			 			
            // entry is not a valid percent effort
			} else if ( !(pRange.containsWithinBounds(reportedEffortConvertedVal.toBigDecimal())) ) {
            
                err = true
                errMessage = "Please enter a valid percent effort."                                    
            				
            // entry is greater than what is assigned
            } else if ( reportedEffortConvertedVal.toBigDecimal() >  assignedEffortConverted.toBigDecimal() ) {

                err = true
                errMessage = "The percent effort you entered is greater than what has been assigned to you."
				
			// combined reported effort (entry + what has already been reported) is greater than what is assigned	
            } else if ( combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() > assignedEffortConverted.toBigDecimal() ) {                
                
                err = true
                errMessage = "The percent effort you entered, plus what has already been reported, is greater than what is assigned to you."
				hideAddButton = true
								
            // combined reported effort equals what is assigned
            } else if (combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() == assignedEffortConverted.toBigDecimal()) {
			
				hideAddButton = true
				
			}			

            if (debug) { log.debug "=> ReportedEffortController.addSave.errMessage: ${errMessage}" }
            
        }

        // USER who entered effort
        def principal = springSecurityService.principal                         
        reportedEffortInstance.userCreated = principal.getUsername()
        if (debug) { log.debug "=> ReportedEffortController.addSave.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}" }        

        // save successfully
        if ( err == false && reportedEffortInstance.validate() && reportedEffortInstance.save(flush: true)) {

            if (debug) { log.debug "SAVE SUCCESSFULLY" }
            render(view: "/assignedEffort/show", model: [assignedEffortInstance: assignedEffortInstance, hideAddButton: hideAddButton] )
                        
        // save fails
        } else {

            if (debug) { 
				log.debug "SAVE FAILED"         
            	reportedEffortInstance.errors.each{ 
					log.debug it 
				}
			}
                        
            render(view: "create", model: [reportedEffortInstance: reportedEffortInstance, errMessage: errMessage])
                       
        } 

    } //def save

    def delete = {
        
		if (debug) {
			log.debug "=> REPORTED EFFORT CONTROLLER > DELETE -------------------"                
			log.debug "=> ReportedEffortController.delete.params: ${params}"
		}        
        
        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.read(params?.id)
        if (debug) { log.debug "=> ReportedEffortController.delete.assignedEffortInstance: ${assignedEffortInstance}" }        

		// REPORTED EFFORT
        def reportedEffortInstance = ReportedEffort.read(params?.reportedEffort?.id)
		if (debug) { log.debug "=> ReportedEffortController.delete.reportedEffortInstance: ${reportedEffortInstance}" }
		
        // validation
        def errMessage

        if ( reportedEffortInstance ) {
			
			if (debug) { log.debug "=> ReportedEffortController.delete.if(reportedEffortInstance)=TRUE" }        
            try {
                reportedEffortInstance.delete(flush: true)
				if (debug) { log.debug "=> DELETE SUCCESSFUL" }
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {				
				if (debug) { log.debug "=> DELETE FAILED" }
                errMessage = "Reported Effort could not be deleted"
            }
			
        } else {
            
			if (debug) { log.debug "=> ReportedEffortController.delete.if(reportedEffortInstance)=FALSE" }        
            errMessage = "Must select a Reported Effort to delete."
			
        }   
		     
        if (debug) { log.debug "=> ReportedEffortController.delete.errMessage: ${errMessage}" }        		
        render(view: "/assignedEffort/show", model: [assignedEffortInstance: assignedEffortInstance, errMessage: errMessage] )
       
    } //def delete
    
    def edit = {
        
		if (debug) {
	        log.debug "=> REPORTED EFFORT CONTROLLER > EDIT --------------------"                
	        log.debug "=> ReportedEffortController.edit.params: ${params}"
		}
        
        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
        if (debug) { log.debug "=> ReportedEffortController.edit.assignedEffortInstance: ${assignedEffortInstance}" }
		
		// CHANGE ABOVE CODE
		// def assignedEffortInstance = AssignedEffort.read(params?.id)
		// TO THIS        
		// def reportedEffortInstance = ReportedEffort.get(params?.id)
		     
        
        // REPORTED EFFORT 
        def reportedEffortInstance
        if ( params?.reportedEffort?.id ) {
            reportedEffortInstance = ReportedEffort.get(params?.reportedEffort?.id)            
        }
        if (debug) { log.debug "=> ReportedEffortController.edit.reportedEffortInstance: ${reportedEffortInstance}" }
		
        if ( reportedEffortInstance ) {
            			
            return [reportedEffortInstance: reportedEffortInstance]
            
        } else {
            
            def errMessage = "Must select a Reported Effort to edit"            
            
            def model = [
                assignedEffortInstance: assignedEffortInstance, 
                errMessage: errMessage
            ]
            
            render(view: "/assignedEffort/show", model: model )
            
        }
        
    } //def edit 
    
   def editSave = {
        
	   if (debug) {
			log.debug "=> REPORTED EFFORT CONTROLLER > EDIT-SAVE ---------------------"                
			log.debug "=> ReportedEffortController.editSave.params: ${params}"
	   }
                
		// REPORTED EFFORT
        def reportedEffortInstance = ReportedEffort.read(params.id)
		
		if (debug) {
	        log.debug "=> ReportedEffortController.editSave.reportedEffortInstance: ${reportedEffortInstance}"        
	        log.debug "=> ReportedEffortController.editSave.reportedEffortInstance.percentEffort: ${reportedEffortInstance.percentEffortConverted}"        
	        log.debug "=> ReportedEffortController.editSave.params?.version: ${params?.version}"
		}
		
        if ( reportedEffortInstance ) {
            
			// Determine version change
            if ( params.version ) {                
                def version = params.version.toLong()                                
                if (reportedEffortInstance.version > version) {
                    render(view: "edit", model: [reportedEffortInstance: reportedEffortInstance])
                    return
                }
            }

			// USER who entered effort
			def principal = springSecurityService.principal
			reportedEffortInstance.userCreated = principal.getUsername()
			if (debug) { log.debug "=> ReportedEffortController.editSave.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}" }

            // ASSIGNED EFFORT
            def assignedEffortInstance = reportedEffortInstance.assignedEffort
            if (debug) { log.debug "=> ReportedEffortController.editSave.assignedEffortInstance.id: ${assignedEffortInstance.id}" }
            if (debug) { log.debug "=> ReportedEffortController.editSave.assignedEffortInstance.assignedEffortConverted: ${assignedEffortInstance.assignedEffortConverted}" }
            
            // already REPORTED EFFORT total
            def cSum = ReportedEffort.createCriteria()
            def sumOfAlreadyReportedPercentEffort = cSum.get {
                eq("assignedEffort", assignedEffortInstance)
                ne("id", reportedEffortInstance.id)
                projections {
                    sum("percentEffort")
                }
            }
            if ( !sumOfAlreadyReportedPercentEffort ) {
                sumOfAlreadyReportedPercentEffort = 0
            }
            if (debug) { log.debug "=> AssignedEffortController.editSave.sumOfAlreadyReportedPercentEffort: ${sumOfAlreadyReportedPercentEffort}" }

            def sumOfAlreadyReportedPercentEffortConverted 
            if ( sumOfAlreadyReportedPercentEffort || sumOfAlreadyReportedPercentEffort == 0 ) {
                sumOfAlreadyReportedPercentEffortConverted = sumOfAlreadyReportedPercentEffort * 100                    
            } 
            if (debug) { log.debug "=> ReportedEffortController.editSave.sumOfAlreadyReportedPercentEffortConverted: ${sumOfAlreadyReportedPercentEffortConverted}" }

			// ReportedEffortInstance accepts new percentEffort entry
			reportedEffortInstance.properties = params
			
            // combined REPORTED EFFORT 
			def combineReportedEffortConverted = laborService.getCombineReportedEffortConverted(sumOfAlreadyReportedPercentEffortConverted, reportedEffortInstance.percentEffortConverted)			
            if (debug) { log.debug "=> ReportedEffortController.editSave.combineReportedEffortConverted: ${combineReportedEffortConverted}" }

            // REPORTED EFFORT ENTRY VALIDATION
            def err = false
            def errMessage
			def pRange = 0.0..100.0
			def hideAddButton = false
						 
            if ( reportedEffortInstance.percentEffortConverted.toBigDecimal() || reportedEffortInstance.percentEffortConverted.toBigDecimal() == 0 ) {

				// entry is zero
				if ( reportedEffortInstance.percentEffortConverted.toBigDecimal() == 0 ) {
					
					err = true
					errMessage = "Cannot enter zero percent effort."
					
				// entry is not a valid percent effort
				} else if ( !(pRange.containsWithinBounds(reportedEffortInstance.percentEffortConverted.toBigDecimal())) ) {
				
					err = true
					errMessage = "Please enter a valid percent effort."
					
                // entry is is greater than what is assigned
            	} else if ( reportedEffortInstance.percentEffortConverted.toBigDecimal() >  assignedEffortInstance.assignedEffortConverted.toBigDecimal() ) {

                    err = true
                    errMessage = "The effort you just entered is greater than what has been assigned to you."
					
                // combined reported effort is > than assigned effort
                } else if ( combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() > assignedEffortInstance.assignedEffortConverted.toBigDecimal() ) {                

                    err = true
                    errMessage = "The effort you just entered, plus what has already been reported, is greater than what is assigned to you."
					
				// combined reported effort equals what is assigned
				} else if ( combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() == assignedEffortInstance.assignedEffortConverted.toBigDecimal() ) {
				
					hideAddButton = true

                }
				
				if (debug) { log.debug "=> ReportedEffortController.editSave.reportedEffortInstance.percentEffortConverted BEFORE DISCARD(): ${reportedEffortInstance.percentEffortConverted}" }
				if (err) {
					reportedEffortInstance.discard()
				}
				if (debug) { log.debug "=> ReportedEffortController.editSave.reportedEffortInstance.percentEffortConverted AFTER DISCARD(): ${reportedEffortInstance.percentEffortConverted}" }
				
            }			
			if (debug) { log.debug "=> ReportedEffortController.editSave.errMessage: ${errMessage}" }
			
            // SAVE
            if ( !err && reportedEffortInstance.validate() && !reportedEffortInstance.hasErrors() && reportedEffortInstance.save(flush: true) ) {
                
                if (debug) { log.debug "SAVE SUCCESSFULLY" }
                render(view: "/assignedEffort/show", model:[assignedEffortInstance: assignedEffortInstance, hideAddButton: hideAddButton] )
                
            } else {
                
                if (debug) { log.debug "=> SAVE FAILED" }
				
                def model = [
                    reportedEffortInstance: reportedEffortInstance, 
                    errMessage: errMessage
                ]
                
                if (debug) { log.debug "=> ReportedEffortController.editSave.model: ${model}" }
                render(view: "edit", model: model)
                
            } //if ( err == false &&  !reportedEffortInstance.hasErrors() && reportedEffortInstance.save(flush: true) )
            
        }  else {
            
            render(view: "main/show")
            
        } //if ( reportedEffortInstance )
        
    } //def editSave
   
} //class ReportedEffortController
    
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

