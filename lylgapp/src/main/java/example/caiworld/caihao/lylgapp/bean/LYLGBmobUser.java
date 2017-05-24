package example.caiworld.caihao.lylgapp.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by caihao on 2017/5/19.
 */
public class LYLGBmobUser extends BmobObject {

    private String username;
    private String password;
    private String friends;

    public LYLGBmobUser() {
    }

    public LYLGBmobUser(String username, String password, String friends) {
        this.username = username;
        this.password = password;
        this.friends = friends;
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

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }
}
