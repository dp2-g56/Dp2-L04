
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditService;
import services.AuditorService;
import services.PositionService;
import domain.Audit;
import domain.Auditor;
import domain.Position;

@Controller
@RequestMapping("/audit/auditor")
public class AuditController extends AbstractController {

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private AuditService	auditService;

	@Autowired
	private PositionService	positionService;


	public AuditController() {
		super();
	}

	// -------------------------------------------------------------------
	// ---------------------------LIST------------------------------------

	// Listar Audits en vista privada, NO en public data
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		List<Audit> audits;

		Auditor loggedAuditor = this.auditorService.loggedAuditor();
		audits = loggedAuditor.getAudits();

		result = new ModelAndView("audit/auditor/list");
		result.addObject("audits", audits);
		result.addObject("requestURI", "audit/auditor/list.do");

		return result;
	}

	// CREATE AUDIT
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createAudit(@RequestParam int positionId) {
		ModelAndView result;

		Position position = this.positionService.findOne(positionId);

		if (position.getIsDraftMode())
			return this.list();

		Audit audit = new Audit();
		audit = this.auditService.create(position);

		result = this.createEditModelAndView(audit);
		result.addObject("audit", audit);

		return result;
	}

	// EDIT AUDIT
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int auditId) {
		ModelAndView result;
		Position position;
		Audit audit = this.auditService.findOne(auditId);
		position = audit.getPosition();

		if (position.getIsDraftMode())
			return this.list();

		result = this.createEditModelAndView(audit);
		return result;
	}

	//SAVE AUDIT
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView save(Audit audit, BindingResult binding) {
		ModelAndView result;

		Audit a = new Audit();

		a = this.auditService.reconstruct(audit, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(audit);
		else
			try {
				this.auditorService.addAudit(audit);

				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(audit, "commit.error");
			}

		return result;
	}
	//DELETE AUDIT
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Audit audit2) {

		ModelAndView result;

		Audit audit = this.auditService.findOne(audit2.getId());

		try {
			this.auditService.deleteAudit(audit);

			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(audit, "commit.error");

		}
		return result;
	}

	//PROTECTED MODEL AND VIEW
	protected ModelAndView createEditModelAndView(Audit audit) {
		ModelAndView result;

		result = this.createEditModelAndView(audit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Audit audit, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("audit/auditor/edit");

		result.addObject("audit", audit);
		result.addObject("message", messageCode);

		return result;
	}

}