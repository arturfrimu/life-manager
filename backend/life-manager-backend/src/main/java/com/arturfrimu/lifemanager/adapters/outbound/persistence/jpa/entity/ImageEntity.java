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
@Table(name = "image", schema = "life_manager")
public class ImageEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "object_key", nullable = false)
    private String objectKey;
    @Column(name = "bucket_name", nullable = false)
    private String bucketName;
    @Column(name = "url", nullable = false)
    private String url;
    @Column(name = "content_type", nullable = false)
    private String contentType;
    @Column(name = "size", nullable = false)
    private Long size;
}
