package edu.umn.ncs
import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE', 'ROLE_NCS_DLR'])
class AssignedEffortController {

    def authenticateService
    def laborService
	def debug = true
	//def debug = true
		
	def index = {
		
		if (debug) { 
			log.debug "ASSIGNED EFFORT CONTROLLER > INDEX ----------------------------"
			log.debug "=> AssignedEffortController.index.params: ${params}"
		}

	}
	
    def show = {
        
		if (debug) {
	        log.debug "ASSIGNED EFFORT CONTROLLER > SHOW -----------------------------"                
	        log.debug "=> params: ${params}"
		}
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)
		if ( assignedEffortInstance ) {
	        log.debug "=> assignedEffortInstance: ${assignedEffortInstance}"
			log.debug "=> assignedEffortInstance.id: ${assignedEffortInstance.id}"
		}
		
		[ assignedEffortInstance: assignedEffortInstance ]        

    } 
 
    def showCurrent = {

		if (debug) {
	        log.debug "ASSIGNED EFFORT CONTROLLER > SHOW CURRENT ---------------------"                
	        log.debug "=> params: ${params}"
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
			log.debug "=> assignedEffortInstance: ${assignedEffortInstance}"
			log.debug "=> reportingStaffInstance: ${reportingStaffInstance}"
			log.debug "=> reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        
        // Get REPORTED EFFORT total
		
		def reportedEffortTotal = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
		if (debug) { log.debug "=> reportedEffortTotal: ${reportedEffortTotal}" }

		def reportedEffortTotalConverted
		if ( reportedEffortTotal ) {
			reportedEffortTotalConverted = reportedEffortTotal * 100
		}
		if (debug) { log.debug "=> reportedEffortTotalConverted: ${reportedEffortTotalConverted}" }
        
		def notReportedEffortConverted
		if ( assignedEffortInstance.assignedEffortConverted && reportedEffortTotalConverted ) {
			notReportedEffortConverted = assignedEffortInstance.assignedEffortConverted - reportedEffortTotalConverted
		} 
		if (debug) { log.debug "=> notReportedEffortConverted: ${notReportedEffortConverted}" }
		
        // Get list of REPORTED EFFORT
        def c2 = ReportedEffort.createCriteria()        
        def reportingEffortInstanceList = c2.list{
            eq("assignedEffort", assignedEffortInstance) 
            order("activity", "asc")
            order("task", "asc")
        }
        if (debug) { log.debug "=> reportingEffortInstanceList: ${reportingEffortInstanceList}" }
		
        // Add records to Reported Effort Instance
        def reportedEffortList = []

        reportingEffortInstanceList.eachWithIndex{ rs, i -> 
            
            // Create record map
            def record = [:]
			
			if (debug) { log.debug "=> rs: ${rs}" }
			if (debug) { log.debug "=> i: ${i}" }
			
            // Get effort id
            record.reportedEffortId = rs.id
            //if (debug) { log.debug "=> record.reportedEffortId: ${record.reportedEffortId}" }            
            
            // Get study activity
            record.studyActivity = rs.activity
            //if (debug) { log.debug "=> record.studyActivity: ${record.studyActivity}" }
            
            // Get study task
            record.studyTask = rs.task
            if (debug) { log.debug "=> record.studyTask: ${record.studyTask}" }
            
            // Get percentEffort
            record.percentEffortConverted = rs.percentEffortConverted
            //if (debug) { log.debug "=> record.percentEffortConverted: ${record.percentEffortConverted}" }
            
            //Get DateCreated
            record.dateCreated = rs.dateCreated
            //if (debug) { log.debug "=> record.dateCreated: ${record.dateCreated}" }
            
            reportedEffortList.add(record)
            
        } 
                    
        if (debug) { log.debug "=> assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}" }            

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
			log.debug "ASSIGNED EFFORT CONTROLLER > SHOW PAST ------------------------"                
			log.debug "=> params: ${params}"
        }
        
		// REPORTING STAFF
        def reportingStaffInstance        
        if (params?.id) {
            reportingStaffInstance = ReportingStaff.read(params?.id)
        }
        if (debug) { log.debug "=> reportingStaffInstance: ${reportingStaffInstance}" }
                
		// REPORTED EFFORT
        def cRE = ReportedEffort.createCriteria()
        //if (debug) { log.debug "=> cRE: ${cRE}" }
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
        //if (debug) { log.debug "=> reportedEffortInstance: ${reportedEffortInstance}" }

        // REPORTED EFFORT LIST
		def reportedEffortList = []
        
        // Add records to Assigned Effort Instance
        reportedEffortInstance.eachWithIndex{ rs, i ->
            
            //if (debug) { log.debug "=> reportedEffortInstance.id: ${reportedEffortInstance.id}" }            
            
            // Create record map
            def record = [:]
            
            // Get Assigned Effort Id
            record.assignedEffortId = rs.assignedEffort.id
            //if (debug) { log.debug "=> record.assignedEffortId: ${record.assignedEffortId}" }
                
            // Get Period Date
            record.period = rs.assignedEffort.period.periodDate          
            //if (debug) { log.debug "=> record.period: ${record.period}" }
                                    
            // Get Study Activity
            record.activity = rs.activity            
            //if (debug) { log.debug "=> record.activity: ${record.activity}" }
            
            // Get Study Task
            record.task = rs.task            
            //if (debug) { log.debug "=> record.task: ${record.task}" }
            
            // Get Reported Effort
            record.reportedEffort = rs.percentEffort
            //if (debug) { log.debug "=> record.reportedEffort: ${record.reportedEffort}" }
                       
            reportedEffortList.add(record)                        
            
        }
		//if (debug) { log.debug "=> reportedEffortList: ${reportedEffortList}" }		
        
        [ reportedEffortList: reportedEffortList ]
                
    } //def showPast
    
    def commit = {

		if (debug) {
	        log.debug "ASSIGNED EFFORT CONTROLLER > COMMIT ---------------------------"                
	        log.debug "=> params: ${params}"
		}
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)        
		if (debug) {log.debug "=> assignedEffortInstance: ${assignedEffortInstance}" }
		
        if ( assignedEffortInstance ) {
            
			if (debug) { log.debug "=> assignedEffortInstance.id: ${assignedEffortInstance.id}" }

			// ASSIGNED EFFORT
            def assignedPercentEffort = assignedEffortInstance.assignedEffort
			if (debug) { log.debug "=> assignedPercentEffort: ${assignedPercentEffort}" }
			
			// sum of REPORTED EFFORT
            def sumOfReportedPercentEffort = laborService.getSumOfReportedPercentEffort(assignedEffortInstance)
            if (debug) { log.debug "=> sumOfReportedPercentEffort: ${sumOfReportedPercentEffort}" }
                        
			// commit REPORTED EFFORT validation			
			def errMessage

			if ( sumOfReportedPercentEffort ) {

				// total reported effort equals assigned effort, attempt to commit
				if ( sumOfReportedPercentEffort.toBigDecimal() == assignedPercentEffort.toBigDecimal() ) {
					
					if (debug) { log.debug "=> if ( sumOfReportedPercentEffort.toBigDecimal() == assignedPercentEffort.toBigDecimal() = TRUE)" }
			
					def principal = authenticateService.principal()
					def reportingStaffInstance = laborService.getReportingStaff(principal)
					if (debug) { log.debug "=> reportingStaffInstance: ${reportingStaffInstance}" }
		
					assignedEffortInstance.dateCommitted = new Date()
					assignedEffortInstance.commitingStaff = reportingStaffInstance
					if (debug) {
						log.debug "=> assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"
						log.debug "=> assignedEffortInstance.commitingStaff: ${assignedEffortInstance.commitingStaff}"
					}
					
					if ( assignedEffortInstance.validate() && !assignedEffortInstance.hasErrors() && assignedEffortInstance.save(flush: true)) {
		
						if (debug) { log.debug "=> COMMIT SUCCESSFULLY" }
						
						// if all assigned effort, for this period, is committed, send email alerting to all administrators
						def countOfNotCommittedAssignedEffort = laborService.countNotCommittedAssignedEffort(assignedEffortInstance.period)
						if (debug) { log.debug "=> countOfNotCommittedAssignedEffort: ${countOfNotCommittedAssignedEffort}" }
			
						if ( !countOfNotCommittedAssignedEffort) {
							
							if (debug) { log.debug "=> if ( !countOfNotCommittedAssignedEffort) = TRUE" }
							
							def message = laborService.sendReportEmail(assignedEffortInstance)							
							if (debug) { log.debug "=> message: ${message}" }
							
							def reportingPeriodInstance = assignedEffortInstance.period
							
							reportingPeriodInstance.preparedDate = assignedEffortInstance.dateCommitted
							reportingPeriodInstance.completedReportDate = assignedEffortInstance.dateCommitted
							
						}
						
						def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
				
						def model = [
							reportingStaffInstance: reportingStaffInstance,
							reportingPeriodInstance: reportingPeriodInstance,
							assignedEffortInstance: assignedEffortInstance
						]
						
						redirect(action: "committed")
						
					} else {
						
						if (debug) { log.debug "COMMIT FAILED" }
						errMessage = "Failed to COMMIT."
						
						render(view: "show", model: [ assignedEffortInstance: assignedEffortInstance, errMessage: errMessage ])
						
					}

				// total reported effort does not equal assigned effort, cannot commit
				} else {
				
					if (debug) { log.debug "=> if ( sumOfReportedPercentEffort.toBigDecimal() == assignedPercentEffort.toBigDecimal() = FALSE)" }
					
					// total reported effort is less than what is assigned
					if ( sumOfReportedPercentEffort.toBigDecimal() < assignedPercentEffort.toBigDecimal() ) {
						errMessage = "Cannot COMMIT your reported effort because it is less than what has been assigned to you."
					// total reported effort is greater than what is assigned
					} else if ( sumOfReportedPercentEffort.toBigDecimal() > assignedPercentEffort.toBigDecimal() ) {
						errMessage = "Cannot COMMIT your reported effort because it is greater than what has been assigned to you."
					}
					if (debug) { log.debug "=> errMessage: ${errMessage}" }
					
					render(view: "show", model: [assignedEffortInstance: assignedEffortInstance, errMessage: errMessage])

				} //( sumOfReportedPercentEffort.toBigDecimal() == assignedPercentEffort.toBigDecimal() )
				
			} //if ( !sumOfReportedPercentEffort )
							
        } //if ( assignedEffortInstance )
		
    } //def commit 
	
	def committed = {
		
		if (debug) {
	        log.debug "ASSIGNED EFFORT CONTROLLER > COMMITTED-------------------------"                
	        log.debug "=> params: ${params}"
		}

		def reportingPeriodInstance = laborService.getCurrentReportingPeriod()
		
		[reportingPeriodInstance: reportingPeriodInstance]		
		
	}

        
}
