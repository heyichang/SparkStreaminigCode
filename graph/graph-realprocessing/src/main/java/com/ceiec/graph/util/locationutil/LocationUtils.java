//package com.ceiec.graph.util.locationutil;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//
///**
// * Created by zoumengcheng on 2017/12/4.
// */
//public class LocationUtils {
//    private static final Logger logger = LoggerFactory.getLogger(LocationUtils.class);
//
//    public static Location getTwitterLoction(Place place){
//        if(place.getBounding_box()!= null){
//            List<List<List<Double>>> locationLists =  place.getBounding_box().getCoordinates();
//            double lon =0;
//            double lat =0;
//            int a = 0;
//            double newLon = 0;
//            double newLat = 0;
//            try {
//        if(locationLists.get(0) != null){
//
//            List<List<Double>> listList =  locationLists.get(0);
//            for (int i =0 ;i <listList.size();i++){
//                try {
//                    lon += listList.get(i).get(0).doubleValue();
//                    lat += listList.get(i).get(1).doubleValue();
//                    a ++;
//                }catch (Exception e ){
//
//                }
//
//            }
//             newLon = lon/a;
//             newLat = lat/a;
//
//        }
//            return  new Location(newLon,newLat);
//            }catch (Exception e){
//                logger.error("getTwitterLoction error from Coordinates : "+e);
//            }
//        }
//        return null;
//    }
//
//    public static Location getNewsLoction(){
//        return null;
//    }
//}
