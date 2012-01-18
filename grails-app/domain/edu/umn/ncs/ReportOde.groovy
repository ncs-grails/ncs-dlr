package edu.umn.ncs
import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent


/**
This class represents the "Operational Data Elements" (ODE) report. 
*/
class ReportOde {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true

	Integer period_id
	String staffFullName
	String staffLaborCategory
	String taskOde
	BigDecimal effortForMonth
	BigDecimal hoursForMonth
	BigDecimal weeklyHours
	Date dateCreated = new Date()
	String userCreated
	String appCreated = 'ncs-dlr'

	static constraints = {
		period_id()
		staffFullName(blank:false)
		staffLaborCategory(blank:false)
		taskOde(blank:false)
		effortForMonth(blank:false)
		hoursForMonth(blank:false)
		weeklyHours(blank:false)
		dateCreated(blank:false)
		userCreated(blank:false)
		appCreated(blank:false)
	}

	/** Trigger that saves old activity information to an auditLog instance, for tracking all changes to this class */
	def onDelete = { oldMap ->

		def now = new Date()

		String oldValue = "ODE file"
			oldValue += ", staffFullName: ${oldMap.staffFullName}"
			oldValue += ", staffLaborCategory: ${oldMap.staffLaborCategory}"
			oldValue += ", taskOde: ${oldMap.taskOde}"
			oldValue += ", effortForMonth: ${oldMap.effortForMonth}"
			oldValue += ", hoursForMonth: ${oldMap.hoursForMonth}"
			oldValue += ", weeklyHours: ${oldMap.weeklyHours}"
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated}"
		//println "PRINTLN SfrReportDomain.onDelete.oldValue: ${oldValue}"

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
