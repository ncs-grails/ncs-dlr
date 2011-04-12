package edu.umn.ncs

class ApplicationLog {

    Date logDate = new Date()
    String sourceIpAddress
    String username
    String event
    String appCreated = 'ncs-dlr'

    String toString() {
       "${logDate}\t${sourceIpAddress}\t${username}\t${event}"
    }

    static constraints = {

        logDate()
        sourceIpAddress()
        username(nullable:true)
        event()
        appCreated()

    }
    
}
