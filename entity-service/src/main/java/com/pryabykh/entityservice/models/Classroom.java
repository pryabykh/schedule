package com.pryabykh.entityservice.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "teacher")
    private Teacher teacher;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "classroom_subject",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects = new HashSet<>();

    @Column(name = "creator_id")
    private Long creatorId;

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

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getClassrooms().add(this);
    }

    public void removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getClassrooms().remove(this);
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
