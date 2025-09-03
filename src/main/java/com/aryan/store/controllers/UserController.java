package com.aryan.store.controllers;

import com.aryan.store.dtos.ChangePasswordRequest;
import com.aryan.store.dtos.RegisterUserRequest;
import com.aryan.store.dtos.UpdateUserRequest;
import com.aryan.store.dtos.UserDto;
import com.aryan.store.entities.User;
import com.aryan.store.mappers.UserMapper;
import com.aryan.store.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public List<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "",name = "sort") String sort){

        if(!Set.of("name","email").contains(sort)){
            sort="name";
        }
      List<User> users=  userRepository.findAll(Sort.by(sort).descending());

      List<UserDto> userDtos = new ArrayList<>();

      for(User user:users){
         // userDtos.add(new UserDto(user.getId(),user.getName(),user.getEmail()));
          UserDto userDto = userMapper.toDto(user);
          userDtos.add(userDto);
      }

      return userDtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
       var user= userRepository.findById(id).orElse(null);
       if(user == null){
           return ResponseEntity.notFound().build();
       }

       return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest userRequest, UriComponentsBuilder uriBuilder){// ResponseEntity<?> means any type of datatype in ResponseEntity- it gives more flexibility
      //  return userDto;

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return ResponseEntity.badRequest().body(Map.of("email","Email already exists"));
        }
        var user=userMapper.toEntity(userRequest);
        System.out.println(user);
        userRepository.save(user);
        UserDto userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "id") Long id, @RequestBody UpdateUserRequest userRequest){

        User user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        userMapper.update(userRequest, user); // we are copying the fields of userRequest to the user object we got from the findById

        userRepository.save(user); // under the hood it checks using the id field, if it matches then that means that the record is already there in the table so it updates that record

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id){

        User user= userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest changePasswordRequest){
        User user= userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        if(!changePasswordRequest.getOldPassword().equals(user.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }



}
