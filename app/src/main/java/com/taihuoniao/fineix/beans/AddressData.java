package com.taihuoniao.fineix.beans;

import java.util.List;

/**
 * @author lilin
 * created at 2016/10/24 14:24
 */
public class AddressData {

    /**
     * _id : 580b07e53ffca292148b4567
     * oid : 1
     * pid : 0
     * name : 北京
     * layer : 1
     * sort : 1
     * status : 1
     * created_on : 1477117925
     * updated_on : 1477117925
     */

    public List<RowsEntity> rows;

    public static class RowsEntity {
        public String _id;
        public int oid;
        public int pid;
        public String name;
        public int layer;
        public int sort;
        public int status;
    }
}
