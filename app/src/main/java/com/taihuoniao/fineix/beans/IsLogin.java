package com.taihuoniao.fineix.beans;
import java.io.Serializable;

/**
 * Created by android on 2016/3/11.
 */
public class IsLogin implements Serializable {
    private String is_login,user_id,success,message;
    private static IsLogin ourInstance;
    private IsLogin(){

    }
    public static IsLogin getInstance() {
        if (ourInstance==null)
            init();
        return ourInstance;
    }
    //初始化于Application onCreate()中执行
    public static void init(){
        ourInstance = new IsLogin();
    }

    @Override
    public String toString() {
        return "IsLogin{" +
                "is_login='" + is_login + '\'' +
                ", user_id='" + user_id + '\'' +
                ", success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getIs_login() {
        return is_login;
    }

    public void setIs_login(String is_login) {
        this.is_login = is_login;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
