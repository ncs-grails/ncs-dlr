package edu.umn.ncs
import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class AssignedEffortController {

    def authenticateService
    def laborService

	def index = {
		
		println "ASSIGNED EFFORT CONTROLLER > INDEX ----------------------------"
		println "=> AssignedEffortController.index.params: ${params}"

	}
	
    def show = {
        
        println "ASSIGNED EFFORT CONTROLLER > SHOW -----------------------------"                
        println "=> AssignedEffortController.show.params: ${params}"
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)
		if ( assignedEffortInstance ) {
	        println "=> AssignedEffortController.show.assignedEffortInstance: ${assignedEffortInstance}"
			println "=> AssignedEffortController.show.assignedEffortInstance.id: ${assignedEffortInstance.id}"
		}
		
		[ assignedEffortInstance: assignedEffortInstance ]        

    } 
 
    def showCurrent = {

        println "ASSIGNED EFFORT CONTROLLER > SHOW CURRENT ---------------------"                
        println "=> AssignedEffortController.showCurrent.params: ${params}"

        // get perameters
        def assignedEffortInstance
        def reportingStaffInstance
        def reportingPeriodInstance
        
        if (params?.id) {
            assignedEffortInstance = AssignedEffort.read(params?.id)
            reportingStaffInstance = assignedEffortInstance.reportingStaff
            reportingPeriodInstance = assignedEffortInstance.period
        }
        println "=> AssignedEffortController.showCurrent.assignedEffortInstance: ${assignedEffortInstance}"
        println "=> AssignedEffortController.showCurrent.reportingStaffInstance: ${reportingStaffInstance}"
        println "=> AssignedEffortController.showCurrent.reportingPeriodInstance: ${reportingPeriodInstance}"
        
        // Get REPORTED EFFORT total
		
		def reportedEffortTotal = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
		println "=> ReportedEffortController.addSave.reportedEffortTotal: ${reportedEffortTotal}"

		def reportedEffortTotalConverted
		if ( reportedEffortTotal ) {
			reportedEffortTotalConverted = reportedEffortTotal * 100
		}
		println "=> ReportedEffortController.addSave.reportedEffortTotalConverted: ${reportedEffortTotalConverted}"
        
		def notReportedEffortConverted
		if ( assignedEffortInstance.assignedEffortConverted && reportedEffortTotalConverted ) {
			notReportedEffortConverted = assignedEffortInstance.assignedEffortConverted - reportedEffortTotalConverted
		} 
			
		println "=> ReportedEffortController.addSave.notReportedEffortConverted: ${notReportedEffortConverted}"
		
        // Get list of REPORTED EFFORT
        def c2 = ReportedEffort.createCriteria()        
        def reportingEffortInstanceList = c2.list{
            eq("assignedEffort", assignedEffortInstance) 
            order("activity", "asc")
            order("task", "asc")
        }
        println "=> AssignedEffortController.showCurrent.reportingEffortInstanceList: ${reportingEffortInstanceList}"

        // Add records to Reported Effort Instance
        def reportedEffortList = []

        reportingEffortInstanceList.eachWithIndex{ rs, i -> 
            
            // Create record map
            def record = [:]
            
            // Get effort id
            record.reportedEffortId = rs.id
            //println "=> AssignedEffortController.showCurrent.record.reportedEffortId: ${record.reportedEffortId}"            
            
            // Get study activity
            record.studyActivity = rs.activity
            //println "=> AssignedEffortController.showCurrent.record.studyActivity: ${record.studyActivity}"
            
            // Get study task
            record.studyTask = rs.task
            //println "=> AssignedEffortController.showCurrent.record.studyTask: ${record.studyTask}"
            
            // Get percentEffort
            record.percentEffortConverted = rs.percentEffortConverted
            //println "=> AssignedEffortController.showCurrent.record.percentEffortConverted: ${record.percentEffortConverted}"
            
            //Get DateCreated
            record.dateCreated = rs.dateCreated
            //println "=> AssignedEffortController.showCurrent.record.dateCreated: ${record.dateCreated}"
            
            reportedEffortList.add(record)
            
        } //reportingStaffInstanceList.eachWithIndex{ rs, i ->        
        
            
        println "=> AssignedEffortController.showCurrent.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"            

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
        
        println "ASSIGNED EFFORT CONTROLLER > SHOW PAST ------------------------"                
        println "=> params: ${params}"
        
		// REPORTING STAFF
        def reportingStaffInstance        
        if (params?.id) {
            reportingStaffInstance = ReportingStaff.read(params?.id)
        }
        println "=> reportingStaffInstance: ${reportingStaffInstance}"
                
		// REPORTED EFFORT
        def cRE = ReportedEffort.createCriteria()
        //println "=> cRE: ${cRE}"
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
        //println "=> reportedEffortInstance: ${reportedEffortInstance}"

        // REPORTED EFFORT LIST
		def reportedEffortList = []
        
        // Add records to Assigned Effort Instance
        reportedEffortInstance.eachWithIndex{ rs, i ->
            
            //println "=> reportedEffortInstance.id: ${reportedEffortInstance.id}"            
            
            // Create record map
            def record = [:]
            
            // Get Assigned Effort Id
            record.assignedEffortId = rs.assignedEffort.id
            //println "=> record.assignedEffortId: ${record.assignedEffortId}"
                
            // Get Period Date
            record.period = rs.assignedEffort.period.periodDate          
            //println "=> record.period: ${record.period}"
                                    
            // Get Study Activity
            record.activity = rs.activity            
            //println "=> record.activity: ${record.activity}"
            
            // Get Study Task
            record.task = rs.task            
            //println "=> record.task: ${record.task}"
            
            // Get Reported Effort
            record.reportedEffort = rs.percentEffort
            //println "=> record.reportedEffort: ${record.reportedEffort}"
                       
            reportedEffortList.add(record)                        
            
        }
		//println "=> reportedEffortList: ${reportedEffortList}"		
        
        [ reportedEffortList: reportedEffortList ]
                
    } //def showPast
    
    def commit = {
        
        println "ASSIGNED EFFORT CONTROLLER > COMMIT ---------------------------"                
        println "=> AssignedEffortController.commit.params: ${params}"
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
		println "=> AssignedEffortController.commit.assignedEffortInstance: ${assignedEffortInstance}"
		
        if ( assignedEffortInstance ) {
            
			// ASSIGNED EFFORT
            def assignedPercentEffort = assignedEffortInstance.assignedEffort
			println "=> AssignedEffortController.commit.assignedEffortInstance.id: ${assignedEffortInstance.id}"     
			println "=> AssignedEffortController.commit.assignedEffortInstance.period: ${assignedEffortInstance.period}"
			
			// sum of REPORTED EFFORT
            def sumOfReportedPercentEffort = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
            println "=> AssignedEffortController.commit.sumOfReportedPercentEffort: ${sumOfReportedPercentEffort}"
                        
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
					println "=> AssignedEffortController.commit.errMessage: ${errMessage}"
					
					render(view: "show", model: [assignedEffortInstance: assignedEffortInstance, errMessage: errMessage])
						
				// total reported effort equals assigned effort, attempt to commit
				} else {
				
					def principal = authenticateService.principal()
					def reportingStaffInstance = laborService.getReportingStaff(principal)
					println "=> AssignedEffortController.commit.reportingStaffInstance: ${reportingStaffInstance}"
		
					assignedEffortInstance.dateCommitted = new Date()
					assignedEffortInstance.commitingStaff = reportingStaffInstance
					println "=> AssignedEffortController.commit.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"
					println "=> AssignedEffortController.commit.assignedEffortInstance.commitingStaff: ${assignedEffortInstance.commitingStaff}"
													
					if ( assignedEffortInstance.validate() && !assignedEffortInstance.hasErrors() && assignedEffortInstance.save(flush: true)) {
		
						println "COMMIT SUCCESSFULLY"
											
						// TODO: if all assigned effort, for this period, is committed, send email alerting to all administrators
						def countOfNotCommittedAssignedEffort = laborService.countNotCommittedAssignedEffort(assignedEffortInstance.period)
						println "=> AssignedEffortController.commit.countOfNotCommittedAssignedEffort: ${countOfNotCommittedAssignedEffort}"
			
						println "=> RUN generateReportEmail"
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
						
						println "COMMIT FAILED"
						errMessage = "Failed to COMMIT."
						
						render(view: "show", model: [ assignedEffortInstance: assignedEffortInstance, errMessage: errMessage ])
						
					} 
	
				} //( sumOfReportedPercentEffort.toBigDecimal() !== assignedPercentEffort.toBigDecimal() )
				
			} //if ( !sumOfReportedPercentEffort )
							
        } //if ( assignedEffortInstance )

    } //def commit 
	
	def committed = {
		
        println "ASSIGNED EFFORT CONTROLLER > COMMITTED-------------------------"                
        println "=> AssignedEffortController.commit.params: ${params}"

		def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
		
		[reportingPeriodInstance: reportingPeriodInstance]		
		
	}

        
}
