package ru.practicum.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty("id")
    private Long userId;

    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name = "name")
    @Length(min = 2, max = 250)
    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    @Email
    @Length(min = 6, max = 254)
    @Column(name = "email")
    private String email;
}
