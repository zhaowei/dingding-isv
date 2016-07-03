package dingdingisv.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by zhaowei on 16/7/3.
 */
public class AccountDTO {

    public static final int PASSWORD_MIN_LENGTH = 2;
    public static final int PASSWORD_MAX_LENGTH = 100;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50, message = "请输入用户名")
    private String username;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "请输入密码")
    private String password;

    public AccountDTO() {}

    public AccountDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
