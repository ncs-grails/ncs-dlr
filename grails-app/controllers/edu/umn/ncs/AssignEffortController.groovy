package edu.umn.ncs
import org.joda.time.*
import java.math.BigDecimal

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_IT', 'ROLE_NCS_DLR_MANAGE'])
class AssignEffortController {

    def authenticateService
    def laborService
    def mailService
	def debug = false
   
    def index = {
        
        if (debug) {
			println "ASSIGN EFFORT CONTROLLER > INDEX ------------------------------"
			println "=> params: ${params}"	
		}
		              
        redirect(action:'show')        

    }
    
    def show = {
        
        if (debug) {
	        println "ASSIGN EFFORT CONTROLLER > SHOW -------------------------------"
	        println "=> params: ${params}"
        }                        
				
        /********************************************************************************************************
         * REPORING PERIODS (current, next, previous )
         ********************************************************************************************************/

        // current PERIOD 
        def currentPeriod = laborService.getCurrentReportingPeriod()
        if (debug) {println "=> currentPeriod: ${currentPeriod}"}
                
        // next PERIOD, after current period. If one does not exit in db, create it 
        def c = ReportingPeriod.createCriteria()
        def nextPeriod = c.list{
            and {
                gt("periodDate", currentPeriod.periodDate)
            }
            maxResults(1)
        }
        if (debug) {println "=> ReportingPeriod.createCriteria.nextPeriod: ${nextPeriod}"}

        if ( ! nextPeriod ) {
            def nextMonthDate = laborService.getNextReportingPeriodDateTime(currentPeriod.periodDate)
            nextPeriod = new ReportingPeriod(periodDate:nextMonthDate)
            nextPeriod.save(flush:true)
            if (debug) {println "=> laborService.getNextReportingPeriodDateTime.nextPeriod: ${nextPeriod}"}
        }
        if (debug) {println "=> nextPeriod: ${nextPeriod}"}

        // any previous PERIODS, in which the efforts have not been committed
        def c2 = ReportingPeriod.createCriteria()
        def previousPeriods = c2.list{
            and {
                    lt("periodDate", currentPeriod.periodDate)
                    isNull("completedReportDate")
                }
                order("periodDate","desc")
        }
        if (debug) {println "=> previousPeriods: ${previousPeriods}"}
       
        // PERIOD LIST for "Reporting Month" control 
        def periodList = []
        
        periodList.addAll(nextPeriod)
        periodList.add(currentPeriod)
        periodList.addAll(previousPeriods)
        if (debug) { 
			periodList.each{ 
				println "=> AssignedEffortController.show.periodList: ${it.periodDate}"
			} 
		}
        
        // format periodDate for control
        def periodSelectiontList = []
        periodList.each{
            periodSelectiontList.add([id:it.id, name: g.formatDate(date:it.periodDate, format:'MMMM yyyy')])
        }		
        if (debug) { println "=> periodSelectiontList: ${periodSelectiontList}" }                        

		
        /********************************************************************************************************
         * DATA FOR ASSIGNED EFFORT TABLE
         ********************************************************************************************************/
        
        // selected REPORTING PERIOD (if no reportingPeriod selected, assume current period)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
        if (debug) { println "=> ReportingPeriod.read(params?.reportingPeriod?.id).reportingPeriodInstance: ${reportingPeriodInstance}" }        
        if ( ! reportingPeriodInstance ) {
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
            if (debug) { println "=> laborService.getCurrentReportingPeriod().reportingPeriodInstance: ${reportingPeriodInstance}" }
        }
        if (debug) { println "=> reportingPeriodInstance: ${reportingPeriodInstance}" }        
                        
        // staff list
        def crs = ReportingStaff.createCriteria()        
        def reportingStaffInstanceList = crs.list{
            eq("reportsEffort", true) 
            order("lastName", "asc")
            order("firstName", "asc")
            order("middleInit", "asc")
        }

		// Create the LIST to pass all parameters to gsp page
		def effortAssignmentList = []

        // Add records to Staff List Instance
        reportingStaffInstanceList.eachWithIndex{ rs, i ->
            
            // record map
            def record = [:]
            
			if (debug) { println "" }        

			// row number
            record.rowNum = i + 1
            if (debug) { println "=> record.rowNum: ${record.rowNum}" }
            
            // staff FULL NAME
            record.fullName = rs.fullNameLFM
            if (debug) { println "=> record.fullName: ${record.fullName}" }
			
            // STAFF ID 
            record.staffId = rs.id
            if (debug) { println "=> record.staffId: ${record.staffId}" }

            // previous period's ASSIGNED EFFORT 
            def previousPeriod = reportingPeriodInstance.previousPeriod            
            def previousAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(previousPeriod, rs)

            record.previousPeriodEffort = previousAssignedEffort?.assignedEffort
            if ( record.previousPeriodEffort ) {
                record.previousPeriodEffort = record.previousPeriodEffort
            }
            if (debug) { println "=> record.previousPeriodEffort: ${record.previousPeriodEffort}" }

			// this period's ASSIGNED EFFORT
			def thisPeriodAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, rs)
			if (debug) { println "=> AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,rs).thisPeriodAssignedEffort: ${thisPeriodAssignedEffort}" }            
            record.thisPeriodAssignedEffort = thisPeriodAssignedEffort?.assignedEffort       
            if (debug) { println "=> record.thisPeriodAssignedEffort: ${record.thisPeriodAssignedEffort}" }           

			// REPORTED EFFORT			                                                
			def cCurrentPeriodReportedEffort = ReportedEffort.createCriteria()
			def sumReportedEffort = cCurrentPeriodReportedEffort.get {
				eq("assignedEffort", thisPeriodAssignedEffort)
				projections {
					sum("percentEffort")
				}
			}
			record.currentPeriodReportedEffort = sumReportedEffort
			if (debug) { println "=> record.currentPeriodReportedEffort: ${record.currentPeriodReportedEffort}" }

			// this period's effort IS COMMITTED
            record.isCommitted = thisPeriodAssignedEffort?.dateCommitted ? true : false
            if (debug) { println "=> record.isCommitted: ${record.isCommitted}" }                                          
                        
            // current effort' DATE COMMITTED
            record.dateCommitted = thisPeriodAssignedEffort?.dateCommitted
            if (debug) { println "=> record.dateCommitted: ${record.dateCommitted}" }
            
            // notifications EMAIL sent
            record.datesEmailsSent = thisPeriodAssignedEffort?.emails?.sort{it.dateSent}?.collect{g.formatDate(date:it.dateSent, format:'MM-dd-yyyy')} 
            if (debug) { println "=> record.datesEmailsSent: ${record.datesEmailsSent}" }
           
            // load all data collected to the LIST that is passed to gsp page
            effortAssignmentList.add(record)
            
        }
		
        [
            reportingPeriodInstance: reportingPeriodInstance, 
            periodSelectiontList: periodSelectiontList,
            effortAssignmentList: effortAssignmentList
        ]

    } //def show 

    def update = {
		
		if (debug) {
			println "ASSIGN EFFORT CONTROLLER > UPDATE ---------------------------"
			//if (debug) {println "=> params: ${params}" }
		}
				
		def err 
		def errMessage = ""
		def errMessageList = []
			
        /********************************************************************************************************
         * LOG-IN USER 
         ********************************************************************************************************/
		
        def principal = authenticateService.principal()
        def username = principal.getUsername()
        def loggedInReportingStaffInstance = ReportingStaff.findByUsername(username)
        if (debug) { println "=> loggedInReportingStaffInstance: ${loggedInReportingStaffInstance}" }
        
        /********************************************************************************************************
         * REPORTING PERIOD (from gsp)
         ********************************************************************************************************/
		
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
		if (debug) { println "=> reporting period, based on parameter from gsp" }
        if ( ! reportingPeriodInstance ) {
			if (debug) { println "=> if reporting period is not a parameter from gsp, create one based on today's date." }            
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        }
		if (debug) { println "=> reportingPeriodInstance: ${reportingPeriodInstance}" }
		
        /********************************************************************************************************
         * per Staff: ASSIGNED EFFORT, previous ASSIGNED EFFORT, and then DELETE/UPDATE/INSERT
         ********************************************************************************************************/
        params.each{
                        
			err = false
			
			if (debug) { 
	            //println "=> AssignedEffortController.update.params.each: ${it}"
				//println "=> AssignedEffortController.update.params.each IT: ${it}, KEY: ${it.key}, VALUE: ${it.value}"
			}

            if ( it.key =~ /^staff-[0-9]*$/) {

				if (debug) { println "" }
		
                /********************************************************************************************************
                 * REPORTING STAFF (from gsp)
                 ********************************************************************************************************/
                
                def reportingStaffId = Integer.parseInt(it.key.replace('staff-', ''))
                if (debug) { println "=> reportingStaffId: ${reportingStaffId}" }

				def reportingStaffInstance
				if ( reportingStaffId ) {
					reportingStaffInstance = ReportingStaff.read(reportingStaffId)
					if (debug) { println "=> reportingStaffInstance: ${reportingStaffInstance}" }
					if ( reportingStaffInstance ) {
						if (debug) { println "=> reportingStaffInstance.id: ${reportingStaffInstance.id}" }
						if (debug) { println "=> reportingStaffInstance.fullNameFML: ${reportingStaffInstance.fullNameFML}" }
					}
	
				}
				                
                /********************************************************************************************************
                 * ASSIGNED EFFORT (from gsp) validation
                 ********************************************************************************************************/
				def assignedEffortValue
				
                def assignedEffortConvertedValue = it.value.thisPeriodAssignedEffort      
				if (debug) { println "=> assignedEffortConvertedValue (raw): ${assignedEffortConvertedValue}" }
				if ( assignedEffortConvertedValue ){
					assignedEffortConvertedValue = assignedEffortConvertedValue.trim()				
				} else {
					assignedEffortConvertedValue = '0'
				}
				if (debug) { println "=> assignedEffortConvertedValue (cleaned): ${assignedEffortConvertedValue}" }
				
				if ( assignedEffortConvertedValue && assignedEffortConvertedValue.toBigDecimal() > 0 ) {
					
					assignedEffortValue = assignedEffortConvertedValue.toBigDecimal()/100
					
					// validate assigned effort (from gsp); that is, does effort have 0-3 digits before the decimal, and 0-2 after the decimal
					if ( assignedEffortConvertedValue =~ /[0-9]{0,3}\.?[0-9]{0,3}/ ) {
						
						if (debug) { println "=> assignedEffortConvertedValue meets requirements (0-3 digits . 0-2 digits)" }
												
						// verify if this assigned effort (from gsp) is between 0 - 100
						def r = 0.0..100.0
						if ( r.containsWithinBounds(assignedEffortConvertedValue.toBigDecimal()) ) {
							if (debug) { println "=> assignedEffortConvertedValue is between 0.0..100.0" }
						} else {
							if (debug) { println "=> assignedEffortConvertedValue is NOT between 0.0..100.0" }
							err = true
							errMessage = "Invalid Assigned Effort entry (${assignedEffortConvertedValue}%) for ${reportingStaffInstance.fullNameFML}.  " 
							errMessageList.add(errMessage)
							if (debug) { println "=> errMessageList: ${errMessageList}" }					
							assignedEffortValue = '0'
						}						

						
					} else {
					
						if (debug) { println "=> assignedEffortConvertedValue DOES NOT meet requirements (0-3 digits . 0-2 digits)" }
						err = true
						errMessage = "Invalid Assigned Effort entry (${assignedEffortConvertedValue}%) for ${reportingStaffInstance.fullNameFML}.  "
						errMessageList.add(errMessage)
						if (debug) { println "=> errMessageList: ${errMessageList}" }
						assignedEffortValue = '0'
						
					}
					
				} else {
				
					assignedEffortValue = '0'
					
				}				
				if (debug) { println "=> assignedEffortValue = ${assignedEffortValue}" }
																
                /********************************************************************************************************
                 * PREVIOUS PERIOD'S ASSIGNED EFFORT (if "Copy Previous to Current" checkbox is selected)
                 ********************************************************************************************************/
				
				// "Copy Previous to Current" checkbox value (from gsp)
				def copyPreviousToCurrentValue = it.value.copyPreviousToCurrent
				if (debug) { println "=> copyPreviousToCurrentValue: ${copyPreviousToCurrentValue}" }
				
				// if "Copy Previous to Current" checkbox is selected
				if ( copyPreviousToCurrentValue ) {
					
					if (debug) { println "=> if(copyPreviousToCurrentValue) = TRUE" }
					
					// previous period's assigned effort (from gsp)
					def previousAssignedEffortValue = it.value.previousPeriodEffort
					if ( !previousAssignedEffortValue ) {
						previousAssignedEffortValue = '0'						
					}					
					if (debug) { println "=> previousAssignedEffortValue (validated): ${previousAssignedEffortValue}" }
	
					// set assigned effort (from gsp) to previous period's assigned effort
					assignedEffortValue = previousAssignedEffortValue
					if (debug) { println "=> then assignedEffortValue (based on preivous period's): ${assignedEffortValue}" }					
					
				}
				
                /********************************************************************************************************
                 * DELETE/UPDATE/INSERT ASSIGNED EFFORT
                 ********************************************************************************************************/

				// ASSIGNED EFFORT INSTANCE, if one exists in the db
                def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
				if (debug) { println "=> assignedEffortInstance: ${assignedEffortInstance}" }
				if ( assignedEffortInstance ) {
	                if (debug) { println "=> assignedEffortInstance.id: ${assignedEffortInstance.id}" }
				}
								
				// ASSIGNED EFFORT INSTANCE exists
				if ( !err && assignedEffortInstance ) {
					
					if (debug) { println "=> if assignedEffortInstance exists in db" }
					
					// DELETE
					if ( !assignedEffortValue || assignedEffortValue.toBigDecimal() == 0 ) {

						if (debug) { println "=> and assignedEffortValue is 0/null" }
						
						try {
							assignedEffortInstance.delete(flush: true)
							if (debug) { println "=> asignedEffortInstance.delete SUCCEEDED" }							
						}
						catch (org.springframework.dao.DataIntegrityViolationException e) {
							if (debug) { println "=> asignedEffortInstance.delete FAILED" }
							err = true
							errMessage = "Unable to DELETE assigned effort for ${reportingStaffInstance.fullNameFML}.  "
							errMessageList.add(errMessage)						
							if (debug) { println "=> errMessageList: ${errMessageList}" }							
						}
						
					// UPDATE
					} else if ( assignedEffortValue &&  assignedEffortValue > 0 && assignedEffortValue.toBigDecimal() != assignedEffortInstance.assignedEffort ) {
					
						if (debug) { println "=> and assignedEffortValue > 0 and assignedEffortValue != assignedEffortInstance.assignedEffort" }
												
						if (debug) { println "=> assignedEffortInstance.version before update: ${assignedEffortInstance.version}" }
						
						if ( !err ) {
							
							assignedEffortInstance.assignedEffort = assignedEffortValue
							
							if ( !assignedEffortInstance.hasErrors() && assignedEffortInstance.save(flush: true)) {
								if (debug) {
									println "=> assignedEffortInstance.assignedEffort update SUCCEEDED"
									println "=> updated assignedEffortInstance.id: ${assignedEffortInstance.id}"
									println "=> updated assignedEffortInstance.assignedEffort: ${assignedEffortInstance.assignedEffort}"
									println "=> updated assignedEffortInstance.version: ${assignedEffortInstance.version}"
								}
							} else {
								err = true
								errMessage = "Unable to UPDATE assigned effort for ${reportingStaffInstance.fullNameFML}.  "
								errMessageList.add(errMessage)
								if (debug) {
									println "=> updated assignedEffortInstance.id: ${assignedEffortInstance.id}"
									println "=> updated assignedEffortInstance.assignedEffort: ${assignedEffortInstance.assignedEffort}"
									println "=> updated assignedEffortInstance.version: ${assignedEffortInstance.version}"
									println "=> errMessageList: ${errMessageList}"								
								}
							}
							
						} //if ( !err )
						
					} //if ( !assignedEffortValue || assignedEffortValue.toBigDecimal() == 0 )
					
				// ASSIGNED EFFORT INSTANCE does not exist (INSERT)
				} else if (!err && !assignedEffortInstance) {
				
					if (debug) {println "=> if assignedEffortInstance DOES NOT exist in db" }
						
					if ( !err && assignedEffortValue &&  assignedEffortValue.toBigDecimal() > 0 ) {
						
						if (debug) {println "=> and assignedEffortValue > 0" }
						
						if (debug) {println "=> create (new) assignedEffort instance based on assignedEffortValue" }
						assignedEffortInstance = new AssignedEffort(
							reportingStaff: reportingStaffInstance,
							laborCategory: reportingStaffInstance.laborCategory,
							period: reportingPeriodInstance,
							assignedEffort: assignedEffortValue.toBigDecimal(),
							assigningStaff: loggedInReportingStaffInstance,
							appCreated: 'ncs-dlr'
						)
						
						if ( assignedEffortInstance.save(flush: true )) {
							if (debug) {
								println "=> asignedEffortInstance.save SUCCEEDED"
								println "=> newly created assignedEffortInstance.id:${assignedEffortInstance.id} "
								println "=> newly created assignedEffortInstance:${assignedEffortInstance} "
							}	
						}
						else {
							if (debug) {println "=> asignedEffortInstance.save FAILED" }
							err = true
							errMessage = "Unable to ADD assigned effort for ${reportingStaffInstance.fullNameFML}.  "
							errMessageList.add(errMessage)
							if (debug) {println "=> errMessageList: ${errMessageList}" }							
						}
						
					}
					
				} //if ( assignedEffortInstance )

                /********************************************************************************************************
                 * SEND EMAIL NOTIFICATION 
                 ********************************************************************************************************/
                
				if ( !err ) {
					
					// if "Send Now" checkbox (from gsp) is selected
					def sendNowValue = it.value.sendNow
					if (debug) {println "=> sendNowValue: ${sendNowValue}" }
					
					if ( sendNowValue ) {
						if (debug) {println "=> if(sendNowValue) = TRUE" }
						def message = laborService.sendEmailNotification(reportingPeriodInstance.id, reportingStaffInstance.id)
						if (debug) {println "=> send email notification" }
					}
	
				}				

            } //if ( it.key =~ /staff-[0-9]*/)

        } //params.each

		
		/********************************************************************************************************
		 * ERROR MESSAGES
		 ********************************************************************************************************/
		
		if ( errMessageList ) {
			
			flash.messageList = errMessageList

			if (debug) {
				println "=> flash.message: ${flash.message}"
				if ( flash.message ) {
					flash.message.each{
						println "=> flash.message.each: ${it}"
					}
				}
			}
			
		}
		
        def params = [ 'reportingPeriodInstance.id': reportingPeriodInstance?.id ]
		if (debug) {println "=> params = [reportingPeriodInstance.id]: ${params}" }
		
        redirect(action:'show', params:params)
                        
    } //def update

} //class AssignEffortController 