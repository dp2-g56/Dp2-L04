package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Curriculum;
import domain.Finder;
import domain.PersonalData;
import domain.Position;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class CurriculumServiceTest extends AbstractTest {

	@Autowired
	private CurriculumService curriculumService;
	@Autowired
	private RookieService rookieService;
	
	/**
	 * R17. An actor who is authenticated as a rookie must be able to:
	 *
	 * 1. Manage his or her curricula, which includes listing them.
	 * 
	 * Ratio of data coverage: 100%
	 *  - Access as a rookie or not
	 * 
	 **/
	@Test
	public void driverListCurriculums() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is listing his curriculums
				 **/
				{ "rookie1", null},
				/**
				 * NEGATIVE TEST: Another user is trying to list curriculums
				 **/
				{ "company1", IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listCurriculumsTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	private void listCurriculumsTemplate(String rookie, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			this.curriculumService.getCurriculumsOfLoggedRookie();
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}
	
	/**
	 * R17. An actor who is authenticated as a rookie must be able to:
	 *
	 * 1. Manage his or her curricula, which includes showing them.
	 * 
	 * Ratio of data coverage: 100%
	 *  - Access as a rookie or not.
	 *  - Show a curriculum that does not belong to the rookie logged in.
	 * 
	 **/
	@Test
	public void driverShowCurriculum() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is showing one of his curriculums
				 **/
				{ "rookie1", super.getEntityId("curriculum1"), null},
				/**
				 * NEGATIVE TEST: Rookie is trying to show a curriculum from other rookie
				 **/
				{ "rookie1", super.getEntityId("curriculum3"), IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to show a curriculum
				 **/
				{ "company1", super.getEntityId("curriculum1"), IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.showCurriculumTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void showCurriculumTemplate(String rookie, int curriculumId, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			this.curriculumService.getCurriculumOfLoggedRookie(curriculumId);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}
	
	/**
	 * R17. An actor who is authenticated as a rookie must be able to:
	 *
	 * 1. Manage his or her curricula, which includes deleting them.
	 * 
	 * Ratio of data coverage: 100%
	 *  - Access as a rookie or not.
	 *  - Delete a curriculum that does belongs to the rookie logged in or not.
	 * 
	 **/
	@Test
	public void driverDeleteCurriculum() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is deleting one of his curriculums
				 **/
				{ "rookie1", super.getEntityId("curriculum1"), null},
				/**
				 * NEGATIVE TEST: Rookie is trying to delete a curriculum from other rookie
				 **/
				{ "rookie1", super.getEntityId("curriculum3"), IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to delete a curriculum
				 **/
				{ "company1", super.getEntityId("curriculum1"), IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteCurriculumTemplate((String) testingData[i][0], (Integer) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void deleteCurriculumTemplate(String rookie, int curriculumId, Class<?> expected) {
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			this.curriculumService.deleteCurriculumAsRookie(curriculumId);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}
	
	/**
	 * R17. An actor who is authenticated as a rookie must be able to:
	 *
	 * 1. Manage his or her curricula, which includes creating them.
	 * 
	 * Ratio of data coverage: 4/7 = 57.14%
	 * - Access as a rookie or not.
	 * - 5 attributes with domain restrictions.
	 * 
	 **/
	@Test
	public void driverCreateCurriculum() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is creating a curriculum
				 **/
				{ "rookie1", "Title", "Full name", "Statement", "+34123456789", "https://github.com", "https://linkedin.com", null},
				/**
				 * NEGATIVE TEST: Another user is trying to create a curriculum
				 **/
				{ "company1", "Title", "Full name", "Statement", "+34123456789", "https://github.com", "https://linkedin.com", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Rookie is creating a curriculum with a title in blank
				 **/
				{ "rookie1", "", "Full name", "Statement", "+34123456789", "https://github.com", "https://linkedin.com", ConstraintViolationException.class},
				/**
				 * NEGATIVE TEST: Rookie is creating a curriculum with a gitHub profile in blank
				 **/
				{ "rookie1", "Title", "Full name", "Statement", "+34123456789", "", "https://linkedin.com", ConstraintViolationException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createCurriculumTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Class<?>) testingData[i][7]);

	}

	private void createCurriculumTemplate(String rookie, String title, String fullName, String statement, String phoneNumber, String gitHubProfile, String linkedinProfile, Class<?> expected) {
		
		PersonalData personalData = new PersonalData();
		personalData.setFullName(fullName);
		personalData.setStatement(statement);
		personalData.setPhoneNumber(phoneNumber);
		personalData.setGitHubProfile(gitHubProfile);
		personalData.setLinkedinProfile(linkedinProfile);
		
		Curriculum curriculum = new Curriculum();
		curriculum.setTitle(title);
		curriculum.setPersonalData(personalData);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			this.rookieService.addOrUpdateCurriculum(curriculum);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}
	
	/**
	 * R17. An actor who is authenticated as a rookie must be able to:
	 *
	 * 1. Manage his or her curricula, which includes updating them.
	 * 
	 * Ratio of data coverage: 5/8 = 62.5%
	 * - Access as a rookie or not.
	 * - Edit a curriculum that does belongs to the rookie logged in or not.
	 * - 5 attributes with domain restrictions.
	 * 
	 **/
	@Test
	public void driverUpdateCurriculum() {

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is updating a curriculum
				 **/
				{ "rookie1", super.getEntityId("curriculum1"), "Title", "Full name", "Statement", "+34123456789", "https://github.com", "https://linkedin.com", null},
				/**
				 * NEGATIVE TEST: Rookie is trying to update a curriculum of other rookie
				 **/
				{ "rookie3", super.getEntityId("curriculum1"), "Title", "Full name", "Statement", "+34123456789", "https://github.com", "https://linkedin.com", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Another user is trying to update a curriculum
				 **/
				{ "company1", super.getEntityId("curriculum1"), "Title", "Full name", "Statement", "+34123456789", "https://github.com", "https://linkedin.com", IllegalArgumentException.class},
				/**
				 * NEGATIVE TEST: Rookie is trying to update a curriculum with a title in blank
				 **/
				{ "rookie1", super.getEntityId("curriculum1"), "", "Full name", "Statement", "+34123456789", "https://github.com", "https://linkedin.com", ConstraintViolationException.class},
				/**
				 * NEGATIVE TEST: Rookie is trying to update a curriculum with a gitHub profile in blank
				 **/
				{ "rookie1", super.getEntityId("curriculum1"), "Title", "Full name", "Statement", "+34123456789", "", "https://linkedin.com", ConstraintViolationException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.updateCurriculumTemplate((String) testingData[i][0], (Integer) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7], (Class<?>) testingData[i][8]);

	}

	private void updateCurriculumTemplate(String rookie, Integer curriculumId, String title, String fullName, String statement, String phoneNumber, String gitHubProfile, String linkedinProfile, Class<?> expected) {
		Curriculum curriculum = this.curriculumService.findOne(curriculumId);
		PersonalData personalData = curriculum.getPersonalData();
		
		personalData.setFullName(fullName);
		personalData.setStatement(statement);
		personalData.setPhoneNumber(phoneNumber);
		personalData.setGitHubProfile(gitHubProfile);
		personalData.setLinkedinProfile(linkedinProfile);
		
		curriculum.setTitle(title);
		curriculum.setPersonalData(personalData);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			this.rookieService.addOrUpdateCurriculum(curriculum);
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
