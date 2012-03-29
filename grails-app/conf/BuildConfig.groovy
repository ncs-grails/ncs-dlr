grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        mavenRepo "http://artifact.ncs.umn.edu/plugins-release"
        //mavenRepo "http://artifact.ncs.umn.edu/plugins-snapshot"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime 'mysql:mysql-connector-java:5.1.18'
    }
    plugins {
		compile ":spring-security-core:1.2.7.3"
		compile ":spring-security-ldap:1.0.6"
		compile ":spring-security-shibboleth-native-sp:1.0.3"
		provided ":spring-security-mock:1.0.1"
	}
}
codenarc.reports = {
	JenkinsXmlReport('xml') {
		outputFile = 'target/test-reports/CodeNarcReport.xml'
		title = 'CodeNarc Report for Direct Labor Reporting System'
	}
	JenkinsHtmlReport('html') {
		outputFile = 'CodeNarcReport.html'
		title = 'CodeNarc Report for Direct Labor Reporting System'
	}
}
codenarc.propertiesFile = 'grails-app/conf/codenarc.properties'

