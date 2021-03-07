package snippet.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Snippet {

    @Id
    @JsonIgnore
    @Column(name = "id", nullable = false)
    private UUID uuid;

    @Column(name = "code_text", columnDefinition = "TEXT", nullable = false)
    private String code;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_created", nullable = false)
    private LocalDateTime date;

    @Column(name = "time_limit", nullable = false)
    private long time;

    @Column(name = "views_limit", nullable = false)
    private long views;

    @JsonIgnore
    @Column(name = "is_restricted_by_views", nullable = false)
    private boolean restrictedByViews;

    @JsonIgnore
    @Column(name = "is_restricted_by_time", nullable = false)
    private boolean restrictedByTime;

    public Snippet() {
        date = LocalDateTime.now();
        uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public boolean isRestrictedByTime() {
        return restrictedByTime;
    }

    public void setRestrictedByTime(boolean restrictedByTime) {
        this.restrictedByTime = restrictedByTime;
    }

    public boolean isRestrictedByViews() {
        return restrictedByViews;
    }

    public void setRestrictedByViews(boolean restrictedByViews) {
        this.restrictedByViews = restrictedByViews;
    }

}