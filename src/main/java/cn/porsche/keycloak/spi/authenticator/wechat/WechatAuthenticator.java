package cn.porsche.keycloak.spi.authenticator.wechat;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import cn.porsche.keycloak.spi.util.AuthenticationError;
import cn.porsche.keycloak.spi.util.AuthenticationException;
import java.util.List;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserModel;

public class WechatAuthenticator extends BaseAuthenticator {

  @Override
  protected void doAuthenticate(AuthenticationFlowContext context) {
    String unionId = retrieveParameter(context, WechatAuthenticatorFactory.PROPERTY_FORM_WECHAT_UNIONID);

    // 参数校验
    if ("".equals(unionId)) {
      throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "微信号不能为空");
    }

    // 使用微信 unionId 查询用户
    List<UserModel> userModelList = context.getSession().userStorageManager().searchForUserByUserAttribute(
        getPropertyValue(context, WechatAuthenticatorFactory.PROPERTY_USER_ATTRIBUTE_WECHAT_UNIONID),
        unionId, context.getRealm());
    if (userModelList.size() > 0) {
      context.setUser(userModelList.get(0));
      context.success();
    } else {
      throw new AuthenticationException(AuthenticationError.USER_NOT_FOUNF_ERROR, "微信号", unionId);
    }
  }
}
