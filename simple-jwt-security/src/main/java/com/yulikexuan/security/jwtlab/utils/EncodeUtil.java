//: com.yulikexuan.utils.jwtlab.utils.EncodeUtil.java


package com.yulikexuan.security.jwtlab.utils;


import io.jsonwebtoken.io.Encoders;


public class EncodeUtil {

    public static String base64Encode(byte[] data) {
        return Encoders.BASE64.encode(data);
    }

    public static String base64URLEncode(byte[] data) {
        return Encoders.BASE64URL.encode(data);
    }

}///:~