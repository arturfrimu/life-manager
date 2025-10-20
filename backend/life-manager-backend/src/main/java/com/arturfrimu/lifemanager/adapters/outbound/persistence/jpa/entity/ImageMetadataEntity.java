package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "image_metadata", schema = "life_manager")
public class ImageMetadataEntity extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "exercise_id", length = 64)
    UUID exerciseId;
    @Column(name = "object_key", length = 64)
    String objectKey;
    @Column(name = "content_type", length = 64)
    String contentType;
    @Column(name = "size_bytes")
    long sizeBytes;
}
