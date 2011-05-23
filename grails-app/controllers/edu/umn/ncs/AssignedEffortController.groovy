package edu.umn.ncs

class AssignedEffortController {

    def index = {
        redirect(controller:'main')
    }
    
    def show = {
        
        /* parameters passed to from show.gsp to reportedEffortTable.gsp
            'period_id':reportingPeriodInstance.id,
            'staff_id':reportingStaffInstance.id,
            'assigned_effort_id': assignedEffortInstance.id
        */
        // Get ASSSIGNED EFFORT instance
        def assignedEffortInstance
        def reportingPeriodInstance
        def reportingStaffInstance
        if (params?.id) {
            assignedEffortInstance = AssignedEffort.read(params?.id)
            reportingPeriodInstance = assignedEffortInstance.period
            reportingStaffInstance = assignedEffortInstance.reportingStaff

            println "PRINTLN MainController.reportedEffortTable.assignedEffortInstance: ${assignedEffortInstance}"
            //println "PRINTLN MainController.show.assignedEffortInstance.id: ${assignedEffortInstance.id}"
        }
        
        // Get REPORTED EFFORT TOTAL
        def reportedEffortTotal
        if ( assignedEffortInstance ) {
            
            def c = ReportedEffort.createCriteria()
            reportedEffortTotal = c.get {
                eq("assignedEffort", assignedEffortInstance)
                projections {
                    sum("percentEffort")
                }
            }
            println "PRINTLN MainController.reportedEffortTable.reportedEffortTotal: ${reportedEffortTotal}"
            
        }
        
        // Get list of reported effort 
        def crs = ReportedEffort.createCriteria()        
        def reportingEffortInstanceList = crs.list{
            eq("assignedEffort", assignedEffortInstance) 
            order("activity", "asc")
            order("task", "asc")
        }
        println "PRINTLN MainController.reportedEffortTable.reportingEffortInstanceList: ${reportingEffortInstanceList}"

        // Add records to Reported Effort Instance
        def reportedEffortList = []

        reportingEffortInstanceList.eachWithIndex{ rs, i -> 
            
            // Create record map
            def record = [:]
            
            // Get effort id
            record.reportedEffortId = rs.id
            println "PRINTLN AssignedEffortController.reportedEffortTable.record.reportedEffortId: ${record.reportedEffortId}"            
            
            // Get study activity
            record.studyActivity = rs.activity
            println "PRINTLN AssignedEffortController.reportedEffortTable.record.studyActivity: ${record.studyActivity}"
            
            // Get study task
            record.studyTask = rs.task
            println "PRINTLN AssignedEffortController.reportedEffortTable.record.studyTask: ${record.studyTask}"
            
            // Get percentEffort
            record.percentEffort = rs.percentEffort
            println "PRINTLN AssignedEffortController.reportedEffortTable.record.percentEffort: ${record.percentEffort}"
            
            //Get DateCreated
            record.dateCreated = rs.dateCreated
            println "PRINTLN AssignedEffortController.reportedEffortTable.record.dateCreated: ${record.dateCreated}"
            
            reportedEffortList.add(record)
            
        } //reportingStaffInstanceList.eachWithIndex{ rs, i ->        
        
        // Get EFFORT COMMITTED DATE
        def committedDateInstance
        if ( assignedEffortInstance ) {
            committedDateInstance = assignedEffortInstance.dateCommitted
            println "PRINTLN MainController.show.committedDateInstance: ${committedDateInstance}"            
        }

        [
            reportingPeriodInstance:reportingPeriodInstance, 
            reportingStaffInstance:reportingStaffInstance,
            assignedEffortInstance: assignedEffortInstance,
            reportedEffortTotal: reportedEffortTotal,
            committedDateInstance: committedDateInstance,
            reportedEffortList: reportedEffortList
        ]

    } //def show 
    
}
