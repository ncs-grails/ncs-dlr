package edu.umn.ncs
import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

/**
 * edu.umn.ncs.ApplicationLog is a domain class for the creation of log events
 * tied to this application that is to be stored in the database
 * (as opposed to the standard Java/Grails logging mechanisms)
 */
class ApplicationLog {

	/**
   	 * The date the log event occurred
	 */
    Date logDate = new Date()
	/**
	 * The source IP address of the logged event
	 */
    String sourceIpAddress
	/**
	 * The username associated with the authenticated connection (if available)
	 */
    String username
	/**
	 * A string describing the event being logged
	 */
    String event
	/**
	 * The application that created the log event
	 */
    String appCreated = 'ncs-dlr'

	/**
	 * The default toString() method for the log event
	 * outputs the log item as date, source IP, username, and event description,
	 * tab delimeted
	 * <pre>${logDate}\t${sourceIpAddress}\t${username}\t${event}</pre>
	 */
	String toString() {
       "${logDate}\t${sourceIpAddress}\t${username}\t${event}"
    }

	/**
	 * Don't allow application logs to be deleted by throwing an exception before
	 * the object delete method is called
	 */
	def beforeDelete = { throw DeleteApplicationLogNotAllowedException }

	/**
	 * Non-default constraints applied to the domain class:
	 * <ul>
	 *   <li>username is nullable
	 * </ul>
	 */
    static constraints = {
        logDate()
        sourceIpAddress()
        username(nullable:true)
        event()
        appCreated()
    }
}
