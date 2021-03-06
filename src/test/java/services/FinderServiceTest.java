package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Finder;
import domain.Position;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	private FinderService finderService;
	@Autowired
	private RookieService rookieService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private ConfigurationService configurationService;
	
	/**
	 * R17. An actor who is authenticated as a rookie must be able to:
	 *
	 * 2. Manage his or her finder, which involves clearing it.
	 * 
	 * Ratio of data coverage: 2/3 = 66.67%
	 * - Access as a rookie or not.
	 * - Clean a finder that belongs to the rookie logged in or not.
	 * 
	 **/
	
	
	/**
	 * Sentence Coverage:
	 * 			FinderService: 43.0%
	 * 
	 * 
	 */
	@Test
	public void driverCleanFinder() {
		Finder finder = this.rookieService.getRookieByUsername("rookie1").getFinder();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is cleaning his finder
				 **/
				{ "rookie1", finder, null },
				/**
				 * NEGATIVE TEST: Rookie is trying to clean another rookie finder
				 **/
				{ "rookie2", finder, IllegalArgumentException.class}
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderCleanTemplate((String) testingData[i][0], (Finder) testingData[i][1],
					(Class<?>) testingData[i][2]);

	}

	private void finderCleanTemplate(String rookie, Finder finder, Class<?> expected) {
		finder.setPositions(new ArrayList<Position> ());
		this.finderService.save(finder);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			this.finderService.getFinalPositionsAndCleanFinder(finder);
			
			Assert.isTrue(this.finderService.findOne(finder.getId()).getPositions().size() == this.positionService.getFinalPositions().size());
			
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
	 * 2. Manage his or her finder, which involves listing its contents.
	 * 
	 * Ratio of data coverage: 2/3 = 66.67%
	 * - Access as a rookie or not.
	 * - List positions of a finder that belongs to the rookie logged in or not.
	 * 
	 **/
	@Test
	public void driverListFinder() {
		Finder finder = this.rookieService.getRookieByUsername("rookie1").getFinder();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is listing his finder
				 **/
				{ "rookie1", finder, null },
				/**
				 * NEGATIVE TEST: Rookie is trying to list another finder
				 **/
				{ "rookie2", finder, IllegalArgumentException.class }
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderListTemplate((String) testingData[i][0], (Finder) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void finderListTemplate(String rookie, Finder finder, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			this.finderService.finderList(finder);
			
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
	 * 2. Manage his or her finder, which involves updating the search criteria.
	 * 
	 * Ratio of data coverage: 2/3 = 66.67%
	 * - Access as a rookie or not.
	 * - Edit a finder that belongs to the rookie logged in or not.
	 * 
	 **/
	@Test
	public void driverUpdateFinder() {
		Finder finder = this.rookieService.getRookieByUsername("rookie1").getFinder();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is updating his finder
				 **/
				{ "rookie1", finder, 1400., null },
				/**
				 * NEGATIVE TEST: Rookie is trying to update another finder
				 **/
				{ "rookie2", finder, 1400., IllegalArgumentException.class }
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderUpdateTemplate((String) testingData[i][0], (Finder) testingData[i][1], (Double) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	private void finderUpdateTemplate(String rookie, Finder finder, Double minSalary, Class<?> expected) {
		Date date = new Date();
	
		finder.setDeadLine(null);
		finder.setKeyWord("");
		finder.setLastEdit(date);
		finder.setMaxDeadLine(null);
		finder.setMinSalary(minSalary);
		this.finderService.save(finder);
		
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			this.finderService.filterPositionsByFinder(finder);
			this.finderService.flush();
			
			Finder finderFounded = this.finderService.findOne(finder.getId());
			List<Position> positions = finderFounded.getPositions();
			
			Assert.isTrue(positions.size() == this.finderService.getPositionsByMinSalary(minSalary).size());
			
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
	 * 2. Manage his or her finder, which involves listing its contents.
	 * 
	 * Ratio of data coverage: 2/3 = 66.67%
	 * - Access as a rookie or not.
	 * - List the content of a finder that belongs to the rookie logged in or not.
	 * 
	 **/
	@Test
	public void driverListSkillsAndTechnologiesFinder() {
		Finder finder = this.rookieService.getRookieByUsername("rookie1").getFinder();

		Object testingData[][] = {

				/**
				 * POSITIVE TEST: Rookie is listing his finder
				 **/
				{ "rookie1", finder, null },
				/**
				 * NEGATIVE TEST: Rookie is trying to list another finder
				 **/
				{ "rookie2", finder, IllegalArgumentException.class }
		};

		for (int i = 0; i < testingData.length; i++)
			this.finderSkillsAndTechnologiesTemplate((String) testingData[i][0], (Finder) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void finderSkillsAndTechnologiesTemplate(String rookie, Finder finder, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.startTransaction();
			super.authenticate(rookie);
			
			finder.setKeyWord("xp");
			this.finderService.filterPositionsByFinder(finder);
			this.finderService.flush();
			
			Finder finderFounded = this.finderService.findOne(finder.getId());
			
			this.positionService.getSkillsAsRookie(finderFounded.getPositions().get(0).getId());
			this.positionService.getTechnologiesAsRookie(finderFounded.getPositions().get(0).getId());
			
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.rollbackTransaction();
		}

		super.checkExceptions(expected, caught);

	}

}
