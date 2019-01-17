package e.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;
import e.model.Account;

@Repository
public interface AccountRepository
                                extends CrudRepository<Account, String> {
}
