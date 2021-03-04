package snippet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import snippet.model.Code;
import snippet.exception.SnippetNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeRepository extends CrudRepository<Code, Long> {

    Optional<Code> findByUuid(UUID id);

    List<Code> findAllByRestrictedByTimeAndRestrictedByViewsOrderByDateDesc(boolean time, boolean views);

    @Transactional
    void deleteByUuid(UUID uuid);

    default List<Code> latestTenPublicSnippets() {
        List<Code> list = findAllByRestrictedByTimeAndRestrictedByViewsOrderByDateDesc(false, false);
        return list.size() > 10
                ? list.subList(0, 10)
                : list;
    }

    default void updateTimeAndViews(Code code) {
        if (code.isRestrictedByViews()) {
            if (code.getViews() == 0) {
                deleteByUuid(code.getUuid());
                throw new SnippetNotFoundException();
            }
            updateViews(code);
        }
        if (code.isRestrictedByTime()) {
            long difference = LocalDateTime.now()
                    .until(code.getDate().plusSeconds(code.getTime()), ChronoUnit.SECONDS);
            if (difference <= 0) {
                deleteByUuid(code.getUuid());
                throw new SnippetNotFoundException();
            }
            updateTime(code, difference);
        }
    }

    @Transactional(readOnly = true)
    default void updateTime(Code code, long difference) {
        code.setTime(difference);
    }

    @Transactional
    default void updateViews(Code code) {
        code.setViews(code.getViews() - 1);
    }

}
