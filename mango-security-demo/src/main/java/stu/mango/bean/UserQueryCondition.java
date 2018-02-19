package stu.mango.bean;

import io.swagger.annotations.ApiModelProperty;

public class UserQueryCondition {
    private String username;

    @ApiModelProperty(value = "用户年龄起始值")
    private String age;

    @ApiModelProperty(value = "用户年龄终止值")
    private String ageTo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(String ageTo) {
        this.ageTo = ageTo;
    }

    @Override
    public String toString() {
        return "UserQueryCondition{" +
                "username='" + username + '\'' +
                ", age='" + age + '\'' +
                ", ageTo='" + ageTo + '\'' +
                '}';
    }
}
