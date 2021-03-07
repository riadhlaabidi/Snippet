package snippet.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import snippet.model.Snippet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SnippetRepository extends CrudRepository<Snippet, Long> {

    Optional<Snippet> findByUuid(UUID id);

    List<Snippet> findAllByRestrictedByTimeAndRestrictedByViewsOrderByDateDesc(boolean time, boolean views);

    @Transactional
    void deleteByUuid(UUID uuid);
}
