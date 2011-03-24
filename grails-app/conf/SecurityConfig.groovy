security {

	// see DefaultSecurityConfig.groovy for all settable/overridable properties
	active = true

	// These are only used if you are not using
	// Active Directory to fill in user/role info
	//loginUserDomainClass = "User"
	//authorityDomainClass = "Role"

	useRequestMapDomainClass = false
	useControllerAnnotations = true

	forceHttps = true
	channelConfig.secure = ['/**']

	// Used for UMN Cookie Auth
	useUmnCookie = true
	umn {
		authServer = 'www.umn.edu'
		validationServer = 'x500.umn.edu'
		moduleName = 'WEBCOOKIE'
		requiredvalidationLevel = 30
		insufficientValidationLevelMessage = 4
		validCookieTimeInSeconds = 60 * 60
	}

	useUmnadRoles = true
	umnad {
		// localhost runs an stunnel4 that redirects
		// ldap://localhost -> ldaps://ad.umn.edu
		ldapUri = 'ldap://localhost'
		// enter the UMNAD grails service  username here
		ldapUserDn = 'umnad-username@ad.umn.edu'
		// enter the password here
		ldapUserPw = 'randompassword'
		ldapPeopleBaseDn = 'OU=People,DC=ad,DC=umn,DC=edu'
		// change this to the OU you have your groups in
		ldapGroupsBaseDn = 'ou=Groups,ou=ENHS,ou=SPH,ou=Medical,ou=TC,ou=Units,dc=ad,dc=umn,dc=edu'
		ldapMemberAttribute = 'member'
		// Change this to your department's prefix, or change 
		// it to '' if you don't want to enter anything
		rolePrefix = 'EnHS-'
		// optional
		roleSuffix = ''
	}
}
