package com.issoft.ticketstoreapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.issoft.ticketstoreapp.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "login can not be empty")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Email(regexp = ".+[@].+[\\.].+", message = "invalid email address")
    private String email;

    @Pattern(regexp = "(\\+\\d{12})", message = "phone should contain 12 digits and start with '+'")
    private String phone;

    private String firstName;

    private String lastName;

    private Set<Role> roles = new HashSet<>();
}