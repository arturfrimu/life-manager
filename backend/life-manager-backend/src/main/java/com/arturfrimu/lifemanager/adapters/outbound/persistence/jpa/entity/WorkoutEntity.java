package com.arturfrimu.lifemanager.adapters.outbound.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workout", schema = "life_manager")
public class WorkoutEntity extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 128)
    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;
}

