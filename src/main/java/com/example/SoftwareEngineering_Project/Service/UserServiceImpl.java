package com.example.SoftwareEngineering_Project.Service;

import com.example.SoftwareEngineering_Project.DTO.UserDTO;
import com.example.SoftwareEngineering_Project.Entity.UserEntity;
import com.example.SoftwareEngineering_Project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    //회원가입
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (isUidDuplicate(userDTO.getUid())) {
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
        } else if (isNicknameDuplicate(userDTO.getNickname())) {
            throw new IllegalArgumentException("닉네임이 이미 존재합니다.");
        }
        UserEntity userEntity = userDTO.dtoToEntity();
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);
        logger.info("회원가입 완료! " + userEntity);
        return UserDTO.entityToDto(savedUser);
    }

    //회원 조회
    @Override
    public UserDTO getUserByUid(String uid) {
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. uid: " + uid));
        return UserDTO.entityToDto(userEntity);
    }

    //아이디 중복 확인
    @Override
    public boolean isUidDuplicate(String uid) {
        return userRepository.existsByUid(uid);
    }

    //닉네임 중복 확인
    @Override
    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //로그인
    @Override
    public UserDTO login(String uid, String password) {
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        logger.info("로그인 성공! " + userEntity);
        return UserDTO.entityToDto(userEntity);
    }

    //회원 정보 수정
    @Override
    public UserDTO updateUser(String uid, UserDTO userDTO) {
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. uid: " + uid));

        userEntity.setPassword(userDTO.getPassword());
        userEntity.setNickname(userDTO.getNickname());
        userEntity.setGender(userDTO.getGender());
        userEntity.setAge(userDTO.getAge());
        userEntity.setMbti(userDTO.getMbti());

        UserEntity updatedUser = userRepository.save(userEntity);
        logger.info("사용자 정보 업데이트 완료! " + updatedUser);
        return UserDTO.entityToDto(updatedUser);
    }

    //회원 탈퇴
    @Override
    public void deleteUser(String uid) {
        UserEntity userEntity = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. uid: " + uid));

        userRepository.delete(userEntity);
        logger.info("회원탈퇴 완료! uid: " + uid);
    }
}