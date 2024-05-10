package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.UserDTO;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (isUidDuplicate(userDTO.getUid())) {
            throw new RuntimeException("중복된 사용자 ID가 존재합니다: " + userDTO.getUid());
        }

        if (isNicknameDuplicate(userDTO.getNickname())) {
            throw new RuntimeException("중복된 닉네임이 존재합니다: " + userDTO.getNickname());
        }

        UserEntity user = UserEntity.builder()
                .uid(userDTO.getUid())
                .password(userDTO.getPassword())
                .nickname(userDTO.getNickname())
                .build();

        UserEntity savedUser = userRepository.save(user);

        return UserDTO.builder()
                .id(savedUser.getId())
                .uid(savedUser.getUid())
                .password(savedUser.getPassword())
                .nickname(savedUser.getNickname())
                .build();
    }

    @Override
    public UserDTO getUserByUid(String uid) {
        UserEntity user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found with uid: " + uid));

        return UserDTO.builder()
                .id(user.getId())
                .uid(user.getUid())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .build();
    }

    @Override
    public boolean isUidDuplicate(String uid) {
        return userRepository.existsByUid(uid);
    }

    @Override
    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}