import groovy.sql.Sql
import org.joda.time.LocalDateTime

/*
 * This is the National Children's Study, Direct Labor 
 * Reporting, Extract - Transform - Load script
 * to move data from the old MS-SQL database, to the new
 * MySQL NCS database.
 */

/*
 * Requirements:
 * You will need joda-time, jtds, and mysql JARs in your groovy classpath.
 * The easiest way to do this on ubuntu linux is:

	sudo apt-get install libjoda-time-java libjtds-java libmysql-java
	mkdir -p ~/.groovy/lib
	cd ~/.groovy/lib
	ln -s /usr/share/java/joda-time.jar .
	ln -s /usr/share/java/jtds.jar .
	ln -s /usr/share/java/mysql.jar .
	
 */

/*
 * Usage:
 * 
 *    groovy NcsDlrEtl.groovy
 *    
 */

// Export from ASP DLR backed by MS-SQL
// Import to Grails DLR backed by MySQL

def msConn
def myConn

def getMssqlConn() {
    /* Tables:
        ncs_labor_category
        ncs_period
        ncs_study_activity
        ncs_task
        ncs_staff

        ncs_effort
        ncs_effort_assignment
        ncs_effort_committed

        ncs_tool_access
        ncs_approval_status
        ncs_dlr_report
    */


    // requires jtds.jar
    def db = [
        username : 'ncs_labor',
        password : 'jhg9dfoDS$&ezz4',
        url : 'jdbc:jtds:sqlserver://sql.cccs.umn.edu/WebLookup',
        driver : 'net.sourceforge.jtds.jdbc.Driver'
    ]

    return Sql.newInstance(db.url, db.username, db.password, db.driver)
    
}

def getMysqlConn() {
    // requires jtds.jar
    def db = [
        username : 'ncs-dlr',
        password : 'ang1ahXiedohsieng5sheThi',
        //url : 'jdbc:mysql://localhost/ncs_dlr?noAccessToProcedureBodies=true&autoReconnect=true'    ,
        url : 'jdbc:mysql://sql.ncs.umn.edu/ncs_dlr?useSSL=true&requireSSL=true&verifyServerCertificate=false&noAccessToProcedureBodies=true&autoReconnect=true'    ,
        driver : 'com.mysql.jdbc.Driver'
    ]
    
    return Sql.newInstance(db.url, db.username, db.password, db.driver)
}

def main() {
    msConn = getMssqlConn()
    myConn = getMysqlConn()
    
    importLaborCategories()
        
    importStudyActivities()
    
    importStudyTasks()
    
    importStaff()
    
    importReportingPeriods()
    
    importAssignedEfforts()
    
    importReportedEfforts()
    
}

/* LABOR CATEGORY SECTION */

def importLaborCategories() {
    def query = """SELECT LaborID
      ,Description
      ,Obsolete
      ,EnteredWhen
      ,EnteredByWhom
      ,EnteredByWhat
  FROM WebLookup.dbo.ncs_labor_category"""
          
    msConn.eachRow(query) { row ->
        
        def laborId = row.LaborID
        def name = row.Description
        def obsolete = row.Obsolete
        def dateCreated = row.EnteredWhen
        def userCreated = row.EnteredByWhom
        def appCreated = row.EnteredByWhat
        
        def laborCategory = getLaborCategory(laborId)
        if ( laborCategory) {
            println "Found Labor Category: ${name}"
        } else {
            println "Creating Labor Category: ${name}"
            laborCategory = newLaborCategory(laborId, name, obsolete, dateCreated, userCreated, appCreated)
        }
    }
}

def getLaborCategory(id) {
    def query = """SELECT id, version, app_created, date_created, name, obsolete, user_created
        FROM labor_category
        WHERE (id = ?);"""
    return myConn.firstRow(query, [id])
}

def newLaborCategory(id, name, obsolete, dateCreated, userCreated, appCreated) {
    def statement = """INSERT INTO `ncs_dlr`.`labor_category`
                    (id, version, name, obsolete, date_created, user_created, app_created)
                    VALUES (?, ?, ?, ?, ?, ?, ?);"""
    def params = [id, 0, name, obsolete, dateCreated, userCreated, appCreated]
    
    myConn.execute(statement, params) 
   
}

/* REPORTING PERIOD */
def importReportingPeriods() {
    
    def query = """SELECT ncs_period.PeriodID, 
            ncs_period.RIN, 
            ncs_period.AssignedMonth, 
            ncs_period.AssignedYear, 
            ncs_dlr_report.DatePrepared, 
            ncs_dlr_report.CreatedDate AS ReportDate
        FROM ncs_period LEFT OUTER JOIN
            ncs_dlr_report ON 'SFR 2706 - ' + CONVERT(varchar(16), ncs_period.RIN) = ncs_dlr_report.ReferenceInvoiceNumber
        GROUP BY ncs_period.PeriodID, 
            ncs_period.RIN, 
            ncs_period.AssignedMonth, 
            ncs_period.AssignedYear, 
            ncs_dlr_report.DatePrepared, 
            ncs_dlr_report.CreatedDate
        ORDER BY ncs_period.AssignedYear, ncs_period.AssignedMonth"""
    msConn.eachRow(query) { row ->
    
        def periodId = row.PeriodID
        int year = row.AssignedYear
        int month = row.AssignedMonth
        def periodDateTime = new LocalDateTime(year, month, 1, 0, 0)
        def periodDate = periodDateTime.toDateTime().toCalendar().time
        def referenceInvoiceNumber = row.RIN
        def preparedDate = row.DatePrepared
        def completedReportDate = row.ReportDate
        
        def reportingPeriod = getReportingPeriod(periodDate)
        if (reportingPeriod) {
            println "Found Period for ${periodDate} (${month}/${year}), RIN: ${reportingPeriod.reference_invoice_number}"
        } else {
            println "Creating Period for ${periodDate}."
            reportingPeriod = newReportingPeriod(periodId, periodDate, referenceInvoiceNumber, preparedDate, completedReportDate)
        }
    }
}

def getReportingPeriod(periodDate) {
    def query = """SELECT id, version, completed_report_date, period_date, prepared_date, reference_invoice_number
        FROM reporting_period
        WHERE (period_date = ?);"""
    return myConn.firstRow(query, [periodDate])
}

def newReportingPeriod(periodId, periodDate, referenceInvoiceNumber, preparedDate, completedReportDate) {
    def statement = """INSERT INTO reporting_period
                    (id, version, period_date, reference_invoice_number, prepared_date, completed_report_date)
                    VALUES (?, 0, ?, ?, ?, ?);"""
    def params = [periodId, periodDate, referenceInvoiceNumber, preparedDate, completedReportDate]
    
    myConn.execute(statement, params) 
   
}

/* STUDY ACTIVIY SECTION */
def importStudyActivities() {
    def query = """SELECT ActivityID
            ,Description
            ,EnteredWhen
            ,EnteredByWhom
            ,EnteredByWhat
            ,Obsolete
        FROM WebLookup.dbo.ncs_study_activity"""
    msConn.eachRow(query) { row ->
        
        def activityId = row.ActivityID
        def name = row.Description
        def obsolete = row.Obsolete
        def dateCreated = row.EnteredWhen
        def userCreated = row.EnteredByWhom
        def appCreated = row.EnteredByWhat
        
        def studyActivity = getStudyActivity(activityId)
        if ( studyActivity) {
            println "Found Activity: ${name}"
        } else {
            println "Creating Activity: ${name}"
            studyActivity = newStudyActivity(activityId, name, obsolete, dateCreated, userCreated, appCreated)
        }
    }
}


def getStudyActivity(id) {
    def query = """SELECT id, version, app_created, date_created, name, obsolete, user_created
        FROM study_activity
        WHERE (id = ?);"""
    return myConn.firstRow(query, [id])
}

def newStudyActivity(id, name, obsolete, dateCreated, userCreated, appCreated) {
    def statement = """INSERT INTO study_activity
                    (id, version, name, obsolete, date_created, user_created, app_created)
                    VALUES (?, ?, ?, ?, ?, ?, ?);"""
    def params = [id, 0, name, obsolete, dateCreated, userCreated, appCreated]
    
    myConn.execute(statement, params) 
   
}

/* TASK SECTION */
def importStudyTasks() {
    def query = """SELECT TaskID
        ,Description
        ,Obsolete
        ,EnteredWhen
        ,EnteredByWhom
        ,EnteredByWhat
        ,etdlrCode
        ,odeCode
            FROM WebLookup.dbo.ncs_task"""
    msConn.eachRow(query) { row ->
        
        def taskId = row.TaskID
        def name = row.Description
        def obsolete = row.Obsolete
        def dateCreated = row.EnteredWhen
        def userCreated = row.EnteredByWhom
        def appCreated = row.EnteredByWhat
        // Not Used
        def etDlrCode = row.etdlrCode
        def odeCode = row.odeCode
        
        def studyTask = getStudyTask(taskId)
        if ( studyTask) {
            println "Found Activity: ${name}"
        } else {
            println "Creating Activity: ${name}"
            studyTask = newStudyTask(taskId, name, obsolete, dateCreated, userCreated, appCreated)
        }
    }
}

def getStudyTask(id) {
    def query = """SELECT id, version, app_created, date_created, name, obsolete, user_created
        FROM study_task
        WHERE (id = ?);"""
    return myConn.firstRow(query, [id])
}

def newStudyTask(id, name, obsolete, dateCreated, userCreated, appCreated) {
    def statement = """INSERT INTO study_task
                    (id, version, name, obsolete, date_created, user_created, app_created)
                    VALUES (?, ?, ?, ?, ?, ?, ?);"""
    def params = [id, 0, name, obsolete, dateCreated, userCreated, appCreated]
    
    myConn.execute(statement, params)    
}
/* STAFF SECTION */
def importStaff() {
    def query = """SELECT StaffID
          ,FirstName
          ,MiddleInit
          ,LastName
          ,LaborCategoryID
          ,Email
          ,UmnInternetId
          ,IsActive
          ,IsHealthStudyStaff
          ,IsAdministrator
          ,IsTestAccount
          ,ReportsEffort
          ,EnteredWhen
          ,EnteredByWhom
          ,EnteredByWhat
          ,ModifiedWhen
          ,ModifiedByWhom
      FROM WebLookup.dbo.ncs_staff
      WHERE (UmnInternetId IS NOT NULL)"""
          
    msConn.eachRow(query) { row ->
        
        def id = row.StaffID
        def username = row.UmnInternetId
        def email = row.Email
        def firstName = row.FirstName
        def middleInit = row.MiddleInit
        def lastName = row.LastName
        def laborCategoryId = row.LaborCategoryID
        def reportsEffort = row.IsActive
        def isTestAccount = row.IsTestAccount
        def dateCreated = row.EnteredWhen
        def userCreated = row.EnteredByWhom
        def appCreated = row.EnteredByWhat
        
        
        def staff = getStaff(username)
        if ( staff) {
            println "Found Staff: ${username}"
        } else {
            println "Creating Staff: ${username}"
            staff = newStaff(id, username, email, firstName, middleInit, lastName, 
            laborCategoryId, reportsEffort, isTestAccount, 
            dateCreated, userCreated, appCreated)
        }
    }
}

def getStaff(Long staffId) {

    if (staffId) {
        def query = """SELECT id, version, 
                username, 
                email, 
                first_name, 
                middle_init, 
                last_name, 
                labor_category_id, 
                reports_effort, 
                is_test_account, 
                date_created, 
                user_created, 
                app_created
            FROM reporting_staff
            WHERE (id = ?);"""
        return myConn.firstRow(query, [staffId])
    } else {
        return null
    }
}


def getStaff(String username) {
    if (username) {
        def query = """SELECT id, version, 
                username, 
                email, 
                first_name, 
                middle_init, 
                last_name, 
                labor_category_id, 
                reports_effort, 
                is_test_account, 
                date_created, 
                user_created, 
                app_created
            FROM reporting_staff
            WHERE (username = ?);"""
        return myConn.firstRow(query, [username])
    } else {
        return null
    }
}

def newStaff(id, username, email, firstName, middleInit, lastName, 
            laborCategoryId, reportsEffort, isTestAccount, 
            dateCreated, userCreated, appCreated) {
    def statement = """INSERT INTO reporting_staff
                        (id, version, username, 
                        email, first_name, middle_init, 
                        last_name, labor_category_id, reports_effort, 
                        is_test_account, date_created, user_created, 
                        app_created)
                    VALUES (?, 0, ?, 
                    ?, ?, ?, 
                    ?, ?, ?, 
                    ?, ?, ?, 
                   ?);"""
    def params = [id, username, email, firstName, middleInit, lastName, 
            laborCategoryId, reportsEffort, isTestAccount, 
            dateCreated, userCreated, appCreated]
    
    myConn.execute(statement, params)    
}

/* ASSIGNED EFFORT */
def importAssignedEfforts() {

    def query = """SELECT ea.AssignmentID, 
            ea.StaffID, 
            ea.PeriodID, 
            ea.AssignedEffort, 
            ea.EnteredWhen, 
            ea.EnteredByWhom, 
            ea.EnteredByWhat, 
            ec.DateCommitted, 
            ns.LaborCategoryID
        FROM ncs_effort_assignment AS ea LEFT OUTER JOIN
            ncs_staff AS ns ON ea.StaffID = ns.StaffID LEFT OUTER JOIN
            ncs_effort_committed AS ec ON ea.StaffID = ec.StaffID AND ea.PeriodID = ec.PeriodID """

    msConn.eachRow(query) { row ->
        
        def assignmentId = row.AssignmentID
        def appCreated = 'ncs.asp'
        def assignedEffort = row.AssignedEffort
        // Make sure they are an actual staff id
        def reportingStaffId = getStaff(row.StaffID)?.id 
        // we'll have to look this one up
        def assigningStaffId = getStaff(row.EnteredByWhom)?.id ?: reportingStaffId
        // this was never saved.
        def commitingStaffId = null
        def dateAssigned = row.EnteredWhen
        def dateCommitted = row.DateCommitted
        def laborCategoryId = row.LaborCategoryID ?: 25
        def periodId = row.PeriodID
        
        if (reportingStaffId) {
            def assignedEffortInstance = getAssignedEffort(assignmentId)
            if ( assignedEffortInstance) {
                println "Found Assigned Effort: ${assignmentId} : ${assignedEffort}"
            } else {
                println "Creating Assigned Effort: ${assignmentId} : ${assignedEffort}"
                assignedEffortInstance = newAssignedEffort(assignmentId,
                    appCreated,
                    assignedEffort,
                    assigningStaffId,
                    commitingStaffId,
                    dateAssigned,
                    dateCommitted,
                    laborCategoryId,
                    periodId,
                    reportingStaffId)
            }
        } else {
            println "Couldn't find staff id: ${row.StaffID}"
        }
    }
}  

def getAssignedEffort(id) {

    def query = """SELECT 
            id,
            version,
            app_created,
            assigned_effort,
            assigning_staff_id,
            commiting_staff_id,
            date_assigned,
            date_committed,
            labor_category_id,
            period_id,
            reporting_staff_id
        FROM assigned_effort
        WHERE (id = ?);"""
        
    return myConn.firstRow(query, [id])
}

def getAssignedEffort(staffId, periodId) {

    def query = """SELECT 
            id,
            version,
            app_created,
            assigned_effort,
            assigning_staff_id,
            commiting_staff_id,
            date_assigned,
            date_committed,
            labor_category_id,
            period_id,
            reporting_staff_id
        FROM assigned_effort
        WHERE (period_id = ?) AND (reporting_staff_id = ?);"""
        
    return myConn.firstRow(query, [periodId, staffId])
}


def newAssignedEffort(id,
            appCreated,
            assignedEffort,
            assigningStaffId,
            commitingStaffId,
            dateAssigned,
            dateCommitted,
            laborCategoryId,
            periodId,
            reportingStaffId) {

    def statement = """INSERT INTO assigned_effort 
            (id,
            version,
            app_created,
            assigned_effort,
            assigning_staff_id,
            commiting_staff_id,
            date_assigned,
            date_committed,
            labor_category_id,
            period_id,
            reporting_staff_id)
        VALUES (?, 0, ?, ?, ?, ?, ?, ?, ?, ?, ?);"""
        
    def params = [id,
            appCreated,
            assignedEffort,
            assigningStaffId,
            commitingStaffId,
            dateAssigned,
            dateCommitted,
            laborCategoryId,
            periodId,
            reportingStaffId]
            
    myConn.execute(statement, params)
}


/* REPORTED EFFORT  */
def importReportedEfforts() {
    def query = """SELECT EffortID
              ,StaffID
              ,PeriodID
              ,ActivityID
              ,TaskID
              ,PercentEffort
              ,EnteredWhen
              ,EnteredByWhom
              ,EnteredByWhat
          FROM WebLookup.dbo.ncs_effort"""
          
    msConn.eachRow(query) { row ->

        def effortId = row.EffortID
        def activityId = row.ActivityID
        
        def assignedEffortId = getAssignedEffort(row.StaffID, row.PeriodID)?.id
        
        
        def percentEffort = row.PercentEffort
        def taskId = row.TaskID
        def dateCreated = row.EnteredWhen
        def userCreated = row.EnteredByWhom
        def appCreated = row.EnteredByWhat
        
        def laborCategory = getReportedEffort(effortId)
        if ( ! assignedEffortId ) {
            println "!!! Couldn't Find assigned effort (ID: ${effortId}) for Staff: ${row.StaffID}, Period: ${row.PeriodID}"
            sleep 3000
        } else if ( laborCategory) {
            println "Found Reported Effort: ${effortId}"
        } else {
            println "Creating Reported Effort: ${effortId} for assigned effort: ${assignedEffortId}"
            laborCategory = newReportedEffort(effortId, activityId, 
                assignedEffortId, 
                percentEffort, 
                taskId, 
                dateCreated, 
                userCreated, 
                appCreated)
        }
    }
}

def getReportedEffort(id) {
    def query = """SELECT id, version, activity_id, assigned_effort_id, percent_effort, task_id, date_created, user_created, app_created
        FROM reported_effort
        WHERE (id = ?);"""
    return myConn.firstRow(query, [id])
}

def newReportedEffort(id, 
        activityId, 
        assignedEffortId, 
        percentEffort, 
        taskId, 
        dateCreated, 
        userCreated, 
        appCreated) {
    def statement = """INSERT INTO reported_effort
                    (id, version, activity_id, assigned_effort_id, percent_effort, task_id, date_created, user_created, app_created)
                    VALUES (?, 0, ?, 
                        ?, ?, ?, 
                        ?, ?, ?);"""
    def params = [id, 
        activityId, 
        assignedEffortId, 
        percentEffort, 
        taskId, 
        dateCreated, 
        userCreated, 
        appCreated]
    
    myConn.execute(statement, params) 
   
}


main()