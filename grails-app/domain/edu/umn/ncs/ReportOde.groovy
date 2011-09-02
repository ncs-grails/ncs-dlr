package edu.umn.ncs
import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ReportOde {

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
		period_id(nullable:false)
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

	} //def onDelete
	
}
