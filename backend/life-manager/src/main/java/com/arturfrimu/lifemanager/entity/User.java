package com.arturfrimu.lifemanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
@Entity
@Table(name = "user", schema = "life_manager")
public class User extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 128)
    @Column(name = "username", nullable = false, length = 128, unique = true)
    private String username;

    @Email
    @Size(max = 256)
    @Column(name = "email", nullable = false, length = 256, unique = true)
    private String email;
}
