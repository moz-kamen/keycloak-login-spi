package cn.porsche.keycloak.spi.authenticator.password;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticatorFactory;
import cn.porsche.keycloak.spi.util.LoginType;
import com.google.common.collect.Lists;
import java.util.List;
import org.keycloak.provider.ProviderConfigProperty;

public class PasswordAuthenticatorFactory extends BaseAuthenticatorFactory {

  @Override
  protected LoginType getLoginType() {
    return LoginType.PASSWORD;
  }

  public static final String PROPERTY_FORM_USERNAME = "form.username";
  public static final String PROPERTY_FORM_PASSWORD = "form.password";

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    List<ProviderConfigProperty> propertyList = Lists.newArrayList();
    // 登陆表单
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_FORM_USERNAME, "login form key - username", "登录表单键名称[用户名]", ProviderConfigProperty.STRING_TYPE, "username"
    ));
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_FORM_PASSWORD, "login form key - password", "登陆表单键名称[密码]", ProviderConfigProperty.STRING_TYPE, "password"
    ));

    return propertyList;
  }
}
