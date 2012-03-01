package edu.umn.ncs
import org.joda.time.*
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/**
This class represents the MONTH and YEAR staff accrued effort to be reported in DLR.
*/
class ReportingPeriod {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true

	/** The number identifying the specific "Sponsor Financial Reporting" (SFR) report. Number is given to us by SFR*/	
	Integer referenceInvoiceNumber
	/** The period's date. Used to capture the periods month and year (the day always defaults to the first day of the month). */
	Date periodDate
	/** Date reports (e.g., SFR, ETDLR, ODE) are created. It is equivilant to the date the last effort was committed for a given period.  */
	Date preparedDate
	/** ???  */
	Date completedReportDate

	/** A reporting period can have many assigned efforts */
	static hasMany = [assignedEfforts: AssignedEffort]
	
	/** Read-only calculated fields  */
	static transients = ['year', 'month', 'previousPeriod', 'nextPeriod', 'effortDate']

	/** Returns the reporting period's year  */
	Integer getYear() {
		def localYear = new LocalDate(periodDate)
		return localYear.year
	}
	
	/** Returns the reporting period's month*/
	Integer getMonth() {
		def localMonth = new LocalDate(periodDate)
		return localMonth.monthOfYear
	}
	
	/** Returns the previous reporting period  */
	ReportingPeriod getPreviousPeriod() {

		ReportingPeriod.createCriteria().get{
			lt("periodDate", periodDate)
			maxResults(1)
			order("periodDate","desc")
		}

	}
    
	/** Returns the next reporting period */
	ReportingPeriod getNextPeriod() {

		ReportingPeriod.createCriteria().get{
			gt("periodDate", periodDate)
			maxResults(1)
			order("periodDate")
		}

	}

	/* Sets default string for this domain to the period's month and year */	
	String toString() { "${month}/${year}" }
    
	/** Non-default constraints for this class 
	<dl>
		<dt>referenceInvoiceNumber</dt>
			<dd>can be null</dd>
		<dt>preparedDate</dt>
			<dd>can be null</dd>
		<dt>completedReportDate</dt>
			<dd>can be null</dd>
	</dl>	
	*/
	static constraints = {
		referenceInvoiceNumber(nullable:true)
		periodDate()
		preparedDate(nullable:true)
		completedReportDate(nullable:true)
	}

	/** Trigger that saves old period information to an auditLog instance, for tracking all changes to this class */
	def onDelete = { oldMap ->
		
		def now = new Date()
		
		String oldValue = "ReportingPeriod"
			oldValue += ", referenceInvoiceNumber: ${oldMap.referenceInvoiceNumber}"
			oldValue += ", periodDate: ${oldMap.periodDate}"
			oldValue += ", preparedDate: ${oldMap.preparedDate}"
			oldValue += ", completedReportDate: ${oldMap.completedReportDate} "
		//println "PRINTLN ReportingPeriodDomain.onDelete.oldValue: ${oldValue}"
			
		String className = this.class.toString().replace('class ', '')
		//println "${now}\tAudit:DELETE::\t${oldValue}"

		def auditLogEventInstance = new AuditLogEvent(
			className: className,
			dateCreated: now,
			eventName: 'DELETE',
			lastUpdated: now,
			oldValue: oldValue,
			persistedObjectId: this.id,
			persistedObjectVersion: this.version
		)
		if ( ! auditLogEventInstance.save() ) {
			auditLogEventInstance.errors.each{
				println "${now}\tError Transacting DELETE:: \t ${it}"
			}
		}

	}
	
}
