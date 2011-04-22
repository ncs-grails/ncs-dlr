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

        def periodList = []
        def reportingPeriod = ReportingPeriod.read(params?.reportingPeriod?.id)

        // get CURRENT PERIOD --------------------------------------------------
        def currentPeriod = laborService.getCurrentReportingPeriod()
        if ( ! reportingPeriod ) {
            reportingPeriod = currentPeriod
        }
        // println "PRINTLN ApplicationManagementController.assign.currentPeriod: ${currentPeriod}"


        // get NEXT PERIOD, after current period -------------------------------
        def c = ReportingPeriod.createCriteria()
        def nextPeriod = c.list{
            and {
                gt("periodDate", currentPeriod.periodDate)
            }
            maxResults(1)
        }
        // println "PRINTLN ApplicationManagementController.assign.nextPeriod: ${nextPeriod}"

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
        // println "PRINTLN ApplicationManagementController.assign.previousTwoPeriods: ${previousTwoPeriods}"
       
        periodList.addAll(nextPeriod)
        periodList.add(currentPeriod)
        periodList.addAll(previousTwoPeriods)
        /*
        //println "PRINTLN ApplicationManagementController.assign.periodList: ${periodList}"
        periodList.each{
            println "PRINTLN ApplicationManagementController.assign.periodList: ${it.periodDate}"
        }
        */

        // REPORTING PERIOD
        def reportingPeriodInstance = ReportingPeriod.get(params?.reportingPeriod?.id)
        println "PRINTLN ApplicationManagementController.assign.reportingPeriodInstance: ${reportingPeriodInstance}"

        [
            periodList: periodList,
            reportingPeriod: reportingPeriod,
            reportingPeriodInstance: reportingPeriodInstance
        ]

    } //def assign

}
