//: com.yulikexuan.security.jwtlab.utils.SigningUtil.java


package com.yulikexuan.security.jwtlab.utils;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.Mac;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


/*
 * A Message Authentication Code or a MAC provides a way to guarantee that a
 * message (a byte array) has not been modified in transit.
 *
 * It is similar to a message digest to calculate a hash, but uses a secret key
 * so that only a person with the secret key can verify the authenticity of the
 * message.
 *
 * Using a MAC to ensure safe transmission of messages requires that the two
 * parties share a secret key to be able to generate and verify the MAC.
 *
 * There are two approaches available here – the two parties can share a secret
 * key directly. Or the secret key can be generated using a password.
 * We investigate both approaches below.
 */
public class SigningUtil {

    private static final String HMAC_SHA_256_ALGORITHM = "HmacSHA256";

    private static Key SECRET_KEY_HS256;

    static {
        SECRET_KEY_HS256 = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public static Key getHS256SecretKey() {
        return SECRET_KEY_HS256;
    }

    public static byte[] hmacSha256(byte[] data, Key key)
            throws NoSuchAlgorithmException, InvalidKeyException {

        /*
         * Returns a Mac object that implements the specified MAC algorithm
         */
        Mac mac = Mac.getInstance(HMAC_SHA_256_ALGORITHM);

        /*
         * Initializes this Mac object with the given key
         */
        mac.init(key);

        /*
         * Processes the given array of bytes and finishes the MAC operation
         */
        return mac.doFinal(data);
    }

}///:~