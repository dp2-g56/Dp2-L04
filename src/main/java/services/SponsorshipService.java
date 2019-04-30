
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.SponsorshipRepository;
import domain.CreditCard;
import domain.Position;
import domain.Provider;
import domain.Sponsorship;
import forms.FormObjectSponsorshipCreditCard;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private ProviderService providerService;

	@Autowired
	private SponsorshipRepository sponsorshipRepository;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CreditCardService creditCardService;

	public List<Sponsorship> findAll() {
		return this.sponsorshipRepository.findAll();
	}

	public Sponsorship save(Sponsorship h) {
		return this.sponsorshipRepository.save(h);
	}

	public void delete(Sponsorship h) {
		this.sponsorshipRepository.delete(h);
	}

	public Sponsorship findOne(int id) {
		return this.sponsorshipRepository.findOne(id);
	}

	public void flush() {
		this.sponsorshipRepository.flush();
	}

	public void deleteAllSponsorships() {

		Provider provider = new Provider();

		provider = this.providerService.loggedProvider();

		List<Sponsorship> sponsorships = new ArrayList<Sponsorship>();
		sponsorships = provider.getSponsorships();

		// Quitamos todos los applications de rookie

		for (Sponsorship spo : sponsorships) {
			Position pos = new Position();
			pos = spo.getPosition();
			pos.getApplications().remove(spo);
			// app.setRookie(null);
			// app.setPosition(null);
		}

		// rookie.getApplications().removeAll(applications);

		/*
		 * List<Position> allPositionsOfRookie = new ArrayList<Position>();
		 * 
		 * allPositionsOfRookie =
		 * this.positionService.positionsOfApplicationOfRookie(rookie);
		 * 
		 * for (Position p : allPositionsOfRookie) if
		 * (Collections.disjoint(p.getApplications(), applications)) {
		 * 
		 * }
		 */

		this.sponsorshipRepository.deleteInBatch(sponsorships);
	}

	public List<Sponsorship> findProviderSponsorships(Provider provider) {
		return provider.getSponsorships();
	}

	public Sponsorship reconstruct(FormObjectSponsorshipCreditCard formObject, BindingResult binding,
			CreditCard creditCard, Position position) {
		Sponsorship spo;
		Provider provider = this.providerService.loggedProvider();
		if (formObject.getId() == 0 && position != null) {
			spo = new Sponsorship();
			spo.setPosition(position);

			spo.setBanner(formObject.getBanner());
			spo.setTargetUrl(formObject.getTargetURL());
			spo.setCreditCard(creditCard);
			spo.setProvider(provider);
		} else {
			Sponsorship spo2 = this.findOne(formObject.getId());

			spo = new Sponsorship();
			spo.setPosition(formObject.getPosition());
			spo.setVersion(spo2.getVersion());
			spo.setId(spo2.getId());

			spo.setBanner(formObject.getBanner());
			spo.setTargetUrl(formObject.getTargetURL());
			spo.setCreditCard(creditCard);
			spo.setProvider(provider);
		}
		// this.validator.validate(spo, binding);

		return spo;
	}

	public Sponsorship addOrUpdateSponsorship(Sponsorship sponsorship) {
		Sponsorship result;

		List<String> cardType = this.configurationService.getConfiguration().getCardType();
		Provider provider = this.providerService.loggedProvider();

		if (sponsorship.getId() > 0)
			Assert.isTrue(provider.equals(sponsorship.getProvider()));

		CreditCard creditCard = sponsorship.getCreditCard();

		Assert.isTrue(cardType.contains(sponsorship.getCreditCard().getBrandName()));

		Assert.isTrue(this.creditCardService.validateNumberCreditCard(creditCard));
		Assert.isTrue(this.creditCardService.validateDateCreditCard(creditCard));

		result = this.save(sponsorship);

		if (sponsorship.getId() == 0) {
			List<Sponsorship> sps = provider.getSponsorships();
			sps.add(result);
			this.providerService.save(provider);
		}

		this.flush();

		return result;
	}
}
