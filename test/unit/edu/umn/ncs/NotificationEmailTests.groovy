import java.util.Date;

package edu.umn.ncs

import grails.test.*

class NotificationEmailTests extends GrailsUnitTestCase {
	
    protected void setUp() {
				
        super.setUp()		
		//ensure validate() is invoked on NotificationEmail domain object 
		//mockForConstraintsTests(NotificationEmail)		
		//set up default email for testing
		//e = new NotificationEmail(assignedEffort: 10, dateSent:'11/30/2011', userSent: 'bxb')
		
    }

	void testConstraintUserSentBlankFalse() {
			
		def testEmail = new NotificationEmail(
			assignedEffort: 10, 
			dateSent:'11/30/2011', 
			userSent: 'bxb'
		)
		mockForConstraintsTests(NotificationEmail, [testEmail])		
		assertTrue testEmail.validate()
		
		
		
		
	}
		 
//		static constraints = {
//			assignedEffort()
//			dateSent()
//			userSent(blank:false)
//		}
		

	protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
    }
	
}
