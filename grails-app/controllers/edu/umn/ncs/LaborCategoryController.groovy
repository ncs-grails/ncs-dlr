package edu.umn.ncs

class LaborCategoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    //def scaffold = true

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [
            laborCategoryInstanceList: LaborCategory.list(params),
            laborCategoryInstanceTotal: LaborCategory.count()
        ]
    }

    def create = {
        def laborCategoryInstance = new LaborCategory()
        laborCategoryInstance.properties = params
        return [laborCategoryInstance: laborCategoryInstance]
    }

    def save = {
        def laborCategoryInstance = new LaborCategory(params)
        if (laborCategoryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'laborCategory.label', default: 'LaborCategory'), laborCategoryInstance.id])}"
            redirect(action: "show", id: laborCategoryInstance.id)
        }
        else {
            render(view: "create", model: [laborCategoryInstance: laborCategoryInstance])
        }
    }

    def show = {
        def laborCategoryInstance = LaborCategory.get(params.id)
        if (!laborCategoryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'laborCategory.label', default: 'LaborCategory'), params.id])}"
            redirect(action: "list")
        }
        else {
            [laborCategoryInstance: laborCategoryInstance]
        }
    }

    def edit = {
        def laborCategoryInstance = LaborCategory.get(params.id)
        if (!laborCategoryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'laborCategory.label', default: 'LaborCategory'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [laborCategoryInstance: laborCategoryInstance]
        }
    }

    def update = {
        def laborCategoryInstance = LaborCategory.get(params.id)
        if (laborCategoryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (laborCategoryInstance.version > version) {
                    
                    laborCategoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'laborCategory.label', default: 'LaborCategory')] as Object[], "Another user has updated this LaborCategory while you were editing")
                    render(view: "edit", model: [laborCategoryInstance: laborCategoryInstance])
                    return
                }
            }
            laborCategoryInstance.properties = params
            if (!laborCategoryInstance.hasErrors() && laborCategoryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'laborCategory.label', default: 'LaborCategory'), laborCategoryInstance.id])}"
                redirect(action: "show", id: laborCategoryInstance.id)
            }
            else {
                render(view: "edit", model: [laborCategoryInstance: laborCategoryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'laborCategory.label', default: 'LaborCategory'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def laborCategoryInstance = LaborCategory.get(params.id)
        if (laborCategoryInstance) {
            try {
                laborCategoryInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'laborCategory.label', default: 'LaborCategory'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'laborCategory.label', default: 'LaborCategory'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'laborCategory.label', default: 'LaborCategory'), params.id])}"
            redirect(action: "list")
        }
    }
}
