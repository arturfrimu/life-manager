package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity;

import com.arturfrimu.lifemanager.domain.model.Type;
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
public class ExerciseEntity extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 128)
    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "type", length = 64)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;
}

