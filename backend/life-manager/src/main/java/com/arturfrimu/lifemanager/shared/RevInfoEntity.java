package com.arturfrimu.lifemanager.shared;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Setter
@Getter
@Entity
@RevisionEntity(AppRevisionListener.class)
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

    @Column(name="username")
    private String username;

    @Column(name="source_ip")
    private String sourceIp;
}

