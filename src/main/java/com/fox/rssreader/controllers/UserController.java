package com.fox.rssreader.controllers;

import com.fox.rssreader.auth.AccessTokenUtils;
import com.fox.rssreader.auth.UserDetailsImpl;
import com.fox.rssreader.graphql.dto.UserDTO;
import com.fox.rssreader.model.entities.User;
import com.fox.rssreader.model.repositories.UserRepository;
import graphql.GraphQLException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.transaction.Transactional;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AccessTokenUtils accessTokenUtils;

    @MutationMapping
    @Transactional
    public UserDTO login(@Argument("user") @NonNull String userName, @Argument String password) {
        var credentials = new UsernamePasswordAuthenticationToken(userName, password);
        try {
            var userCredentials = authenticationManager.authenticate(credentials);
            if (userCredentials.getPrincipal() instanceof UserDetailsImpl userDetails) {
                var user = new UserDTO(userDetails.getUser());
                user.setToken(accessTokenUtils.generateToken(userDetails.getUser(),
                        new AccessTokenUtils.RequestParametersImpl()));

                return user;
            }
            return null;
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Login failed");
        }
    }

    @MutationMapping
    @PreAuthorize("isAnonymous()")
    public Boolean register(@Argument("user") @NonNull String userName,
                            @Argument @NonNull String password,
                            @Argument String passwordConfirmation) {
        if (userName.isBlank()) {
            throw new GraphQLException("Empty username");
        }
        if (password.isBlank()) {
            throw new GraphQLException("Blank password");
        }
        if (passwordConfirmation != null && !passwordConfirmation.equals(password)) {
            throw new GraphQLException("Password confirmation doesn't match");
        }
        if (userRepository.existsByLogin(userName)) {
            throw new GraphQLException("User already exists");
        }

        User user = new User();
        user.setLogin(userName);
        user.setName(userName);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return true;
    }


}
