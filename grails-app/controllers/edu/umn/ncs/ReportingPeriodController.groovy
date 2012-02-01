package edu.umn.ncs

class ReportingPeriodController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def debug = true
	//def debug = true
		
    def index = {
		
		if (debug) { 
			log.debug "=> REPORTING PERIOD CONTROLLER > INDEX -------------------"  
			log.debug "=> params: ${params}" 
		} 
		
		redirect(action: "list", params: params)
		
    }
	
	def list = {
		
		if (debug) {
			log.debug "=> REPORTING PERIOD CONTROLLER > LIST --------------------" 
			log.debug "=> params: ${params}" 
		}
		
		def reportingPeriodInstance = ReportingPeriod.list(sort:"periodDate",order:"desc", max:12)
		if (debug) { log.debug "=> reportingPeriodInstance: ${reportingPeriodInstance}" } 
		
		[ reportingPeriodInstance: reportingPeriodInstance ]

	}
	
	def updateRin = {
		
		if (debug) { 
			log.debug "=> REPORTING PERIOD CONTROLLER > updateRin -------------------"  
			log.debug "=> params: ${params}" 
		} 
		
		def reportingPeriodInstance = ReportingPeriod.get(params.id)
		
		if (reportingPeriodInstance) {
			reportingPeriodInstance.properties = params
			
			if (!reportingPeriodInstance.hasErrors() && reportingPeriodInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), reportingPeriodInstance.id])}"
				redirect(action: "show", id: reportingPeriodInstance.id)
			}
			else {
				response.sendError(400)
				// render(view: "edit", model: [reportingPeriodInstance: reportingPeriodInstance])
			}
		}
		else {
			//flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), params.id])}"
			//redirect(action: "list")
			response.sendError(404)
		}
	}

	def show = {
		
		def reportingPeriodInstance = ReportingPeriod.get(params.id)
		
		if (!reportingPeriodInstance) {
			render "not found."
		}
		else {
			render reportingPeriodInstance.referenceInvoiceNumber ?: ''
		}
	}

	def update = {
		
		if (debug) { 
			log.debug "=> REPORTING PERIOD CONTROLLER > UPDATE --------------------" 
			log.debug "=> params: ${params}"
		} 

		def reportingPeriodInstanceAll = ReportingPeriod.list(sort:"periodDate",order:"desc", max:12)
		if (debug) { log.debug "=> ReportingPeriodController.save.reportingPeriodInstanceAll: ${reportingPeriodInstanceAll}" }

		def errList = []
		def succesList = []
		
		params.each{
			
			//if (debug) { log.debug "=> AssignedEffortController.update.params.each: ${it}" }
			//if (debug) { log.debug "=> ReportingPeriodController.save.params.each KEY: ${it.key}, VALUE: ${it.value}" }
						
			if ( it.key =~ /^id-[0-9]*$/ && it.value ) {
				
				def reportingPeriodInstance = ReportingPeriod.get(Integer.parseInt(it.key.replace('id-', '')))
				if (debug) { log.debug "=> ReportingPeriodController.save.reportingPeriodInstance: ${reportingPeriodInstance}" }  				
								
				if ( reportingPeriodInstance ) {
					
					if ( params.version ) {
						
						def version = params.version.toLong()
						
						if (reportingPeriodInstance.version > version) {
							if (debug) { log.debug "=> VERSION ERROR" }			
							reportingPeriodInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'reportingPeriod.label', default: 'ReportingPeriod')] as Object[], "Another user has updated this ReportingPeriod while you were editing")							
							render(view: "list", model: [reportingPeriodInstance: reportingPeriodInstanceAll])
							return
						}
						
					}
					
					reportingPeriodInstance.referenceInvoiceNumber = it.value.toLong()
					
					if ( !reportingPeriodInstance.hasErrors() && reportingPeriodInstance.save(flush: true) ) {
						
						if (debug) { log.debug "=> UPDATE SUCCESSFULLY" } 				
						flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), reportingPeriodInstance.id])}"
						render(view: "list", model: [reportingPeriodInstance: reportingPeriodInstanceAll])
						
					} else {
					
						if (debug) { 
							log.debug "=> UPDATE FAILED"  
							reportingPeriodInstance.errors.each{
								log.debug "=> reportingPeriodInstance.error: ${it}"
							}
						}

						render(view: "list", model: [reportingPeriodInstance: reportingPeriodInstanceAll])
						
					}
					
				} else {
				
					flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), params.id])}"
					render(view: "list", model: [reportingPeriodInstance: reportingPeriodInstanceAll])
					
				} //if (reportingPeriodInstance)
				
			} //if ( it.key =~ /^id-[0-9]*$/ && it.value )
				
		} //params.each
		
	} //def update

	
} //class ReportingPeriodController



/*
def list = {
	params.max = Math.min(params.max ? params.int('max') : 10, 100)
	[reportingPeriodInstanceList: ReportingPeriod.list(params), reportingPeriodInstanceTotal: ReportingPeriod.count()]
}

def create = {
	def reportingPeriodInstance = new ReportingPeriod()
	reportingPeriodInstance.properties = params
	return [reportingPeriodInstance: reportingPeriodInstance]
}

def save = {
	def reportingPeriodInstance = new ReportingPeriod(params)
	if (reportingPeriodInstance.save(flush: true)) {
		flash.message = "${message(code: 'default.created.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), reportingPeriodInstance.id])}"
		redirect(action: "show", id: reportingPeriodInstance.id)
	}
	else {
		render(view: "create", model: [reportingPeriodInstance: reportingPeriodInstance])
	}
}

def show = {
	def reportingPeriodInstance = ReportingPeriod.get(params.id)
	if (!reportingPeriodInstance) {
		flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), params.id])}"
		redirect(action: "list")
	}
	else {
		[reportingPeriodInstance: reportingPeriodInstance]
	}
}

def edit = {
	def reportingPeriodInstance = ReportingPeriod.get(params.id)
	if (!reportingPeriodInstance) {
		flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), params.id])}"
		redirect(action: "list")
	}
	else {
		return [reportingPeriodInstance: reportingPeriodInstance]
	}
}

def update = {
	def reportingPeriodInstance = ReportingPeriod.get(params.id)
	if (reportingPeriodInstance) {
		if (params.version) {
			def version = params.version.toLong()
			if (reportingPeriodInstance.version > version) {
				
				reportingPeriodInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'reportingPeriod.label', default: 'ReportingPeriod')] as Object[], "Another user has updated this ReportingPeriod while you were editing")
				render(view: "edit", model: [reportingPeriodInstance: reportingPeriodInstance])
				return
			}
		}
		reportingPeriodInstance.properties = params
		if (!reportingPeriodInstance.hasErrors() && reportingPeriodInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), reportingPeriodInstance.id])}"
			redirect(action: "show", id: reportingPeriodInstance.id)
		}
		else {
			render(view: "edit", model: [reportingPeriodInstance: reportingPeriodInstance])
		}
	}
	else {
		flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), params.id])}"
		redirect(action: "list")
	}
}

def delete = {
	def reportingPeriodInstance = ReportingPeriod.get(params.id)
	if (reportingPeriodInstance) {
		try {
			reportingPeriodInstance.delete(flush: true)
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), params.id])}"
			redirect(action: "list")
		}
		catch (org.springframework.dao.DataIntegrityViolationException e) {
			flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), params.id])}"
			redirect(action: "show", id: params.id)
		}
	}
	else {
		flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reportingPeriod.label', default: 'ReportingPeriod'), params.id])}"
		redirect(action: "list")
	}
}
*/