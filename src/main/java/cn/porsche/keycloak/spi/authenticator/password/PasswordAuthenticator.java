package cn.porsche.keycloak.spi.authenticator.password;

import cn.porsche.keycloak.spi.authenticator.BaseAuthenticator;
import cn.porsche.keycloak.spi.util.AuthenticationError;
import cn.porsche.keycloak.spi.util.AuthenticationException;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

public class PasswordAuthenticator extends BaseAuthenticator {

  @Override
  protected void doAuthenticate(AuthenticationFlowContext context) {
    String username = retrieveParameter(context, PasswordAuthenticatorFactory.PROPERTY_FORM_USERNAME);
    String password = retrieveParameter(context, PasswordAuthenticatorFactory.PROPERTY_FORM_PASSWORD);

    // 参数校验
    if ("".equals(username)) {
      throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "用户名不能为空");
    }
    if ("".equals(password)) {
      throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "密码不能为空");
    }

    // 使用用户名查询用户
    UserModel userModel = context.getSession().userStorageManager().getUserByUsername(
      username, context.getRealm()
    );
    if (userModel != null) {
      boolean isValid = context.getSession().userCredentialManager().isValid(
        context.getRealm(), userModel, UserCredentialModel.password(password)
      );
      if (isValid) {
        context.setUser(userModel);
        context.success();
      } else {
        throw new AuthenticationException(AuthenticationError.PASSWORD_INVALID_ERROR);
      }
    } else {
      throw new AuthenticationException(AuthenticationError.USER_NOT_FOUNF_ERROR, "用户名", username);
    }
  }
}
