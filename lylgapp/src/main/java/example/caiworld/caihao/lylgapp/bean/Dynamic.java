package example.caiworld.caihao.lylgapp.bean;

import java.util.List;

/**
 * 动态
 * Created by caihao on 2017/7/11.
 */
public class Dynamic {

    private int picHeader;//头像
    private String username;//用户名
    private String content;//内容
    private int[] pics;//图片
    private String date;//日期
    private int zan;//点赞数
    private List<String> comments;//评论

    public Dynamic(int picHeader, String username, String content, int[] pics, String date, int zan, List<String> comments) {
        this.picHeader = picHeader;
        this.username = username;
        this.content = content;
        this.pics = pics;
        this.date = date;
        this.zan = zan;
        this.comments = comments;
    }

    public int getPicHeader() {
        return picHeader;
    }

    public void setPicHeader(int picHeader) {
        this.picHeader = picHeader;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int[] getPics() {
        return pics;
    }

    public void setPics(int[] pics) {
        this.pics = pics;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
