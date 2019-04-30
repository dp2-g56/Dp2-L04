
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AuditorService;
import domain.Position;

@Controller
@RequestMapping("/position/auditor")
public class PositionAuditorController extends AbstractController {

	@Autowired
	private AuditorService	auditorService;


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

		result = new ModelAndView("position/auditor/listAssignablePositions");
		result.addObject("positions", positions);
		result.addObject("requestURI", "position/auditor/listAssignablePositions.do");

		return result;
	}

}
