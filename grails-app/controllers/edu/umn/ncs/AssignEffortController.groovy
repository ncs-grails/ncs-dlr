package edu.umn.ncs
import org.joda.time.*

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class AssignEffortController {

    def authenticateService
    def laborService
   
    // display page
    def index = {
        redirect(action:'show')        
    }
    
    def show = {
        
        // GET PERIODS FOR PERIOD CONTROL --------------------------------------

        // Get current period 
        def currentPeriod = laborService.getCurrentReportingPeriod()
        //println "PRINTLN AssignedEffortController.show.currentPeriod: ${currentPeriod}"

        // Get next period, after current period. If one does not exit in db, create it 
        def c = ReportingPeriod.createCriteria()
        def nextPeriod = c.list{
            and {
                gt("periodDate", currentPeriod.periodDate)
            }
            maxResults(1)
        }
        //println "PRINTLN AssignedEffortController.show.ReportingPeriod.createCriteria.nextPeriod: ${nextPeriod}"

        if ( ! nextPeriod ) {
            def nextMonthDate = laborService.getNextReportingPeriodDateTime(currentPeriod.periodDate)
            nextPeriod = new ReportingPeriod(periodDate:nextMonthDate)
            nextPeriod.save(flush:true)
            //println "PRINTLN AssignedEffortController.show.laborService.getNextReportingPeriodDateTime.nextPeriod: ${nextPeriod}"
        }
        //println "PRINTLN AssignedEffortController.show.nextPeriod: ${nextPeriod}"

        // Get any previous periods, in which the efforts have not been committed
        def c2 = ReportingPeriod.createCriteria()
        def previousPeriods = c2.list{
            and {
                    lt("periodDate", currentPeriod.periodDate)
                    isNull("completedReportDate")
                }
                //maxResults(2)
                order("periodDate","desc")
        }
        //println "PRINTLN AssignedEffortController.show.previousPeriods: ${previousPeriods}"
       
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

        // GET DATA FOR ASSIGNED EFFORT TABLE ----------------------------------
        
        // Get the selected REPORTING PERIOD. If no reportingPeriod selected, assume current period
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
        //println "PRINTLN AssignedEffortController.show.(params?.reportingPeriod?.id).reportingPeriodInstance: ${reportingPeriodInstance}"        
        if ( ! reportingPeriodInstance ) {
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
            //println "PRINTLN AssignedEffortController.show.laborService.getCurrentReportingPeriod().reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        //println "PRINTLN AssignedEffortController.show.reportingPeriodInstance: ${reportingPeriodInstance}"        
                        
        // Create the LIST to pass all parameters to gsp page
        def effortAssignmentList = []

        // Create instance of staff list: 
        // -- include those who should be reporting effort, 
        // -- then organized by staff (lastname, firstname, middleinit)
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
            
            // row number
            record.rowNum = i + 1
            //println "PRINTLN AssignedEffortController.show.record.rowNum: ${record.rowNum}"
            
            record.staffId = rs.id
            //println "PRINTLN AssignedEffortController.show.record.staffId: ${record.staffId}"

            // staff full name
            record.fullName = rs.fullNameLFM
            //println "PRINTLN AssignedEffortController.show.record.fullName: ${record.fullName}"
                                    
            // effort ofthis period
            def thisPeriodAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, rs)
            //println "PRINTLN AssignedEffortController.show.thisPeriodPeriodEffort: ${thisPeriodAssignedEffort}"
            
            record.thisPeriodAssignedEffort = thisPeriodAssignedEffort?.assignedEffort
            
            //println "PRINTLN AssignedEffortController.show.record.thisPeriodAssignedEffort: ${record.thisPeriodAssignedEffort}"
            
            // dateCommitted of this period, if it was committed
            record.isCommitted = thisPeriodAssignedEffort?.dateCommitted ? true : false
                    
            // effort of previous period
            def previousPeriod = reportingPeriodInstance.previousPeriod
            def previousAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(previousPeriod, rs)
            record.previousPeriodEffort = previousAssignedEffort?.assignedEffort
            if ( record.previousPeriodEffort ) {
                record.previousPeriodEffort = record.previousPeriodEffort
            }
            
            // percent effort of period, that has been committed
            record.percentCommitted = thisPeriodAssignedEffort?.dateCommitted ? AssginedEffort.assignedEffort : null
            if ( record.percentCommitted ) {
                record.percentCommitted = record.percentCommitted / 100
            }
            
            // date current effort was committed
            record.dateCommitted = thisPeriodAssignedEffort?.dateCommitted
            
            // email notifications sent
            //record.datesEmailsSent = thisPeriodAssignedEffort?.emails?.sort{it.dateSent}?.collect{ g.formatDate(date:it.dateSent, format:'MM-dd-yyyy')} 
            record.datesEmailsSent = thisPeriodAssignedEffort?.emails
            //println "PRINTLN AssignedEffortController.show.record.datesEmailsSent: ${record.datesEmailsSent}"
           
            // load all data collected to the LIST that is passed to gsp page
            effortAssignmentList.add(record)
            
        }
        /*
        effortAssignmentList.each{ 
            println "PRINTLN AssignedEffortController.show.effortAssignmentList.thisPeriodAssignedEffort: ${it.thisPeriodAssignedEffort}" 
        }
        */
                
        [
            reportingPeriodInstance: reportingPeriodInstance, 
            periodSelectiontList: periodSelectiontList,
            effortAssignmentList: effortAssignmentList
        ]

    } //def show 

    // UPDATE when COMMIT button is clicked
    def update = {
                
        //println "PRINTLN AssignedEffortController.update.params: ${params}"
        
        // Get reporting period instance from gsp page
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
        println "PRINTLN AssignedEffortController.update.reportingPeriodInstance: ${reportingPeriodInstance}"
                
        params.each{
                        
            //println "PRINTLN AssignedEffortController.update.params: ${it}"
            //println "PRINTLN AssignedEffortController.update.params.each IT: ${it}, KEY: ${it.key}, VALUE: ${it.value}"

            if ( it.key =~ /^staff-[0-9]*$/) {

                // Get reporting STAFF ID from form
                def reportingStaffId = Integer.parseInt(it.key.replace('staff-', ''))
                println "PRINTLN AssignedEffortController.update.params.each.reportingStaffId: ${reportingStaffId}"

                // Get REPORTING STAFF instance
                def reportingStaffInstance = ReportingStaff.read(reportingStaffId)
                //println "PRINTLN AssignedEffortController.update.params.each.reportingStaffInstance: ${reportingStaffInstance}"
                
                // Get CURRENT PERIOD ASSIGNED EFFORT from form
                def currentPeriodAssignedEffortValue = it.value.thisPeriodEffort
                //println "PRINTLN AssignedEffortController.update.params.each.currentPeriodAssignedEffortValue: ${currentPeriodAssignedEffortValue}"
                 
                // Get CURRENT PERIOD ASSIGNED EFFORT instance
                def currentPeriodAssignedEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffInstance)
                //println "PRINTLN AssignedEffortController.update.params.each.currentPeriodAssignedEffortInstance: ${currentPeriodAssignedEffortInstance}"
                

                // COPY PREVIOUS TO CURRENT ---------------------------------------------------------------------------------------
                
                // Get value of "Copy Previous to Current" checkbox
                def copyPreviousToCurrentValue = it.value.copyPreviousToCurrent
                //println "PRINTLN AssignedEffortController.update.params.each.copyPreviousToCurrentValue: ${copyPreviousToCurrentValue}"
                
                // if "Copy Previous to Current" checkbox is checked
                if ( copyPreviousToCurrentValue ) { 
                    
                    // Get previous period assigned effort
                    def previousPeriodEffortValue = it.value.previousPeriodEffort
                    //println "PRINTLN AssignedEffortController.update.params.each.previousPeriodEffortValue: ${previousPeriodEffortValue}"
                                                                                
                    // if there is a previous effort, to copy over
                    if ( previousPeriodEffortValue ) {
                                                                       
                        // TODO: if current effort is null, insert previous period effort to current assigned effort
                        if ( currentPeriodAssignedEffortInstance == null ) {                            
                            
                            /*
                            // insert previous effort to current                            
                            def assignedEffortInstance = new AssignedEffort(
                                reportingStaff: reportingStaffId, 
                                laborCategory: , 
                                period: reportingPeriodInstance.id, 
                                assignedEffort: previousPeriodEffortValue, 
                                dateAssigned: , 
                                assigningStaff: , 
                                appCreated: , 
                                dateCommitted: '', 
                                commitingStaff: ''
                            ).save()
                            
                            if ( ! assignedEffortInstance.save(flush:true) ) {
                                flash.message = "Unable to save previous effort to current period for ${reportingStaffInstance.fullName}}"
                            }
                            */    
                            //println "PRINTLN currentPeriodAssignedEffortInstance == null"  
                            
                        // TODO: if current effort is not null
                        } else {
                            
                            // if previous and current effort are different, conduct update
                            if ( previousPeriodEffortValue != currentPeriodAssignedEffortInstance ) {
                                println "PRINTLN previousPeriodEffortValue != currentPeriodAssignedEffortInstance"
                                
                            } //if ( previousPeriodEffortValue != currentPeriodAssignedEffortInstance )
                            
                        } //if ( currentPeriodAssignedEffortInstance == null )
                        
                    }//if ( previousPeriodEffortValue ) 
                    
                } //if (copyPreviousToCurrentValue )
                
                                
                // TODO: ENTER CURRENT EFFORT -----------------------------------------------------------------------------

                // if "Copy Previous to Current" checkbox was not check, but there is effort in the "Assigned Effort:Curent Period" textbox
                if ( ! copyPreviousToCurrentValue && currentPeriodAssignedEffortValue != null) {
                    
                    // Does current period assigned effort already exist in db?
                    
                    // TODO: no - perform insert
                    if ( currentPeriodAssignedEffortInstance == null ) {
                        
                    // TODO: yes - update if current period assigned effort is different in textbox from what is in db
                    } else {
                        
                        if ( currentPeriodAssignedEffortValue != currentPeriodAssignedEffortInstance ) {
                            
                        }
                        
                    } //if ( currentPeriodAssignedEffortInstance == null )
                    
                } //if ( ! copyPreviousToCurrentValue && currentPeriodAssignedEffortValue == null)
                
                // SEND NOW -----------------------------------------------------------------------------------------------
                def sendNowValue = it.value.sendNow
                //println "PRINTLN AssignedEffortController.update.params.each.sendNowValue: ${sendNowValue}"
                
                if ( sendNowValue ) { 
                    //println "PRINTLN send now"
                    
                    // TODO: run send email
                    
                } //if ( sendNowValue )
                    
                
            } //if ( it.key =~ /staff-[0-9]*/)


        } //params.each
        
        
        // REDIRECT TO SHOW GSP PAGE
        def params = ['reportingPeriodInstance.id': reportingPeriodInstance?.id ]
        redirect(action:'show', params:params)
                
        
    } //def update


} //class AssignEffortController {