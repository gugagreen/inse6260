package ca.concordia.createPassword;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * To use this password generator you must have the BCryptPasswordEncoder import.
 * Suggested method is to make a temporary folder in the src/main/java/ca/concordia called /createPassword
 * copy this .java file there and generate as many passwords as needed, copy them in the password field on the Server database for your new users. 
*/
public class PasswordEncoderGenerator {

  public static void main(String[] args) {

	int i = 0;
	while (i < 10) {
		String password = "123456";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);

		System.out.println(hashedPassword);
		i++;
	}

  }
}
