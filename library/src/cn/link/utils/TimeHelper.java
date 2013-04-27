package cn.link.utils;

public class TimeHelper {

    private static long start;
    private static long end;

    public static void start() {
        start = System.currentTimeMillis();
    }

    public static void end() {
        end = System.currentTimeMillis();

    }

    public static String getTimeDes(String ms) {
        long lms = Long.parseLong(ms);
        return getTimeDes(lms);
    }

    public static String getTimeDes(long ms) {
        long currentM = System.currentTimeMillis();

        long delta = currentM - ms * 1000;

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = delta / dd;
        long hour = (delta - day * dd) / hh;
        long minute = (delta - day * dd - hour * hh) / mi;
        long second = (delta - day * dd - hour * hh - minute * mi) / ss;
        // long milliSecond = ms - day * dd - hour * hh - minute * mi - second *
        // ss;
        StringBuilder str = new StringBuilder();
        if (day > 0) {
            str.append(day).append("天前");
        } else if (hour > 0) {
            str.append(hour).append("小时前");
        } else if (minute > 0) {
            str.append(minute).append("分钟前");
        } else if (second > 0) {
            str.append(second).append("秒前");
        }
        // if (milliSecond > 0) {
        // str.append(milliSecond).append("毫秒,");
        // }
        // if (str.length() > 0) {
        // str = str.deleteCharAt(str.length() - 1);
        // }
        return str.toString();
    }
}
