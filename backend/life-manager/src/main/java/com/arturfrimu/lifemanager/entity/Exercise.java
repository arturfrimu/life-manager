package com.arturfrimu.lifemanager.entity;

import jakarta.persistence.*;
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
@Table(name = "exercise", schema = "life_manager")
public class Exercise extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 128)
    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Size(max = 64)
    @Column(name = "type", length = 64)
    private String type;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;
}
