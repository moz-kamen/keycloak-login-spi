package cn.porsche.keycloak.spi.authenticator.phone;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import cn.porsche.keycloak.spi.authenticator.BaseAuthenticatorFactory;
import cn.porsche.keycloak.spi.util.LoginType;
import com.google.common.collect.Lists;
import java.util.List;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;

public class PhoneAuthenticatorFactory extends BaseAuthenticatorFactory {

  @Override
  protected LoginType getLoginType() {
    return LoginType.PHONE;
  }

  @Override
  public Authenticator create(KeycloakSession session) {
    BaseAuthenticator authenticator = new PhoneAuthenticator();
    authenticator.setLoginType(getLoginType());
    authenticator.setSession(session);
    return authenticator;
  }

  public static final String PROPERTY_USER_ATTRIBUTE_PHONE = "user.attribute.phone";
  public static final String PROPERTY_FORM_PHONE = "form.phone";
  public static final String PROPERTY_FORM_CODE = "form.code";
  public static final String PROPERTY_SMS_REQUEST_URL = "sms.request.url";
  public static final String PROPERTY_SMS_REQUEST_METHOD = "sms.request.method";
  public static final String PROPERTY_SMS_REQUEST_PARAM_DEFAULT = "sms.request.param.default";
  public static final String PROPERTY_SMS_REQUEST_PARAM_PHONE = "sms.request.param.phone";
  public static final String PROPERTY_SMS_REQUEST_PARAM_CODE = "sms.request.param.code";
  public static final String PROPERTY_SMS_RESPONSE_CHECK_KEY = "sms.response.check.key";
  public static final String PROPERTY_SMS_RESPONSE_CHECK_VALUE = "sms.response.check.value";

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    List<ProviderConfigProperty> propertyList = Lists.newArrayList();
    // 用户属性
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_USER_ATTRIBUTE_PHONE, "user attribute key - phone", "用户属性键名称[手机号]", ProviderConfigProperty.STRING_TYPE, "phone"
    ));
    // 登陆表单
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_FORM_PHONE, "login form key - phone", "登录表单键名称[手机号]", ProviderConfigProperty.STRING_TYPE, "phone"
    ));
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_FORM_CODE, "login form key - code", "登陆表单键名称[验证码]", ProviderConfigProperty.STRING_TYPE, "code"
    ));
    // 短信服务接口
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_SMS_REQUEST_URL, "sms request url", "SMS校验接口地址", ProviderConfigProperty.STRING_TYPE, "https://"
    ));
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_SMS_REQUEST_METHOD, "sms request method", "SMS校验接口请求类型[get|post.form|post.json]", ProviderConfigProperty.STRING_TYPE, "post.json"
    ));
    // 短信接口请求参数
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_SMS_REQUEST_PARAM_DEFAULT, "sms request param - default", "SMS校验默认参数", ProviderConfigProperty.STRING_TYPE, "{}"
    ));
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_SMS_REQUEST_PARAM_PHONE, "sms request param - phone", "SMS校验手机号参数名", ProviderConfigProperty.STRING_TYPE, "phone"
    ));
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_SMS_REQUEST_PARAM_CODE, "sms request param - code", "SMS校验验证码参数名", ProviderConfigProperty.STRING_TYPE, "code"
    ));
    // 短信接口响应结果
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_SMS_RESPONSE_CHECK_KEY, "sms response check - key", "SMS校验结果确认键", ProviderConfigProperty.STRING_TYPE, "phone"
    ));
    propertyList.add(new ProviderConfigProperty(
      PROPERTY_SMS_RESPONSE_CHECK_VALUE, "sms response check - value", "SMS校验结果确认值", ProviderConfigProperty.STRING_TYPE, "true"
    ));

    return propertyList;
  }
}
