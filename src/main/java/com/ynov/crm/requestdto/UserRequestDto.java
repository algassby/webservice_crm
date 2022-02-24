package com.ynov.crm.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)

public class UserRequestDto {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    
}