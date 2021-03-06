package snippet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import snippet.model.Code;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeRepository extends CrudRepository<Code, Long> {

    Optional<Code> findByUuid(UUID id);

    List<Code> findAllByRestrictedByTimeAndRestrictedByViewsOrderByDateDesc(boolean time, boolean views);

    @Transactional
    void deleteByUuid(UUID uuid);
}
