package stu.mango.bean;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import stu.mango.validator.annotation.MyConstraint;

import javax.validation.constraints.Past;
import java.util.Date;

public class User {
    private String id;

    @MyConstraint(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空") // 字符串必须有字符、集合不为空
    private String password;

    @Past(message = "生日必须为过去时间")
    private Date birthday; // 对于日期数据，尽量不要向前台发会呆有固定格式的数据（返回“时间戳，即秒数字符串”）

    /**
     * JsonView 使用：1、使用接口声明多个视图
     */
    public interface UserSimpleView {}
    public interface UserDetailView extends UserSimpleView {}

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * JsonView 使用：2、在值对象的Getter方法上指定视图
     */
    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * JsonView 使用：2、在值对象的Getter方法上指定视图(视图之间存在继承关系)
     *      在显示UserDetailView的所声明的信息的同时也会显示UserSimpleView的指定信息
     */
    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(UserSimpleView.class)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonView(UserSimpleView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
