
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CurriculumService;
import services.EducationDataService;
import services.RookieService;
import domain.Curriculum;
import domain.EducationData;
import domain.Rookie;

@Controller
@RequestMapping("/educationData/rookie")
public class EducationDataRookieController extends AbstractController {

	@Autowired
	private CurriculumService		curriculumService;
	@Autowired
	private EducationDataService	educationDataService;
	@Autowired
	private RookieService			rookieService;


	public EducationDataRookieController() {
		super();
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newEducationData(@RequestParam int curriculumId) {
		try {
			ModelAndView result;

			EducationData educationData = new EducationData();

			result = this.createEditModelAndView("rookie/createEducationData", educationData, curriculumId);

			return result;
		} catch (Throwable oops) {
			return new ModelAndView("redirect:/");
		}
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editEducationData(@RequestParam int educationDataId) {
		ModelAndView result;

		try {
			Rookie rookie = this.rookieService.securityAndRookie();
			EducationData educationData = this.educationDataService.findOne(educationDataId);
			Curriculum curriculum = this.curriculumService.getCurriculumOfEducationData(educationDataId);
			Assert.notNull(this.curriculumService.getCurriculumOfRookie(rookie.getId(), curriculum.getId()));

			result = this.createEditModelAndView("rookie/editEducationData", educationData, curriculum.getId());
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteEducationData(@RequestParam int educationDataId) {
		ModelAndView result = null;

		try {
			Curriculum curriculum = this.curriculumService.getCurriculumOfEducationData(educationDataId);
			this.educationDataService.deleteEducationDataAsRookie(educationDataId);
			result = new ModelAndView("redirect:/curriculum/rookie/show.do?curriculumId=" + curriculum.getId());
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView savePositionData(@Valid EducationData educationData, BindingResult binding, @Valid int curriculumId) {
		ModelAndView result;
		try {
			String tiles;
			if (educationData.getId() == 0)
				tiles = "rookie/createEducationData";
			else
				tiles = "rookie/editEducationData";

			if (educationData.getEndDate() != null && educationData.getStartDate() != null) {
				String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

				if (educationData.getStartDate().after(educationData.getEndDate()))
					if (locale.contains("ES"))
						binding.addError(new FieldError("educationData", "startDate", educationData.getStartDate(), false, null, null, "La fecha de fin no puede ser anterior a la de inicio"));
					else
						binding.addError(new FieldError("educationData", "startDate", educationData.getStartDate(), false, null, null, "The end date can not be before the start date"));
			}

			if (binding.hasErrors())
				result = this.createEditModelAndView(tiles, educationData, curriculumId);
			else
				try {
					this.educationDataService.addOrUpdateEducationDataAsRookie(educationData, curriculumId);
					result = new ModelAndView("redirect:/curriculum/rookie/show.do?curriculumId=" + curriculumId);
				} catch (Throwable oops) {
					result = this.createEditModelAndView(tiles, educationData, curriculumId, "commit.error");
				}

			return result;
		} catch (Throwable oops) {
			return new ModelAndView("redirect:/");
		}
	}

	private ModelAndView createEditModelAndView(String tiles, EducationData educationData, int curriculumId) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("educationData", educationData);
		result.addObject("curriculumId", curriculumId);
		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, EducationData educationData, int curriculumId, String message) {
		ModelAndView result = this.createEditModelAndView(tiles, educationData, curriculumId);
		result.addObject("message", message);
		return result;
	}

}
