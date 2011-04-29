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

        // get CURRENT PERIOD --------------------------------------------------
        def currentPeriod = laborService.getCurrentReportingPeriod()
        println "PRINTLN ApplicationManagementController.assign.currentPeriod: ${currentPeriod}"

        // get NEXT PERIOD, after current period -------------------------------
        def c = ReportingPeriod.createCriteria()
        def nextPeriod = c.list{
            and {
                gt("periodDate", currentPeriod.periodDate)
            }
            maxResults(1)
        }
        println "PRINTLN ApplicationManagementController.assign.nextPeriod: ${nextPeriod}"

        if ( ! nextPeriod ) {
            def nextMonthDate = laborService.getNextReportingPeriodDateTime(currentPeriod.periodDate)
            // println "PRINTLN ApplicationManagementController.assign.nextMonthDate: ${nextMonthDate}"
            nextPeriod = new ReportingPeriod(periodDate:nextMonthDate)
            nextPeriod.save(flush:true)
            // println "PRINTLN ApplicationManagementController.assign.nextPeriod: ${nextPeriod}"
        }

       // get PREVIOUS TWO PERIODS, before current period ----------------------
        def c2 = ReportingPeriod.createCriteria()
        def previousTwoPeriods = c2.list{
            and {
                    lt("periodDate", currentPeriod.periodDate)
                }
                maxResults(2)
                order("periodDate","desc")
        }
        println "PRINTLN ApplicationManagementController.assign.previousTwoPeriods: ${previousTwoPeriods}"
       
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

        // get the appropriate REPORTING PERIOD to display in Effort Assignment table
        def reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriodInstance?.id)
        println "PRINTLN ApplicationManagementController.assign.(params?.reportingPeriod?.id).reportingPeriodInstance: ${reportingPeriodInstance}"
        
        // if no reportingPeriod param, assume current period
        if ( ! reportingPeriodInstance ) {
            reportingPeriodInstance = laborService.getCurrentReportingPeriod()
            println "PRINTLN ApplicationManagementController.assign.laborService.getCurrentReportingPeriod().reportingPeriodInstance: ${reportingPeriodInstance}"
        }
        
        // get reportingPeriodMonthName
        //def reportingreportingPeriodMonth = reportingPeriodInstance.getMonth()
        //println "PRINTLN ApplicationManagementController.assign.reportingreportingPeriodMonth: ${reportingreportingPeriodMonth}"
        //def reportingreportingPeriodMonthName = laborService.getMonthName(reportingreportingPeriodMonth)
        //println "PRINTLN ApplicationManagementController.assign.reportingreportingPeriodMonthName: ${reportingreportingPeriodMonthName}"

        [
            periodList: periodList,
            reportingPeriodInstance: reportingPeriodInstance 
        ]

    } //def assign

}
