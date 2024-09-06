package ru.practicum.dto.complaint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.comment.Comment;
import ru.practicum.dto.enums.ComplaintStatus;
import ru.practicum.dto.user.User;

import java.time.LocalDateTime;

import static ru.practicum.dto.enums.ComplaintStatus.NEW;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "complainer_id")
    private User complainer;

    @Column(name = "create_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime create;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ComplaintStatus status = NEW;
}