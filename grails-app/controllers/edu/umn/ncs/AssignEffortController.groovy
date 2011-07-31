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
   
    def index = {
        
        println "ASSIGN EFFORT CONTROLLER > INDEX ------------------------------"
        println "=> params: ${params}"          
		              
        redirect(action:'show')        

    }
    
    def show = {
        
        println "ASSIGN EFFORT CONTROLLER > SHOW -------------------------------"
        println "=> params: ${params}"                        

        /********************************************************************************************************
         * REPORING PERIODS (current, next, previous )
         ********************************************************************************************************/

        // current PERIOD 
        def currentPeriod = laborService.getCurrentReportingPeriod()
        //println "=> currentPeriod: ${currentPeriod}"
                
        // next PERIOD, after current period. If one does not exit in db, create it 
        def c = ReportingPeriod.createCriteria()
        def nextPeriod = c.list{
            and {
                gt("periodDate", currentPeriod.periodDate)
            }
            maxResults(1)
        }
        //println "=> ReportingPeriod.createCriteria.nextPeriod: ${nextPeriod}"

        if ( ! nextPeriod ) {
            def nextMonthDate = laborService.getNextReportingPeriodDateTime(currentPeriod.periodDate)
            nextPeriod = new ReportingPeriod(periodDate:nextMonthDate)
            nextPeriod.save(flush:true)
            //println "=> laborService.getNextReportingPeriodDateTime.nextPeriod: ${nextPeriod}"
        }
        //println "=> nextPeriod: ${nextPeriod}"

        // any previous PERIODS, in which the efforts have not been committed
        def c2 = ReportingPeriod.createCriteria()
        def previousPeriods = c2.list{
            and {
                    lt("periodDate", currentPeriod.periodDate)
                    isNull("completedReportDate")
                }
                order("periodDate","desc")
        }
        //println "=> previousPeriods: ${previousPeriods}"
       
        // PERIOD LIST for "Reporting Month" control 
        def periodList = []
        
        periodList.addAll(nextPeriod)
        periodList.add(currentPeriod)
        periodList.addAll(previousPeriods)
        //periodList.each{ println "=> AssignedEffortController.show.periodList: ${it.periodDate}" }
        
        // format periodDate for control
        def periodSelectiontList = []
        periodList.each{
            periodSelectiontList.add([id:it.id, name: g.formatDate(date:it.periodDate, format:'MMMM yyyy')])
        }		
        //println "=> periodSelectiontList: ${periodSelectiontList}"                        

		
        /********************************************************************************************************
         * DATA FOR ASSIGNED EFFORT TABLE
         ********************************************************************************************************/
        
        // selected REPORTING PERIOD (if no reportingPeriod selected, assume current period)
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
        //println "=> ReportingPeriod.read(params?.reportingPeriod?.id).reportingPeriodInstance: ${reportingPeriodInstance}"        
        if ( ! reportingPeriodInstance ) {
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
            //println "=> laborService.getCurrentReportingPeriod().reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        println "=> reportingPeriodInstance: ${reportingPeriodInstance}"        
                        
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
            
			println ""        

		            // row number
            record.rowNum = i + 1
            println "=> record.rowNum: ${record.rowNum}"
            
            // staff FULL NAME
            record.fullName = rs.fullNameLFM
            println "=> record.fullName: ${record.fullName}"
			
            // STAFF ID 
            record.staffId = rs.id
            println "=> record.staffId: ${record.staffId}"

            // previous period's ASSIGNED EFFORT 
            def previousPeriod = reportingPeriodInstance.previousPeriod            
            def previousAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(previousPeriod, rs)

            record.previousPeriodEffort = previousAssignedEffort?.assignedEffort
            if ( record.previousPeriodEffort ) {
                record.previousPeriodEffort = record.previousPeriodEffort
            }
            println "=> record.previousPeriodEffort: ${record.previousPeriodEffort}"

			// this period's ASSIGNED EFFORT
			def thisPeriodAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, rs)
			//println "=> AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,rs).thisPeriodAssignedEffort: ${thisPeriodAssignedEffort}"            
            record.thisPeriodAssignedEffort = thisPeriodAssignedEffort?.assignedEffort       
            println "=> record.thisPeriodAssignedEffort: ${record.thisPeriodAssignedEffort}"           

			// REPORTED EFFORT			                                                
			def cCurrentPeriodReportedEffort = ReportedEffort.createCriteria()
			def sumReportedEffort = cCurrentPeriodReportedEffort.get {
				eq("assignedEffort", thisPeriodAssignedEffort)
				projections {
					sum("percentEffort")
				}
			}
			record.currentPeriodReportedEffort = sumReportedEffort
			println "=> record.currentPeriodReportedEffort: ${record.currentPeriodReportedEffort}"

			// this period's effort IS COMMITTED
            record.isCommitted = thisPeriodAssignedEffort?.dateCommitted ? true : false
            println "=> record.isCommitted: ${record.isCommitted}"                                          
                        
            // current effort' DATE COMMITTED
            record.dateCommitted = thisPeriodAssignedEffort?.dateCommitted
            println "=> record.dateCommitted: ${record.dateCommitted}"
            
            // notifications EMAIL sent
            record.datesEmailsSent = thisPeriodAssignedEffort?.emails?.sort{it.dateSent}?.collect{g.formatDate(date:it.dateSent, format:'MM-dd-yyyy')} 
            println "=> record.datesEmailsSent: ${record.datesEmailsSent}"
           
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
                
        println "ASSIGNED EFFORT CONTROLLER > UPDATE ---------------------------"
        //println "=> params: ${params}"
        
        /********************************************************************************************************
         * LOG-IN USER 
         ********************************************************************************************************/
		
        def principal = authenticateService.principal()
        def username = principal.getUsername()
        def loggedInReportingStaffInstance = ReportingStaff.findByUsername(username)
        println "=> loggedInReportingStaffInstance: ${loggedInReportingStaffInstance}"
        
        /********************************************************************************************************
         * REPORTING PERIOD (from gsp)
         ********************************************************************************************************/
		
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
		println "=> reporting period, based on parameter from gsp"
        if ( ! reportingPeriodInstance ) {
			println "=> if reporting period is not a parameter from gsp, create one based on today's date."            
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
        }
		println "=> reportingPeriodInstance: ${reportingPeriodInstance}"
		
        /********************************************************************************************************
         * per Staff: ASSIGNED EFFORT, previous ASSIGNED EFFORT, SAVE
         ********************************************************************************************************/
        params.each{
                        
            //println "=> AssignedEffortController.update.params.each: ${it}"
            //println "=> AssignedEffortController.update.params.each IT: ${it}, KEY: ${it.key}, VALUE: ${it.value}"

            if ( it.key =~ /^staff-[0-9]*$/) {

				println ""
		
                /********************************************************************************************************
                 * REPORTING STAFF (from gsp)
                 ********************************************************************************************************/
                
                def reportingStaffId = Integer.parseInt(it.key.replace('staff-', ''))
                //println "=> reportingStaffId: ${reportingStaffId}"

                // reporting staff instance
                def reportingStaffInstance = ReportingStaff.read(reportingStaffId)
                println "=> reportingStaffInstance: ${reportingStaffInstance}"                

                /********************************************************************************************************
                 * ASSIGNED EFFORT (from gsp)
                 ********************************************************************************************************/
				
				def assignedEffortValue
				
                def assignedEffortConvertedValue = it.value.thisPeriodAssignedEffort      
				println "=> assignedEffortConvertedValue (raw): ${assignedEffortConvertedValue}"
				if ( assignedEffortConvertedValue ){
					assignedEffortConvertedValue = assignedEffortConvertedValue.trim()				
				} else {
					assignedEffortConvertedValue = '0'
				}
				println "=> assignedEffortConvertedValue (cleaned): ${assignedEffortConvertedValue}"
				
				if ( assignedEffortConvertedValue && assignedEffortConvertedValue.toBigDecimal() > 0 ) {
					
					assignedEffortValue = assignedEffortConvertedValue.toBigDecimal()/100
					
					// validate assigned effort (from gsp); that is, does effort have 0-3 digits before the decimal, and 0-2 after the decimal
					if ( assignedEffortConvertedValue =~ /[0-9]{1,3}\.?[0-9]{0,2}/ ) {
						println "=> assignedEffortConvertedValue meets requirements (0-3 digits . 0-2 digits)"
					} else {
						println "=> assignedEffortConvertedValue DOES NOT meet requirements (0-3 digits . 0-2 digits)"
						assignedEffortValue = '0'
					}
					println "=> assignedEffortValue = ${assignedEffortValue}"
					
					// verify if this assigned effort (from gsp) is between 0 - 100
					def r = 0.0..100.0
					if ( r.containsWithinBounds(assignedEffortConvertedValue.toBigDecimal()) ) {
						println "=> assignedEffortConvertedValue is between 0.0..100.0"
					} else {
						println "=> assignedEffortConvertedValue is NOT between 0.0..100.0"
						assignedEffortValue = '0'
					}
					
				} else {
					assignedEffortValue = '0'
				}				
				println "=> assignedEffortValue = ${assignedEffortValue}"
																
                /********************************************************************************************************
                 * PREVIOUS PERIOD'S ASSIGNED EFFORT (if "Copy Previous to Current" checkbox is selected)
                 ********************************************************************************************************/
				
				// "Copy Previous to Current" checkbox value (from gsp)
				def copyPreviousToCurrentValue = it.value.copyPreviousToCurrent
				println "=> copyPreviousToCurrentValue: ${copyPreviousToCurrentValue}"
				
				// if "Copy Previous to Current" checkbox is selected
				if ( copyPreviousToCurrentValue ) {
					
					println "=> if(copyPreviousToCurrentValue) = TRUE"
					
					// previous period's assigned effort (from gsp)
					def previousAssignedEffortValue = it.value.previousPeriodEffort
					if ( !previousAssignedEffortValue ) {
						previousAssignedEffortValue = '0'						
					}					
					println "=> previousAssignedEffortValue (validated): ${previousAssignedEffortValue}"
	
					// set assigned effort (from gsp) to previous period's assigned effort
					assignedEffortValue = previousAssignedEffortValue
					println "=> then assignedEffortValue (based on preivous period's): ${assignedEffortValue}"					
					
				}
				
                /********************************************************************************************************
                 * DELETE/UPDATE/INSERT ASSIGNED EFFORT
                 ********************************************************************************************************/

				// ASSIGNED EFFORT INSTANCE, if one exists in the db
                def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
				println "=> assignedEffortInstance: ${assignedEffortInstance}"
				if ( assignedEffortInstance ) {
	                println "=> assignedEffortInstance.id: ${assignedEffortInstance.id}"
				}
								
				// ASSIGNED EFFORT INSTANCE exists
				if ( assignedEffortInstance ) {
					
					println "=> if assignedEffortInstance exists in db"
					
					// DELETE
					if ( !assignedEffortValue || assignedEffortValue.toBigDecimal() == 0 ) {

						println "=> and assignedEffortValue is 0/null"
						
						try {
							assignedEffortInstance.delete(flush: true)
							println "=> asignedEffortInstance.delete SUCCEEDED"							
						}
						catch (org.springframework.dao.DataIntegrityViolationException e) {
							println "=> asignedEffortInstance.delete FAILED"
							flash.message = "Unable DELETE assigned effort for ${reportingStaffInstance.getFullNameLFM()}"
						}
						
					// UPDATE
					} else if ( assignedEffortValue &&  assignedEffortValue > 0 && assignedEffortValue.toBigDecimal() != assignedEffortInstance.assignedEffort ) {
					
						println "=> and assignedEffortValue > 0 and assignedEffortValue != assignedEffortInstance.assignedEffort"
												
						println "=> assignedEffortInstance.version before update: ${assignedEffortInstance.version}"
						
						assignedEffortInstance.assignedEffort = assignedEffortValue
												
						if ( !assignedEffortInstance.hasErrors() && assignedEffortInstance.save(flush: true)) {
							
							println "=> assignedEffortInstance.assignedEffort update SUCCEEDED"
							println "=> updated assignedEffortInstance.id: ${assignedEffortInstance.id}"
							println "=> updated assignedEffortInstance.assignedEffort: ${assignedEffortInstance.assignedEffort}"
							println "=> updated assignedEffortInstance.version: ${assignedEffortInstance.version}"
							
						} else {
						
							println "=> updated assignedEffortInstance.id: ${assignedEffortInstance.id}"
							println "=> updated assignedEffortInstance.assignedEffort: ${assignedEffortInstance.assignedEffort}"
							println "=> updated assignedEffortInstance.version: ${assignedEffortInstance.version}"
							flash.message = "Unable UPDATE assigned effort for ${reportingStaffInstance.getFullNameLFM()}"
													
						}
						
					} //if ( !assignedEffortValue || assignedEffortValue.toBigDecimal() == 0 )
					
				// ASSIGNED EFFORT INSTANCE does not exist (INSERT)
				} else {
				
					println "=> if assignedEffortInstance DOES NOT exist in db"
					
					if ( assignedEffortValue &&  assignedEffortValue.toBigDecimal() > 0 ) {
						
						println "=> and assignedEffortValue > 0"
						
						println "=> create (new) assignedEffort instance based on assignedEffortValue"
						assignedEffortInstance = new AssignedEffort(
							reportingStaff: reportingStaffInstance,
							laborCategory: reportingStaffInstance.laborCategory,
							period: reportingPeriodInstance,
							assignedEffort: assignedEffortValue.toBigDecimal(),
							assigningStaff: loggedInReportingStaffInstance,
							appCreated: 'ncs-dlr'
						)
						
						if ( assignedEffortInstance.save(flush: true )) {
							println "=> asignedEffortInstance.save SUCCEEDED"
							println "=> newly created assignedEffortInstance.id:${assignedEffortInstance.id} "
							println "=> newly created assignedEffortInstance:${assignedEffortInstance} "	
						}
						else {
							println "=> asignedEffortInstance.save FAILED"
							flash.message = "Unable ADD assigned effort for ${reportingStaffInstance.getFullNameLFM()}"
						}
						
					}
					
				} //if ( assignedEffortInstance )

                /********************************************************************************************************
                 * SEND EMAIL NOTIFICATION 
                 ********************************************************************************************************/
                
                def sendNowValue = it.value.sendNow
                println "=> sendNowValue: ${sendNowValue}"
                
                // if "Send Now" checkbox (from gsp) is selected
                if ( sendNowValue ) { 
					println "=> if(sendNowValue) = TRUE"
                    def message = laborService.sendEmailNotification(reportingPeriodInstance.id, reportingStaffInstance.id)
                    println "=> send email notification"
                } 
                
            } //if ( it.key =~ /staff-[0-9]*/)

        } //params.each
        
        def params = ['reportingPeriodInstance.id': reportingPeriodInstance?.id ]
		println "=> params = [reportingPeriodInstance.id]: ${params}"
		
        redirect(action:'show', params:params)
                        
    } //def update

} //class AssignEffortController 