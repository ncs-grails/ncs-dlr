import groovy.sql.Sql
import org.joda.time.LocalDateTime

/*
 * This is the National Children's Study, Direct Labor Reporting, *
 * Extract - Transform - Load (ETL) script to move data from the old MS-SQL database, *
 * to the new MySQL NCS database.
 */

/*
 * REQUIREMENTS:
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
 * HOW TO RUN SCRIPT:
 * 
 *    groovy NcsDlrEtl.groovy
 *    
 */

/*
 * CAUTION:
 *
 *    Make sure getMysqlConn() is pointing to the correct destinitation *
 *    (e.g., //localhost/ncs_dlr vs //sql.ncs.umn.edu/ncs_dlr).
 *
 */

/* Tables to pull from MsSql (sqv)
 * 
 * ncs_assignment_alert
 * ncs_dlr_report
 * ncs_effort
 * ncs_effort_assignment
 * ncs_effort_committed
 * ncs_labor_category
 * ncs_period
 * ncs_staff 
 * ncs_study_activity
 * ncs_task
 * ncs_task_etdlr
 * ncs_task_ode
 * 
 */

/* Tables NOT to pull from MsSql (sqv)
 * 
 * ncs_iplog
 * ncs_transacted_fields
 * ncs_transaction
 * 
 */

/* Tables in MySQL
 * 
 * application_log: application_log.id, version, app_created, event, log_date, source_ip_address, username
 * assigned_effort: id, version, app_created, assigned_effort, assigning_staff_id, commiting_staff_id, date_assigned, date_committed, labor_category_id, period_id, reporting_staff_id
 * audit_log: id, actor, class_name, date_created, event_name, last_updated, new_value, old_value, persisted_object_id, persisted_object_version, property_name, uri
 * labor_category: id, version, app_created, date_created, name, obsolete, user_created
 * notification_email: id, version, assigned_effort_id, date_sent, user_sent
 * project_info: id, version, contract_number, principal_investigator, sfr, start_date, sponsored_financial_reporting 
 * reported_effort: id, version, activity_id, app_created, assigned_effort_id, date_created, percent_effort, task_id, user_created
 * reporting_period: id, version, completed_report_date, period_date, prepared_date, reference_invoice_number
 * reporting_staff: id, version, app_created, date_created, email, first_name, is_test_account, labor_category_id, last_name, middle_init, reports_effort, user_created, username
 * study_activity: id, version, app_created, date_created, name, obsolete, user_created
 * study_task: id, version, app_created, date_created, etdlr_code, name, obsolete, ode_code, user_created
 *  
 */

// Export from ASP DLR backed by MS-SQL
// Import to Grails DLR backed by MySQL

class RunNcsDlrEtl {
	
	def msConn
	def myConn
	
	// Establish connection to source db
    /* Tables:
     	ncs_assignement_alert 		// need to add
        ncs_dlr_report
        ncs_effort
        ncs_effort_assignment
        ncs_effort_committed        
        ncs_labor_category
        ncs_period
        ncs_staff        
        ncs_study_activity
        ncs_task
        ncs_task_etdlr				// need to add
        ncs_task_ode				// need to add
    */
	def getMssqlConn() {
		
	    // requires jtds.jar
	    def db = [
	        username : 'ncs_labor',
	        password : 'jhg9dfoDS$&ezz4',
	        url : 'jdbc:jtds:sqlserver://sql.cccs.umn.edu/WebLookup',
	        driver : 'net.sourceforge.jtds.jdbc.Driver'
	    ]
	
	    return Sql.newInstance(db.url, db.username, db.password, db.driver)
	    
	}
	
	// Establish connection to destination db
	def getMysqlConn() {
		
	    // requires jtds.jar
	    def db = [
	        username : 'ncs-dlr',
	        password : 'ang1ahXiedohsieng5sheThi',
	        url : 'jdbc:mysql://localhost/ncs_dlr?noAccessToProcedureBodies=true&autoReconnect=true',
	        //url : 'jdbc:mysql://sql.ncs.umn.edu/ncs_dlr?useSSL=true&requireSSL=true&verifyServerCertificate=false&noAccessToProcedureBodies=true&autoReconnect=true'    ,
	        driver : 'com.mysql.jdbc.Driver'
	    ]
	    
	    return Sql.newInstance(db.url, db.username, db.password, db.driver)
		
	}
	
	def main() {
		
		//setup connection
	    msConn = getMssqlConn()
	    myConn = getMysqlConn()
		
		//import data 
		//importLaborCategories()
	    //importStaff()
	    //importReportingPeriods()
	    //importStudyActivities()
	    //importStudyTasks()
	    importAssignedEfforts()
	    //importReportedEfforts()
	    
	}
	
	/* LABOR CATEGORY SECTION **************************************************/
	
	def importLaborCategories() {
		
	    def query = """SELECT LaborID, Description, Obsolete, EnteredWhen, EnteredByWhom, EnteredByWhat
			FROM WebLookup.dbo.ncs_labor_category
			ORDER BY EnteredWhen"""
	          
	    msConn.eachRow(query) { row ->
	        
	        def laborId = row.LaborID
	        def name = row.Description
	        def obsolete = row.Obsolete
	        def dateCreated = row.EnteredWhen
	        def userCreated = row.EnteredByWhom
	        def appCreated = row.EnteredByWhat
	        
	        def laborCategory = getLaborCategory(laborId)			
	        if ( laborCategory ) {
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
	
	/* STAFF SECTION ***********************************************************/
	
	def importStaff() {
		
		//No need to pull IsHealthStudyStaff, IsAdministrator, ModifiedWhen, ModifiedByWhom.  IsActive (mssql) is equivalent to reportsEffort (mysql)
		//(mysql) reporting_staff: id, version, username, last_name, first_name, middle_init, labor_category_id, email, reports_effort, is_test_account, date_created, user_created, app_created
		
	    def query = """SELECT StaffID, UmnInternetId, LastName, FirstName, MiddleInit, LaborCategoryID, Email, IsActive, ReportsEffort, IsTestAccount, EnteredWhen, EnteredByWhom, EnteredByWhat
			FROM WebLookup.dbo.ncs_staff
			WHERE (UmnInternetId IS NOT NULL)
			ORDER BY EnteredWhen"""
	          
	    msConn.eachRow(query) { row ->
	        
	        def id = row.StaffID
	        def username = row.UmnInternetId
			def lastName = row.LastName
			def firstName = row.FirstName
			def middleInit = row.MiddleInit
			def laborCategoryId = row.LaborCategoryID
			def email = row.Email
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
	            staff = newStaff(id, username, lastName, firstName, middleInit, laborCategoryId, email, reportsEffort, isTestAccount, dateCreated, userCreated, appCreated)
	        }
			
	    }
		
	}

	def getStaff(String username) {
		if (username) {
			def query = """SELECT id, version, username, last_name, first_name, middle_init, labor_category_id, email, reports_effort, is_test_account, date_created, user_created, app_created
				FROM reporting_staff
				WHERE (username = ?);"""
			return myConn.firstRow(query, [username])
		} else {
			return null
		}
	}
	
	def getStaff(Long staffId) {
	    if (staffId) {
	        def query = """SELECT id, version, username, last_name, first_name, middle_init, labor_category_id, email, reports_effort, is_test_account, date_created, user_created, app_created
	            FROM reporting_staff
	            WHERE (id = ?);"""
	        return myConn.firstRow(query, [staffId])
			
	    } else {
	        return null
	    }		
	}
		
	def newStaff(id, username, lastName, firstName, middleInit, laborCategoryId, email, reportsEffort, isTestAccount, dateCreated, userCreated, appCreated) {
		def statement = """INSERT INTO reporting_staff 
			(id, version, username, last_name, first_name, middle_init, labor_category_id, email, reports_effort, is_test_account, date_created, user_created, app_created)
	        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"""		
	    def params = [id, 0, username, lastName, firstName, middleInit, laborCategoryId, email, reportsEffort, isTestAccount, dateCreated, userCreated, appCreated]	    
	    myConn.execute(statement, params)		    
	}
	
	/* REPORTING PERIOD ********************************************************/
	
	def importReportingPeriods() {

		// (mysql) reporting_period: id, version, period_date, reference_invoice_number, prepared_date, completed_report_date
	    def query = """SELECT p.PeriodID, p.RIN, p.AssignedMonth, p.AssignedYear, r.DatePrepared, r.CreatedDate AS CompletedReportDate
		FROM ncs_period p LEFT OUTER JOIN
	    	ncs_dlr_report r ON 'SFR 2706 - ' + CONVERT(varchar(16), p.RIN) = r.ReferenceInvoiceNumber
		GROUP BY p.PeriodID, p.RIN, p.AssignedMonth, p.AssignedYear, r.DatePrepared, r.CreatedDate
		ORDER BY p.AssignedYear, p.AssignedMonth"""
		
	    msConn.eachRow(query) { row ->
	    
	        def periodId = row.PeriodID
			
	        int year = row.AssignedYear
	        int month = row.AssignedMonth
			
	        def periodDateTime = new LocalDateTime(year, month, 1, 0, 0)
	        def periodDate = periodDateTime.toDateTime().toCalendar().time
			
	        def referenceInvoiceNumber = row.RIN
	        def preparedDate = row.DatePrepared
	        def completedReportDate = row.CompletedReportDate		
	        
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
	    def query = """SELECT id, version, period_date, reference_invoice_number, prepared_date, completed_report_date
	        FROM reporting_period
	        WHERE (period_date = ?);"""
	    return myConn.firstRow(query, [periodDate])
	}
	
	def newReportingPeriod(periodId, periodDate, referenceInvoiceNumber, preparedDate, completedReportDate) {
		
	    def statement = """INSERT INTO reporting_period 
	    	(id, version, period_date, reference_invoice_number, prepared_date, completed_report_date)
			VALUES (?, ?, ?, ?, ?, ?);"""
	    def params = [periodId, 0, periodDate, referenceInvoiceNumber, preparedDate, completedReportDate]
	    myConn.execute(statement, params) 
	}
	
/* STUDY ACTIVIY SECTION ***************************************************/
	
	def importStudyActivities() {
		
		// do not get "status" column
		def query = """SELECT ActivityID, Description, Obsolete, EnteredWhen, EnteredByWhom, EnteredByWhat
			FROM WebLookup.dbo.ncs_study_activity
			ORDER BY EnteredWhen"""
		
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
	    def query = """SELECT id, version, name, obsolete, date_created, user_created, app_created
	        FROM study_activity
	        WHERE (id = ?);"""		
	    return myConn.firstRow(query, [id])		
	}
	
	def newStudyActivity(id, name, obsolete, dateCreated, userCreated, appCreated) {
		
	    def statement = """INSERT INTO study_activity (id, version, name, obsolete, date_created, user_created, app_created)
			VALUES (?, ?, ?, ?, ?, ?, ?);"""		
	    def params = [id, 0, name, obsolete, dateCreated, userCreated, appCreated]
	    myConn.execute(statement, params) 
	}
	
	/* TASK SECTION ************************************************************/
	
	def importStudyTasks() {
		
		// do not get "status" column
	    def query = """SELECT TaskID, Description, Obsolete, etdlrCode, odeCode, EnteredWhen, EnteredByWhom, EnteredByWhat
			FROM WebLookup.dbo.ncs_task"""
		
	    msConn.eachRow(query) { row ->
	        
	        def taskId = row.TaskID
	        def name = row.Description
	        def obsolete = row.Obsolete
	        def etdlrCode = row.etdlrCode
	        def odeCode = row.odeCode
	        def dateCreated = row.EnteredWhen
	        def userCreated = row.EnteredByWhom
	        def appCreated = row.EnteredByWhat
	        
	        def studyTask = getStudyTask(taskId)			
	        if ( studyTask) {
	            println "Found Task: ${name}"
	        } else {
	            println "Creating Task: ${name}"
	            studyTask = newStudyTask(taskId, name, obsolete, etdlrCode, odeCode, dateCreated, userCreated, appCreated)
	        }
			
	    }
		
	}
	
	def getStudyTask(id) {
	    def query = """SELECT id, version, name, obsolete, etdlr_code, ode_code, date_created, user_created, app_created
	        FROM study_task
	        WHERE (id = ?);"""
	    return myConn.firstRow(query, [id])
	}
	
	def newStudyTask(id, name, obsolete, etdlrCode, odeCode, dateCreated, userCreated, appCreated) {
	    def statement = """INSERT INTO study_task (id, version, name, obsolete, etdlr_code, ode_code, date_created, user_created, app_created)
	                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"""
	    def params = [id, 0, name, obsolete, etdlrCode, odeCode, dateCreated, userCreated, appCreated]
	    myConn.execute(statement, params)
	}
	
	/* ASSIGNED EFFORT *********************************************************/

	def importAssignedEfforts() {

		//assigned_effort: id, version, period_id, date_assigned, assigning_staff_id, assigned_effort, reporting_staff_id, labor_category_id, date_committed, commiting_staff_id, app_created
				 
	    def query = """SELECT ea.AssignmentID, ea.PeriodID, ea.EnteredWhen, ea.EnteredByWhom, ea.AssignedEffort, ea.StaffID, s.LaborCategoryID, ec.DateCommitted, ea.EnteredByWhat
	        FROM ncs_effort_assignment AS ea LEFT OUTER JOIN
	            ncs_staff AS s ON ea.StaffID = s.StaffID LEFT OUTER JOIN
	            ncs_effort_committed AS ec ON ea.StaffID = ec.StaffID AND ea.PeriodID = ec.PeriodID"""
		
	    def query = """SELECT 
	    		a.AssignmentID, a.PeriodID, 
	    		a.EnteredWhen AS dateAssigned, a.EnteredByWhom AS assigningStaffId, a.AssignedEffort, 
	    		a.StaffID As reportingStaffId, s.LaborCategoryID, c.DateCommitted, a.EnteredByWhat, e.EnteredByWhom AS committingStaff
			FROM ncs_effort_assignment AS a LEFT OUTER JOIN
				(
					SELECT StaffID, PeriodID, EnteredByWhom
					FROM ncs_effort
					GROUP BY StaffID, PeriodID, EnteredByWhom
				) AS e ON a.StaffID = e.StaffID AND a.PeriodID = e.PeriodID LEFT OUTER JOIN
				ncs_effort_committed AS c ON e.PeriodID = c.PeriodID AND e.StaffID = c.StaffID AND a.StaffID = c.StaffID AND a.PeriodID = c.PeriodID LEFT OUTER JOIN
				ncs_staff AS s ON a.StaffID = s.StaffID"""
		  
		  
	
	    msConn.eachRow(query) { row ->
	        
	        def assignmentId = row.AssignmentID
			def periodId = row.PeriodID
			def dateAssigned = row.EnteredWhen
			def reportingStaffId = getStaff(row.StaffID)?.id
			def assigningStaffId = getStaff(row.EnteredByWhom)?.id ?: reportingStaffId
			def assignedEffort = row.AssignedEffort
			def laborCategoryId = row.LaborCategoryID ?: 25
			def dateCommitted = row.DateCommitted
			def commitingStaffId = null		        				
	        def appCreated = '/hsbin/ncs.asp'
	        
	        if (reportingStaffId) {
	            def assignedEffortInstance = getAssignedEffort(assignmentId)				
	            if ( assignedEffortInstance) {
	                println "Found Assigned Effort: ${assignmentId} : ${assignedEffort}"
	            } else {
	                println "Creating Assigned Effort: ${assignmentId} : ${assignedEffort}"
	                assignedEffortInstance = newAssignedEffort(
						assignmentId, periodId, dateAssigned, assigningStaffId, assignedEffort, 
						reportingStaffId, laborCategoryId, dateCommitted, commitingStaffId, appCreated)
	            }
	        } else {
	            println "Couldn't find staff id: ${row.StaffID}"
	        }
			
	    }
		
	}  
	
	def getAssignedEffort(id) {	
	    def query = """SELECT id, version, period_id, date_assigned, assigning_staff_id, assigned_effort, reporting_staff_id, labor_category_id, date_committed, commiting_staff_id, app_created
	        FROM assigned_effort
	        WHERE (id = ?);"""
	    return myConn.firstRow(query, [id])
	}
	
	def getAssignedEffort(staffId, periodId) {	
	    def query = """SELECT id, version, period_id, date_assigned, assigning_staff_id, assigned_effort, reporting_staff_id, labor_category_id, date_committed, commiting_staff_id, app_created
	        FROM assigned_effort
	        WHERE (period_id = ?) AND (reporting_staff_id = ?);"""	        
	    return myConn.firstRow(query, [periodId, staffId])
	}	
	
	//assigned_effort: id, version, period_id, date_assigned, assigning_staff_id, assigned_effort, reporting_staff_id, labor_category_id, date_committed, commiting_staff_id, app_created
	def newAssignedEffort(id, periodId, dateAssigned, assigningStaffId, assignedEffort, reportingStaffId, laborCategoryId, dateCommitted, commitingStaffId, appCreated) {
	
	    def statement = """INSERT INTO assigned_effort 
			(id, version, period_id, date_assigned, assigning_staff_id, assigned_effort, reporting_staff_id, labor_category_id, date_committed, commiting_staff_id, app_created )
	        VALUES (?, 0, ?, ?, ?, ?, ?, ?, ?, ?, ?);"""	        
	    def params = [id, periodId, dateAssigned, assigningStaffId, assignedEffort, reportingStaffId, laborCategoryId, dateCommitted, commitingStaffId, appCreated]	            
	    myConn.execute(statement, params)
	}
		
	/* REPORTED EFFORT  ********************************************************/
				
	def importReportedEfforts() {

		//reported_effort: id, version, assigned_effort_id, activity_id, task_id, percent_effort, date_created, user_created, app_created 
				
	    def query = """SELECT EffortID, StaffID, PeriodID, ActivityID, TaskID, PercentEffort, EnteredWhen, EnteredByWhom, EnteredByWhat
			FROM WebLookup.dbo.ncs_effort"""
	          
	    msConn.eachRow(query) { row ->
	
	        def effortId = row.EffortID
			def assignedEffortId = getAssignedEffort(row.StaffID, row.PeriodID)?.id			
	        def activityId = row.ActivityID
			def taskId = row.TaskID			
	        def percentEffort = row.PercentEffort
	        def dateCreated = row.EnteredWhen
	        def userCreated = row.EnteredByWhom
	        def appCreated = row.EnteredByWhat
	        
	        def reportedEffort = getReportedEffort(effortId)
			
	        if ( ! assignedEffortId ) {
	            println "!!! Couldn't Find assigned effort (ID: ${effortId}) for Staff: ${row.StaffID}, Period: ${row.PeriodID}"
	            sleep 3000
	        } else if ( reportedEffort ) {
	            println "Found Reported Effort: ${effortId}"
	        } else {			
	            println "Creating Reported Effort: ${effortId} for assigned effort: ${assignedEffortId}"				
	            laborCategory = newReportedEffort(effortId, assignedEffortId, activityId, taskId, percentEffort, dateCreated, userCreated,appCreated)				
	        }
			
	    }
		
	}
	
	def getReportedEffort(id) {		
	    def query = """SELECT id, version, assigned_effort_id, activity_id, task_id, percent_effort, date_created, user_created, app_created
			FROM reported_effort
	        WHERE (id = ?);"""		
	    return myConn.firstRow(query, [id])		
	}
	
	def newReportedEffort(id, assignedEffortId, activityId, taskId, percentEffort, dateCreated, userCreated, appCreated) {
		def statement = """INSERT INTO reported_effort
			(id, version, assigned_effort_id, activity_id, task_id, percent_effort, date_created, user_created, app_created)
			VALUES (?, 0, ?, ?, ?, ?,?, ?, ?);"""
	    def params = [id, assignedEffortId, activityId, taskId, percentEffort, dateCreated, userCreated, appCreated]	    
	    myConn.execute(statement, params) 	   
	}

}

def go = new RunNcsDlrEtl()
go.main()
