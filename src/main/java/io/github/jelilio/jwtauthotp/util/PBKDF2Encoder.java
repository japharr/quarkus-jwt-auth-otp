package io.github.jelilio.jwtauthotp.util;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.RequestScoped;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@RequestScoped
public class PBKDF2Encoder {
  @ConfigProperty(name = "io.jelilio.github.jwt-auth-otp.password.secret")  String secret;
  @ConfigProperty(name = "io.jelilio.github.jwt-auth-otp.password.iteration")  Integer iteration;
  @ConfigProperty(name = "io.jelilio.github.jwt-auth-otp.password.keylength")  Integer keylength;

  public String encode(CharSequence cs) {
    try {
      byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
          .generateSecret(new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, keylength))
          .getEncoded();
      return Base64.getEncoder().encodeToString(result);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
      throw new RuntimeException(ex);
    }
  }
}
