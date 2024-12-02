package dev.vishal.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class User extends BaseModel{

    private String email;
    private String password;
    @ManyToMany(fetch =  FetchType.LAZY)
    private Set<Role> roles;
}
