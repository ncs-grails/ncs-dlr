package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class AssignedEffort {

	static auditable = true

	LaborCategory laborCategory
	BigDecimal assignedEffort
	Date dateAssigned = new Date()
	ReportingStaff assigningStaff
	String appCreated = 'ncs-dlr'       
	Date dateCommitted
	ReportingStaff commitingStaff

	static belongsTo = [reportingStaff: ReportingStaff, period: ReportingPeriod]
	static hasMany = [emails: NotificationEmail, reportedEffort: ReportedEffort]

	def transients = ['assignedEffortConverted']

	BigDecimal getAssignedEffortConverted() {        
		return assignedEffort * 100
	}

	void setAssignedEffortConverted(BigDecimal assignedEffortConverted) {        
		percentEffort = assignedEffortConverted / 100.00
	}

	void setAssignedEffortConverted(String assignedEffortConvertedString) {
		try {
			assignedEffortConverted = assignedEffortConvertedString.toBigDecimal()
		} finally {
			assignedEffort = assignedEffortConverted / 100.00
		}
	}

	String toString() { assignedEffort }

	static constraints = {
		reportingStaff()
		laborCategory()
		period()
		assignedEffort(max:1.0, scale:5)
		dateAssigned()
		assigningStaff(blank:false)
		appCreated(blank:false)
		dateCommitted(nullable:true)
		commitingStaff(nullable:true)
	}

	/** Trigger that saves old activity information to an auditLog instance, for tracking all changes to this class */
	def onDelete = { oldMap ->

		def now = new Date()

		String oldValue = "Assigned Effort associated with"
			oldValue += " reportingStaff.id ${oldMap.reportingStaff.id}"
			oldValue += " & period.id ${oldMap.period.id}"
			oldValue += ", laborCategory.id: ${oldMap.laborCategory.id}"
			oldValue += ", assignedEffort: ${oldMap.assignedEffort}"
			oldValue += ", dateAssigned: ${oldMap.dateAssigned}"
			oldValue += ", assigningStaff.id: ${oldMap.assigningStaff.id}"
			oldValue += ", appCreated: ${oldMap.appCreated}"
			oldValue += ", dateCommitted: ${oldMap.dateCommitted}"
			oldValue += ", commitingStaff.id: "
			oldValue += "${oldMap.commitingStaff}" ? "${null} " : "${oldMap.commitingStaff.id} "
			//oldValue += ", commitingStaff.id: ${oldMap.commitingStaff.id} "

		String className = this.class.toString().replace('class ', '')

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
