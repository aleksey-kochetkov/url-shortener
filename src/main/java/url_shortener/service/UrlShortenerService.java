package url_shortener.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.security.Principal;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

import url_shortener.RegisterPageRequest;
import url_shortener.model.Account;
import url_shortener.model.Reference;
import url_shortener.repository.AccountRepository;
import url_shortener.repository.ReferenceRepository;

@Service
public class UrlShortenerService {
    @PersistenceContext
    private EntityManager entityManager;
    private AccountRepository accountRepository;
    private ReferenceRepository referenceRepository;
    private RandomValueStringGenerator generator;

    @Transactional
    public Account createAccount(Account account) {
        account.setPassword(this.generator.generate());
        this.accountRepository.persist(account);
        return account;
    }

    @Transactional(readOnly = true)
    public List<Account> findAllAccounts() {
        return (List<Account>) this.accountRepository.findAll();
    }

    @Transactional
    public Reference createReference(RegisterPageRequest request,
                                   Principal principal, String baseUrl) {
        Account account = this.entityManager.getReference(
                                     Account.class, principal.getName());
        Reference reference = new Reference(account, request.getUrl(),
            request.getRedirectType() == null
                                       ? 302 : request.getRedirectType(),
             String.format("%s/%s", baseUrl, this.generateIdentifier()));
        this.referenceRepository.save(reference);
        return reference;
    }

    @Transactional(readOnly = true)
    public Map<String, Integer> findReferenceByAccountId(
                                                      String accountId) {
        Map<String, Integer> result = new HashMap<>();
        for(Reference r : this.referenceRepository
                                    .findByAccountAccountId(accountId)) {
            result.put(r.getUrl(), r.getCount());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Reference findReferenceByShortUrl(String shortUrl) {
        Reference result = null;
        List<Reference> references =
                       this.referenceRepository.findByShortUrl(shortUrl);
        if (!references.isEmpty()) {
            result = references.get(0);
            result.incCount();
            this.referenceRepository.save(result);
        }
        return result;
    }

    @Autowired
    public void setAccountRepository(
                                   AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setReferenceRepository(ReferenceRepository repository) {
        this.referenceRepository = repository;
    }

    @Autowired
    public void setRandomValueStringGenerator(
                                  RandomValueStringGenerator generator) {
        this.generator = generator;
    }

    private String generateIdentifier() {
        RandomValueStringGenerator generator =
                                       new RandomValueStringGenerator(6);
        return generator.generate();
    }
}
