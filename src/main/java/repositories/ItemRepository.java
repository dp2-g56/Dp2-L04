
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

}
