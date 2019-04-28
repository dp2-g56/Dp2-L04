
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Auditor;

@Repository
public interface AuditorRepository extends JpaRepository<Auditor, Integer> {

	@Query("select m from Auditor m join m.userAccount u where u.username = ?1")
	public Auditor getAuditorByUsername(String username);

}
