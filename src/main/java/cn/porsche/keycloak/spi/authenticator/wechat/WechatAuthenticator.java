package cn.porsche.keycloak.spi.authenticator.wechat;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import java.util.List;
import javax.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserModel;

public class WechatAuthenticator extends BaseAuthenticator {

  @Override
  protected void doAuthenticate(AuthenticationFlowContext context) {
    String unionId = retrieveParameter(context, WechatAuthenticatorFactory.PROPERTY_FORM_WECHAT_UNIONID);
    // 参数校验

    // 使用微信 unionId 查询用户
    List<UserModel> userModelList = context.getSession().userStorageManager().searchForUserByUserAttribute(
        getPropertyValue(context, WechatAuthenticatorFactory.PROPERTY_USER_ATTRIBUTE_WECHAT_UNIONID),
        unionId, context.getRealm());
    if (userModelList.size() > 0) {
      context.setUser(userModelList.get(0));
      context.success();
    } else {
      // 用户不存在
      Response challengeResponse = getErrorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_request", "Invalid user credentials");
      context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
    }
  }
}
