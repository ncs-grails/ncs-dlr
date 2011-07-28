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
        
        println "PRINTLN ASSIGN EFFORT CONTROLLER > INDEX --------------------"
        println "PRINTLN params: ${params}"          
		              
        redirect(action:'show')        

    }
    
    def show = {
        
        println "PRINTLN ASSIGN EFFORT CONTROLLER > SHOW ---------------------"
        println "PRINTLN params: ${params}"                        

        // REPORING PERIODS ----------------------------------------------------

        // current period 
        def currentPeriod = laborService.getCurrentReportingPeriod()
        //println "PRINTLN currentPeriod: ${currentPeriod}"
                
        // next period, after current period. If one does not exit in db, create it 
        def c = ReportingPeriod.createCriteria()
        def nextPeriod = c.list{
            and {
                gt("periodDate", currentPeriod.periodDate)
            }
            maxResults(1)
        }
        //println "PRINTLN ReportingPeriod.createCriteria.nextPeriod: ${nextPeriod}"

        if ( ! nextPeriod ) {
            def nextMonthDate = laborService.getNextReportingPeriodDateTime(currentPeriod.periodDate)
            nextPeriod = new ReportingPeriod(periodDate:nextMonthDate)
            nextPeriod.save(flush:true)
            //println "PRINTLN laborService.getNextReportingPeriodDateTime.nextPeriod: ${nextPeriod}"
        }
        //println "PRINTLN nextPeriod: ${nextPeriod}"

        // any previous periods, in which the efforts have not been committed
        def c2 = ReportingPeriod.createCriteria()
        def previousPeriods = c2.list{
            and {
                    lt("periodDate", currentPeriod.periodDate)
                    isNull("completedReportDate")
                }
                order("periodDate","desc")
        }
        //println "PRINTLN previousPeriods: ${previousPeriods}"
       
        // Create PERIOD LIST for "Reporting Month" control 
        def periodList = []
        
        periodList.addAll(nextPeriod)
        periodList.add(currentPeriod)
        periodList.addAll(previousPeriods)
        //periodList.each{ println "PRINTLN AssignedEffortController.show.periodList: ${it.periodDate}" }
        
        // format periodDate for control
        def periodSelectiontList = []
        periodList.each{
            periodSelectiontList.add([id:it.id, name: g.formatDate(date:it.periodDate, format:'MMMM yyyy')])
        }		
        println "PRINTLN periodSelectiontList: ${periodSelectiontList}"                        

        // DATA FOR ASSIGNED EFFORT TABLE ----------------------------------
        
        // Get the selected REPORTING PERIOD. If no reportingPeriod selected, assume current period
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
        println "PRINTLN ReportingPeriod.read(params?.reportingPeriod?.id).reportingPeriodInstance: ${reportingPeriodInstance}"        
        if ( ! reportingPeriodInstance ) {
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
            println "PRINTLN laborService.getCurrentReportingPeriod().reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        println "PRINTLN reportingPeriodInstance: ${reportingPeriodInstance}"        
                        
        // Create the LIST to pass all parameters to gsp page
        def effortAssignmentList = []

        // instance of staff list
        def crs = ReportingStaff.createCriteria()        
        def reportingStaffInstanceList = crs.list{
            eq("reportsEffort", true) 
            order("lastName", "asc")
            order("firstName", "asc")
            order("middleInit", "asc")
        }
        
        // Add records to Staff List Instance
        reportingStaffInstanceList.eachWithIndex{ rs, i ->
            
            // Create record map
            def record = [:]
            
            // Get row number
            record.rowNum = i + 1
            println "PRINTLN record.rowNum: ${record.rowNum}"
            
            // Get staff id 
            record.staffId = rs.id
            println "PRINTLN record.staffId: ${record.staffId}"

            // Get staff full name
            record.fullName = rs.fullNameLFM
            println "PRINTLN record.fullName: ${record.fullName}"
                                    
            // Get this period's assigned effort
            def thisPeriodAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, rs)
            println "PRINTLN AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,rs).thisPeriodAssignedEffort: ${thisPeriodAssignedEffort}"
            
            record.thisPeriodAssignedEffort = thisPeriodAssignedEffort?.assignedEffort       
            println "PRINTLN record.thisPeriodAssignedEffort?.assignedEffort.thisPeriodAssignedEffort: ${record.thisPeriodAssignedEffort}"
           
            // Was this period's effort committed
            record.isCommitted = thisPeriodAssignedEffort?.dateCommitted ? true : false
            println "PRINTLN record.isCommitted: ${record.isCommitted}"
                    
            // Get previous period's effort 
            def previousPeriod = reportingPeriodInstance.previousPeriod            
            def previousAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(previousPeriod, rs)
            
            record.previousPeriodEffort = previousAssignedEffort?.assignedEffort
            if ( record.previousPeriodEffort ) {
                record.previousPeriodEffort = record.previousPeriodEffort
            }
            println "PRINTLN record.previousPeriodEffort: ${record.previousPeriodEffort}"
                        
            def cCurrentPeriodReportedEffort = ReportedEffort.createCriteria()           
            def sumReportedEffort = cCurrentPeriodReportedEffort.get {
                eq("assignedEffort", thisPeriodAssignedEffort)
                projections {
                    sum("percentEffort")
                }
            }
            record.currentPeriodReportedEffort = sumReportedEffort
            println "PRINTLN record.currentPeriodReportedEffort: ${record.currentPeriodReportedEffort}"
                        
            // Get date current effort was committed
            record.dateCommitted = thisPeriodAssignedEffort?.dateCommitted
            println "PRINTLN record.dateCommitted: ${record.dateCommitted}"
            
            // Get email notifications sent
            record.datesEmailsSent = thisPeriodAssignedEffort?.emails?.sort{it.dateSent}?.collect{g.formatDate(date:it.dateSent, format:'MM-dd-yyyy')} 
            println "PRINTLN record.datesEmailsSent: ${record.datesEmailsSent}"
           
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
                
        println "PRINTLN ASSIGNED EFFORT CONTROLLER > UPDATE -------------------"
        //println "PRINTLN params: ${params}"
        
        // Get logged in user account
        def principal = authenticateService.principal()
        def username = principal.getUsername()
        def loggedInReportingStaffInstance = ReportingStaff.findByUsername(username)
        println "PRINTLN loggedInReportingStaffInstance: ${loggedInReportingStaffInstance}"
        
        // Get reporting period instance from gsp 
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
        println "PRINTLN reportingPeriodInstance: ${reportingPeriodInstance}"
        
        if ( ! reportingPeriodInstance ) {
        println "PRINTLN if(!reportingPeriodInstance) = TRUE"
            
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
            println "PRINTLN laborService.getCurrentReportingPeriod().reportingPeriodInstance: ${reportingPeriodInstance}"
        }
                
        //loop through each parameter
        params.each{
                        
            //println "PRINTLN AssignedEffortController.update.params.each: ${it}"
            //println "PRINTLN AssignedEffortController.update.params.each IT: ${it}, KEY: ${it.key}, VALUE: ${it.value}"

            if ( it.key =~ /^staff-[0-9]*$/) {

                /********************************************************************************************************
                 * REPORTING STAFF
                 ********************************************************************************************************/
                
                // Get reporting staff id from gsp 
                def reportingStaffId = Integer.parseInt(it.key.replace('staff-', ''))
                //println "PRINTLN params.each.reportingStaffId: ${reportingStaffId}"

                // Create reporting staff instance
                def reportingStaffInstance = ReportingStaff.read(reportingStaffId)
                //println "PRINTLN params.each.reportingStaffInstance: ${reportingStaffInstance}"                

                /********************************************************************************************************
                 * CURRENT PERIOD'S ASSIGNED EFFORT
                 ********************************************************************************************************/
                
                // Get current period's assigned effort from gsp
                def currentPeriodAssignedEffortValue = it.value.thisPeriodAssignedEffort                                
                //println "PRINTLN params.each.currentPeriodAssignedEffortValue: ${currentPeriodAssignedEffortValue}"
                
                // validate current period's assigned effort from gsp
                // if effort has 0-3 digits before the decimal, and 0-2 after the decimal
                if ( currentPeriodAssignedEffortValue =~ /[0-9]{1,3}\.?[0-9]{0,2}/ ) {
                //println "PRINTLN params.each.if(currentPeriodAssignedEffortValue=~) = TRUE"
                    
                    // convert effort to bigDecimal
                    currentPeriodAssignedEffortValue = currentPeriodAssignedEffortValue.toBigDecimal()
                    //println "PRINTLN params.each.currentPeriodAssignedEffortValue.toBigDecimal().currentPeriodAssignedEffortValue: ${currentPeriodAssignedEffortValue}"
                    
                    // if the effort value is NOT between 0 - 100
                    def r = 0.0..100.0
                    
                    if ( r.containsWithinBounds(currentPeriodAssignedEffortValue) ) {
                    //println "PRINTLN AssignedEffortController.update.params.each.if(r.containsWithinBounds(currentPeriodAssignedEffortValue)) = TRUE"
                    
                        // convert effort to decimal
                        currentPeriodAssignedEffortValue = currentPeriodAssignedEffortValue /100
                        
                    } else {
                    //println "PRINTLN AssignedEffortController.update.params.each.if(r.containsWithinBounds(currentPeriodAssignedEffortValue)) = FALSE"
                        
                        // make effort null
                        currentPeriodAssignedEffortValue = null
                        //println "PRINTLN AssignedEffortController.update.params.each.currentPeriodAssignedEffortValue = null"
                        
                    } //if ( r.containsWithinBounds(currentPeriodAssignedEffortValue) )
                    
                // if effort is NOT 0-3 digits before the decimal, and 0-2 after the decimal
                } else {
				
					//println "PRINTLN AssignedEffortController.update.params.each.if(currentPeriodAssignedEffortValue=~) = FALSE"
            
                    // make effort null
                    currentPeriodAssignedEffortValue = null
                    
                }
                //println "PRINTLN params.each.validated.currentPeriodAssignedEffortValue: ${currentPeriodAssignedEffortValue}"
                                

                /********************************************************************************************************
                 * PREVIOUS PERIOD'S ASSIGNED EFFORT
                 ********************************************************************************************************/
                
                // Get previous period's assigned effort from gsp
                def previousPeriodAssignedEffortValue = it.value.previousPeriodEffort
                //println "PRINTLN params.each.previousPeriodAssignedEffortValue: ${previousPeriodAssignedEffortValue}"
                
                // validate previous period's assigned effort from gsp
                // if effort is not null or is 0
                if ( previousPeriodAssignedEffortValue || previousPeriodAssignedEffortValue == '0' ) {
                    // convert effort to bigdecimal
                    previousPeriodAssignedEffortValue = previousPeriodAssignedEffortValue.toBigDecimal()
                // otherwise, make effort null
                } else {
                    previousPeriodAssignedEffortValue = null
                }
                //println "PRINTLN params.each.validated.previousPeriodAssignedEffortValue: ${previousPeriodAssignedEffortValue}"
                                 
                
                /********************************************************************************************************
                 * SUBMIT CURRENT PERIOD'S ASSIGNED EFFORT
                 ********************************************************************************************************/

                // Get value of checkbox "Copy Previous to Current" from gsp
                def copyPreviousToCurrentValue = it.value.copyPreviousToCurrent
                //println "PRINTLN AssignedEffortController.update.params.each.copyPreviousToCurrentValue: ${copyPreviousToCurrentValue}"
                
                // if checkbox "Copy Previous to Current" is checked
                if ( copyPreviousToCurrentValue ) {
                //println "PRINTLN AssignedEffortController.update.params.each.if(copyPreviousToCurrentValue) = TRUE"                    
                    
                    // set current period's assigned effort from gsp to previous period's assigned effort
                    currentPeriodAssignedEffortValue = previousPeriodAssignedEffortValue
                    //println "PRINTLN AssignedEffortController.update.params.each.currentPeriodAssignedEffortValue: ${currentPeriodAssignedEffortValue}"
                    
                }

                // Get curent period's assigned effort instance, if one has been assigned already
                def currentPeriodAssignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
                //println "PRINTLN AssignedEffortController.update.params.each.currentPeriodAssignedEffortInstance: ${currentPeriodAssignedEffortInstance}"
                
                // if current period's assigned effort from gsp is null or 0
                if ( ! currentPeriodAssignedEffortValue || currentPeriodAssignedEffortValue == 0 ) {
                    //println "PRINTLN if current period's assigned effort from gsp is null or 0"
                
                    // and there is a current period assigned effort in db
                    if ( currentPeriodAssignedEffortInstance ) {                        
                        //println "PRINTLN and there is a current period assigned effort in db"
                        
                        // delete current period assigned effort in db
                        def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
                        assignedEffortInstance.delete()
                        //println "PRINTLN delete assignedEffortInstance"
                        
                    }                    
                   
                // if current period's assigned effort from gsp is > 0
                } else {
				                                        
                    //println "PRINTLN if current period's assigned effort from gsp is > 0"                    
                                        
                    // and if current period's assigned effort instance has a value and it doesn't match with valid value that was entered, remove current period's assigned effort instance from db
                    if ( currentPeriodAssignedEffortInstance ) {                        
                        //println "PRINTLN there is a currentPeriodAssignedEffortInstance"                    
                                                
                        if ( currentPeriodAssignedEffortInstance.assignedEffort !=  currentPeriodAssignedEffortValue ) {
                            //println "PRINTLN if current period's assigned effort instance has a value and it DOES NOT match with value that was entered"                    
                            
                            def assignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
                            assignedEffortInstance.delete()
                            //println "PRINTLN delete current period's assigned effort instance"

                        }                                                                        
                        
                    } 
                    
                    // Create current period's assigned effort intance here, with effort from gsp, and save it
                    currentPeriodAssignedEffortInstance = new AssignedEffort(
                        reportingStaff: reportingStaffInstance, 
                        laborCategory: reportingStaffInstance.laborCategory, 
                        period: reportingPeriodInstance,
                        assignedEffort: currentPeriodAssignedEffortValue, 
                        assigningStaff: loggedInReportingStaffInstance, 
                        appCreated: 'ncs-dlr'
                    )                        
                    //println "PRINTLN Create new current period's assigned effort instance based on currentPeriodAssignedEffortValue"
                    
                    // if current period's assgined effort instance has been modified
                    if ( ! currentPeriodAssignedEffortInstance.id || currentPeriodAssignedEffortInstance.isDirty() ) {
                        
                        // if save failed
                        if ( ! currentPeriodAssignedEffortInstance.save(flush:true) ) {
                            
                            currentPeriodAssignedEffortInstance.errors.each{
                                //println "PRINTLN currentPeriodAssignedEffortInstance.error: ${it}"
                            }
                            
                            // if checkbox "Copy Previous to Current" is checked, notify user it failed
                            if ( copyPreviousToCurrentValue ) {
                                flash.message = "Unable to copy previous period's assigned effort to current period for ${reportingStaffInstance.getFullNameLFM()}}"                            
                            // if user enter modifies effort in textbox "Current Period's Assigned Effort", notify user it failed    
                            } else {
                                flash.message = "Unable to save current period's assigned effort for ${reportingStaffInstance.getFullNameLFM()}}"                                                            
                            } //if ( copyPreviousToCurrentValue )
                            
                        } //if ( ! currentPeriodAssignedEffortInstance.save(flush:true) )
                        
                    } //if ( currentPeriodAssignedEffortInstance.isDirty )

                    
                } //if ( ! currentPeriodAssignedEffortValue || currentPeriodAssignedEffortValue == 0 ) {

                                             
                /********************************************************************************************************
                 * SEND EMAIL NOTIFICATION 
                 ********************************************************************************************************/
                
                // Get value of checkbox "Send Now" from gsp
                def sendNowValue = it.value.sendNow
                //println "PRINTLN params.each.sendNowValue: ${sendNowValue}"
                
                // if checkbox "Send Now" from gsp is checked
                if ( sendNowValue ) { 
                //println "PRINTLN params.each.if(sendNowValue) = TRUE"
                
                    //println "PRINTLN reportingPeriodInstance.id: ${reportingPeriodInstance.id}"
                    //println "PRINTLN reportingStaffInstance.id: ${reportingStaffInstance.id}"
                
                    def message = laborService.sendEmailNotification(reportingPeriodInstance.id, reportingStaffInstance.id)      
                    //println "PRINTLN send email notification"
                                                           
                } //if ( sendNowValue )
                
            } //if ( it.key =~ /staff-[0-9]*/)

        } //params.each
        
        
        // REDIRECT TO SHOW GSP PAGE ----------------------------------------------------------------------------------------------------
        def params = ['reportingPeriodInstance.id': reportingPeriodInstance?.id ]
		println "PRINTLN params = [reportingPeriodInstance.id]: ${params}"
        redirect(action:'show', params:params)
                        
    } //def update

} //class AssignEffortController 