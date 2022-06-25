package com.springsecurity.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    //create firstName of type String
    private String firstName;
    //create lastName of type String
    private String lastName;
    // @Id
    private String userName;
    //create mobile of type String
    private String mobile;
    private String country;
    private String profilePic;
    private String emailId;
    //create password of type String
    private String password;
    //create createdDate of type String
    private String createdDate;
    //create salt of type String
    private String salt;
    //all the mentioned members must be private
}
