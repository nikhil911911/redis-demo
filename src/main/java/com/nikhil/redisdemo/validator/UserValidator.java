package com.nikhil.redisdemo.validator;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    public interface ErrorMessages{
        String INVALID_EMAIL="Invalid Email";
    }

    public static void validate(String email) {
        Pattern pattern=Pattern.compile("^\\S+@\\S+\\.\\S+$");
        Matcher emailMatcher=pattern.matcher(email);
        if(!emailMatcher.matches()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_EMAIL);
        }
    }
}
