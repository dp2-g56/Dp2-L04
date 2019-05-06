
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

import domain.Curriculum;
import domain.PositionData;
import services.CurriculumService;
import services.PositionDataService;
import services.RookieService;

@Controller
@RequestMapping("/positionData/rookie")
public class PositionDataRookieController extends AbstractController {

	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private PositionDataService positionDataService;
	@Autowired
	private RookieService rookieService;

	public PositionDataRookieController() {
		super();
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newPositionData(@RequestParam int curriculumId) {
		ModelAndView result;

		PositionData positionData = new PositionData();

		result = this.createEditModelAndView("rookie/createPositionData", positionData, curriculumId);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editPositionData(@RequestParam int positionDataId) {
		ModelAndView result;

		try {
			PositionData positionData = this.positionDataService.findOne(positionDataId);
			Curriculum curriculum = this.curriculumService.getCurriculumOfPositionData(positionDataId);
			Assert.notNull(this.curriculumService.getCurriculumOfLoggedRookie(curriculum.getId()));

			result = this.createEditModelAndView("rookie/editPositionData", positionData, curriculum.getId());
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deletePositionData(@RequestParam int positionDataId) {
		ModelAndView result = null;

		Curriculum curriculum = this.curriculumService.getCurriculumOfPositionData(positionDataId);

		try {
			this.positionDataService.deletePositionDataAsRookie(positionDataId);
			result = new ModelAndView("redirect:/curriculum/rookie/show.do?curriculumId=" + curriculum.getId());
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView savePositionData(@Valid PositionData positionData, BindingResult binding,
			@Valid int curriculumId) {
		ModelAndView result;

		String tiles;
		if (positionData.getId() == 0)
			tiles = "rookie/createPositionData";
		else
			tiles = "rookie/editPositionData";

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		if (positionData.getStartDate().after(positionData.getEndDate()))
			if (locale.contains("ES"))
				binding.addError(new FieldError("positionData", "startDate", positionData.getStartDate(), false, null,
						null, "La fecha de fin no puede ser anterior a la de inicio"));
			else
				binding.addError(new FieldError("positionData", "startDate", positionData.getStartDate(), false, null,
						null, "The end date can not be before the start date"));

		if (binding.hasErrors())
			result = this.createEditModelAndView(tiles, positionData, curriculumId);
		else
			try {
				this.positionDataService.addOrUpdatePositionDataAsRookie(positionData, curriculumId);
				result = new ModelAndView("redirect:/curriculum/rookie/show.do?curriculumId=" + curriculumId);
			} catch (Throwable oops) {
				result = this.createEditModelAndView(tiles, positionData, curriculumId, "commit.error");
			}

		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, PositionData positionData, int curriculumId) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("positionData", positionData);
		result.addObject("curriculumId", curriculumId);
		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, PositionData positionData, int curriculumId,
			String message) {
		ModelAndView result = this.createEditModelAndView(tiles, positionData, curriculumId);
		result.addObject("message", message);
		return result;
	}

}
