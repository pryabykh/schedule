package com.pryabykh.entityservice.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "first_grade")
    private Integer firstGrade;

    @Column(name = "second_grade")
    private Integer secondGrade;

    @Column(name = "third_grade")
    private Integer thirdGrade;

    @Column(name = "fourth_grade")
    private Integer fourthGrade;

    @Column(name = "fifth_grade")
    private Integer fifthGrade;

    @Column(name = "sixth_grade")
    private Integer sixthGrade;

    @Column(name = "seventh_grade")
    private Integer seventhGrade;

    @Column(name = "eighth_grade")
    private Integer eighthGrade;

    @Column(name = "ninth_grade")
    private Integer ninthGrade;

    @Column(name = "tenth_grade")
    private Integer tenthGrade;

    @Column(name = "eleventh_grade")
    private Integer eleventhGrade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subject subject = (Subject) o;
        return id != null && Objects.equals(id, subject.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
