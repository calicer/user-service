package dev.vishal.userservice.dto;

import dev.vishal.userservice.models.Role;
import dev.vishal.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDto {

    private String email;
    private Set<Role> roles;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
    }

}
