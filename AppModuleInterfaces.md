App接口文档
地图功能
MapUtil方法列表

//根据纬度经度获取地址
public static void getAddressByCoordinate(LatLng latLng,final OnGetReverseGeoCodeResultListener listener)

//根据纬度经度获取地址
public static void getAddressByCoordinate(double lat, double lon,OnGetReverseGeoCodeResultListener listener)

使用举例： MapUtil.getAddressByCoordinate(40.216938,116.234051,new MapUtil.OnGetReverseGeoCodeResultListener() { @Override public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) { //其中list为返回的地址列表 List<PoiInfo> list = result.getPoiList(); } });


//根据关键字进行周边搜索
public static void getPoiNearbyByKeyWord(String keyword, LatLng ll,int radius,int pageCapacity,PoiSortType sortType,MyOnGetPoiSearchResultListener listener)