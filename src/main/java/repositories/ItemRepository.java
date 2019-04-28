
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Item;
import domain.Provider;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
	
	@Query("select p.items from Provider p where p = ?1")
	List<Item> getItemsByProvider(Provider provider);

	@Query("select l from Provider p join p.items i join i.links l where i.id = ?1 and p.id = ?2")
	List<String> getLinksOfItem(Integer itemId, Integer providerId);

	@Query("select pics from Provider p join p.items i join i.pictures pics where i.id = ?1 and p.id = ?2")
	List<String> getPicturesOfItem(Integer itemId, Integer id);

}
