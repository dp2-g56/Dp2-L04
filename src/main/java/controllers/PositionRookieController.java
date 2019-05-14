
package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditService;
import services.PositionService;
import domain.Audit;
import domain.Position;

@Controller
@RequestMapping("/position/rookie")
public class PositionRookieController extends AbstractController {

	@Autowired
	private PositionService	positionService;

	@Autowired
	private AuditService	auditService;


	public PositionRookieController() {
		super();
	}

	@RequestMapping(value = "/listSkills", method = RequestMethod.GET)
	public ModelAndView listSkills(@RequestParam int positionId) {
		try {
			ModelAndView result;

			try {
				List<String> skills = this.positionService.getSkillsAsRookie(positionId);

				result = new ModelAndView("rookie/skills");
				result.addObject("skills", skills);
			} catch (Throwable oops) {
				result = new ModelAndView("redirect:/finder/rookie/list.do");
			}

			return result;
		} catch (Throwable oops) {
			return new ModelAndView("redirect:/");
		}
	}

	@RequestMapping(value = "/listTechnologies", method = RequestMethod.GET)
	public ModelAndView listTechnologies(@RequestParam int positionId) {
		try {
			ModelAndView result;

			try {
				List<String> technologies = this.positionService.getTechnologiesAsRookie(positionId);

				result = new ModelAndView("rookie/technologies");
				result.addObject("technologies", technologies);
			} catch (Throwable oops) {
				result = new ModelAndView("redirect:/finder/rookie/list.do");
			}

			return result;
		} catch (Throwable oops) {
			return new ModelAndView("redirect:/");
		}
	}

	@RequestMapping(value = "/audit/list", method = RequestMethod.GET)
	public ModelAndView listAudits(@RequestParam int positionId) {
		try {

			ModelAndView result;

			List<Audit> finalAudits = new ArrayList<Audit>();
			finalAudits = this.auditService.getFinalAuditsByPosition(positionId);
			Position position = this.positionService.findOne(positionId);
			Assert.isTrue(position.getAudits().containsAll(finalAudits));

			if (position.getIsCancelled() == true && position.getIsDraftMode() == false)
				result = new ModelAndView("redirect:/finder/rookie/list.do");
			else {
				result = new ModelAndView("position/rookie/audit/list");

				result.addObject("finalAudits", finalAudits);
				result.addObject("requestURI", "position/rookie/audit/list.do");
				result.addObject("positionId", positionId);

			}

			return result;
		} catch (Throwable oops) {
			return new ModelAndView("redirect:/");
		}
	}
}
