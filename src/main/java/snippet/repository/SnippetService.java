package snippet.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import snippet.exception.SnippetNotFoundException;
import snippet.model.Code;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Component
public class SnippetService {

    private final CodeRepository codeRepository;

    @Autowired
    public SnippetService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public void add(final Code code) {
        codeRepository.save(code);
    }

    public Code get(final UUID uuid) {
        return codeRepository.findByUuid(uuid).orElseThrow(SnippetNotFoundException::new);
    }

    public void updateTimeAndViews(final Code snippet) {
        if (snippet.isRestrictedByViews()) {
            if (snippet.getViews() == 0) {
                codeRepository.deleteByUuid(snippet.getUuid());
                throw new SnippetNotFoundException();
            }
            snippet.setViews(snippet.getViews() - 1);
            codeRepository.save(snippet);

        }
        if (snippet.isRestrictedByTime()) {
            long difference = LocalDateTime.now()
                    .until(snippet.getDate().plusSeconds(snippet.getTime()), ChronoUnit.SECONDS);
            if (difference <= 0) {
                codeRepository.deleteByUuid(snippet.getUuid());
                throw new SnippetNotFoundException();
            }
            snippet.setTime(difference);
        }
    }

    public List<Code> latestSnippets() {
        List<Code> list = codeRepository.findAllByRestrictedByTimeAndRestrictedByViewsOrderByDateDesc(false, false);
        return list.size() > 10
                ? list.subList(0, 10)
                : list;
    }
}
