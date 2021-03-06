package fz.cs.daoyun.utils.tools;

import fz.cs.daoyun.domain.User;
import fz.cs.daoyun.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class Md5Util {

    private static final String ALGORITH_NAME = "md5";

    private static final int HASH_ITERATIONS = 5;

    public static String encrypt(String username, String password) {
//        String source = StringUtils.lowerCase(username);
//        password = StringUtils.lowerCase(password);
//
//        return new SimpleHash(ALGORITH_NAME, password, ByteSource.Util.bytes(source), HASH_ITERATIONS).toHex();
        String source = username;
        return new SimpleHash(
                ALGORITH_NAME,
                password,
                ByteSource.Util.bytes(source),
                HASH_ITERATIONS).toHex();

    }

}
