package com.hasl.gorest.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;
}