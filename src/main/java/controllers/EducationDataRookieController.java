
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curriculum;
import domain.EducationData;
import domain.Rookie;
import domain.PersonalData;
import domain.PositionData;
import forms.FormObjectCurriculumPersonalData;
import services.CurriculumService;
import services.EducationDataService;
import services.RookieService;
import services.PersonalDataService;
import services.PositionDataService;

@Controller
@RequestMapping("/educationData/rookie")
public class EducationDataRookieController extends AbstractController {
	
	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private EducationDataService educationDataService;
	@Autowired
	private RookieService rookieService;

	public EducationDataRookieController() {
		super();
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newEducationData(@RequestParam int curriculumId) {
		ModelAndView result;
		
		EducationData educationData = new EducationData();
		
		result = this.createEditModelAndView("rookie/createEducationData", educationData, curriculumId);
		
		return result;	
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod .GET)
	public ModelAndView editEducationData(@RequestParam int educationDataId) {
		ModelAndView result;
		
		try {
			Rookie rookie = this.rookieService.securityAndRookie();
			EducationData educationData = this.educationDataService.findOne(educationDataId);
			Curriculum curriculum = this.curriculumService.getCurriculumOfEducationData(educationDataId);
			Assert.notNull(this.curriculumService.getCurriculumOfRookie(rookie.getId(), curriculum.getId()));
			
			result = this.createEditModelAndView("rookie/editEducationData", educationData, curriculum.getId());
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/delete", method =RequestMethod .GET)
	public ModelAndView deleteEducationData(@RequestParam int educationDataId) {
		ModelAndView result = null;
		
		Curriculum curriculum = this.curriculumService.getCurriculumOfEducationData(educationDataId);
		
		try {
			this.educationDataService.deleteEducationDataAsRookie(educationDataId);
			result = new ModelAndView("redirect:/curriculum/rookie/show.do?curriculumId=" + curriculum.getId());
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView savePositionData(@Valid EducationData educationData, BindingResult binding, @Valid int curriculumId) {
		ModelAndView result;
		
		String tiles;
		if(educationData.getId()==0) {
			tiles = "rookie/createEducationData";
		} else {
			tiles = "rookie/editEducationData";
		}
		
		if(binding.hasErrors()) {
			result = this.createEditModelAndView(tiles, educationData, curriculumId);
		} else {
			try {
				this.educationDataService.addOrUpdateEducationDataAsRookie(educationData, curriculumId);
				result = new ModelAndView("redirect:/curriculum/rookie/show.do?curriculumId=" + curriculumId);
			} catch(Throwable oops) {
				result = this.createEditModelAndView(tiles, educationData, curriculumId, "commit.error");
			}
		}
		
		return result;	
	}

	private ModelAndView createEditModelAndView(String tiles, EducationData educationData, int curriculumId) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("educationData", educationData);
		result.addObject("curriculumId", curriculumId);
		return result;
	}
	
	private ModelAndView createEditModelAndView(String tiles, EducationData educationData, int curriculumId,
			String message) {
		ModelAndView result = this.createEditModelAndView(tiles, educationData, curriculumId);
		result.addObject("message", message);
		return result;
	}

}
