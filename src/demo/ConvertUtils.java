package demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

public abstract class ConvertUtils {

    public static String toHex(byte input[]){
        if(input == null){
            return null;
        }
        StringBuffer output = new StringBuffer(input.length * 2);
        for(int i = 0; i < input.length; i++){
            int current = input[i] & 0xff;
            if(current < 16){
                output.append("0");
            }
            output.append(Integer.toString(current, 16));
        }
        return output.toString();
    }

    public static byte[] fromHex(String input){
        if(input == null){
            return null;
        }
        byte output[] = new byte[input.length() / 2];
        for(int i = 0; i < output.length; i++){
            output[i] = (byte)Integer.parseInt(input.substring(i * 2, (i + 1) * 2), 16);
        }
        return output;
    }

    public static String timeZoneToString(TimeZone tz){
        return tz != null ? tz.getID() : "";
    }

    public static TimeZone stringToTimeZone(String tzString){
        return TimeZone.getTimeZone(tzString != null ? tzString : "");
    }
    
    /**
     * 将请求参数封装成map对象
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Map<String,String> getParameterRequsetMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
}