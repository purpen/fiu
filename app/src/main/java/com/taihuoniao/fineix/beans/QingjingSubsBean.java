package com.taihuoniao.fineix.beans;

/**
 * Created by taihuoniao on 2016/4/28.
 */
public class QingjingSubsBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private int subscription_count;

        public int getSubscription_count() {
            return subscription_count;
        }

        public void setSubscription_count(int subscription_count) {
            this.subscription_count = subscription_count;
        }
    }
}
