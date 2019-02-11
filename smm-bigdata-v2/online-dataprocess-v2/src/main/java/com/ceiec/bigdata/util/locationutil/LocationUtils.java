package com.ceiec.bigdata.util.locationutil;

import com.ceiec.bigdata.entity.table.Location;
import com.ceiec.bigdata.entity.twitter.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by heyichang on 2017/12/4.
 */
public class LocationUtils {
    private static final Logger logger = LoggerFactory.getLogger(LocationUtils.class);

    public static Location getTwitterLoction(Place place){
        if(place.getBounding_box()!= null){
            List<List<List<Double>>> locationLists =  place.getBounding_box().getCoordinates();
            double lon =0;
            double lat =0;
            int a = 0;
            double newLon = 0;
            double newLat = 0;
            try {
        if(locationLists.get(0) != null){

            List<List<Double>> listList =  locationLists.get(0);
            for (int i =0 ;i <listList.size();i++){
                try {
                    lon += listList.get(i).get(0).doubleValue();
                    lat += listList.get(i).get(1).doubleValue();
                    a ++;
                }catch (Exception e ){

                }

            }
             newLon = lon/a;
             newLat = lat/a;

        }
            return  new Location(newLon,newLat);
            }catch (Exception e){
                logger.error("getTwitterLoction error from Coordinates : "+e);
            }
        }
        return null;
    }

    public static Location getNewsLoction(){
        return null;
    }


//    public static void main(String[] args) {
//        String str = "{\n" +
//                "\t\t\"country\": \"香港\",\n" +
//                "\t\t\"attributes\": {},\n" +
//                "\t\t\"url\": \"https://api.twitter.com/1.1/geo/id/35fd5bacecc4c6e5.json\",\n" +
//                "\t\t\"country_code\": \"HK\",\n" +
//                "\t\t\"id\": \"35fd5bacecc4c6e5\",\n" +
//                "\t\t\"bounding_box\": {\n" +
//                "\t\t\t\"type\": \"Polygon\",\n" +
//                "\t\t\t\"coordinates\": [\n" +
//                "\t\t\t\t[\n" +
//                "\t\t\t\t\t[113.81813, 22.1465116],\n" +
//                "\t\t\t\t\t[114.502196, 22.1465116],\n" +
//                "\t\t\t\t\t[114.502196, 22.614177],\n" +
//                "\t\t\t\t\t[113.81813, 22.614177]\n" +
//                "\t\t\t\t]\n" +
//                "\t\t\t]\n" +
//                "\t\t},\n" +
//                "\t\t\"place_type\": \"country\",\n" +
//                "\t\t\"full_name\": \"香港\",\n" +
//                "\t\t\"contained_within\": [],\n" +
//                "\t\t\"name\": \"香港\"\n" +
//                "}";
//        Place place = JSON.parseObject(str,Place.class);
//        Location location = getTwitterLoction( place);
//        System.out.println("lon:" +location.getLon() +" lat:" +location.getLat());
//    }

}
