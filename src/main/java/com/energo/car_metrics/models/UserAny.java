package com.energo.car_metrics.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.energo.car_metrics.models.enums.EStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users", schema = "car_metrics_rest",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = "email"),
            @UniqueConstraint(columnNames = "username")
    })
@Data
@NoArgsConstructor
public class UserAny {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Size(min = 3, max = 250)
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 250)
    private String password;
    @Enumerated(EnumType.STRING)
    private EStatus status;
    @CreationTimestamp
    private LocalDateTime created;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public UserAny(String username, String email, String password, EStatus status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }
}
