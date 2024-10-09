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

    public void saveUser(User user, Long roleId) {
        // Encrypt the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user); // Save the user first

        // Fetch the Role by roleId
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Role not found"));

        // Create and assign user and role to the UserRole
        UserRole userRole = new UserRole();
        userRole.setUser(user);  // Set the User object, not just the userId
        userRole.setRole(role);  // Set the Role object, not just the roleId

        // Save the UserRole to associate the user with the role
        userRoleRepository.save(userRole);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        List<Role> roles = userRoles.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        // Debugging: Ensure roles are mapped correctly
        System.out.println("Roles for user: " + user.getEmail() + " are: " + roles);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(roles)
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName())) // Add ROLE_ prefix
                .collect(Collectors.toList());
    }
}
