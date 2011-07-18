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

    def addSave = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > ADD-SAVE ---------------------"
        println "PRINTLN ReportedEffortController.addSave.params: ${params}"        
        
        // REPORTED EFFORT
        def reportedEffortInstance = new ReportedEffort(params)
        def reportedEffortConvertedVal = reportedEffortInstance.percentEffortConverted

        println "PRINTLN ReportedEffortController.addSave.reportedEffortInstance: ${reportedEffortInstance}"                    
        println "PRINTLN ReportedEffortController.addSave.reportedEffortConvertedVal: ${reportedEffortConvertedVal}"
		
		def dataTypeIs = reportedEffortConvertedVal.getClass()
		println "PRINTLN ReportedEffortController.addSave.reportedEffortConvertedVal.dataTypeIs: ${dataTypeIs}"
		
        // ASSIGNED EFFORT
        def assignedEffortInstance = AssignedEffort.read(params?.assignedEffort?.id)
        def assignedEffortConverted = assignedEffortInstance.assignedEffortConverted

        println "PRINTLN ReportedEffortController.addSave.assignedEffortInstance: ${assignedEffortInstance}"                                
        println "PRINTLN ReportedEffortController.addSave.assignedEffortConverted}: ${assignedEffortConverted}"

        // COMBINED REPORTED EFFORT
		def sumReportedPercentEffort = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)		
        println "PRINTLN ReportedEffortController.addSave.sumReportedPercentEffort: ${sumReportedPercentEffort}"        

        def sumReportedPercentEffortConverted 
        if ( sumReportedPercentEffort ) {
            sumReportedPercentEffortConverted = sumReportedPercentEffort * 100                    
        }
        println "PRINTLN ReportedEffortController.addSave.sumReportedPercentEffortConverted: ${sumReportedPercentEffortConverted}"
        
        def combineReportedEffortConverted = laborService.getCombineReportedEffortConverted(sumReportedPercentEffortConverted, reportedEffortConvertedVal)
        println "PRINTLN ReportedEffortController.addSave.combineReportedEffortConverted: ${combineReportedEffortConverted}"

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

            println "PRINTLN ReportedEffortController.addSave.errMessage: ${errMessage}"
            
        }

        // USER who entered effort
        def principal = authenticateService.principal()                         
        reportedEffortInstance.userCreated = principal.getUsername()
        println "PRINTLN ReportedEffortController.addSave.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}"        

        // save successfully
        if ( err == false && reportedEffortInstance.validate() && reportedEffortInstance.save(flush: true)) {

            println "SAVE SUCCESSFULLY"

            render(view: "/assignedEffort/show", model: [assignedEffortInstance: assignedEffortInstance, hideAddButton: hideAddButton] )
                        
        // save fails
        } else {

            println "SAVE FAILED"        
            reportedEffortInstance.errors.each{ 
				println it 
			}
                        
            render(view: "create", model: [reportedEffortInstance: reportedEffortInstance, errMessage: errMessage])
                       
        } 

    } //def save

    def delete = {
        
        println "PRINTLN REPORTED EFFORT CONTROLLER > DELETE -------------------"                
        println "PRINTLN ReportedEffortController.delete.params: ${params}"        
        
        // ASSIGNED & REPORTED EFFORT
        def reportedEffortInstance = ReportedEffort.read(params?.reportedEffort?.id)
		println "PRINTLN ReportedEffortController.delete.reportedEffortInstance: ${reportedEffortInstance}"
		
        def assignedEffortInstance 
        if ( reportedEffortInstance ) {
            assignedEffortInstance = reportedEffortInstance.assignedEffort            
        }
        println "PRINTLN ReportedEffortController.delete.assignedEffortInstance: ${assignedEffortInstance}"        
 
        // DELETE
        def errMessage

        if ( reportedEffortInstance ) {
			println "PRINTLN ReportedEffortController.delete.if(reportedEffortInstance)=true"        
            try {
                reportedEffortInstance.delete(flush: true)
				println "PRINTLN DELETE SUCCESSFUL"        
                //flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				println "PRINTLN DELETE FAILED"        
                errMessage = "Reported Effort could not be deleted"
                //flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
            }
        } else {
            
			println "PRINTLN ReportedEffortController.delete.if(reportedEffortInstance)=false"        
            errMessage = "Must select a Reported Effort to delete."
            //flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportedEffort.label', default: 'ReportedEffort'), params.id])}"
        }        
        println "PRINTLN ReportedEffortController.delete.errMessage: ${errMessage}"        
		
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
        println "PRINTLN ReportedEffortController.editSave.params: ${params}"
                
		// REPORTED EFFORT
        def reportedEffortInstance = ReportedEffort.read(params.id)
        println "PRINTLN ReportedEffortController.editSave.reportedEffortInstance: ${reportedEffortInstance}"        
        println "PRINTLN ReportedEffortController.editSave.params?.version: ${params?.version}"
        
        if ( reportedEffortInstance ) {
            
			// Determine version change
            if ( params.version ) {                
                def version = params.version.toLong()                                
                if (reportedEffortInstance.version > version) {
                    render(view: "edit", model: [reportedEffortInstance: reportedEffortInstance])
                    return
                }
            }
            
            // ASSIGNED EFFORT
            def assignedEffortInstance = reportedEffortInstance.assignedEffort
            println "PRINTLN ReportedEffortController.editSave.assignedEffortInstance: ${assignedEffortInstance}"
            
            // REPORTED EFFORT totals, excluding what is currently being edited
            def cSum = ReportedEffort.createCriteria()
            def sumOfReportedPercentEffort = cSum.get {
                eq("assignedEffort", assignedEffortInstance)
                ne("id", reportedEffortInstance.id)
                projections {
                    sum("percentEffort")
                }
            }
            if ( !sumOfReportedPercentEffort ) {
                sumOfReportedPercentEffort = 0
            }
            println "PRINTLN AssignedEffortController.editSave.sumOfReportedPercentEffort: ${sumOfReportedPercentEffort}"

            def sumReportedPercentEffortConverted 
            if ( sumOfReportedPercentEffort || sumOfReportedPercentEffort == 0 ) {
                sumReportedPercentEffortConverted = sumOfReportedPercentEffort * 100                    
            } 
            println "PRINTLN ReportedEffortController.editSave.sumReportedPercentEffortConverted: ${sumReportedPercentEffortConverted}"

			reportedEffortInstance = ReportedEffort.get(params.id)
            println "PRINTLN ReportedEffortController.editSave.reportedEffortInstance: ${reportedEffortInstance}"
			reportedEffortInstance.properties = params

	        // USER who entered effort
	        def principal = authenticateService.principal()   
			reportedEffortInstance.userCreated = principal.getUsername()
	        println "PRINTLN ReportedEffortController.editSave.reportedEffortInstance.userCreated: ${reportedEffortInstance.userCreated}"
			
			def combineReportedEffortConverted = laborService.getCombineReportedEffortConverted(sumReportedPercentEffortConverted, reportedEffortInstance.percentEffortConverted)			
            println "PRINTLN ReportedEffortController.editSave.combineReportedEffortConverted: ${combineReportedEffortConverted}"

            // REPORTED EFFORT ENTRY VALIDATION
            def err = false
            def errMessage
			def pRange = 0.0..100.0
			def hideAddButton = false
			
			/*
			if ( reportedEffortInstance.percentEffortConverted.toBigDecimal() == 0 ) { 
				println "reportedEffortInstance.percentEffortConverted.toBigDecimal() == 0" 
			}
			if ( combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() == assignedEffortInstance.assignedEffortConverted.toBigDecimal() ) {
				println "combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() == assignedEffortInstance.assignedEffortConverted.toBigDecimal()"
			}
			*/
			 
            println "PRINTLN ReportedEffortController.editSave.reportedEffortInstance.percentEffortConverted: ${reportedEffortInstance.percentEffortConverted}"			
            if ( reportedEffortInstance.percentEffortConverted.toBigDecimal() || reportedEffortInstance.percentEffortConverted.toBigDecimal() == 0 ) {

				// entry is zero
				if ( reportedEffortInstance.percentEffortConverted.toBigDecimal() == 0 ) {
					
					err = true
					errMessage = "Cannot enter zero percent effort."
					//reportedEffortInstance.discard()
					
				// entry is not a valid percent effort
				} else if ( !(pRange.containsWithinBounds(reportedEffortInstance.percentEffortConverted.toBigDecimal())) ) {
				
					err = true
					errMessage = "Please enter a valid percent effort."
					//reportedEffortInstance.discard()
					
                // entry is is greater than what is assigned
            	} else if ( reportedEffortInstance.percentEffortConverted.toBigDecimal() >  assignedEffortInstance.assignedEffortConverted.toBigDecimal() ) {

                    err = true
                    errMessage = "The effort you just entered is greater than what has been assigned to you."
					def rei = ReportedEffort.read(reportedEffortInstance.id)
					println "rei: ${rei.percentEffort} "
					//reportedEffortInstance.discard()
					
                // combined reported effort is > than assigned effort
                } else if ( combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() > assignedEffortInstance.assignedEffortConverted.toBigDecimal() ) {                

                    err = true
                    errMessage = "The effort you just entered, plus what has already been reported, is greater than what is assigned to you."
					//reportedEffortInstance.discard()
					
				// combined reported effort equals what is assigned
				} else if ( combineReportedEffortConverted && combineReportedEffortConverted.toBigDecimal() == assignedEffortInstance.assignedEffortConverted.toBigDecimal() ) {
				
					hideAddButton = true

                }

            }
            println "PRINTLN ReportedEffortController.editSave.errMessage: ${errMessage}"
                    
            // SAVE
            if ( !err && reportedEffortInstance.validate() && !reportedEffortInstance.hasErrors() && reportedEffortInstance.save(flush: true) ) {
                
                println "SAVE SUCCESSFULLY"
                render(view: "/assignedEffort/show", model:[assignedEffortInstance: assignedEffortInstance, hideAddButton: hideAddButton] )
                
            } else {
                
                println "PRINTLN SAVE FAILED"
				
                def model = [
                    reportedEffortInstance: reportedEffortInstance, 
                    errMessage: errMessage
                ]
                
                println "PRINTLN ReportedEffortController.editSave.model: ${model}"
                render(view: "edit", model: model)
                
            } //if ( err == false &&  !reportedEffortInstance.hasErrors() && reportedEffortInstance.save(flush: true) )
            
        }  else {
            
            render(view: "main/show")
            
        } //if ( reportedEffortInstance )
        
    } //def editSave
        
    
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