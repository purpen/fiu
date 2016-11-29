package com.taihuoniao.fineix.personal.salesevice.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *  退款/ 售后 列表
 *
 * Created by Stephen on 2016/11/25.
 */

public class ChargeBackListBean implements Parcelable {

    /**
     * total_rows : 2
     * rows : [{"_id":1112900012,"number":"116112930371","user_id":1136552,"target_id":1154024178,"product_id":1154024056,"target_type":1,"stage_label":"已退款","order_rid":"116112948755","sub_order_id":null,"refund_price":40,"quantity":1,"type":1,"type_label":"退款","freight":0,"stage":2,"reason":2,"reason_label":"未按约定时间发货","content":"拍错了","summary":null,"status":1,"deleted":0,"created_on":1480412595,"updated_on":1480413684,"refund_on":1480413684,"product":{"title":"模范镇 福鹿双全勺","name":"模范镇 福鹿双全勺","short_title":"模范镇 福鹿双全勺","cover_url":"http://frbird.qiniudn.com/product/161121/5832cdeffc8b12cd058b4718-1-p500x500.jpg","sale_price":40,"quantity":1,"sku_name":"福鹿双全勺"},"refund_at":"16-11-29","created_at":"16-11-29"},{"_id":1112900006,"number":"116112987559","user_id":1136552,"target_id":1153086510,"product_id":1153086430,"target_type":1,"stage_label":"已退款","order_rid":"116112948750","sub_order_id":null,"refund_price":95,"quantity":1,"type":1,"type_label":"退款","freight":0,"stage":2,"reason":1,"reason_label":"不喜欢/不想要了","content":"不想要了","summary":null,"status":1,"deleted":0,"created_on":1480411359,"updated_on":1480413941,"refund_on":1480413941,"product":{"title":"ONEDAY 匹诺曹《and you》纸钱包","name":"ONEDAY 匹诺曹《and you》纸钱包","short_title":"ONEDAY 匹诺曹《and you》纸钱包","cover_url":"http://frbird.qiniudn.com/product/161114/582976a0fc8b121d788b6e09-1-p500x500.jpg","sale_price":95,"quantity":1,"sku_name":"and you"},"refund_at":"16-11-29","created_at":"16-11-29"}]
     * total_page : 1
     * current_page : 1
     * pager : 
     * next_page : 0
     * prev_page : 0
     * current_user_id : 1136552
     */

    private String total_rows;
    private String total_page;
    private String current_page;
    private String pager;
    private String next_page;
    private String prev_page;
    private String current_user_id;
    private List<RowsEntity> rows;

    public void setTotal_rows(String total_rows) {
        this.total_rows = total_rows;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public void setPrev_page(String prev_page) {
        this.prev_page = prev_page;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public String getTotal_rows() {
        return total_rows;
    }

    public String getTotal_page() {
        return total_page;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public String getPager() {
        return pager;
    }

    public String getNext_page() {
        return next_page;
    }

    public String getPrev_page() {
        return prev_page;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public static class RowsEntity implements Parcelable {
        /**
         * _id : 1112900012
         * number : 116112930371
         * user_id : 1136552
         * target_id : 1154024178
         * product_id : 1154024056
         * target_type : 1
         * stage_label : 已退款
         * order_rid : 116112948755
         * sub_order_id : null
         * refund_price : 40
         * quantity : 1
         * type : 1
         * type_label : 退款
         * freight : 0
         * stage : 2
         * reason : 2
         * reason_label : 未按约定时间发货
         * content : 拍错了
         * summary : null
         * status : 1
         * deleted : 0
         * created_on : 1480412595
         * updated_on : 1480413684
         * refund_on : 1480413684
         * product : {"title":"模范镇 福鹿双全勺","name":"模范镇 福鹿双全勺","short_title":"模范镇 福鹿双全勺","cover_url":"http://frbird.qiniudn.com/product/161121/5832cdeffc8b12cd058b4718-1-p500x500.jpg","sale_price":40,"quantity":1,"sku_name":"福鹿双全勺"}
         * refund_at : 16-11-29
         * created_at : 16-11-29
         */

        private String _id;
        private String number;
        private String user_id;
        private String target_id;
        private String product_id;
        private String target_type;
        private String stage_label;
        private String order_rid;
        private String sub_order_id;
        private String refund_price;
        private String quantity;
        private String type;
        private String type_label;
        private String freight;
        private String stage;
        private String reason;
        private String reason_label;
        private String content;
        private String summary;
        private String status;
        private String deleted;
        private String created_on;
        private String updated_on;
        private String refund_on;
        private ProductEntity product;
        private String refund_at;
        private String created_at;

        public void set_id(String _id) {
            this._id = _id;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setTarget_id(String target_id) {
            this.target_id = target_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public void setStage_label(String stage_label) {
            this.stage_label = stage_label;
        }

        public void setOrder_rid(String order_rid) {
            this.order_rid = order_rid;
        }

        public void setSub_order_id(String sub_order_id) {
            this.sub_order_id = sub_order_id;
        }

        public void setRefund_price(String refund_price) {
            this.refund_price = refund_price;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setType_label(String type_label) {
            this.type_label = type_label;
        }

        public void setFreight(String freight) {
            this.freight = freight;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setReason_label(String reason_label) {
            this.reason_label = reason_label;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public void setUpdated_on(String updated_on) {
            this.updated_on = updated_on;
        }

        public void setRefund_on(String refund_on) {
            this.refund_on = refund_on;
        }

        public void setProduct(ProductEntity product) {
            this.product = product;
        }

        public void setRefund_at(String refund_at) {
            this.refund_at = refund_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String get_id() {
            return _id;
        }

        public String getNumber() {
            return number;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getTarget_id() {
            return target_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getTarget_type() {
            return target_type;
        }

        public String getStage_label() {
            return stage_label;
        }

        public String getOrder_rid() {
            return order_rid;
        }

        public String getSub_order_id() {
            return sub_order_id;
        }

        public String getRefund_price() {
            return refund_price;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getType() {
            return type;
        }

        public String getType_label() {
            return type_label;
        }

        public String getFreight() {
            return freight;
        }

        public String getStage() {
            return stage;
        }

        public String getReason() {
            return reason;
        }

        public String getReason_label() {
            return reason_label;
        }

        public String getContent() {
            return content;
        }

        public String getSummary() {
            return summary;
        }

        public String getStatus() {
            return status;
        }

        public String getDeleted() {
            return deleted;
        }

        public String getCreated_on() {
            return created_on;
        }

        public String getUpdated_on() {
            return updated_on;
        }

        public String getRefund_on() {
            return refund_on;
        }

        public ProductEntity getProduct() {
            return product;
        }

        public String getRefund_at() {
            return refund_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public static class ProductEntity implements Parcelable {
            /**
             * title : 模范镇 福鹿双全勺
             * name : 模范镇 福鹿双全勺
             * short_title : 模范镇 福鹿双全勺
             * cover_url : http://frbird.qiniudn.com/product/161121/5832cdeffc8b12cd058b4718-1-p500x500.jpg
             * sale_price : 40
             * quantity : 1
             * sku_name : 福鹿双全勺
             */

            private String title;
            private String name;
            private String short_title;
            private String cover_url;
            private String sale_price;
            private String quantity;
            private String sku_name;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setShort_title(String short_title) {
                this.short_title = short_title;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public void setSale_price(String sale_price) {
                this.sale_price = sale_price;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public void setSku_name(String sku_name) {
                this.sku_name = sku_name;
            }

            public String getTitle() {
                return title;
            }

            public String getName() {
                return name;
            }

            public String getShort_title() {
                return short_title;
            }

            public String getCover_url() {
                return cover_url;
            }

            public String getSale_price() {
                return sale_price;
            }

            public String getQuantity() {
                return quantity;
            }

            public String getSku_name() {
                return sku_name;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.title);
                dest.writeString(this.name);
                dest.writeString(this.short_title);
                dest.writeString(this.cover_url);
                dest.writeString(this.sale_price);
                dest.writeString(this.quantity);
                dest.writeString(this.sku_name);
            }

            public ProductEntity() {
            }

            protected ProductEntity(Parcel in) {
                this.title = in.readString();
                this.name = in.readString();
                this.short_title = in.readString();
                this.cover_url = in.readString();
                this.sale_price = in.readString();
                this.quantity = in.readString();
                this.sku_name = in.readString();
            }

            public static final Creator<ProductEntity> CREATOR = new Creator<ProductEntity>() {
                public ProductEntity createFromParcel(Parcel source) {
                    return new ProductEntity(source);
                }

                public ProductEntity[] newArray(int size) {
                    return new ProductEntity[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.number);
            dest.writeString(this.user_id);
            dest.writeString(this.target_id);
            dest.writeString(this.product_id);
            dest.writeString(this.target_type);
            dest.writeString(this.stage_label);
            dest.writeString(this.order_rid);
            dest.writeString(this.sub_order_id);
            dest.writeString(this.refund_price);
            dest.writeString(this.quantity);
            dest.writeString(this.type);
            dest.writeString(this.type_label);
            dest.writeString(this.freight);
            dest.writeString(this.stage);
            dest.writeString(this.reason);
            dest.writeString(this.reason_label);
            dest.writeString(this.content);
            dest.writeString(this.summary);
            dest.writeString(this.status);
            dest.writeString(this.deleted);
            dest.writeString(this.created_on);
            dest.writeString(this.updated_on);
            dest.writeString(this.refund_on);
            dest.writeParcelable(this.product, flags);
            dest.writeString(this.refund_at);
            dest.writeString(this.created_at);
        }

        public RowsEntity() {
        }

        protected RowsEntity(Parcel in) {
            this._id = in.readString();
            this.number = in.readString();
            this.user_id = in.readString();
            this.target_id = in.readString();
            this.product_id = in.readString();
            this.target_type = in.readString();
            this.stage_label = in.readString();
            this.order_rid = in.readString();
            this.sub_order_id = in.readString();
            this.refund_price = in.readString();
            this.quantity = in.readString();
            this.type = in.readString();
            this.type_label = in.readString();
            this.freight = in.readString();
            this.stage = in.readString();
            this.reason = in.readString();
            this.reason_label = in.readString();
            this.content = in.readString();
            this.summary = in.readString();
            this.status = in.readString();
            this.deleted = in.readString();
            this.created_on = in.readString();
            this.updated_on = in.readString();
            this.refund_on = in.readString();
            this.product = in.readParcelable(ProductEntity.class.getClassLoader());
            this.refund_at = in.readString();
            this.created_at = in.readString();
        }

        public static final Parcelable.Creator<RowsEntity> CREATOR = new Parcelable.Creator<RowsEntity>() {
            public RowsEntity createFromParcel(Parcel source) {
                return new RowsEntity(source);
            }

            public RowsEntity[] newArray(int size) {
                return new RowsEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total_rows);
        dest.writeString(this.total_page);
        dest.writeString(this.current_page);
        dest.writeString(this.pager);
        dest.writeString(this.next_page);
        dest.writeString(this.prev_page);
        dest.writeString(this.current_user_id);
        dest.writeTypedList(rows);
    }

    public ChargeBackListBean() {
    }

    protected ChargeBackListBean(Parcel in) {
        this.total_rows = in.readString();
        this.total_page = in.readString();
        this.current_page = in.readString();
        this.pager = in.readString();
        this.next_page = in.readString();
        this.prev_page = in.readString();
        this.current_user_id = in.readString();
        this.rows = in.createTypedArrayList(RowsEntity.CREATOR);
    }

    public static final Parcelable.Creator<ChargeBackListBean> CREATOR = new Parcelable.Creator<ChargeBackListBean>() {
        public ChargeBackListBean createFromParcel(Parcel source) {
            return new ChargeBackListBean(source);
        }

        public ChargeBackListBean[] newArray(int size) {
            return new ChargeBackListBean[size];
        }
    };
}
