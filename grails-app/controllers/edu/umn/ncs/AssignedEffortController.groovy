package edu.umn.ncs

import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;

class AssignedEffortController {

    def authenticateService
    def laborService

	def index = {
		
		println "PRINTLN ASSIGNED EFFORT CONTROLLER > INDEX --------------------"
		println "PRINTLN AssignedEffortController.index.params: ${params}"

	}
	
    def show = {
        
        println "PRINTLN ASSIGNED EFFORT CONTROLLER > SHOW ---------------------"                
        println "PRINTLN AssignedEffortController.show.params: ${params}"
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)
		if ( assignedEffortInstance ) {
	        println "PRINTLN AssignedEffortController.show.assignedEffortInstance: ${assignedEffortInstance}"
			println "PRINTLN AssignedEffortController.show.assignedEffortInstance.id: ${assignedEffortInstance.id}"
		}
		
		[ assignedEffortInstance: assignedEffortInstance ]        

    } 
 
    def showCurrent = {

        println "PRINTLN ASSIGNED EFFORT CONTROLLER > SHOW CURRENT -------------"                
        println "PRINTLN AssignedEffortController.showCurrent.params: ${params}"

        // get perameters
        def assignedEffortInstance
        def reportingStaffInstance
        def reportingPeriodInstance
        
        if (params?.id) {
            assignedEffortInstance = AssignedEffort.read(params?.id)
            reportingStaffInstance = assignedEffortInstance.reportingStaff
            reportingPeriodInstance = assignedEffortInstance.period
        }
        println "PRINTLN AssignedEffortController.showCurrent.assignedEffortInstance: ${assignedEffortInstance}"
        println "PRINTLN AssignedEffortController.showCurrent.reportingStaffInstance: ${reportingStaffInstance}"
        println "PRINTLN AssignedEffortController.showCurrent.reportingPeriodInstance: ${reportingPeriodInstance}"
        
        // Get REPORTED EFFORT total
		
		def reportedEffortTotal = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
		println "PRINTLN ReportedEffortController.addSave.reportedEffortTotal: ${reportedEffortTotal}"

		def reportedEffortTotalConverted
		if ( reportedEffortTotal ) {
			reportedEffortTotalConverted = reportedEffortTotal * 100
		}
		println "PRINTLN ReportedEffortController.addSave.reportedEffortTotalConverted: ${reportedEffortTotalConverted}"
        
		def notReportedEffortConverted
		if ( assignedEffortInstance.assignedEffortConverted && reportedEffortTotalConverted ) {
			notReportedEffortConverted = assignedEffortInstance.assignedEffortConverted - reportedEffortTotalConverted
		} 
			
		println "PRINTLN ReportedEffortController.addSave.notReportedEffortConverted: ${notReportedEffortConverted}"
		
        // Get list of REPORTED EFFORT
        def c2 = ReportedEffort.createCriteria()        
        def reportingEffortInstanceList = c2.list{
            eq("assignedEffort", assignedEffortInstance) 
            order("activity", "asc")
            order("task", "asc")
        }
        println "PRINTLN AssignedEffortController.showCurrent.reportingEffortInstanceList: ${reportingEffortInstanceList}"

        // Add records to Reported Effort Instance
        def reportedEffortList = []

        reportingEffortInstanceList.eachWithIndex{ rs, i -> 
            
            // Create record map
            def record = [:]
            
            // Get effort id
            record.reportedEffortId = rs.id
            //println "PRINTLN AssignedEffortController.showCurrent.record.reportedEffortId: ${record.reportedEffortId}"            
            
            // Get study activity
            record.studyActivity = rs.activity
            //println "PRINTLN AssignedEffortController.showCurrent.record.studyActivity: ${record.studyActivity}"
            
            // Get study task
            record.studyTask = rs.task
            //println "PRINTLN AssignedEffortController.showCurrent.record.studyTask: ${record.studyTask}"
            
            // Get percentEffort
            record.percentEffortConverted = rs.percentEffortConverted
            //println "PRINTLN AssignedEffortController.showCurrent.record.percentEffortConverted: ${record.percentEffortConverted}"
            
            //Get DateCreated
            record.dateCreated = rs.dateCreated
            //println "PRINTLN AssignedEffortController.showCurrent.record.dateCreated: ${record.dateCreated}"
            
            reportedEffortList.add(record)
            
        } //reportingStaffInstanceList.eachWithIndex{ rs, i ->        
        
            
        println "PRINTLN AssignedEffortController.showCurrent.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"            

        [
            reportingPeriodInstance:reportingPeriodInstance, 
            reportingStaffInstance:reportingStaffInstance,
            assignedEffortInstance: assignedEffortInstance,
            reportedEffortTotalConverted: reportedEffortTotalConverted,
			notReportedEffortConverted: notReportedEffortConverted,
            reportedEffortList: reportedEffortList,
            isForm: params?.isForm
        ]
       
    } //def showCurrent
    
    
    def showPast = {
        
        println "PRINTLN ASSIGNED EFFORT CONTROLLER > SHOW PAST ----------------"                
        println "PRINTLN AssignedEffortController.showPast.params: ${params}"
        
		// REPORTING STAFF
        def reportingStaffInstance        
        if (params?.id) {
            reportingStaffInstance = ReportingStaff.read(params?.id)
        }
        println "PRINTLN AssignedEffortController.showPast.reportingStaffInstance: ${reportingStaffInstance}"
                
		// REPORTED EFFORT
        def cre = ReportedEffort.createCriteria()
        def reportedEffortInstance = cre.list{
            and {
                assignedEffort{
                    period {
                        order("periodDate","desc")            
                    }
                    reportingStaff { 
                        idEq(reportingStaffInstance.id) 
                    }                    
                    isNotNull("dateCommitted")    
                    maxResults(12)
                }                                
            }
            order("percentEffort", "desc")
        }
        println "PRINTLN AssignedEffortController.showPast.reportedEffortInstance: ${reportedEffortInstance}"

        // REPORTED EFFORT LIST
		def reportedEffortList = []
        
        // Add records to Assigned Effort Instance
        reportedEffortInstance.eachWithIndex{ rs, i ->
            
            //println "PRINTLN AssignedEffortController.showPast.reportedEffortInstance.id: ${reportedEffortInstance.id}"            
            
            // Create record map
            def record = [:]
            
            // Get Assigned Effort Id
            record.assignedEffortId = rs.assignedEffort.id
            //println "PRINTLN AssignedEffortController.showPast.record.assignedEffortId: ${record.assignedEffortId}"
                
            // Get Period Date
            record.period = rs.assignedEffort.period.periodDate          
            //println "PRINTLN AssignedEffortController.showPast.record.period: ${record.period}"
                                    
            // Get Study Activity
            record.activity = rs.activity            
            //println "PRINTLN AssignedEffortController.showPast.record.activity: ${record.activity}"
            
            // Get Study Task
            record.task = rs.task            
            //println "PRINTLN AssignedEffortController.showPast.record.task: ${record.task}"
            
            // Get Reported Effort
            record.reportedEffort = rs.percentEffort
            //println "PRINTLN AssignedEffortController.showPast.record.reportedEffort: ${record.reportedEffort}"
                       
            reportedEffortList.add(record)                        
            
        }
        
        [ reportedEffortList: reportedEffortList ]
                
    } //def showPast
    
    def commit = {
        
        println "PRINTLN ASSIGNED EFFORT CONTROLLER > COMMIT -------------------"                
        println "PRINTLN AssignedEffortController.commit.params: ${params}"
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
		println "PRINTLN AssignedEffortController.commit.assignedEffortInstance: ${assignedEffortInstance}"
		
        if ( assignedEffortInstance ) {
            
			// ASSIGNED EFFORT
            def assignedPercentEffort = assignedEffortInstance.assignedEffort
			println "PRINTLN AssignedEffortController.commit.assignedEffortInstance.id: ${assignedEffortInstance.id}"     
			println "PRINTLN AssignedEffortController.commit.assignedEffortInstance.period: ${assignedEffortInstance.period}"
			
			// sum of REPORTED EFFORT
            def sumOfReportedPercentEffort = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
            println "PRINTLN AssignedEffortController.commit.sumOfReportedPercentEffort: ${sumOfReportedPercentEffort}"
                        
			// commit REPORTED EFFORT validation			
			def errMessage
						
			// total reported effort does not equals assigned effort, attempt to commit                
			if ( sumOfReportedPercentEffort.toBigDecimal() != assignedPercentEffort.toBigDecimal() ) {
				
				// total reported effort is less than what is assigned
				if ( sumOfReportedPercentEffort.toBigDecimal() < assignedPercentEffort.toBigDecimal() ) {
					
					errMessage = "Cannot COMMIT your reported effort because it is less than what has been assigned to you."
	
				// total reported effort is greater than what is assigned
				} else if ( sumOfReportedPercentEffort.toBigDecimal() > assignedPercentEffort.toBigDecimal() ) {
					
					errMessage = "Cannot COMMIT your reported effort because it is greater than what has been assigned to you."
				}
				
				println "PRINTLN AssignedEffortController.commit.errMessage: ${errMessage}"
				render(view: "/assignedEffort/show", model: [ assignedEffortInstance: assignedEffortInstance, errMessage: errMessage ])
				
					
			// total reported effort equals assigned effort, attempt to commit                
			} else {
			
				def principal = authenticateService.principal()
				def reportingStaffInstance = laborService.getReportingStaff(principal)
				println "PRINTLN AssignedEffortController.commit.reportingStaffInstance: ${reportingStaffInstance}"
	
				assignedEffortInstance.dateCommitted = new Date()
				assignedEffortInstance.commitingStaff = reportingStaffInstance
				println "PRINTLN AssignedEffortController.commit.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"
				println "PRINTLN AssignedEffortController.commit.assignedEffortInstance.commitingStaff: ${assignedEffortInstance.commitingStaff}"
												
				if ( assignedEffortInstance.validate() && !assignedEffortInstance.hasErrors() && assignedEffortInstance.save(flush: true)) {
	
					println "COMMIT SUCCESSFULLY"
										
					// TODO: if all assigned effort is committed, send email alert to all administrators
					def countOfNotCommittedAssignedEffort = laborService.countNotCommittedAssignedEffort(assignedEffortInstance.period)
					println "PRINTLN AssignedEffortController.commit.countOfNotCommittedAssignedEffort: ${countOfNotCommittedAssignedEffort}"
		
					if ( !countOfNotCommittedAssignedEffort) {
						println "PRINTLN RUN sendAllAssignedEffortIsCommittedEmailAlert "
					}
						
					redirect(controller: 'main', action: "show")
					
				} else {
					
					println "COMMIT FAILED"
					errMessage = "Failed to COMMIT."
					render(view: "/assignedEffort/show", model: [ assignedEffortInstance: assignedEffortInstance, errMessage: errMessage ])
					
				} 

			} //( sumOfReportedPercentEffort.toBigDecimal() !== assignedPercentEffort.toBigDecimal() )
							
        } //if ( assignedEffortInstance )

    } //def commit 
        
}
