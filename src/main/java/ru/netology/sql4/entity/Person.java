package ru.netology.sql4.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.netology.sql4.entity.enums.Gender;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "persons")
public class Person {

    @EmbeddedId
    private PersonalData personalData;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 9)
    private String phoneNumber;

    @ManyToOne(optional = false)
    private City city;

}
