package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.category.Category;
import ru.practicum.dto.compilation.Compilation;
import ru.practicum.dto.location.Location;
import ru.practicum.dto.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static ru.practicum.dto.event.Status.PENDING;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 20, max = 2000)
    @Column(name = "annotation")
    private String annotation;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 20, max = 7000)
    @Column(name = "description")
    private String description;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 120)
    @Column(name = "title")
    private String title;

    @Column(name = "state")
    @Builder.Default()
    @Enumerated(EnumType.STRING)
    private Status state = PENDING;

    @Column(name = "created", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default()
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "event_date", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;

    @Column(name = "published", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @Column(name = "paid")
    @Builder.Default()
    private Boolean paid = false;

    @Column(name = "request_moderation")
    @Builder.Default()
    private Boolean requestModeration = false;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator_id", referencedColumnName = "user_id")
    private User initiator;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "locations_id")
    private Location location;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "events")
    @ElementCollection
    private Set<Compilation> compilations = new HashSet<>();

    public void update(Event data) {
        if (data.getAnnotation() != null) {
            this.annotation = data.getAnnotation();
        }
        if (data.getDescription() != null) {
            this.description = data.getDescription();
        }
        if (data.getCategory() != null) {
            this.category = data.getCategory();
        }
        if (data.getEventDate() != null) {
            this.eventDate = data.getEventDate();
        }
        if (data.getLocation() != null) {
            this.location = data.getLocation();
        }
        if (data.getPaid() != null) {
            this.paid = data.getPaid();
        }
        if (data.getParticipantLimit() != null) {
            this.participantLimit = data.getParticipantLimit();
        }
        if (data.requestModeration != null) {
            this.requestModeration = data.getRequestModeration();
        }
        if (data.getTitle() != null) {
            this.title = data.getTitle();
        }
    }
}

