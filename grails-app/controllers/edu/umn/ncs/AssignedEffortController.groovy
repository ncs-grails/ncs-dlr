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
    
    
    def showPast = {
        
        def reportingStaffInstance
        
        if (params?.id) {
            reportingStaffInstance = ReportingStaff.read(params?.id)
        }
        println "PRINTLN AssignedEffortController.showPast.reportingStaffInstance: ${reportingStaffInstance}"
                
        def cRE = ReportedEffort.createCriteria()
        def reportedEffortInstance = cRE.list{
            and {
                assignedEffort{
                    period {
                        order("periodDate","desc")            
                    }
                    reportingStaff { 
                        idEq(reportingStaffInstance.id) 
                    }                    
                    isNotNull("dateCommitted")    
                    maxResults(12)
                }                                
            }
            order("percentEffort", "desc")
        }
        println "PRINTLN AssignedEffortController.showPast.reportedEffortInstance: ${reportedEffortInstance}"

        def reportedEffortList = []
        
        println "PRINTLN BEGIN LOOP"

        // Add records to Assigned Effort Instance
        reportedEffortInstance.eachWithIndex{ rs, i ->
            
            //println "PRINTLN AssignedEffortController.showPast.reportedEffortInstance.id: ${reportedEffortInstance.id}"            
            
            // Create record map
            def record = [:]
            
            // Get Assigned Effort Id
            record.assignedEffortId = rs.assignedEffort.id
            println "PRINTLN AssignedEffortController.showPast.record.assignedEffortId: ${record.assignedEffortId}"
                
            // Get Period Date
            record.period = rs.assignedEffort.period.periodDate          
            println "PRINTLN AssignedEffortController.showPast.record.period: ${record.period}"
                                    
            // Get Study Activity
            record.activity = rs.activity            
            println "PRINTLN AssignedEffortController.showPast.record.activity: ${record.activity}"
            
            // Get Study Task
            record.task = rs.task            
            println "PRINTLN AssignedEffortController.showPast.record.task: ${record.task}"
            
            // Get Reported Effort
            record.reportedEffort = rs.percentEffort            
            println "PRINTLN AssignedEffortController.showPast.record.reportedEffort: ${record.reportedEffort}"
                       
            reportedEffortList.add(record)                        
            
        }

        println "PRINTLN END LOOP"
        [
            reportedEffortList: reportedEffortList
        ]
        
        
        
    } //def showPast
    
    
}
