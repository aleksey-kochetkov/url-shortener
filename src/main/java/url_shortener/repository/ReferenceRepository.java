package url_shortener.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import url_shortener.model.Reference;

@Repository
public interface ReferenceRepository
                             extends CrudRepository<Reference, Integer> {
    List<Reference> findByAccountAccountId(String accountId);
    List<Reference> findByShortUrl(String shortUrl);
}
