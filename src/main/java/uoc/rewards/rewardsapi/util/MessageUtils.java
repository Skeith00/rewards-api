package uoc.rewards.rewardsapi.util;


import org.springframework.http.HttpStatus;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


public class MessageUtils {


    public static Map<String, Object> getErrorAttributes(HttpStatus status, String message) {
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("timestamp", new Date());
        attributes.put("error", status.getReasonPhrase());
        attributes.put("status", status.value());
        attributes.put("message", message);
        return attributes;
    }
}