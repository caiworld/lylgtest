package example.caiworld.caihao.lylgapp.bean;

import cn.bmob.v3.BmobObject;

/**
 * 发货详情
 * Created by caihao on 2017/7/7.
 */
public class SenderDetail extends BmobObject{

    @Override
    public String toString() {
        return "SenderDetail{" +
                "username='" + username + '\'' +
                ", sendAddress='" + sendAddress + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", sendName='" + sendName + '\'' +
                ", sendNumber='" + sendNumber + '\'' +
                ", receiveAddress='" + receiveAddress + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", receiveNumber='" + receiveNumber + '\'' +
                ", pickUpTime='" + pickUpTime + '\'' +
                ", goodsDetail='" + goodsDetail + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    private String username;//用户名

    private String sendAddress;//发货人地址
    private double lat;//发货人纬度
    private double lon;//发货人经度
    private String sendName;//发货人姓名
    private String sendNumber;//发货人号码

    private String receiveAddress;//收货人地址
    private String receiveName;//收货人名字
    private String receiveNumber;//收货人号码

    private String pickUpTime;//取货时间
    private String goodsDetail;//物品信息
    private String comment;//备注

    public SenderDetail(){
    }

    public SenderDetail(String tableName, String username, String sendAddress, double lat, double lon, String sendName, String sendNumber, String receiveAddress, String receiveName, String receiveNumber, String pickUpTime, String goodsDetail, String comment) {
        super(tableName);
        this.username = username;
        this.sendAddress = sendAddress;
        this.lat = lat;
        this.lon = lon;
        this.sendName = sendName;
        this.sendNumber = sendNumber;
        this.receiveAddress = receiveAddress;
        this.receiveName = receiveName;
        this.receiveNumber = receiveNumber;
        this.pickUpTime = pickUpTime;
        this.goodsDetail = goodsDetail;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(String receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
