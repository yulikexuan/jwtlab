//: com.yulikexuan.security.jwtlab.utils.SigningUtil.java


package com.yulikexuan.security.jwtlab.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;

import javax.crypto.Mac;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;
import java.util.Random;


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

    public static class MySigningKeyResolver extends SigningKeyResolverAdapter {

        /*
         * The JwtParser will invoke the resolveSigningKey method after parsing
         * the JWS JSON, but before verifying the jws signature.
         *
         * This allows you to inspect the JwsHeader and Claims arguments for any
         * information that can help you look up the Key to use for verifying
         * that specific jws.
         *
         * This is very powerful for applications with more complex security
         * models that might use different keys at different times or for
         * different users or customers.
         *
         */
        @Override
        public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {

            // Inspect the header or claims, lookup and return the signing key

            String keyId = jwsHeader.getKeyId();

            Key key = SigningUtil
                    .getKey(Long.valueOf(keyId))
                    .orElseThrow(IllegalArgumentException::new);

            return key;
        }
    }

    /*
     * the JWT specification RFC 7518, Sections 3.2 through 3.5 requires
     * (mandates) that you MUST use keys that are sufficiently strong for a
     * chosen algorithm.
     *
     * This means that JJWT - a specification-compliant library - will also
     * enforce that you use sufficiently strong keys for the algorithms you
     * choose.
     *
     * If you provide a weak key for a given algorithm, JJWT will reject it and
     * throw an exception.
     *
     * That said, in keeping with best practices and increasing key lengths for
     * security longevity, JJWT recoommends that you use:
     *     at least 2048 bit keys with RS256 and PS256
     *     at least 3072 bit keys with RS384 and PS384
     *     at least 4096 bit keys with RS512 and PS512
     *
     * the requirement is the RSA key (modulus) length in bits MUST be >= 2048 bits.
     *
     * JJWT has provided the io.jsonwebtoken.security.Keys utility class that
     * can generate sufficiently secure keys for any given JWT signature
     * algorithm you might want to use.
     */
    private static Key SECRET_KEY_HS256;

    private static Long KEY_ID_1;
    private static Long KEY_ID_2;

    private static Map<Long, Key> keys;

    static {
        /*
         * Under the hood, JJWT uses the JCA provider's KeyGenerator to create
         * a secure-random key with the correct minimum length for the given
         * algorithm.
         *
         * JCA Name: Java Cryptography Architecture Name
         */
        SECRET_KEY_HS256 = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        Random random = new Random(System.currentTimeMillis());
        KEY_ID_1 = Long.valueOf(random.nextInt(999));
        KEY_ID_2 = Long.valueOf(random.nextInt(999));

        keys = Map.of(KEY_ID_1, SECRET_KEY_HS256,
                KEY_ID_2, Keys.secretKeyFor(SignatureAlgorithm.HS256));
    }

    public static Key getHS256SecretKey() {
        return SECRET_KEY_HS256;
    }

    public static byte[] hmacSha256(byte[] data, Key key)
            throws NoSuchAlgorithmException, InvalidKeyException {

        /*
         * Returns a Mac object that implements the specified MAC algorithm
         *
         * JCA Name: Java Cryptography Architecture Name
         */
        Mac mac = Mac.getInstance(SignatureAlgorithm.HS256.getJcaName());

        /*
         * Initializes this Mac object with the given key
         */
        mac.init(key);

        /*
         * Processes the given array of bytes and finishes the MAC operation
         */
        return mac.doFinal(data);
    }

    public static Optional<Key> getKey(Long keyId) {
        return Optional.ofNullable(keys.get(keyId));
    }

    public static Long[] getKeyIds() {
        return new Long[] {KEY_ID_1, KEY_ID_2};
    }

}///:~