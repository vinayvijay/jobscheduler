package com.jobscheduler.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodedPasswordGenerator {

	public static void main(String[] args) {
        String password = "demo";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode(password));
    }
	 
}
