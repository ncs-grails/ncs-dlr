package edu.umn.ncs
import org.joda.time.*

class NotificationEmail {

    Date dateSent = new Date()
    String userSent

    static belongsTo = [assignedEffort: AssignedEffort]
   
    String toString() {
        "${dateSent}"
    }

    static constraints = {
        assignedEffort()
        dateSent()
        userSent(blank:false)
    }
    
}
