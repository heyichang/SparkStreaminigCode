package com.ceiec.bigdata.action;

import com.ceiec.bigdata.entity.table.location.CnnLocation;

/**
 * Created by heyichang on 2018/3/26.
 */
public class TestCnn {
    public static void main(String[] args) {
        System.out.println(CnnLocation.CNN_LOCATION1.getLon()+" "+CnnLocation.CNN_LOCATION1.getLat()+" "+CnnLocation.CNN_LOCATION1.getP_region_id() +" "+CnnLocation.CNN_LOCATION1.getRegion_id());

//        int count1 =0;
//        int count2 =0;
//        int count3 =0;
//        int count4 =0;
//        int count5 =0;
//        for ( int i =0 ;i <10000 ;i++){
//            int a = TestUtils.get1To4IntRadom();
//            System.out.println(a);
//            switch (a){
//                case 1:
//                    count1++;
//                    break;
//                case 2:
//                    count2++;
//                    break;
//                case 3:
//                    count3++;
//                    break;
//                case 4:
//                    count4++;
//                    break;
//                default :
//                    count5++;
//                    break;
//
//            }
//        }
//        System.out.println("1: "+count1 + " 2:"+count2 +" 3:"+count3 +" 4:"+count4 + " 5:"+count5);
    }
}
