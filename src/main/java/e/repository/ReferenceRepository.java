package e.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import e.model.Reference;

@Repository
public interface ReferenceRepository
                             extends CrudRepository<Reference, Integer> {
    List<Reference> findByAccountAccountId(String accountId);
    List<Reference> findByShortUrl(String shortUrl);
}
