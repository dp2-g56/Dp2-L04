
package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import services.FinderService;
import services.RookieService;
import services.SponsorshipService;
import services.PositionService;
import services.ProblemService;
import domain.Company;
import domain.Finder;
import domain.Rookie;
import domain.Sponsorship;
import domain.Position;
import domain.Problem;
import forms.FormObjectPositionProblemCheckbox;
import security.LoginService;
import security.UserAccount;

@Controller
@RequestMapping("/finder/rookie")
public class FinderRookieController extends AbstractController {

	@Autowired
	private PositionService positionService;
	@Autowired
	private RookieService rookieService;
	@Autowired
	private FinderService finderService;
	@Autowired
	private SponsorshipService sponsorshipService;

	public FinderRookieController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		try {
			Rookie rookie = this.rookieService.securityAndRookie();
			List<Position> positions = this.finderService.finderList(rookie.getFinder());

			Map<Integer, Sponsorship> randomSpo = new HashMap<Integer, Sponsorship>();
			for (Position p : positions) {
				if (!p.getSponsorships().isEmpty()) {
					Sponsorship spo = this.sponsorshipService.getRandomSponsorship(p.getId());
					this.sponsorshipService.sendMessageToProvider(spo.getProvider());
					randomSpo.put(p.getId(), spo);
				}
			}

			result = new ModelAndView("rookie/finderResult");
			result.addObject("randomSpo", randomSpo);
			result.addObject("positions", positions);
			result.addObject("rookie", rookie);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/");
		}

		return result;
	}

	@RequestMapping(value = "/clean", method = RequestMethod.POST, params = "save")
	public ModelAndView save() {
		ModelAndView result;

		try {
			Rookie rookie = this.rookieService.securityAndRookie();
			this.finderService.getFinalPositionsAndCleanFinder(rookie.getFinder());

			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;

		try {
			Rookie rookie = this.rookieService.securityAndRookie();

			Finder finder = rookie.getFinder();
			Assert.notNull(finder);

			result = this.createEditModelAndView(finder);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;

	}

	// Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Finder finderForm, BindingResult binding) {
		ModelAndView result;

		Finder finder = this.finderService.reconstruct(finderForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(finderForm);
		else
			try {
				this.finderService.filterPositionsByFinder(finder);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				try {
					Rookie rookie = this.rookieService.securityAndRookie();
					result = this.createEditModelAndView(finder, "finder.commit.error");
					result.addObject("rookie", rookie);
				} catch (Throwable oops2) {
					result = new ModelAndView("redirect:/");
				}
			}
		return result;
	}

	// CreateEditModelAndView
	protected ModelAndView createEditModelAndView(Finder finder) {
		ModelAndView result;

		result = this.createEditModelAndView(finder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Finder finder, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("rookie/finder");

		result.addObject("finder", finder);
		result.addObject("message", messageCode);

		return result;

	}

}
