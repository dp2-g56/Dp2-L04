
package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuditRepository;
import domain.Audit;
import domain.Auditor;
import domain.Position;

@Service
@Transactional
public class AuditService {

	@Autowired
	private AuditRepository	auditRepository;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private Validator		validator;


	public List<Audit> getFinalAuditsByPosition(int positionId) {
		return this.auditRepository.getFinalAuditsByPosition(positionId);
	}

	public Audit save(Audit audit) {
		return this.auditRepository.save(audit);
	}

	public Audit findOne(int auditId) {
		return this.auditRepository.findOne(auditId);
	}

	//CREATE
	public Audit create(Position position) {
		Audit audit = new Audit();

		audit.setAuditor(null);
		audit.setFreeText("");
		audit.setIsDraftMode(true);
		audit.setMomentCreation(null);
		audit.setPosition(position);
		audit.setScore(0);

		return audit;
	}

	public Audit reconstruct(Audit audit, BindingResult binding) {
		this.auditorService.loggedAsAuditor();
		Auditor auditor = this.auditorService.loggedAuditor();
		Audit result = new Audit();

		if (audit.getId() == 0) {
			result = audit;
			result.setAuditor(auditor);

			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1);

			result.setMomentCreation(thisMoment);

		} else {
			Audit copy = this.findOne(audit.getId());

			result.setFreeText(audit.getFreeText());
			result.setIsDraftMode(audit.getIsDraftMode());
			result.setScore(audit.getScore());

			result.setPosition(copy.getPosition());
			result.setMomentCreation(copy.getMomentCreation());
			result.setAuditor(copy.getAuditor());

			result.setId(copy.getId());
			result.setVersion(copy.getVersion());
		}
		this.validator.validate(result, binding);
		return result;
	}

	public void deleteAudit(Audit audit) {
		this.auditorService.loggedAsAuditor();
		Auditor loggedAuditor = this.auditorService.loggedAuditor();

		Assert.isTrue(loggedAuditor.getAudits().contains(audit));
		Assert.isTrue(this.findOne(audit.getId()).getIsDraftMode());

		this.auditRepository.delete(audit);

	}

	public void deleteAllAudits() {

		Auditor auditor = new Auditor();

		auditor = this.auditorService.loggedAuditor();

		List<Audit> audits = new ArrayList<Audit>();
		audits = auditor.getAudits();

		// Quitamos todos los applications de rookie

		for (Audit add : audits) {
			Position pos = new Position();
			pos = add.getPosition();
			pos.getApplications().remove(add);
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

		this.auditRepository.deleteInBatch(audits);
	}
}
