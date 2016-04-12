# App接口文档 #
## 地图功能 ##
### MapUtil方法列表 ###

1.      `void getAddressByCoordinate(LatLng latLng,final OnGetReverseGeoCodeResultListener listener)`
    //根据纬度经度获取地址
    
2. `void getAddressByCoordinate(double lat, double lon,OnGetReverseGeoCodeResultListener listener)`
    根据纬度经度获取地址
    
    **使用举例：** 
        `MapUtil.getAddressByCoordinate(40.216938,116.234051,new MapUtil.OnGetReverseGeoCodeResultListener() {
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
	//其中list为返回的地址列表
    List<PoiInfo> list = result.getPoiList(); 
    }
    });`
