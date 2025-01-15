package com.example.capstoneWebsite.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//this DTO is to transfer data between User entity and User service
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String email;
    private String password;
    private String username;

}
