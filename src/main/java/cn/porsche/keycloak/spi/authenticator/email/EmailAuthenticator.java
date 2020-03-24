package cn.porsche.keycloak.spi.authenticator.email;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import cn.porsche.keycloak.spi.util.AuthenticationError;
import cn.porsche.keycloak.spi.util.AuthenticationException;
import cn.porsche.keycloak.spi.util.ExceptionUtils;
import cn.porsche.keycloak.spi.util.JsonUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;

public class EmailAuthenticator extends BaseAuthenticator {

  @Override
  protected void doAuthenticate(AuthenticationFlowContext context) {
    String email = retrieveParameter(context, EmailAuthenticatorFactory.PROPERTY_FORM_EMAIL);
    String code = retrieveParameter(context, EmailAuthenticatorFactory.PROPERTY_FORM_CODE);

    // 参数校验
    if ("".equals(email)) {
      throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "邮箱不能为空");
    }
    if ("".equals(code)) {
      throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "验证码不能为空");
    }

    // 使用邮箱查询用户
    Map<String, String> searchMap = Maps.newHashMap();
    searchMap.put("email", email);
    List<UserModel> userModelList = context.getSession().userStorageManager().searchForUser(searchMap, context.getRealm());

    if (userModelList.size() > 0) {
      // 用户存在，校验邮箱验证码
      try {
        doEMSAuthenticate(context, email, code);
        context.setUser(userModelList.get(0));
        context.success();
      } catch (AuthenticationException e) {
        throw e;
      } catch (Exception e) {
        throw new AuthenticationException(AuthenticationError.EMS_REQUEST_ERROR, ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new AuthenticationException(AuthenticationError.USER_NOT_FOUNF_ERROR, "邮箱", email);
    }
  }

  private void doEMSAuthenticate(AuthenticationFlowContext context, String phone, String code)
      throws IOException {
    String requestUrl = getPropertyValue(context, EmailAuthenticatorFactory.PROPERTY_EMS_REQUEST_URL);
    String requestMethod = getPropertyValue(context, EmailAuthenticatorFactory.PROPERTY_EMS_REQUEST_METHOD);

    HttpClient client = HttpClients.createDefault();
    Map<String, Object> requestParam = buildRequestParam(context, phone, code);

    HttpResponse response = null;
    if ("get".equals(requestMethod)) {
      HttpGet httpGet = new HttpGet(requestUrl + buildGetParam());
      response = client.execute(httpGet);
    } else if ("post.form".equals(requestMethod) || "post.json".equals(requestMethod)) {
      HttpPost httpPost = new HttpPost(requestUrl);
      if ("post.form".equals(requestMethod)) {
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencode");
        List<NameValuePair> nameValuePairList = Lists.newArrayList();
        requestParam.forEach((key, value) -> nameValuePairList.add(new BasicNameValuePair(key, value.toString())));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairList, "utf-8"));
      } else {
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(JsonUtil.toString(requestParam)));
      }
      response = client.execute(httpPost);
    } else {
      throw new AuthenticationException(AuthenticationError.CONFIG_INVALID_ERROR, EmailAuthenticatorFactory.PROPERTY_EMS_REQUEST_METHOD + requestMethod);
    }

    String checkKey = getPropertyValue(context, EmailAuthenticatorFactory.PROPERTY_EMS_RESPONSE_CHECK_KEY);
    String checkValue = getPropertyValue(context, EmailAuthenticatorFactory.PROPERTY_EMS_RESPONSE_CHECK_VALUE);

    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      Map<String, Object> responseData = JsonUtil.parseObject(EntityUtils.toString(response.getEntity()), Map.class);
      String[] keyArray = checkKey.split("\\.");
      Object value = responseData;
      for (String key : keyArray) {
        if (value instanceof Map) {
          value = ((Map<String, Object>) value).get(key);
        }
      }

      if ((value instanceof String && checkValue.equals(value)) ||
          (value instanceof Integer && Integer.parseInt(checkValue) == (Integer) value) ||
          (value instanceof Boolean && Boolean.valueOf(checkValue) == value)) {
        return;
      } else {
        throw new AuthenticationException(AuthenticationError.PHONE_CODE_INVALID_ERROR);
      }
    } else {
      throw new AuthenticationException(AuthenticationError.EMS_REQUEST_ERROR, String.valueOf(response.getStatusLine().getStatusCode()));
    }
  }

  private Map<String, Object> buildRequestParam(AuthenticationFlowContext context, String phone, String code) {
    String requestParamDefault = getPropertyValue(context, EmailAuthenticatorFactory.PROPERTY_EMS_REQUEST_PARAM_DEFAULT);
    String requestParamEmail = getPropertyValue(context, EmailAuthenticatorFactory.PROPERTY_EMS_REQUEST_PARAM_EMAIL);
    String requestParamCode = getPropertyValue(context, EmailAuthenticatorFactory.PROPERTY_EMS_REQUEST_PARAM_CODE);

    Map<String, Object> requestParam = JsonUtil.parseObject(requestParamDefault, Map.class);
    requestParam.put(requestParamEmail, phone);
    requestParam.put(requestParamCode, code);
    return requestParam;
  }

  private String buildGetParam() {
    return "";
  }
}
