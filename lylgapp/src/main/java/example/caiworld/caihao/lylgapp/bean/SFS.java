package example.caiworld.caihao.lylgapp.bean;

/**
 * Created by caihao on 2017/7/14.
 */
public class SFS {
    private int ivHeader;
    private String name;
    private String from;
    private String to;

    public SFS(int ivHeader, String name, String from, String to) {
        this.ivHeader = ivHeader;
        this.name = name;
        this.from = from;
        this.to = to;
    }

    public int getIvHeader() {
        return ivHeader;
    }

    public void setIvHeader(int ivHeader) {
        this.ivHeader = ivHeader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
