package com.ynov.crm.requestdto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginForm {
    @NotBlank
    @Size(min=3, max = 60, message = "username is n ot valid, is too long or too short")
    @NotEmpty(message = "user name is not valid")
    private String username;

    @NotBlank(message = "Api key is not valid")
    @Size(min = 6, max = 250, message = "Api is too long or short")
    private String userKey;

    
}