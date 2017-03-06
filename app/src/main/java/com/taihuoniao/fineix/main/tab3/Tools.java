package com.taihuoniao.fineix.main.tab3;

import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneDetailsBean2;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;

import java.util.ArrayList;
import java.util.List;

/**
 * 情景列表 数据 新数据格式转换成旧的数据格式
 * Created by Stephen on 2017/3/4 1:18
 * Email: 895745843@qq.com
 */

public class Tools {

    /**
     * 新数据格式转换成旧的数据格式
     * @param newRowEntitys  newRowEntitys
     * @return oleRowEntitys
     */
    public static List<SceneList.DataBean.RowsBean> newListConvertOldList(List<SceneListBean2.RowsEntity> newRowEntitys){
        if (newRowEntitys == null) {
            return null;
        }

        List<SceneList.DataBean.RowsBean> oldRowEntitys = new ArrayList<>();
        for(int i = 0; i < newRowEntitys.size(); i ++) {
            SceneListBean2.RowsEntity newRowBean = newRowEntitys.get(i);
            SceneList.DataBean.RowsBean oldRowBean = new SceneList.DataBean.RowsBean();
            oldRowBean.set_id(newRowBean.get_id());
            oldRowBean.setAddress(newRowBean.getAddress());
            oldRowBean.setCategory_id(newRowBean.getCategory_id());
            oldRowBean.setCity(newRowBean.getCity());
            oldRowBean.setUser_id(newRowBean.getUser_id());
            oldRowBean.setTitle(newRowBean.getTitle());
            oldRowBean.setDes(newRowBean.getDes());
            oldRowBean.setCategory_ids(newRowBean.getCategory_ids());
            oldRowBean.setScene_id(newRowBean.getScene_id());
            oldRowBean.setUsed_count(newRowBean.getUsed_count());

            oldRowBean.setView_count(newRowBean.getView_count());
            oldRowBean.setLove_count(newRowBean.getLove_count());
            oldRowBean.setComment_count(Integer.valueOf(newRowBean.getComment_count()));
            oldRowBean.setStick(Integer.valueOf(newRowBean.getStick()));
            oldRowBean.setFine(Integer.valueOf(newRowBean.getFine()));
            oldRowBean.setIs_check(Integer.valueOf(newRowBean.getIs_check()));
            oldRowBean.setStatus(newRowBean.getStatus());
            oldRowBean.setDeleted(newRowBean.getDeleted());
            oldRowBean.setCreated_on(Long.valueOf(newRowBean.getCreated_on()));
            oldRowBean.setUpdated_on(newRowBean.getUpdated_on());

            oldRowBean.setTags_s(newRowBean.getTags_s());
            oldRowBean.setCover_url(newRowBean.getCover_url());
            oldRowBean.setCreated_at(newRowBean.getCreated_at());
            oldRowBean.setIs_love(Integer.valueOf(newRowBean.getIs_love()));
            oldRowBean.setIs_favorite(Integer.valueOf(newRowBean.getIs_favorite()));

            oldRowBean.setTags(newRowBean.getTags());

            SceneList.DataBean.RowsBean.LocationBean oldLocationBean = new SceneList.DataBean.RowsBean.LocationBean();
            SceneListBean2.RowsEntity.LocationEntity location = newRowBean.getLocation();
            oldLocationBean.setType(location.getType());

            final List<String> coordinates = location.getCoordinates();
            List<Double> co = new ArrayList<>();
            for(int j = 0 ; j < coordinates.size(); j ++) {
                co.add(Double.valueOf(coordinates.get(j)));
            }
            oldLocationBean.setCoordinates(co);
            oldRowBean.setLocation(oldLocationBean);

            SceneList.DataBean.RowsBean.UserInfoBean oldUserINfoBean = new SceneList.DataBean.RowsBean.UserInfoBean();
            SceneListBean2.RowsEntity.UserInfoEntity user_info = newRowBean.getUser_info();

            oldUserINfoBean.setUser_id(user_info.getUser_id());
            oldUserINfoBean.setNickname(user_info.getNickname());
            oldUserINfoBean.setAvatar_url(user_info.getAvatar_url());
            oldUserINfoBean.setSummary(user_info.getSummary());
            oldUserINfoBean.setFollow_count(user_info.getFollow_count());
            oldUserINfoBean.setFans_count(user_info.getFans_count());
            oldUserINfoBean.setLove_count(user_info.getLove_count());
            oldUserINfoBean.setIs_expert(Integer.valueOf(user_info.getIs_expert()));
            oldUserINfoBean.setIs_follow(Integer.valueOf(user_info.getIs_follow()));
            oldUserINfoBean.setLabel(user_info.getLabel());
            oldUserINfoBean.setExpert_label(user_info.getExpert_label());
            oldUserINfoBean.setExpert_info(user_info.getExpert_info());

            SceneList.DataBean.RowsBean.UserInfoBean.CounterBean oldCounterBean = new SceneList.DataBean.RowsBean.UserInfoBean.CounterBean();
            oldCounterBean.setMessage_count(user_info.getCounter().getMessage_count());
            oldCounterBean.setNotice_count(user_info.getCounter().getNotice_count());
            oldCounterBean.setAlert_count(user_info.getCounter().getAlert_count());
            oldCounterBean.setFans_count(user_info.getCounter().getFans_count());
            oldCounterBean.setComment_count(user_info.getCounter().getComment_count());
            oldCounterBean.setPeople_count(user_info.getCounter().getPeople_count());
            oldCounterBean.setOrder_wait_payment(user_info.getCounter().getOrder_wait_payment());
            oldCounterBean.setOrder_ready_goods(user_info.getCounter().getOrder_ready_goods());
            oldCounterBean.setOrder_sended_goods(user_info.getCounter().getOrder_sended_goods());
            oldCounterBean.setOrder_evaluate(user_info.getCounter().getOrder_evaluate());
            oldCounterBean.setFiu_comment_count(user_info.getCounter().getFiu_comment_count());
            oldCounterBean.setFiu_alert_count(user_info.getCounter().getFiu_alert_count());
            oldCounterBean.setFiu_notice_count(user_info.getCounter().getFiu_notice_count());
            oldUserINfoBean.setCounter(oldCounterBean);
            oldRowBean.setUser_info(oldUserINfoBean);
            oldRowBean.setCategory_ids(newRowBean.getCategory_ids());

            List<SceneList.DataBean.RowsBean.ProductBean> oldProductBeans = new ArrayList<>();
            List<SceneListBean2.RowsEntity.ProductEntity> newProduct = newRowBean.getProduct();
            for(int k = 0 ; k < newProduct.size(); k ++) {
                SceneList.DataBean.RowsBean.ProductBean oldProductBean = new SceneList.DataBean.RowsBean.ProductBean();
                oldProductBean.setId(newProduct.get(k).getId());
                oldProductBean.setLoc(Integer.valueOf(newProduct.get(k).getLoc()));
                oldProductBean.setTitle(newProduct.get(k).getTitle());
                oldProductBean.setX(newProduct.get(k).getX());
                oldProductBean.setY(newProduct.get(k).getY());
                oldProductBean.price= newProduct.get(k).getPrice();
                oldProductBeans.add(oldProductBean);
            }
            oldRowBean.setProduct(oldProductBeans);

            List<SceneList.DataBean.RowsBean.CommentsBean> oldCommentBeans = new ArrayList<>();
            SceneList.DataBean.RowsBean.CommentsBean oldCommentBean = new SceneList.DataBean.RowsBean.CommentsBean();
            List<SceneListBean2.RowsEntity.CommentsEntity> comments = newRowBean.getComments();
            for(int m = 0 ; m < comments.size(); m++) {
                oldCommentBean.set_id(comments.get(m).get_id());
                oldCommentBean.setContent(comments.get(m).getContent());
                oldCommentBean.setUser_avatar_url(comments.get(m).getUser_avatar_url());
                oldCommentBean.setUser_id(comments.get(m).getUser_id());
                oldCommentBean.setUser_nickname(comments.get(m).getUser_nickname());
                oldCommentBeans.add(oldCommentBean);
            }
            oldRowBean.setComments(oldCommentBeans);
            oldRowEntitys.add(oldRowBean);
        }
        return oldRowEntitys;
    }

    /**
     * 新数据格式转换成旧的数据格式2
     * @param newRowEntitys  newRowEntitys
     * @return oleRowEntitys
     */
    public static List<SceneList.DataBean.RowsBean> newListConvertOldList2(SceneDetailsBean2 newRowEntitys){
        if (newRowEntitys == null) {
            return null;
        }

        List<SceneList.DataBean.RowsBean> oldRowEntitys = new ArrayList<>();
        for(int i = 0; i < 1; i ++) {
//            SceneListBean2.RowsEntity newRowBean = newRowEntitys.get(i);
            SceneList.DataBean.RowsBean oldRowBean = new SceneList.DataBean.RowsBean();
            oldRowBean.set_id(newRowEntitys.get_id());
            oldRowBean.setAddress(newRowEntitys.getAddress());
            oldRowBean.setCategory_id(newRowEntitys.getCategory_id());
            oldRowBean.setCity(newRowEntitys.getCity());
            oldRowBean.setUser_id(newRowEntitys.getUser_id());
            oldRowBean.setTitle(newRowEntitys.getTitle());
            oldRowBean.setDes(newRowEntitys.getDes());
            oldRowBean.setCategory_ids(newRowEntitys.getCategory_ids());
            oldRowBean.setScene_id(newRowEntitys.getScene_id());
            oldRowBean.setUsed_count(newRowEntitys.getUsed_count());

            oldRowBean.setView_count(newRowEntitys.getView_count());
            oldRowBean.setLove_count(newRowEntitys.getLove_count());
            oldRowBean.setComment_count(Integer.valueOf(newRowEntitys.getComment_count()));
            oldRowBean.setStick(Integer.valueOf(newRowEntitys.getStick()));
            oldRowBean.setFine(Integer.valueOf(newRowEntitys.getFine()));
            oldRowBean.setIs_check(Integer.valueOf(newRowEntitys.getIs_check()));
            oldRowBean.setStatus(newRowEntitys.getStatus());
            oldRowBean.setDeleted(newRowEntitys.getDeleted());
            oldRowBean.setCreated_on(Long.valueOf(newRowEntitys.getCreated_on()));
            oldRowBean.setUpdated_on(newRowEntitys.getUpdated_on());

            oldRowBean.setTags_s(newRowEntitys.getTags_s());
            oldRowBean.setCover_url(newRowEntitys.getCover_url());
            oldRowBean.setCreated_at(newRowEntitys.getCreated_at());
            oldRowBean.setIs_love(Integer.valueOf(newRowEntitys.getIs_love()));
            oldRowBean.setIs_favorite(Integer.valueOf(newRowEntitys.getIs_favorite()));

            oldRowBean.setTags(newRowEntitys.getTags());

            SceneList.DataBean.RowsBean.LocationBean oldLocationBean = new SceneList.DataBean.RowsBean.LocationBean();
            SceneDetailsBean2.LocationEntity location = newRowEntitys.getLocation();
            oldLocationBean.setType(location.getType());

            final List<String> coordinates = location.getCoordinates();
            List<Double> co = new ArrayList<>();
            for(int j = 0 ; j < coordinates.size(); j ++) {
                co.add(Double.valueOf(coordinates.get(j)));
            }
            oldLocationBean.setCoordinates(co);
            oldRowBean.setLocation(oldLocationBean);

            SceneList.DataBean.RowsBean.UserInfoBean oldUserINfoBean = new SceneList.DataBean.RowsBean.UserInfoBean();
            SceneDetailsBean2.UserInfoEntity user_info = newRowEntitys.getUser_info();

            oldUserINfoBean.setUser_id(user_info.getUser_id());
            oldUserINfoBean.setNickname(user_info.getNickname());
            oldUserINfoBean.setAvatar_url(user_info.getAvatar_url());
            oldUserINfoBean.setSummary(user_info.getSummary());
            oldUserINfoBean.setFollow_count(user_info.getFollow_count());
            oldUserINfoBean.setFans_count(user_info.getFans_count());
            oldUserINfoBean.setLove_count(user_info.getLove_count());
            oldUserINfoBean.setIs_expert(Integer.valueOf(user_info.getIs_expert()));
            oldUserINfoBean.setIs_follow(Integer.valueOf(user_info.getIs_follow()));
            oldUserINfoBean.setLabel(user_info.getLabel());
            oldUserINfoBean.setExpert_label(user_info.getExpert_label());
            oldUserINfoBean.setExpert_info(user_info.getExpert_info());

            SceneList.DataBean.RowsBean.UserInfoBean.CounterBean oldCounterBean = new SceneList.DataBean.RowsBean.UserInfoBean.CounterBean();
            oldCounterBean.setMessage_count(user_info.getCounter().getMessage_count());
            oldCounterBean.setNotice_count(user_info.getCounter().getNotice_count());
            oldCounterBean.setAlert_count(user_info.getCounter().getAlert_count());
            oldCounterBean.setFans_count(user_info.getCounter().getFans_count());
            oldCounterBean.setComment_count(user_info.getCounter().getComment_count());
            oldCounterBean.setPeople_count(user_info.getCounter().getPeople_count());
            oldCounterBean.setOrder_wait_payment(user_info.getCounter().getOrder_wait_payment());
            oldCounterBean.setOrder_ready_goods(user_info.getCounter().getOrder_ready_goods());
            oldCounterBean.setOrder_sended_goods(user_info.getCounter().getOrder_sended_goods());
            oldCounterBean.setOrder_evaluate(user_info.getCounter().getOrder_evaluate());
            oldCounterBean.setFiu_comment_count(user_info.getCounter().getFiu_comment_count());
            oldCounterBean.setFiu_alert_count(user_info.getCounter().getFiu_alert_count());
            oldCounterBean.setFiu_notice_count(user_info.getCounter().getFiu_notice_count());
            oldUserINfoBean.setCounter(oldCounterBean);
            oldRowBean.setUser_info(oldUserINfoBean);
            oldRowBean.setCategory_ids(newRowEntitys.getCategory_ids());

            List<SceneList.DataBean.RowsBean.ProductBean> oldProductBeans = new ArrayList<>();
            List<SceneDetailsBean2.ProductEntity> newProduct = newRowEntitys.getProduct();
            for(int k = 0 ; k < newProduct.size(); k ++) {
                SceneList.DataBean.RowsBean.ProductBean oldProductBean = new SceneList.DataBean.RowsBean.ProductBean();
                oldProductBean.setId(newProduct.get(k).getId());

                try {
                    oldProductBean.setLoc(Integer.valueOf(newProduct.get(k).getLoc()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                oldProductBean.setTitle(newProduct.get(k).getTitle());
                oldProductBean.setX(Double.valueOf(newProduct.get(k).getX()));
                oldProductBean.setY(Double.valueOf(newProduct.get(k).getY()));
                oldProductBeans.add(oldProductBean);
            }
            oldRowBean.setProduct(oldProductBeans);

            List<SceneList.DataBean.RowsBean.CommentsBean> oldCommentBeans = new ArrayList<>();
            SceneList.DataBean.RowsBean.CommentsBean oldCommentBean = new SceneList.DataBean.RowsBean.CommentsBean();
            List<SceneDetailsBean2.CommentsEntity> comments = newRowEntitys.getComments();
            for(int m = 0 ; m < comments.size(); m++) {
                oldCommentBean.set_id(comments.get(m).get_id());
                oldCommentBean.setContent(comments.get(m).getContent());
                oldCommentBean.setUser_avatar_url(comments.get(m).getUser_avatar_url());
                oldCommentBean.setUser_id(comments.get(m).getUser_id());
                oldCommentBean.setUser_nickname(comments.get(m).getUser_nickname());
                oldCommentBeans.add(oldCommentBean);
            }
            oldRowBean.setComments(oldCommentBeans);
            oldRowEntitys.add(oldRowBean);
        }
        return oldRowEntitys;
    }
}
