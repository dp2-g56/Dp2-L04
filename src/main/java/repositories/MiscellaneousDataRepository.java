
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MiscellaneousData;

@Repository
public interface MiscellaneousDataRepository extends JpaRepository<MiscellaneousData, Integer> {

	@Query("select m from Rookie h join h.curriculums c join c.miscellaneousData m where h.id = ?1")
	public List<MiscellaneousData> getMiscellaneousDataOfRookie(int rookieId);

	@Query("select m from Rookie h join h.curriculums c join c.miscellaneousData m where h.id = ?1 and m.id = ?2")
	public MiscellaneousData getMiscellaneousDataOfRookie(int rookieId, int miscellaneousDataId);
	
}
