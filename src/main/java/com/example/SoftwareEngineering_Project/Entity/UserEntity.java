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
    private String phoneNumber;
    private String uid;
    private String password;
    private String nickname;
    private String gender;
    private String age;
    private String mbti;

    //동일 유저 중복 방지를 위해 추가한 코드
    @ManyToMany
    @JoinTable(name = "user_liked_reviews",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "review_id"))
    private Set<ReviewEntity> likedReviews = new HashSet<>();

    //동일 유저가 중복해서 무언가를 하지 못하게 할때 엔티티에 생성자를 작성하여 파라미터를 일치시킴 (이렇게 하면 DTO 오류 해결)
    public UserEntity(Long id, String phoneNumber, String uid, String password, String nickname,
                      String gender, String age, String mbti) {
        this.id = id;
        this.phoneNumber = phoneNumber; //추가
        this.uid = uid;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.mbti = mbti;
    }
}