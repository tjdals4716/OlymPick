package com.example.SoftwareEngineering_Project.DTO;

import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private Long id;
    private String phoneNumber;
    private String uid;
    private String password;
    private String nickname;
    private String gender;
    private String age;
    private String mbti;

    public static UserDTO entityToDto(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getPhoneNumber(),
                userEntity.getUid(),
                userEntity.getPassword(),
                userEntity.getNickname(),
                userEntity.getGender(),
                userEntity.getAge(),
                userEntity.getMbti()
        );
    }

    public UserEntity dtoToEntity(){
        return new UserEntity(id, phoneNumber, uid, password, nickname, gender, age, mbti);
    }
}