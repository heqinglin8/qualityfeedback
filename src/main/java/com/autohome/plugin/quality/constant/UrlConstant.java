package com.autohome.plugin.quality.constant;

public class UrlConstant {
    private final static String HTTP = "http://";

    private final static String HTTPS = "https://";
    // host
    public final static String HOST = "mobile.app.autohome.com.cn";
    private final static String QUALITY_API = "/quality_v5.6.0";
    public final static boolean isHttp = true;


    /**
     *  baseUR 获取请求接口根接口
     * @return
     */
    public static String getQualityApiRequestUrl() {
        StringBuffer sb = new StringBuffer();
        try {
            if (isHttp) {
                sb.append(HTTP);
            } else {
                sb.append(HTTPS);
            }
//            if (havanc) {
//                sb.append(KOUBEI_DOMAINNAME_NC);
//            } else {
//                sb.append(KOUBEI_DOMAINNAME);
//            }
            sb.append(HOST);
            sb.append(QUALITY_API);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }





    /**
     * 获取车系故障大类和故障大类占比接口
     * http://mobile.app.autohome.com.cn/quality_v5.6.0/quality/qualitycategorypercent-sid812-t2.json
     */
    public static String QUALITY_API_CATEGORYPERCENTLIST = getQualityApiRequestUrl().concat("/quality/qualitycategorypercent");


}
