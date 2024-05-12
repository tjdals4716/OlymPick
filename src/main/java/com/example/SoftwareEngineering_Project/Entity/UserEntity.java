package com.example.SoftwareEngineering_Project.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uid;
    private String password;
    private String nickname;
    private String gender;
    private String age;
    private String mbti;

    @ManyToMany
    @JoinTable(name = "user_liked_reviews",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id"))
    private Set<ReviewEntity> likedReviews = new HashSet<>();

    public UserEntity(Long id, String uid, String password, String nickname,
                      String gender, String age, String mbti) {
        this.id = id;
        this.uid = uid;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.mbti = mbti;
    }
}