package dev.vishal.userservice.Controllers;


import dev.vishal.userservice.dto.LoginRequestDto;
import dev.vishal.userservice.dto.LogoutRequestDto;
import dev.vishal.userservice.dto.SignUpReuestDto;
import dev.vishal.userservice.dto.UserDto;
import dev.vishal.userservice.exceptions.UserAlreadyExistsException;
import dev.vishal.userservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request){
        ResponseEntity<UserDto> response = authService.login(request.getEmail(), request.getPassword());
        return response;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpReuestDto request) throws UserAlreadyExistsException {

        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    private ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request){

        authService.logout(request.getToken(), request.getUserId());

        return ResponseEntity.ok().build();
    }
}
