
package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AuditorService;
import services.SponsorshipService;
import domain.Position;
import domain.Sponsorship;

@Controller
@RequestMapping("/position/auditor")
public class PositionAuditorController extends AbstractController {

	@Autowired
	private AuditorService auditorService;

	@Autowired
	private SponsorshipService sponsorshipService;

	public PositionAuditorController() {
		super();
	}

	// -------------------------------------------------------------------
	// ---------------------------LIST------------------------------------

	// Listar Positions
	@RequestMapping(value = "/listAssignablePositions", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		List<Position> positions;

		positions = this.auditorService.showAssignablePositions();

		Map<Integer, Sponsorship> randomSpo = new HashMap<Integer, Sponsorship>();
		for (Position p : positions) {
			if (!p.getSponsorships().isEmpty()) {
				Sponsorship spo = this.sponsorshipService.getRandomSponsorship(p.getId());
				this.sponsorshipService.sendMessageToProvider(spo.getProvider());
				randomSpo.put(p.getId(), spo);
			}
		}

		result = new ModelAndView("position/auditor/listAssignablePositions");
		result.addObject("randomSpo", randomSpo);
		result.addObject("positions", positions);
		result.addObject("requestURI", "position/auditor/listAssignablePositions.do");

		return result;
	}

}
