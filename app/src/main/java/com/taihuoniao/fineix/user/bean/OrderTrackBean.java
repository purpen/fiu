package com.taihuoniao.fineix.user.bean;

import java.util.List;

/**
 * Created by Stephen on 2016/12/8 17:41
 * Email: 895745843@qq.com
 */

public class OrderTrackBean {

    /**
     * EBusinessID : 1266663
     * ShipperCode : HTKY
     * Success : true
     * LogisticCode : 70091213621890
     * State : 2
     * Traces : [{"AcceptTime":"2016-12-04 17:55:35","AcceptStation":"深圳市【深圳新龙岗站】，【承拓四海/84618166】已揽收","Remark":""},{"AcceptTime":"2016-12-04 22:57:38","AcceptStation":"到深圳市【深圳转运中心】","Remark":""},{"AcceptTime":"2016-12-05 00:20:10","AcceptStation":"深圳市【深圳转运中心】，正发往【北京转运中心】","Remark":""}]
     * current_user_id : 36
     */

    private String EBusinessID;
    private String ShipperCode;
    private boolean Success;
    private String LogisticCode;
    private String State;
    private int current_user_id;
    private List<TracesEntity> Traces;

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public void setShipperCode(String ShipperCode) {
        this.ShipperCode = ShipperCode;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public void setState(String State) {
        this.State = State;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setTraces(List<TracesEntity> Traces) {
        this.Traces = Traces;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public boolean getSuccess() {
        return Success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public String getState() {
        return State;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public List<TracesEntity> getTraces() {
        return Traces;
    }

    public static class TracesEntity {
        /**
         * AcceptTime : 2016-12-04 17:55:35
         * AcceptStation : 深圳市【深圳新龙岗站】，【承拓四海/84618166】已揽收
         * Remark :
         */

        private String AcceptTime;
        private String AcceptStation;
        private String Remark;

        public void setAcceptTime(String AcceptTime) {
            this.AcceptTime = AcceptTime;
        }

        public void setAcceptStation(String AcceptStation) {
            this.AcceptStation = AcceptStation;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getAcceptTime() {
            return AcceptTime;
        }

        public String getAcceptStation() {
            return AcceptStation;
        }

        public String getRemark() {
            return Remark;
        }
    }

    @Override
    public String toString() {
        return "OrderTrackBean{" +
                "EBusinessID='" + EBusinessID + '\'' +
                ", ShipperCode='" + ShipperCode + '\'' +
                ", Success=" + Success +
                ", LogisticCode='" + LogisticCode + '\'' +
                ", State='" + State + '\'' +
                ", current_user_id=" + current_user_id +
                ", Traces=" + Traces +
                '}';
    }
}
