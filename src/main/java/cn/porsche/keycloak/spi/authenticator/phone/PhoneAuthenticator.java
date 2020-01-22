package cn.porsche.keycloak.spi.authenticator.phone;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import cn.porsche.keycloak.spi.util.AuthenticationError;
import cn.porsche.keycloak.spi.util.AuthenticationException;
import cn.porsche.keycloak.spi.util.ExceptionUtils;
import cn.porsche.keycloak.spi.util.JsonUtil;
import com.google.common.collect.Lists;
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

public class PhoneAuthenticator extends BaseAuthenticator {

  @Override
  public void doAuthenticate(AuthenticationFlowContext context) {
    String phone = retrieveParameter(context, PhoneAuthenticatorFactory.PROPERTY_FORM_PHONE);
    String code = retrieveParameter(context, PhoneAuthenticatorFactory.PROPERTY_FORM_CODE);

    // 参数校验
    if ("".equals(phone)) {
      throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "手机号不能为空");
    }
    if ("".equals(code)) {
      throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "验证码不能为空");
    }

    // 使用手机号查询用户
    List<UserModel> userModelList = context.getSession().userStorageManager().searchForUserByUserAttribute(
        getPropertyValue(context, PhoneAuthenticatorFactory.PROPERTY_USER_ATTRIBUTE_PHONE),
        phone, context.getRealm());
    if (userModelList.size() > 0) {
      // 用户存在，校验手机验证码
      try {
        doSMSAuthenticate(context, phone, code);
        context.setUser(userModelList.get(0));
        context.success();
      } catch (AuthenticationException e) {
        throw e;
      } catch (Exception e) {
        throw new AuthenticationException(AuthenticationError.SMS_REQUEST_ERROR, ExceptionUtils.getStackTrace(e));
      }
    } else {
      throw new AuthenticationException(AuthenticationError.USER_NOT_FOUNF_ERROR, "手机号", phone);
    }
  }

  private void doSMSAuthenticate(AuthenticationFlowContext context, String phone, String code)
      throws IOException {
    String requestUrl = getPropertyValue(context, PhoneAuthenticatorFactory.PROPERTY_SMS_REQUEST_URL);
    String requestMethod = getPropertyValue(context, PhoneAuthenticatorFactory.PROPERTY_SMS_REQUEST_METHOD);

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
      throw new AuthenticationException(AuthenticationError.CONFIG_INVALID_ERROR, PhoneAuthenticatorFactory.PROPERTY_SMS_REQUEST_METHOD + requestMethod);
    }

    String checkKey = getPropertyValue(context, PhoneAuthenticatorFactory.PROPERTY_SMS_RESPONSE_CHECK_KEY);
    String checkValue = getPropertyValue(context, PhoneAuthenticatorFactory.PROPERTY_SMS_RESPONSE_CHECK_VALUE);

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
      throw new AuthenticationException(AuthenticationError.SMS_REQUEST_ERROR, String.valueOf(response.getStatusLine().getStatusCode()));
    }
  }

  private Map<String, Object> buildRequestParam(AuthenticationFlowContext context, String phone, String code) {
    String requestParamDefault = getPropertyValue(context, PhoneAuthenticatorFactory.PROPERTY_SMS_REQUEST_PARAM_DEFAULT);
    String requestParamPhone = getPropertyValue(context, PhoneAuthenticatorFactory.PROPERTY_SMS_REQUEST_PARAM_PHONE);
    String requestParamCode = getPropertyValue(context, PhoneAuthenticatorFactory.PROPERTY_SMS_REQUEST_PARAM_CODE);

    Map<String, Object> requestParam = JsonUtil.parseObject(requestParamDefault, Map.class);
    requestParam.put(requestParamPhone, phone);
    requestParam.put(requestParamCode, code);
    return requestParam;
  }

  private String buildGetParam() {
    return "";
  }
}
