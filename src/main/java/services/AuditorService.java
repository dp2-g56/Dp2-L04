
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AuditorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Audit;
import domain.Auditor;

@Service
@Transactional
public class AuditorService {

	@Autowired
	private AuditorRepository	auditorRepository;

	@Autowired
	private AuditService		auditService;


	//-----------------------------------------SECURITY-----------------------------
	//------------------------------------------------------------------------------

	public Auditor save(Auditor auditor) {
		return this.auditorRepository.save(auditor);
	}

	/**
	 * LoggedCompany now contains the security of loggedAsCompany
	 * 
	 * @return
	 */
	public Auditor loggedAuditor() {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		return this.auditorRepository.getAuditorByUsername(userAccount.getUsername());
	}

	public void loggedAsAuditor() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("AUDITOR"));

	}

	public void addAudit(Audit a) {
		this.loggedAsAuditor();
		Auditor loggedAuditor = this.loggedAuditor();
		Assert.isTrue(a.getId() == 0);

		Audit audit = this.auditService.save(a);
		List<Audit> audits = loggedAuditor.getAudits();
		audits.add(audit);
		loggedAuditor.setAudits(audits);
		this.save(loggedAuditor);
	}

}
