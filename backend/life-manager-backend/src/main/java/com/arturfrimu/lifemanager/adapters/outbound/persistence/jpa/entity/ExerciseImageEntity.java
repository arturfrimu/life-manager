package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
@AuditOverride(forClass = BaseEntity.class)
@Entity
@Table(name = "exercise_images", schema = "life_manager")
@IdClass(ExerciseImageEntity.ExerciseImagesId.class)
public class ExerciseImageEntity extends BaseEntity {

    @Id
    @Column(name = "exercise_id", nullable = false)
    private UUID exerciseId;
    @Id
    @Column(name = "image_id", nullable = false)
    private UUID imageId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ExerciseImagesId implements Serializable {
        private UUID exerciseId;
        private UUID imageId;
    }
}
