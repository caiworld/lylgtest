package example.caiworld.caihao.lylgapp.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 动态
 * Created by caihao on 2017/7/11.
 */
public class Dynamic extends BmobObject{

    private BmobFile picHeader;//头像
    private String username;//用户名
    private String content;//内容
    private List<BmobFile> pics;//图片
    private String date;//日期
    private List<String> zan;//点赞的人，统计赞的个数时获取长度即可，这样还可获取点赞的人
    private List<Comments> comments;//评论

    public Dynamic() {
    }

    public Dynamic(BmobFile picHeader, String username, String content, List<BmobFile> pics, String date, List<String> zan, List<Comments> comments) {
        this.picHeader = picHeader;
        this.username = username;
        this.content = content;
        this.pics = pics;
        this.date = date;
        this.zan = zan;
        this.comments = comments;
    }

    public BmobFile getPicHeader() {
        return picHeader;
    }

    public void setPicHeader(BmobFile picHeader) {
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

    public List<BmobFile> getPics() {
        return pics;
    }

    public void setPics(List<BmobFile> pics) {
        this.pics = pics;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getZan() {
        return zan;
    }

    public void setZan(List<String> zan) {
        this.zan = zan;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
