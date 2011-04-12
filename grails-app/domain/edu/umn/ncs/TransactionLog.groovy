package edu.umn.ncs

class TransactionLog {

    String className
    String attributeName
    String oldValue
    TransactionLog parent
    Date dateTransacted = new Date()
    String username
    String appName = 'ncs-dlr'

    static constraints = {

        className()
        attributeName()
        oldValue()
        parent(nullable:true)
        dateTransacted()
        username(nullable:true)
        appName()

    }

}
