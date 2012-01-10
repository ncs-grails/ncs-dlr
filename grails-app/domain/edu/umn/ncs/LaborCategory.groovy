package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/** 
This class represents study staff labor categories (job titles).
*/
class LaborCategory {

	/** Flags this domain for auditing, on all updates and changes, using the auditable plugin */
	static auditable = true

	/** Description of the labor category) */
	String name
	/** Indicates whether this specific labor category is active. */
	Boolean obsolete = true
	/** Date this labor category was added to database */
	Date dateCreated = new Date()
	/** User account of person that added this labor category to the database */
	String userCreated
	/** Application, used by person, to add the labor category to the database. */
	String appCreated = 'ncs-dlr'

	/** Sets default string, for this domain, to LaborCategory "name." */	
	String toString() { name }

	/** Non-default constraints for this class 
	<dl>
		<dt>name</dt>
			<dd>cannot be blank, maximum length of 128 characters</dd>
		<dt>userCreated</dt>
			<dd>cannot be blank</dd>
		<dt>appCreated</dt>
			<dd>cannot be blank</dd>
	</dl>	
	*/	
	static constraints = {
		name(blank:false, maxSize:128)
		userCreated(blank:false)
		appCreated(blank:false)
	}

	/** Trigger that saves old activity information to an auditLog instance, for tracking all changes to this class */
	def onDelete = { oldMap ->

		def now = new Date()

		String oldValue = "Labor Category"
			oldValue += ", name: ${oldMap.name}"
			oldValue += ", obsolete: ${oldMap.obsolete}"
			oldValue += ", dateCreated: ${oldMap.dateCreated}"
			oldValue += ", userCreated: ${oldMap.userCreated}"
			oldValue += ", appCreated: ${oldMap.appCreated} "
		//println "PRINTLN LaborCategoryDomain.onDelete.oldValue: ${oldValue}"

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
