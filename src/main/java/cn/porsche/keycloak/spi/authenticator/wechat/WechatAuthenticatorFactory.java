package cn.porsche.keycloak.spi.authenticator.wechat;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import cn.porsche.keycloak.spi.authenticator.BaseAuthenticatorFactory;
import cn.porsche.keycloak.spi.util.LoginType;
import com.google.common.collect.Lists;
import java.util.List;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;

public class WechatAuthenticatorFactory extends BaseAuthenticatorFactory {

  @Override
  protected LoginType getLoginType() {
    return LoginType.WECHAT;
  }

  @Override
  public Authenticator create(KeycloakSession session) {
    BaseAuthenticator authenticator = new WechatAuthenticator();
    authenticator.setLoginType(getLoginType());
    authenticator.setSession(session);
    return authenticator;
  }

  public static final String PROPERTY_USER_ATTRIBUTE_WECHAT_UNIONID = "user.attribute.wechat.unionid";
  public static final String PROPERTY_FORM_WECHAT_UNIONID = "form.wechat.unionid";

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    List<ProviderConfigProperty> propertyList = Lists.newArrayList();
    // 用户属性
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_USER_ATTRIBUTE_WECHAT_UNIONID, "user attribute key - wechat unionid", "用户属性键名称[微信uid]", ProviderConfigProperty.STRING_TYPE, "wechat_unionid"
    ));
    // 登陆表单
    propertyList.add(new ProviderConfigProperty(
        PROPERTY_FORM_WECHAT_UNIONID, "login form key - wechat unionid", "登录表单键名称[微信uid]", ProviderConfigProperty.STRING_TYPE, "wechat_unionid"
    ));

    return propertyList;
  }
}
