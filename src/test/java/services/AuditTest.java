
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Audit;
import domain.Auditor;
import domain.Position;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AuditTest extends AbstractTest {

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private AuditService	auditService;

	@Autowired
	private PositionService	positionService;


	/*
	 * 10.An actor who is authenticated as a rookie must be able to:1.Manage his or
	 * her applications, which includes listing them grouped by status, showing
	 * them, creating them, and updating them.When an application is created, the
	 * system assigns an arbitrary problem to it (from the set of problems that have
	 * been registered for the corresponding position). Updating an application
	 * consists in submitting a solution to the corresponding problem (a piece of
	 * text with explanations and a link to the code), registering the submission
	 * moment, and changing the status to SUBMITTED
	 */

	// Ratio of data coverage: 6/6 = 100%
	@Test
	public void driverAssignPosition() {

		Object testingData[][] = {
			// Positive test: Listing all applications of the rookie
			{
				"auditor1", "position1", "freeText1", 9, true, null
			}, {
				"auditor1", "position1", "", 9, true, ConstraintViolationException.class
			}, {
				"auditor1", "position1", "freeText1", 11, true, ConstraintViolationException.class
			}, {
				"auditor1", "position1", "freeText1", 9, null, ConstraintViolationException.class
			}, {
				"company1", "position1", "freeText1", 9, true, IllegalArgumentException.class
			}, {
				"auditor1", "position3", "freeText1", 9, true, IllegalArgumentException.class
			}, {
				"auditor1", "position4", "freeText1", 9, true, IllegalArgumentException.class
			}, {
				"auditor1", "position2", "freeText1", 9, true, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.templateassignPosition((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][5]);
		}
	}
	private void templateassignPosition(String username, String position, String text, Integer score, Boolean draftMode, Class<?> expected) {

		Class<?> caught = null;

		this.startTransaction();
		super.authenticate(username);
		try {

			Auditor auditor = this.auditorService.loggedAuditor();

			Position positionA = this.positionService.findOne(super.getEntityId(position));

			Audit audit = this.auditService.create(positionA);

			audit.setAuditor(auditor);
			audit.setFreeText(text);
			audit.setIsDraftMode(draftMode);
			audit.setScore(score);

			this.auditorService.addAudit(audit);

			this.auditorService.flush();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.unauthenticate();

		super.checkExceptions(expected, caught);

		this.rollbackTransaction();
	}

}
