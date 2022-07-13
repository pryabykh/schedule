package com.pryabykh.entityservice.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "classrooms")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "description")
    private String description;

    @Version
    @Column(name = "version")
    private int version;

    @Temporal(TemporalType.TIMESTAMP) //or @Temporal(TemporalType.DATE)
    @Column(name = "created_at", insertable = true, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP) //or @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    void onCreate() {
        Date now = new Date();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }

    @PreUpdate
    void onUpdate() {
        this.setUpdatedAt(new Date());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Classroom classroom = (Classroom) o;
        return id != null && Objects.equals(id, classroom.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
