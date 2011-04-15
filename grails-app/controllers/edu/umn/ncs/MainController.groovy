package edu.umn.ncs

// security annotation
import org.codehaus.groovy.grails.plugins.springsecurity.Secured

@Secured(['ROLE_NCS_DLR', 'ROLE_NCS_IT'])
class MainController {

    def authenticateService
    def laborService

    def index = {

        // principal is whoever is logged in
        def principal = authenticateService.principal()

        def reportingStaffInstance = laborService.getReportingStaff(principal)

        //  optional pull from params.reportingPeriod.id
        def reportingPeriodInstance
        if (params?.reportingPeriod?.id) {
            reportingPeriodInstance = ReportingPeriod.read(params?.reportingPeriod?.id)
        } else {
            reportingPeriodInstance laborService.getCurrentReportingPeriod()
        }

        def assignedEffortInstance = AssignedEffort.findByReportingStaffAndPeriod(reportingStaffInstance, reportingPeriodInstance)
        def studyActivityInstanceList = StudyActivity.findAllByObsolete(false)
        def studyTaskInstanceList = StudyTask.findAllByObsolete(false)
        def reportedEffortInstanceList
        def previouslyReportedEffortInstanceList

        [
            reportingStaffInstance: reportingStaffInstance,
            studyActivityInstanceList: studyActivityInstanceList,
            studyTaskInstanceList: studyTaskInstanceList
        ]


    }

}
