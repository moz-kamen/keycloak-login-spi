package cn.porsche.keycloak.spi.authenticator.password;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import javax.ws.rs.core.Response;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

public class PasswordAuthenticator extends BaseAuthenticator {

  @Override
  protected void doAuthenticate(AuthenticationFlowContext context) {
    String username = retrieveParameter(context, PasswordAuthenticatorFactory.PROPERTY_FORM_USERNAME);
    String password = retrieveParameter(context, PasswordAuthenticatorFactory.PROPERTY_FORM_PASSWORD);
    // TODO 参数校验

    // 使用用户名查询用户
    UserModel userModel = context.getSession().userStorageManager().getUserByUsername(
      username, context.getRealm()
    );
    if (userModel != null ) {
      boolean isValid = context.getSession().userCredentialManager().isValid(
        context.getRealm(), userModel, UserCredentialModel.password(password)
      );
      if (isValid) {
        context.setUser(userModel);
        context.success();
      } else {
        Response challengeResponse = getErrorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_request", "Invalid user credentials");
        context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
      }
    } else {
      // 用户不存在
      Response challengeResponse = getErrorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_request", "Invalid user credentials");
      context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
    }
  }
}
