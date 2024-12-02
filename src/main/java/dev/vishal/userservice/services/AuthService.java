package dev.vishal.userservice.services;

import dev.vishal.userservice.dto.UserDto;
import dev.vishal.userservice.exceptions.UserAlreadyExistsException;
import dev.vishal.userservice.models.Session;
import dev.vishal.userservice.models.SessionStatus;
import dev.vishal.userservice.models.User;
import dev.vishal.userservice.repositories.SessionRepository;
import dev.vishal.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<UserDto> login(String email, String password){
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("User name is invalid");
        }

        User user = userOptional.get();

        if(!passwordEncoder.matches(password, user.getPassword())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String token = RandomStringUtils.randomAscii(20);
        MultiValueMapAdapter<String, String > headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add("AUTH_TOKEN", token);

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = UserDto.from(user);

        ResponseEntity<UserDto> response = new ResponseEntity<>(
                userDto,
                headers,
                HttpStatus.OK
        );

        return response;

    }

    public UserDto signUp(String email, String password) throws UserAlreadyExistsException {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if(!userOptional.isEmpty()){
            throw new UserAlreadyExistsException("User with " + email + " already exists!!");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return UserDto.from(user);
    }

    public void logout(String token, Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUserId(token, userId);

        if(sessionOptional.isEmpty()){
            return;
        }

        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.LOGGED_OUT);

        sessionRepository.save(session);

        return;
    }
}
