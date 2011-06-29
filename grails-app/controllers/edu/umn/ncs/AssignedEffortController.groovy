package edu.umn.ncs

class AssignedEffortController {

    def index = {
        
        println "PRINTLN ASSIGNED EFFORT CONTROLLER > INDEX --------------------"                
        println "PRINTLN AssignedEffortController.index.params: ${params}"
        
        def assignedEffortInstance = AssignedEffort.read(params?.id)
        println "PRINTLN AssignedEffortController.index.assignedEffortInstance: ${assignedEffortInstance}"
            
        [ assignedEffortInstance: assignedEffortInstance ]        

    } // def index
 
    def showCurrent = {

        println "PRINTLN ASSIGNED EFFORT CONTROLLER > SHOW CURRENT -------------"                
        println "PRINTLN AssignedEffortController.showCurrent.params: ${params}"

        // get perameters
        def assignedEffortInstance
        def reportingPeriodInstance
        def reportingStaffInstance
        
        if (params?.id) {
            assignedEffortInstance = AssignedEffort.read(params?.id)
            reportingStaffInstance = assignedEffortInstance.reportingStaff
            reportingPeriodInstance = assignedEffortInstance.period
        }
        println "PRINTLN AssignedEffortController.showCurrent.assignedEffortInstance: ${assignedEffortInstance}"
        println "PRINTLN AssignedEffortController.showCurrent.reportingStaffInstance: ${reportingStaffInstance}"
        println "PRINTLN AssignedEffortController.showCurrent.reportingPeriodInstance: ${reportingPeriodInstance}"
        
        // Get REPORTED EFFORT total
        def reportedEffortTotal
        if ( assignedEffortInstance ) {            
            def c = ReportedEffort.createCriteria()
            reportedEffortTotal = c.get {
                eq("assignedEffort", assignedEffortInstance)
                projections {
                    sum("percentEffort")
                }
            }
            println "PRINTLN AssignedEffortController.showCurrent.reportedEffortTotal: ${reportedEffortTotal}"
        }
        
        // Get list of REPORTED EFFORT
        def c2 = ReportedEffort.createCriteria()        
        def reportingEffortInstanceList = c2.list{
            eq("assignedEffort", assignedEffortInstance) 
            order("activity", "asc")
            order("task", "asc")
        }
        println "PRINTLN AssignedEffortController.showCurrent.reportingEffortInstanceList: ${reportingEffortInstanceList}"

        // Add records to Reported Effort Instance
        def reportedEffortList = []

        reportingEffortInstanceList.eachWithIndex{ rs, i -> 
            
            // Create record map
            def record = [:]
            
            // Get effort id
            record.reportedEffortId = rs.id
            //println "PRINTLN AssignedEffortController.showCurrent.record.reportedEffortId: ${record.reportedEffortId}"            
            
            // Get study activity
            record.studyActivity = rs.activity
            //println "PRINTLN AssignedEffortController.showCurrent.record.studyActivity: ${record.studyActivity}"
            
            // Get study task
            record.studyTask = rs.task
            //println "PRINTLN AssignedEffortController.showCurrent.record.studyTask: ${record.studyTask}"
            
            // Get percentEffort
            record.percentEffort = rs.percentEffort
            //println "PRINTLN AssignedEffortController.showCurrent.record.percentEffort: ${record.percentEffort}"
            
            //Get DateCreated
            record.dateCreated = rs.dateCreated
            //println "PRINTLN AssignedEffortController.showCurrent.record.dateCreated: ${record.dateCreated}"
            
            reportedEffortList.add(record)
            
        } //reportingStaffInstanceList.eachWithIndex{ rs, i ->        
        
            
        println "PRINTLN AssignedEffortController.showCurrent.assignedEffortInstance.dateCommitted: ${assignedEffortInstance.dateCommitted}"            

        [
            reportingPeriodInstance:reportingPeriodInstance, 
            reportingStaffInstance:reportingStaffInstance,
            assignedEffortInstance: assignedEffortInstance,
            reportedEffortTotal: reportedEffortTotal,
            reportedEffortList: reportedEffortList,
            isForm: params?.isForm
        ]
       
    } //def showCurrent
    
    
    def showPast = {
        
        println "PRINTLN ASSIGNED EFFORT CONTROLLER > SHOWPAST -----------------"                
        println "PRINTLN AssignedEffortController.showPast.params: ${params}"
        
        def reportingStaffInstance        
        if (params?.id) {
            reportingStaffInstance = ReportingStaff.read(params?.id)
        }
        println "PRINTLN AssignedEffortController.showPast.reportingStaffInstance: ${reportingStaffInstance}"
                
        def cre = ReportedEffort.createCriteria()
        def reportedEffortInstance = cre.list{
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
        
        // Add records to Assigned Effort Instance
        reportedEffortInstance.eachWithIndex{ rs, i ->
            
            //println "PRINTLN AssignedEffortController.showPast.reportedEffortInstance.id: ${reportedEffortInstance.id}"            
            
            // Create record map
            def record = [:]
            
            // Get Assigned Effort Id
            record.assignedEffortId = rs.assignedEffort.id
            //println "PRINTLN AssignedEffortController.showPast.record.assignedEffortId: ${record.assignedEffortId}"
                
            // Get Period Date
            record.period = rs.assignedEffort.period.periodDate          
            //println "PRINTLN AssignedEffortController.showPast.record.period: ${record.period}"
                                    
            // Get Study Activity
            record.activity = rs.activity            
            //println "PRINTLN AssignedEffortController.showPast.record.activity: ${record.activity}"
            
            // Get Study Task
            record.task = rs.task            
            //println "PRINTLN AssignedEffortController.showPast.record.task: ${record.task}"
            
            // Get Reported Effort
            record.reportedEffort = rs.percentEffort
            //println "PRINTLN AssignedEffortController.showPast.record.reportedEffort: ${record.reportedEffort}"
                       
            reportedEffortList.add(record)                        
            
        }
        
        [ reportedEffortList: reportedEffortList ]
                
    } //def showPast
    
    
}
