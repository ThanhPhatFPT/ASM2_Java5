package com.example.java5_petshop.service;

import com.example.java5_petshop.model.Role;
import com.example.java5_petshop.model.User;
import com.example.java5_petshop.model.UserRole;
import com.example.java5_petshop.repository.RoleRepository;
import com.example.java5_petshop.repository.UserRepository;
import com.example.java5_petshop.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Save a user with a specific role
    public void saveUser(User user, Role role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user); // Save user first

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role); // Assign passed role
        userRoleRepository.save(userRole); // Save user role
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }


        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        List<Role> roles = userRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(roles)
        );
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

}
