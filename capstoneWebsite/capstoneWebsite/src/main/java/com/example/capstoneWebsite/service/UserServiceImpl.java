package com.example.capstoneWebsite.service;


import com.example.capstoneWebsite.dto.UserDTO;
import com.example.capstoneWebsite.entity.Role;
import com.example.capstoneWebsite.entity.User;
import com.example.capstoneWebsite.repository.RoleRepository;
import com.example.capstoneWebsite.repository.UserRepository;
import com.example.capstoneWebsite.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j //look up
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) { //the LAZY annotation is very important to prevent
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void save(UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("User with email: " + userDTO.getEmail() + " already exists");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setRoles(List.of(roleRepository.findRoleByName("ROLE_USER")));
        user.setUsername(userDTO.getUsername());
        user.setEnabled("Y");
        userRepository.save(user);
    }

    @Override//this method checks if the input username already exists in the database
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(login);
        log.info("User found: {}", user);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new UserPrincipal(user, (List<Role>) user.getRoles());
       /* return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));*/
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return mapRoles;
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        return null;
    }
}
