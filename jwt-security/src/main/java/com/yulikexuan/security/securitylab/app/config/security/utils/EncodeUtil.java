//: com.yulikexuan.utils.securitylab.config.utils.utils.EncodeUtil.java


package com.yulikexuan.security.securitylab.app.config.security.utils;


import io.jsonwebtoken.io.Encoders;


public class EncodeUtil {

    public static String base64Encode(byte[] data) {
        return Encoders.BASE64.encode(data);
    }

    public static String base64URLEncode(byte[] data) {
        return Encoders.BASE64URL.encode(data);
    }

}///:~