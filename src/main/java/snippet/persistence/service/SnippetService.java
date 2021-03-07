package snippet.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snippet.exception.SnippetNotFoundException;
import snippet.model.Snippet;
import snippet.persistence.SnippetRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class SnippetService {

    private final SnippetRepository snippetRepository;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    public void add(final Snippet snippet) {
        snippetRepository.save(snippet);
    }

    public Snippet get(final UUID uuid) {
        return snippetRepository.findByUuid(uuid).orElseThrow(SnippetNotFoundException::new);
    }

    public void updateTimeAndViews(final Snippet snippet) {
        if (snippet.isRestrictedByViews()) {
            if (snippet.getViews() == 0) {
                snippetRepository.deleteByUuid(snippet.getUuid());
                throw new SnippetNotFoundException();
            }
            snippet.setViews(snippet.getViews() - 1);
            snippetRepository.save(snippet);

        }
        if (snippet.isRestrictedByTime()) {
            long difference = LocalDateTime.now()
                    .until(snippet.getDate().plusSeconds(snippet.getTime()), ChronoUnit.SECONDS);
            if (difference <= 0) {
                snippetRepository.deleteByUuid(snippet.getUuid());
                throw new SnippetNotFoundException();
            }
            snippet.setTime(difference);
        }
    }

    public List<Snippet> latestSnippets() {
        List<Snippet> list = snippetRepository.findAllByRestrictedByTimeAndRestrictedByViewsOrderByDateDesc(false, false);
        return list.size() > 10
                ? list.subList(0, 10)
                : list;
    }
}
