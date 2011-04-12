package edu.umn.ncs

class NotificationEmail {

    Date dateSent = new Date()
    String userSent

    String toString() {
        "Email sent on ${dateSent}"
    } 

    static belongsTo = [assignedEffort: AssignedEffort]

    static constraints = {

        assignedEffort()
        dateSent()
        userSent(blank:false)
        
    }
    
}
