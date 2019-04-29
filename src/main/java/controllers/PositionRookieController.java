
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curriculum;
import domain.MiscellaneousData;
import domain.Position;
import domain.PositionData;
import services.CurriculumService;
import services.MiscellaneousDataService;
import services.PositionService;

@Controller
@RequestMapping("/position/rookie")
public class PositionRookieController extends AbstractController {
	
	@Autowired
	private PositionService positionService;

	public PositionRookieController() {
		super();
	}

	@RequestMapping(value = "/listSkills", method = RequestMethod.GET)
	public ModelAndView listSkills(@RequestParam int positionId) {
		ModelAndView result;
		
		try {
			List<String> skills = this.positionService.getSkillsAsRookie(positionId);
			
			result = new ModelAndView("rookie/skills");
			result.addObject("skills", skills);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/finder/rookie/list.do");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/listTechnologies", method = RequestMethod.GET)
	public ModelAndView listTechnologies(@RequestParam int positionId) {
		ModelAndView result;
		
		try {
			List<String> technologies = this.positionService.getTechnologiesAsRookie(positionId);
			
			result = new ModelAndView("rookie/technologies");
			result.addObject("technologies", technologies);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/finder/rookie/list.do");
		}
		
		return result;	
	}
}
