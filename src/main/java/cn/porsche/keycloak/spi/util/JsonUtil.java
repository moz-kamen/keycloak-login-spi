package cn.porsche.keycloak.spi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtil {

  public static String toString(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "{}";
  }

  public static <T> T parseObject(String jsonStr, Class<T> clazz) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(jsonStr, clazz);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
