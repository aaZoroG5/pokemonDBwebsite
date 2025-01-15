package com.example.capstoneWebsite.service;

import com.example.capstoneWebsite.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

//still need to add security dependency
public interface UserService extends UserDetailsService {
    public void save(UserDTO userDTO);
    public UserDTO findUserByEmail(String email);
}
