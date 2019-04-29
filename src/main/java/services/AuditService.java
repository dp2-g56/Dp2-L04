
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

		Assert.isTrue(!position.getIsDraftMode());

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
		Audit result = new Audit();

		if (audit.getId() == 0) {
			result = audit;
			result.setAuditor(this.auditorService.loggedAuditor());

			Date thisMoment = new Date();
			thisMoment.setTime(thisMoment.getTime() - 1);
			result.setPosition(audit.getPosition());
			result.setMomentCreation(thisMoment);

		} else {
			Audit copy = this.findOne(audit.getId());

			result.setFreeText(audit.getFreeText());
			result.setIsDraftMode(audit.getIsDraftMode());
			result.setScore(audit.getScore());

			result.setPosition(audit.getPosition());
			result.setMomentCreation(audit.getMomentCreation());
			result.setAuditor(audit.getAuditor());

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
		Assert.isTrue(audit.getIsDraftMode());

		loggedAuditor.getAudits().remove(audit);

		this.auditRepository.delete(audit);

	}

	public void deleteAllAudits() {

		Auditor auditor = new Auditor();

		auditor = this.auditorService.loggedAuditor();

		List<Audit> audits = new ArrayList<Audit>();
		audits = auditor.getAudits();

		// Quitamos todos los applications de hacker

		for (Audit add : audits) {
			Position pos = new Position();
			pos = add.getPosition();
			pos.getApplications().remove(add);
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

		this.auditRepository.deleteInBatch(audits);
	}
}