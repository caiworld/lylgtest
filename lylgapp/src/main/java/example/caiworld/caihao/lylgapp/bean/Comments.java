package example.caiworld.caihao.lylgapp.bean;

/**
 * Created by caihao on 2017/7/25.
 */
public class Comments {
    private String from;
    private String to;
    private String comment;

    public Comments(String from, String to, String comment) {
        this.from = from;
        this.to = to;
        this.comment = comment;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
