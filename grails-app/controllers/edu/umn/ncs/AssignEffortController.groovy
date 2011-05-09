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

                // Get reporting staff id
                def reportingStaffId = Integer.parseInt(it.key.replace('staff-', ''))
                println "PRINTLN AssignedEffortController.update.params.each.reportingStaffId: ${reportingStaffId}"

                // COPY PREVIOUS TO CURRENT ------------------------------                
                def copyPreviousToCurrentValue = it.value.copyPreviousToCurrent
                println "PRINTLN AssignedEffortController.update.params.each.copyPreviousToCurrentValue: ${copyPreviousToCurrentValue}"
                       
                // if "Copy Previous to Current" checkbox is checked
                if (copyPreviousToCurrentValue ) { 
                    
                    // Get reporting staff instance
                    //def reportingStaffInstance = ReportingStaff.read(reportingStaffId)
                    //println "PRINTLN AssignedEffortController.update.params.each.reportingStaffInstance: ${reportingStaffInstance}"

                    // Get previous period assigned effort
                    def previousPeriodEffortValue = it.value.previousPeriodEffort
                    println "PRINTLN AssignedEffortController.update.params.each.previousPeriodEffortValue: ${previousPeriodEffortValue}"
                                        
                    // Get current period assigned effort, if there is one
                    def assignedCurrentPeriodEffortInstance = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance,reportingStaffId)
                    println "PRINTLN AssignedEffortController.update.params.each.assignedCurrentPeriodEffortInstance: ${assignedCurrentPeriodEffortInstance}"
                                        
                    if ( previousPeriodEffortValue ) {
                        
                        //println "PRINTLN AssignedEffortController.update.params.each.previousPeriodEffortValue: ${previousPeriodEffortValue}"

                        if ( previousPeriodEffortValue != assignedCurrentPeriodEffortInstance ) {
                            println "previousPeriodEffortValue != assignedCurrentPeriodEffortInstance SUCCEED"
                        } else {
                            println "previousPeriodEffortValue != assignedCurrentPeriodEffortInstance FAILED"
                        }
                        if ( assignedCurrentPeriodEffortInstance == null ) {
                            println "assignedCurrentPeriodEffortInstance == null SUCCEED"
                        } else {
                            println "assignedCurrentPeriodEffortInstance == null FAILED"
                        }                                                
                        
                        if ( (previousPeriodEffortValue != assignedCurrentPeriodEffortInstance) | (assignedCurrentPeriodEffortInstance == null) ) {                            
                            println "(previousPeriodEffortValue != assignedCurrentPeriodEffortInstance) | (assignedCurrentPeriodEffortInstance == null) SUCCEED"
                        } else {
                            println "(previousPeriodEffortValue != assignedCurrentPeriodEffortInstance) | (assignedCurrentPeriodEffortInstance == null) FAILED"
                        } 
                        
                    }//if ( previousPeriodEffortValue ) 
                    
                } //if (copyPreviousToCurrentValue )
                
                        //assignedEffortInstance.assignedEffort = it.value.currentPeriodEffort
                        
                        //if ( ! assignedEffortInstance.save(flush:true) ) {
                            //flash.message = "Unable to save assigned effort for xx for period xx"
                        //}
                        //assignedEffortInstance = new AssignedEffort(//parameters)
                
                // ENTER CURRENT EFFORT ----------------------------------
                def currentPeriodEffortValue = it.value.thisPeriodEffort
                //println "PRINTLN AssignedEffortController.update.params.each.currentPeriodEffortValue: ${currentPeriodEffortValue}"
                
                if ( currentPeriodEffortValue ) { 
                    //println "PRINTLN update current effort"
                }
                
                
                // SEND NOW ----------------------------------------------                
                def sendNowValue = it.value.sendNow
                //println "PRINTLN AssignedEffortController.update.params.each.sendNowValue: ${sendNowValue}"
                
                if ( sendNowValue ) { 
                    //println "PRINTLN send now"
                }
                    
                
                
                
            } //if ( it.key =~ /staff-[0-9]*/)


        } //params.each
        
        
        // Redirect to assign, passing those params
        def params = ['reportingPeriodInstance.id': reportingPeriodInstance?.id ]
        redirect(action:'show', params:params)
                
        
    } //def update


}