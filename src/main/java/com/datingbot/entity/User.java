package com.datingbot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "age")
    private int age;

    @Column(name = "sex")
    private int sex;

    @Column(name = "opposite_sex")
    private int oppositeSex;

    @Column(name = "city")
    private String city;

    @Column(name = "description")
    private String description = "";

    @Column(name = "opposite_sex_id")
    private long oppositeSexId;

    @OneToMany(mappedBy = "oppositeUserId")
    private List<Match> matches;

    @Column(name = "chat_status")
    @Enumerated(value = EnumType.STRING)
    private ChatStatus chatStatus;

    @Column(name = "language")
    @Enumerated(value = EnumType.STRING)
    private Language language;
}
