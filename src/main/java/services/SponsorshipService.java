
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

		// Quitamos todos los applications de hacker

		for (Sponsorship spo : sponsorships) {
			Position pos = new Position();
			pos = spo.getPosition();
			pos.getApplications().remove(spo);
			// app.setHacker(null);
			// app.setPosition(null);
		}

		// hacker.getApplications().removeAll(applications);

		/*
		 * List<Position> allPositionsOfHacker = new ArrayList<Position>();
		 * 
		 * allPositionsOfHacker =
		 * this.positionService.positionsOfApplicationOfHacker(hacker);
		 * 
		 * for (Position p : allPositionsOfHacker) if
		 * (Collections.disjoint(p.getApplications(), applications)) {
		 * 
		 * }
		 */

		this.sponsorshipRepository.deleteInBatch(sponsorships);
	}
}
