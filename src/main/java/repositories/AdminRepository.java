
package repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Admin;
import domain.Company;
import domain.Position;
import domain.Rookie;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	@Query("select m from Admin m join m.userAccount u where u.username = ?1")
	public Admin getAdminByUsername(String username);

	//The minimum, the maximum, the average, and the standard deviation of the
	//number of curricula per rookie.

	@Query("select max(h.curriculums.size) from Rookie h")
	public Float maxCurriculumPerRookie();

	@Query("select min(h.curriculums.size) from Rookie h")
	public Float minCurriculumPerRookie();

	@Query("select avg(h.curriculums.size) from Rookie h")
	public Float avgCurriculumPerRookie();

	@Query("select stddev(h.curriculums.size) from Rookie h")
	public Float stddevCurriculumPerRookie();

	/*
	 * The minimum, the maximum, the average, and the standard deviation of the
	 * number of results in the finders.
	 */

	@Query("select min(a.positions.size) from Finder a where a.lastEdit > ?1")
	public Float minResultFinders(Date date);

	@Query("select max(a.positions.size) from Finder a where a.lastEdit > ?1")
	public Float maxResultFinders(Date date);

	@Query("select avg(a.positions.size) from Finder a where a.lastEdit > ?1")
	public Float avgResultFinders(Date date);

	@Query("select stddev(a.positions.size) from Finder a where a.lastEdit > ?1")
	public Float stddevResultFinders(Date date);

	@Query("select (cast((select count(a) from Finder a where a.positions.size = 0 AND a.lastEdit > ?1) as float)/(select count(c) from Finder c where c.positions.size > 0 AND c.lastEdit > ?1)) from Configuration b")
	public Float ratioEmptyFinder(Date date);

	@Query("select count(c) from Finder c where c.lastEdit > ?1")
	public Integer numberNonEmptyFinders(Date date);

	/*
	 * The minimum, the maximum, the average, and the standard deviation of the
	 * number of positions per company.
	 */

	@Query("select min(a.positions.size) from Company a")
	public Float minPositionsCompany();

	@Query("select max(a.positions.size) from Company a")
	public Float maxPositionsCompany();

	@Query("select avg(a.positions.size) from Company a")
	public Float avgPositionsCompany();

	@Query("select stddev(a.positions.size) from Company a")
	public Float stddevPositionsCompany();

	/*
	 * The minimum, the maximum, the average, and the standard deviation of the
	 * number of applications per rookie.
	 */

	@Query("select min(a.applications.size) from Rookie a")
	public Float minApplicationsRookie();

	@Query("select max(a.applications.size) from Rookie a")
	public Float maxApplicationsRookie();

	@Query("select avg(a.applications.size) from Rookie a")
	public Float avgApplicationsRookie();

	@Query("select stddev(a.applications.size) from Rookie a")
	public Float stddevApplicationsRookie();

	/*
	 * Companies that have offered more positions.
	 */

	@Query("select b from Company b where b.positions.size = (select max(a.positions.size) from Company a)")
	public List<Company> companiesMorePositions();

	/*
	 * Rookies who have made more applications.
	 */

	@Query("select b from Rookie b where b.applications.size = (select max(a.applications.size) from Rookie a)")
	public List<Rookie> rookiesMoreApplications();

	/*
	 * The minimum, the maximum, the average, and the standard deviation of the
	 * salaries offered.
	 */

	@Query("select min(a.offeredSalary) from Position a")
	public Float minSalary();

	@Query("select max(a.offeredSalary) from Position a")
	public Float maxSalary();

	@Query("select avg(a.offeredSalary) from Position a")
	public Float avgSalaries();

	@Query("select stddev(a.offeredSalary) from Position a")
	public Float stddevSalaries();

	/*
	 * Best and Worst Position in terms od salary.
	 */

	@Query("select b from Position b where b.offeredSalary = (select max(a.offeredSalary) from Position a)")
	public List<Position> bestSalaryPositions();

	@Query("select b from Position b where b.offeredSalary = (select min(a.offeredSalary) from Position a)")
	public List<Position> worstSalaryPositions();

	@Query("select ((select round(avg(l.score),1) from Company c join c.positions p join p.audits l where c = d and l.isDraftMode = false)/10) from Company d")
	public List<Double> computeScore();

}
