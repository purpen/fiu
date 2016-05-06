package com.taihuoniao.fineix.beans;

import com.taihuoniao.fineix.base.NetBean;

import java.util.List;

/**
 * Created by taihuoniao on 2016/5/6.
 */
public class JDProductBean extends NetBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{
        private List<JDProductItem> listproductbase_result;

        public List<JDProductItem> getListproductbase_result() {
            return listproductbase_result;
        }

        public void setListproductbase_result(List<JDProductItem> listproductbase_result) {
            this.listproductbase_result = listproductbase_result;
        }
    }
    public static class JDProductItem{
//        oid	String	原ID	NOT NULL
//        sku_id	String	sku_ID	NULL
//        title	String	标题	NOT NULL
//        market_price	Float	市场价	NOT NULL
//        sale_price	Float	销售价	NOT NULL
//        link	String	原文链接	NOT NULL
//        cover_url	String	封面图地址	NOT NULL
        private String skuId;
        private String name;
        private String market_price;
        private String sale_price;
        private String url;
        private String imagePath;

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
