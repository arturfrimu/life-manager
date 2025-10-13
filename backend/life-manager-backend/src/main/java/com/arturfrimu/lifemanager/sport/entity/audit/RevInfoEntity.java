package com.arturfrimu.lifemanager.sport.entity.audit;

import jakarta.persistence.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@RevisionEntity
@Table(name="revinfo", schema = "life_manager_history")
public class RevInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name="rev")
    private long rev;

    @RevisionTimestamp
    @Column(name="revtstmp")
    private Long revtstmp;
}
