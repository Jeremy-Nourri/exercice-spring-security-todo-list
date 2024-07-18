package com.example.todolistapi.service;

import com.example.todolistapi.config.jwt.JwtTokenProvider;
import com.example.todolistapi.dto.BaseDtoResponse;
import com.example.todolistapi.dto.RegisterDtoPost;
import com.example.todolistapi.dto.UserDtoResponse;
import com.example.todolistapi.model.User;
import com.example.todolistapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public boolean checkIfUserExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String generateToken(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    public boolean checkIfPasswordMatches(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return passwordEncoder.matches(password, user.getPassword());
    }

    public BaseDtoResponse save(RegisterDtoPost registerDtoPost) {
        if (checkIfUserExists(registerDtoPost.getEmail())) {
            return new BaseDtoResponse("User already exists", null);
        }
        User user = new User();
        user.setEmail(registerDtoPost.getEmail());
        user.setPassword(passwordEncoder.encode(registerDtoPost.getPassword()));
        user.setName(registerDtoPost.getName());
        user.setRole(registerDtoPost.getRole());
        userRepository.save(user);
        return new BaseDtoResponse("User registered successfully", null);
    }

    public UserDtoResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return convertToDto(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDtoResponse updateById(Long id, RegisterDtoPost registerDtoPost) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEmail(registerDtoPost.getEmail());
        user.setPassword(passwordEncoder.encode(registerDtoPost.getPassword()));
        user.setName(registerDtoPost.getName());
        userRepository.save(user);
        return convertToDto(user);
    }

    public boolean deleteById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }
        userRepository.deleteById(id);
        user = userRepository.findById(id).orElse(null);
        return user == null;
    }

    public UserDtoResponse convertToDto(User user) {
        return UserDtoResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }




}
