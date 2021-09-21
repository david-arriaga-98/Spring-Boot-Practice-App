package com.isac.ecommerce.services;

import com.isac.ecommerce.config.UserDetailsImpl;
import com.isac.ecommerce.models.RoleEnum;
import com.isac.ecommerce.models.User;
import com.isac.ecommerce.models.payload.request.SignInRequest;
import com.isac.ecommerce.models.payload.request.SignUpRequest;
import com.isac.ecommerce.models.payload.response.JwtResponse;
import com.isac.ecommerce.repositories.UserRepository;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final MailService mailService;

    private final String EMAIL_VERIFICATION_ID = "d-93b2ed892ff944e998b9571f3f6a9277";

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, MailService mailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    public JwtResponse getAuthorizedUser(Authentication authentication, String token) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(
                userDetails.getFirstname(),
                userDetails.getLastname(),
                token,
                userDetails.getId(),
                userDetails.getEmail(),
                roles.get(0)
        );
    }

    public JwtResponse signIn(SignInRequest signIn) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateJwtToken(authentication);
        return getAuthorizedUser(authentication, jwt);
    }

    public User getUserByAuthentication(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = new User();
        user.setId(userDetails.getId());
        user.setFirstname(userDetails.getFirstname());
        user.setLastname(userDetails.getLastname());
        user.setEmail(userDetails.getEmail());
        user.setEnabled(userDetails.isEnabled());
        return user;
    }

    public void signUp(SignUpRequest signUp) throws FluxCommerceException {
        if (userRepository.existsByEmail(signUp.getEmail()))
            throw new FluxCommerceException("El correo electrónico con el cuál desea registrar a este usuario, ya existe", 400, HttpStatus.BAD_REQUEST);
        String token = UUID.randomUUID().toString().replace('-', 'a');
        Email to = new Email(signUp.getEmail(), signUp.getFirstname() + " " + signUp.getLastname());
        Map<String, String> verificationParams = new HashMap<>();
        verificationParams.put("name", signUp.getFirstname() + " " + signUp.getLastname());
        verificationParams.put("verify_url", token);

        try {
            int status = mailService.sendDynamicEmail("Verificación de correo electónico", to, verificationParams, EMAIL_VERIFICATION_ID);
            if (status > 399 && status < 500)
                throw new FluxCommerceException("Ha ocurrido un error al enviar el correo de verificación", 400, HttpStatus.BAD_REQUEST);

            User user = new User();
            user.setFirstname(signUp.getFirstname());
            user.setLastname(signUp.getLastname());
            user.setEmail(signUp.getEmail());
            user.setEnabled(false);
            user.setPassword(new BCryptPasswordEncoder().encode(signUp.getPassword()));
            user.setRole(RoleEnum.ROLE_USER);
            user.setToken(token);

            userRepository.save(user);

        } catch (IOException ex) {
            throw new FluxCommerceException("Ha ocurrido un error al enviar el correo de verificación", 400, HttpStatus.BAD_REQUEST);
        }
    }

    public void verify(String token) throws FluxCommerceException {
        Optional<User> optionalUser = userRepository.findByToken(token);
        if (optionalUser.isEmpty())
            throw new FluxCommerceException("El usuario al que usted desea verificar, no se ha encontrado", 400, HttpStatus.BAD_REQUEST);
        User user = optionalUser.get();
        if (user.getEnabled())
            throw new FluxCommerceException("Este usuario ya verificado anteriormente", 403, HttpStatus.FORBIDDEN);
        user.setEnabled(true);
        userRepository.save(user);
    }
}
