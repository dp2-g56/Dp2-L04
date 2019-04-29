
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.SponsorshipRepository;
import domain.Position;
import domain.Provider;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	ProviderService			providerService;

	@Autowired
	SponsorshipRepository	sponsorshipRepository;


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
}
