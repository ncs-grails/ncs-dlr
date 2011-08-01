package edu.umn.ncs
import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class AssignedEffortController {

    def authenticateService
    def laborService
	def debug = true

	def index = {
		
		if (debug) { 
			println "ASSIGNED EFFORT CONTROLLER > INDEX ----------------------------"
			println "=> AssignedEffortController.index.params: ${params}"
		}

	}
	
    def show = {
        
		if (debug) {
	        println "ASSIGNED EFFORT CONTROLLER > SHOW -----------------------------"                
	        println "=> AssignedEffortController.show.params: ${params}"
		}
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)
		if ( assignedEffortInstance ) {
	        println "=> AssignedEffortController.show.assignedEffortInstance: ${assignedEffortInstance}"
			println "=> AssignedEffortController.show.assignedEffortInstance.id: ${assignedEffortInstance.id}"
		}
		
		[ assignedEffortInstance: assignedEffortInstance ]        

    } 
 
    def showCurrent = {

		if (debug) {
	        println "ASSIGNED EFFORT CONTROLLER > SHOW CURRENT ---------------------"                
	        println "=> AssignedEffortController.showCurrent.params: ${params}"
		}

        // get perameters
        def assignedEffortInstance
        def reportingStaffInstance
        def reportingPeriodInstance
        
        if (params?.id) {
            assignedEffortInstance = AssignedEffort.read(params?.id)
            reportingStaffInstance = assignedEffortInstance.reportingStaff
            reportingPeriodInstance = assignedEffortInstance.period
        }
        if (debug) { 
			println "=> AssignedEffortController.showCurrent.assignedEffortInstance: ${assignedEffortInstance}"
			println "=> AssignedEffortController.showCurrent.reportingStaffInstance: ${reportingStaffInstance}"
			println "=> AssignedEffortController.showCurrent.reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        
        // Get REPORTED EFFORT total
		
		def reportedEffortTotal = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
		if (debug) { println "=> ReportedEffortController.addSave.reportedEffortTotal: ${reportedEffortTotal}" }

		def reportedEffortTotalConverted
		if ( reportedEffortTotal ) {
			reportedEffortTotalConverted = reportedEffortTotal * 100
		}
		if (debug) { println "=> ReportedEffortController.addSave.reportedEffortTotalConverted: ${reportedEffortTotalConverted}" }
        
		def notReportedEffortConverted
		if ( assignedEffortInstance.assignedEffortConverted && reportedEffortTotalConverted ) {
			notReportedEffortConverted = assignedEffortInstance.assignedEffortConverted - reportedEffortTotalConverted
		} 
			
		if (debug) { println "=> ReportedEffortController.addSave.notReportedEffortConverted: ${notReportedEffortConverted}" }
		
        // Get list of REPORTED EFFORT
        def c2 = ReportedEffort.createCriteria()        
        def reportingEffortInstanceList = c2.list{
            eq("assignedEffort", assignedEffortInstance) 
            order("activity", "asc")
            order("task", "asc")
        }
        if (debug) { println "=> AssignedEffortController.showCurrent.reportingEffortInstanceList: ${reportingEffortInstanceList}" }

        // Add records to Reported Effort Instance
        def reportedEffortList = []

        reportingEffortInstanceList.eachWithIndex{ rs, i -> 
            
            // Create record map
            def record = [:]
            
            // Get effort id
            record.reportedEffortId = rs.id
            //if (debug) { println "=> AssignedEffortController.showCurrent.record.reportedEffortId: ${record.reportedEffortId}" }            
            
            // Get study activity
            record.studyActivity = rs.activity
            //if (debug) { println "=> AssignedEffortController.showCurrent.record.studyActivity: ${record.studyActivity}" }
            
            // Get study task
            record.studyTask = rs.task
            //if (debug) { println "=> AssignedEffortController.showCurrent.record.studyTask: ${record.studyTask}" }
            
            // Get percentEffort
            record.percentEffortConverted = rs.percentEffortConverted
            //if (debug) { println "=> AssignedEffortController.showCurrent.record.percentEffortConverted: ${record.percentEffortConverted}" }
            
            //Get DateCreated
            record.dateCreated = rs.dateCreated
            //if (debug) { println "=> AssignedEffortController.showCurrent.record.dateCreated: ${record.dateCreated}" }
            
            reportedEffortList.add(record)
            
        } //reportingStaffInstanceList.eachWithIndex{ rs, i ->        
        
            
        if (debug) { println "=> AssignedEffortController.showCurrent.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}" }            

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
        
        if (debug) { 
			println "ASSIGNED EFFORT CONTROLLER > SHOW PAST ------------------------"                
			println "=> params: ${params}"
        }
        
		// REPORTING STAFF
        def reportingStaffInstance        
        if (params?.id) {
            reportingStaffInstance = ReportingStaff.read(params?.id)
        }
        if (debug) { println "=> reportingStaffInstance: ${reportingStaffInstance}" }
                
		// REPORTED EFFORT
        def cRE = ReportedEffort.createCriteria()
        //if (debug) { println "=> cRE: ${cRE}" }
        def reportedEffortInstance = cRE.list{
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
        //if (debug) { println "=> reportedEffortInstance: ${reportedEffortInstance}" }

        // REPORTED EFFORT LIST
		def reportedEffortList = []
        
        // Add records to Assigned Effort Instance
        reportedEffortInstance.eachWithIndex{ rs, i ->
            
            //if (debug) { println "=> reportedEffortInstance.id: ${reportedEffortInstance.id}" }            
            
            // Create record map
            def record = [:]
            
            // Get Assigned Effort Id
            record.assignedEffortId = rs.assignedEffort.id
            //if (debug) { println "=> record.assignedEffortId: ${record.assignedEffortId}" }
                
            // Get Period Date
            record.period = rs.assignedEffort.period.periodDate          
            //if (debug) { println "=> record.period: ${record.period}" }
                                    
            // Get Study Activity
            record.activity = rs.activity            
            //if (debug) { println "=> record.activity: ${record.activity}" }
            
            // Get Study Task
            record.task = rs.task            
            //if (debug) { println "=> record.task: ${record.task}" }
            
            // Get Reported Effort
            record.reportedEffort = rs.percentEffort
            //if (debug) { println "=> record.reportedEffort: ${record.reportedEffort}" }
                       
            reportedEffortList.add(record)                        
            
        }
		//if (debug) { println "=> reportedEffortList: ${reportedEffortList}" }		
        
        [ reportedEffortList: reportedEffortList ]
                
    } //def showPast
    
    def commit = {

		if (debug) {
	        println "ASSIGNED EFFORT CONTROLLER > COMMIT ---------------------------"                
	        println "=> AssignedEffortController.commit.params: ${params}"
		}
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
		if (debug) { println "=> AssignedEffortController.commit.assignedEffortInstance: ${assignedEffortInstance}" }
		
        if ( assignedEffortInstance ) {
            
			// ASSIGNED EFFORT
            def assignedPercentEffort = assignedEffortInstance.assignedEffort
			if (debug) {
				println "=> AssignedEffortController.commit.assignedEffortInstance.id: ${assignedEffortInstance.id}"     
				println "=> AssignedEffortController.commit.assignedEffortInstance.period: ${assignedEffortInstance.period}"
			}
			
			// sum of REPORTED EFFORT
            def sumOfReportedPercentEffort = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
            if (debug) { println "=> AssignedEffortController.commit.sumOfReportedPercentEffort: ${sumOfReportedPercentEffort}" }
                        
			// commit REPORTED EFFORT validation			
			def errMessage

			if ( sumOfReportedPercentEffort ) {

				// total reported effort does not equal assigned effort, cannot commit
				if ( sumOfReportedPercentEffort.toBigDecimal() != assignedPercentEffort.toBigDecimal() ) {
					
					// total reported effort is less than what is assigned
					if ( sumOfReportedPercentEffort.toBigDecimal() < assignedPercentEffort.toBigDecimal() ) {
						errMessage = "Cannot COMMIT your reported effort because it is less than what has been assigned to you."
					// total reported effort is greater than what is assigned
					} else if ( sumOfReportedPercentEffort.toBigDecimal() > assignedPercentEffort.toBigDecimal() ) {
						errMessage = "Cannot COMMIT your reported effort because it is greater than what has been assigned to you."
					}
					if (debug) { println "=> AssignedEffortController.commit.errMessage: ${errMessage}" }
					
					render(view: "show", model: [assignedEffortInstance: assignedEffortInstance, errMessage: errMessage])
						
				// total reported effort equals assigned effort, attempt to commit
				} else {
				
					def principal = authenticateService.principal()
					def reportingStaffInstance = laborService.getReportingStaff(principal)
					if (debug) { println "=> AssignedEffortController.commit.reportingStaffInstance: ${reportingStaffInstance}" }
		
					assignedEffortInstance.dateCommitted = new Date()
					assignedEffortInstance.commitingStaff = reportingStaffInstance
					if (debug) {
						println "=> AssignedEffortController.commit.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"
						println "=> AssignedEffortController.commit.assignedEffortInstance.commitingStaff: ${assignedEffortInstance.commitingStaff}"
					}
													
					if ( assignedEffortInstance.validate() && !assignedEffortInstance.hasErrors() && assignedEffortInstance.save(flush: true)) {
		
						if (debug) { println "COMMIT SUCCESSFULLY" }
											
						// TODO: if all assigned effort, for this period, is committed, send email alerting to all administrators
						def countOfNotCommittedAssignedEffort = laborService.countNotCommittedAssignedEffort(assignedEffortInstance.period)
						if (debug) { println "=> AssignedEffortController.commit.countOfNotCommittedAssignedEffort: ${countOfNotCommittedAssignedEffort}" }
			
						if (debug) { println "=> RUN generateReportEmail" }
						if ( !countOfNotCommittedAssignedEffort) {
							def message = laborService.sendAllAssignedEffortIsCommittedEmailAlert(assignedEffortInstance.period)
						}
						
						def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
				
						def model = [
							reportingStaffInstance: reportingStaffInstance,
							reportingPeriodInstance: reportingPeriodInstance,
							assignedEffortInstance: assignedEffortInstance
						]
						
						redirect(action: "committed")
						
					} else {
						
						if (debug) { println "COMMIT FAILED" }
						errMessage = "Failed to COMMIT."
						
						render(view: "show", model: [ assignedEffortInstance: assignedEffortInstance, errMessage: errMessage ])
						
					} 
	
				} //( sumOfReportedPercentEffort.toBigDecimal() !== assignedPercentEffort.toBigDecimal() )
				
			} //if ( !sumOfReportedPercentEffort )
							
        } //if ( assignedEffortInstance )

    } //def commit 
	
	def committed = {
		
		if (debug) {
	        println "ASSIGNED EFFORT CONTROLLER > COMMITTED-------------------------"                
	        println "=> AssignedEffortController.commit.params: ${params}"
		}

		def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
		
		[reportingPeriodInstance: reportingPeriodInstance]		
		
	}

        
}
