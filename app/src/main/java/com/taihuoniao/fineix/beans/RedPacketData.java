package com.taihuoniao.fineix.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author lilin
 * created at 2016/5/24 14:30
 */
public class RedPacketData implements Serializable{
    public int total_rows;
    public List<RedPacketItem> rows;
    public class RedPacketItem{
        public String code;
        public String amount;
        public int min_amount;
        public String expired_label;
        public String product_name;
        public int product_id;
    }
}
