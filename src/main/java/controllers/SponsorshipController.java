package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Provider;
import domain.Sponsorship;
import services.ProviderService;
import services.SponsorshipService;

@Controller
@RequestMapping("/sponsorship/provider/")
public class SponsorshipController {

	@Autowired
	private SponsorshipService sponsorshipService;

	@Autowired
	private ProviderService providerService;

	public SponsorshipController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		Provider provider = this.providerService.loggedProvider();

		List<Sponsorship> sponsorships = this.sponsorshipService.findProviderSponsorships(provider);

		result = new ModelAndView("provider/sponsorships");

		result.addObject("sponsorships", sponsorships);
		result.addObject("requestURI", "sponsorship/provider/list.do");

		return result;
	}

}
