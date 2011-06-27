package edu.umn.ncs

import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent

class ReportedEffort {

    static auditable = true

    StudyActivity activity
    StudyTask task
    BigDecimal percentEffort
    Date dateCreated = new Date()
    String userCreated
    String appCreated  = 'ncs-dlr'

    def onDelete = { oldMap ->
        
        def now = new Date()

        // TODO: expand on items to delete
        String oldValue = "Deleting Reported Effort for assignedEffort.id ${oldMap.assignedEffort.id}, reportedEffort.id "

        String className = this.class.toString().replace('class ', '')
        //println "${now}\tAudit:DELETE::\t${oldValue}"

        def auditLogEventInstance = new AuditLogEvent(className: className,
            dateCreated: now,
            eventName: 'DELETE',
            lastUpdated: now,
            oldValue: oldValue,
            persistedObjectId: this.id,
            persistedObjectVersion: this.version)
            if ( ! auditLogEventInstance.save() ) {
            auditLogEventInstance.errors.each{
                    println "${now}\tError Transacting DELETE:: \t ${it}"
            }
        }
            
    } //def onDelete 

    static belongsTo = [assignedEffort: AssignedEffort]

    static constraints = {
        assignedEffort()
        activity()
        task()
        percentEffort(max:1.0, scale:5)
        dateCreated()
        userCreated(blank:false)
        appCreated(blank:false)

    }

}