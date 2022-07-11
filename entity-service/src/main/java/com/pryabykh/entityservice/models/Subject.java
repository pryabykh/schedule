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
    private int firstGrade;

    @Column(name = "second_grade")
    private int secondGrade;

    @Column(name = "third_grade")
    private int thirdGrade;

    @Column(name = "fourth_grade")
    private int fourthGrade;

    @Column(name = "fifth_grade")
    private int fifthGrade;

    @Column(name = "sixth_grade")
    private int sixthGrade;

    @Column(name = "seventh_grade")
    private int seventhGrade;

    @Column(name = "eighth_grade")
    private int eighthGrade;

    @Column(name = "ninth_grade")
    private int ninthGrade;

    @Column(name = "tenth_grade")
    private int tenthGrade;

    @Column(name = "eleventh_grade")
    private int eleventhGrade;

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
