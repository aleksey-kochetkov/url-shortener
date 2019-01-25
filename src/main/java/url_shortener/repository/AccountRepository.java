package url_shortener.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import url_shortener.model.Account;

@Repository
public interface AccountRepository
       extends CrudRepository<Account, String>, AccountRepositoryCustom {
}
