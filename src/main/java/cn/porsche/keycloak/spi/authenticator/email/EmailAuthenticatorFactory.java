package cn.porsche.keycloak.spi.authenticator.email;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import cn.porsche.keycloak.spi.authenticator.BaseAuthenticatorFactory;
import cn.porsche.keycloak.spi.authenticator.phone.PhoneAuthenticator;
import cn.porsche.keycloak.spi.util.LoginType;
import com.google.common.collect.Lists;
import java.util.List;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;

public class EmailAuthenticatorFactory extends BaseAuthenticatorFactory {

  @Override
  protected LoginType getLoginType() {
    return LoginType.EMAIL;
  }

  @Override
  public Authenticator create(KeycloakSession session) {
    BaseAuthenticator authenticator = new EmailAuthenticator();
    authenticator.setLoginType(getLoginType());
    authenticator.setSession(session);
    return authenticator;
  }

  public static final String PROPERTY_FORM_EMAIL = "form.email";
  public static final String PROPERTY_FORM_CODE = "form.code";
  public static final String PROPERTY_EMS_REQUEST_URL = "ems.request.url";
  public static final String PROPERTY_EMS_REQUEST_METHOD = "ems.request.method";
  public static final String PROPERTY_EMS_REQUEST_PARAM_DEFAULT = "ems.request.param.default";
  public static final String PROPERTY_EMS_REQUEST_PARAM_EMAIL = "ems.request.param.email";
  public static final String PROPERTY_EMS_REQUEST_PARAM_CODE = "ems.request.param.code";
  public static final String PROPERTY_EMS_RESPONSE_CHECK_KEY = "ems.response.check.key";
  public static final String PROPERTY_EMS_RESPONSE_CHECK_VALUE = "ems.response.check.value";

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    List<ProviderConfigProperty> propertyList = Lists.newArrayList();
    // 登陆表单
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_FORM_EMAIL, "login form key - email", "登录表单键名称[邮箱]", ProviderConfigProperty.STRING_TYPE, "email"
    ));
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_FORM_CODE, "login form key - code", "登陆表单键名称[验证码]", ProviderConfigProperty.STRING_TYPE, "code"
    ));
    // 短信服务接口
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_EMS_REQUEST_URL, "ems request url", "EMS校验接口地址", ProviderConfigProperty.STRING_TYPE, "https://"
    ));
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_EMS_REQUEST_METHOD, "ems request method", "EMS校验接口请求类型[get|post.form|post.json]", ProviderConfigProperty.STRING_TYPE, "post.json"
    ));
    // 短信接口请求参数
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_EMS_REQUEST_PARAM_DEFAULT, "ems request param - default", "EMS校验默认参数", ProviderConfigProperty.STRING_TYPE, "{}"
    ));
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_EMS_REQUEST_PARAM_EMAIL, "ems request param - email", "EMS校验邮箱参数名", ProviderConfigProperty.STRING_TYPE, "email"
    ));
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_EMS_REQUEST_PARAM_CODE, "ems request param - code", "EMS校验验证码参数名", ProviderConfigProperty.STRING_TYPE, "code"
    ));
    // 短信接口响应结果
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_EMS_RESPONSE_CHECK_KEY, "ems response check - key", "EMS校验结果确认键", ProviderConfigProperty.STRING_TYPE, "email"
    ));
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_EMS_RESPONSE_CHECK_VALUE, "ems response check - value", "EMS校验结果确认值", ProviderConfigProperty.STRING_TYPE, "true"
    ));

    return propertyList;
  }
}
