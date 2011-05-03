package edu.umn.ncs
import org.joda.time.*

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class ApplicationManagementController {

    def authenticateService
    def laborService

    def index = { 
    
    }

    def assign = {
        
        // GET PERIODS FOR PERIOD CONTROL --------------------------------------

        // get current period 
        def currentPeriod = laborService.getCurrentReportingPeriod()
        //println "PRINTLN ApplicationManagementController.assign.currentPeriod: ${currentPeriod}"

        // get next period, after current period 
        def c = ReportingPeriod.createCriteria()
        def nextPeriod = c.list{
            and {
                gt("periodDate", currentPeriod.periodDate)
            }
            maxResults(1)
        }
        //println "PRINTLN ApplicationManagementController.assign.nextPeriod: ${nextPeriod}"

        if ( ! nextPeriod ) {
            def nextMonthDate = laborService.getNextReportingPeriodDateTime(currentPeriod.periodDate)
            // println "PRINTLN ApplicationManagementController.assign.nextMonthDate: ${nextMonthDate}"
            nextPeriod = new ReportingPeriod(periodDate:nextMonthDate)
            nextPeriod.save(flush:true)
            //println "PRINTLN ApplicationManagementController.assign.nextPeriod: ${nextPeriod}"
        }

       // get previous two periods, before current period 
        def c2 = ReportingPeriod.createCriteria()
        def previousTwoPeriods = c2.list{
            and {
                    lt("periodDate", currentPeriod.periodDate)
                }
                maxResults(2)
                order("periodDate","desc")
        }
        //println "PRINTLN ApplicationManagementController.assign.previousTwoPeriods: ${previousTwoPeriods}"
       
        // create PERIOD LIST for "Reporting Month" control by adding all (NEXT, CURRENT, and PREVIOUS TWO) to list
        def periodList = []
        periodList.addAll(nextPeriod)
        periodList.add(currentPeriod)
        periodList.addAll(previousTwoPeriods)
        //println "PRINTLN ApplicationManagementController.assign.periodList: ${periodList}"
        /*
        periodList.each{
            println "PRINTLN ApplicationManagementController.assign.periodList: ${it.periodDate}"
        }
        */

        // GET DATA FOR ASSIGNED EFFORT TABLE
        
        // get the selected REPORTING PERIOD to display in Effort Assignment table
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
        //println "PRINTLN ApplicationManagementController.assign.(params?.reportingPeriod?.id).reportingPeriodInstance: ${reportingPeriodInstance}"
        
        // if no reportingPeriod selected, assume current period
        if ( ! reportingPeriodInstance ) {
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
            //println "PRINTLN ApplicationManagementController.assign.laborService.getCurrentReportingPeriod().reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        
        // create list containing data for Effort Assignment table
        def effortAssignmentList = []
        
        def reportingStaffInstanceList = ReportingStaff.list([sort:'lastName'])
        
        reportingStaffInstanceList.eachWithIndex{ rs, i ->
            
            // row number
            def record = [:]
            record.rowNum = i + 1
            
            // staff full name
            record.fullName = rs.fullNameLFM
            record.staffId = rs.id
                        
            // effort of current period
            def currentAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(reportingPeriodInstance, rs)
            record.currentPeriodEffort = currentAssignedEffort?.assignedEffort
            record.isCommitted = currentAssignedEffort?.dateCommitted ? true : false
                    
            // effort of previous period
            def previousPeriod = reportingPeriodInstance.previousPeriod
            def previousAssignedEffort = AssignedEffort.findByPeriodAndReportingStaff(previousPeriod, rs)
            record.previousPeriodEffort = previousAssignedEffort?.assignedEffort
            if ( record.previousPeriodEffort ) {
                record.previousPeriodEffort = record.previousPeriodEffort / 100
            }
            //record.previousPeriodEffort = 42/100
            
            record.percentCommitted = currentAssignedEffort?.dateCommitted ? AssginedEffort.assignedEffort : null
            if ( record.percentCommitted ) {
                record.percentCommitted = record.percentCommitted / 100
            }
            //record.percentCommitted = 33/100
            
            // Date Committed
            record.dateCommitted = currentAssignedEffort?.dateCommitted
            
            record.datesEmailSent = currentAssignedEffort?.emails?.sort{it.dateSent}?.collect{g.formatDate(date:it.dateSent, format:'MM-dd-yyyy')}
            
            effortAssignmentList.add(record)
            
        }
        
        def periodSelectList = []
        periodList.each{
            periodSelectList.add([id:it.id, name: g.formatDate(date:it.periodDate, format:'MMMM yyyy')])
        }
        
        [
            periodList: periodList,
            reportingPeriodInstance: reportingPeriodInstance, 
            effortAssignmentList: effortAssignmentList,
            periodSelectList: periodSelectList
        ]

    } //def assign

}
