package com.example.SoftwareEngineering_Project.Controller;

import com.example.SoftwareEngineering_Project.DTO.UserDTO;
import com.example.SoftwareEngineering_Project.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getUserByUid(@PathVariable String uid) {
        UserDTO user = userService.getUserByUid(uid);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/check-uid")
    public ResponseEntity<Boolean> isUidDuplicate(@RequestParam String uid) {
        boolean isDuplicate = userService.isUidDuplicate(uid);
        return ResponseEntity.ok(isDuplicate);
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> isNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = userService.isNicknameDuplicate(nickname);
        return ResponseEntity.ok(isDuplicate);
    }
}