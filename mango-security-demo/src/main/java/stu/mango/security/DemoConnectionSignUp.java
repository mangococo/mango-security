package stu.mango.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * 第三方登录后查询userId时默认注册
 */
//@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

    /**
     * 根据社交用户信息创建用户并返回用户唯一标识
     */
    @Override
    public String execute(Connection<?> connection) {
        return connection.getDisplayName(); // mock
    }
}
