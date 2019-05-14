
package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CurriculumService;
import services.MiscellaneousDataService;
import services.RookieService;
import domain.Curriculum;
import domain.MiscellaneousData;

@Controller
@RequestMapping("/miscellaneousData/rookie")
public class MiscellaneousDataRookieController extends AbstractController {

	@Autowired
	private CurriculumService			curriculumService;
	@Autowired
	private MiscellaneousDataService	miscellaneousDataService;
	@Autowired
	private RookieService				rookieService;


	public MiscellaneousDataRookieController() {
		super();
	}

	@RequestMapping(value = "/listAttachments", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int miscellaneousDataId) {
		ModelAndView result;

		try {
			List<String> attachments = this.miscellaneousDataService.getAttachmentsOfMiscellaneousDataOfLoggedRookie(miscellaneousDataId);

			result = new ModelAndView("rookie/attachments");
			result.addObject("attachments", attachments);
			result.addObject("miscellaneousDataId", miscellaneousDataId);
			result.addObject("curriculumId", this.curriculumService.getCurriculumOfMiscellaneousData(miscellaneousDataId).getId());
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/newAttachment", method = RequestMethod.GET)
	public ModelAndView newAttachment(@RequestParam int miscellaneousDataId) {
		try {
			ModelAndView result;

			result = new ModelAndView("rookie/createAttachment");
			result.addObject("miscellaneousDataId", miscellaneousDataId);
			result.addObject("attachment", "");

			return result;
		} catch (Throwable oops) {
			return new ModelAndView("redirect:/");
		}
	}

	@RequestMapping(value = "/deleteAttachment", method = RequestMethod.GET)
	public ModelAndView newAttachment(@RequestParam int miscellaneousDataId, @RequestParam int attachmentIndex) {
		ModelAndView result;

		try {
			this.miscellaneousDataService.deleteAttachmentAsRookie(miscellaneousDataId, attachmentIndex);
			result = new ModelAndView("redirect:listAttachments.do?miscellaneousDataId=" + miscellaneousDataId);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/saveAttachment", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAttachment(@RequestParam int miscellaneousDataId, @RequestParam String attachment) {
		ModelAndView result;

		try {
			this.miscellaneousDataService.addAttachmentAsRookie(miscellaneousDataId, attachment);
			result = new ModelAndView("redirect:listAttachments.do?miscellaneousDataId=" + miscellaneousDataId);
		} catch (Throwable oops) {
			result = new ModelAndView("rookie/createAttachment");
			result.addObject("miscellaneousDataId", miscellaneousDataId);
			result.addObject("attachment", attachment);
			result.addObject("message", "commit.error.url");
		}

		return result;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newMiscellaneousData(@RequestParam int curriculumId) {
		try {
			ModelAndView result;

			MiscellaneousData miscellaneousData = this.miscellaneousDataService.create();

			result = this.createEditModelAndView("rookie/createMiscellaneousData", miscellaneousData, curriculumId);

			return result;
		} catch (Throwable oops) {
			return new ModelAndView("redirect:/");
		}
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editMiscellaneousData(@RequestParam int miscellaneousDataId) {
		ModelAndView result;

		try {
			MiscellaneousData miscellaneousData = this.miscellaneousDataService.getMiscellaneousDataOfLoggedRookie(miscellaneousDataId);
			Curriculum curriculum = this.curriculumService.getCurriculumOfMiscellaneousData(miscellaneousDataId);

			result = this.createEditModelAndView("rookie/editMiscellaneousData", miscellaneousData, curriculum.getId());
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteMiscellaneousData(@RequestParam int miscellaneousDataId) {
		ModelAndView result = null;

		try {
			Curriculum curriculum = this.curriculumService.getCurriculumOfMiscellaneousData(miscellaneousDataId);
			this.miscellaneousDataService.deleteMiscellaneousDataAsRookie(miscellaneousDataId);
			result = new ModelAndView("redirect:/curriculum/rookie/show.do?curriculumId=" + curriculum.getId());
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/rookie/list.do");
		}

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMiscellaneousData(MiscellaneousData miscellaneousData, BindingResult binding, int curriculumId) {
		ModelAndView result;
		try {
			String tiles;
			if (miscellaneousData.getId() == 0)
				tiles = "rookie/createMiscellaneousData";
			else
				tiles = "rookie/editMiscellaneousData";

			MiscellaneousData miscellaneousDataReconstructed = this.miscellaneousDataService.reconstruct(miscellaneousData, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(tiles, miscellaneousDataReconstructed, curriculumId);
			else
				try {
					this.miscellaneousDataService.addOrUpdateMiscellaneousDataAsRookie(miscellaneousDataReconstructed, curriculumId);
					result = new ModelAndView("redirect:/curriculum/rookie/show.do?curriculumId=" + curriculumId);
				} catch (Throwable oops) {
					result = this.createEditModelAndView(tiles, miscellaneousDataReconstructed, curriculumId, "commit.error");
				}

			return result;
		} catch (Throwable oops) {
			return new ModelAndView("redirect:/");
		}
	}

	private ModelAndView createEditModelAndView(String tiles, MiscellaneousData miscellaneousData, int curriculumId) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("miscellaneousData", miscellaneousData);
		result.addObject("curriculumId", curriculumId);
		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, MiscellaneousData miscellaneousData, int curriculumId, String message) {
		ModelAndView result = this.createEditModelAndView(tiles, miscellaneousData, curriculumId);
		result.addObject("message", message);
		return result;
	}
}
