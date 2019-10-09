package wang.wangby.echars4j.utils;

import wang.wangby.utils.DateTime;

import java.util.Date;
import java.util.List;

public class CharUtil {

    //创建字符串数组
    public static long[] createxAxis(long begin, long end, long interval) {
        long size = (end - begin) / interval + 2;//x轴至少要有2条数据
        long[] result = new long[(int) size];
        for (int i = 0; i < result.length; i++) {
            result[i] = (begin + i * interval);
        }
        return result;
    }

    public static long[] createxAxis(int size) {
        return createxAxis(1L, size, 1L);
    }

    //将时间戳转为时间字符串
    public static String[] toTimeString(long[] timestamp) {
        long interval=timestamp[1]-timestamp[0];
        int type=0;
        if(interval<1000){
            type=DateTime.SECOND_TO_MILLISECOND;
        }else if(interval<1000*60){
            type=DateTime.MINUTE_TO_SECOND;
        } else if(interval<1000*60*60){
            type=DateTime.HOUR_TO_MINUTE;
        } else if(interval<1000*60*60*24){
            type=DateTime.DAY_TO_HOUR;
        }else{
            type=DateTime.MONTH_TO_DAY;
        }

        String[] times=new String[timestamp.length];
        for(int i=0;i<timestamp.length;i++){
            times[i]=new DateTime(timestamp[i],type).toString();
        }
        return times;
    }
}
