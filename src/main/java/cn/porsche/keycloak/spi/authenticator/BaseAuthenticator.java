package cn.porsche.keycloak.spi.authenticator;

import cn.porsche.keycloak.spi.authenticator.phone.PhoneAuthenticator;
import cn.porsche.keycloak.spi.util.AuthenticationError;
import cn.porsche.keycloak.spi.util.AuthenticationException;
import cn.porsche.keycloak.spi.util.ExceptionUtils;
import cn.porsche.keycloak.spi.util.LoginType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.OAuth2ErrorRepresentation;

public abstract class BaseAuthenticator implements Authenticator {

  protected static Logger logger = Logger.getLogger(PhoneAuthenticator.class);

  public static final String PROPERTY_LOGIN_TYPE = "login_type";

  protected KeycloakSession session;

  protected LoginType loginType;

  @Override
  public void authenticate(AuthenticationFlowContext context) {
    try {
      String loginTypeCode = retrieveParameterByString(context, PROPERTY_LOGIN_TYPE);

      // 参数校验
      if ("".equals(loginTypeCode)) {
        throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "login_type不能为空");
      }
      if (!LoginType.containsCode(loginTypeCode)) {
        throw new AuthenticationException(AuthenticationError.PARAM_NOT_CHECKED_ERROR, "不支持的login_type[" + loginTypeCode + "]");
      }

      if (loginType.getCode().equals(loginTypeCode)) {
        doAuthenticate(context);
      } else {
        context.attempted();
      }
    } catch (AuthenticationException e) {
      Response challengeResponse = getErrorResponse(e);
      context.failure(e.getError().getFlowError(), challengeResponse);
    } catch (Exception e) {
      Response challengeResponse = getErrorResponse(AuthenticationError.SYSTEM_ERROR, ExceptionUtils.getStackTrace(e));
      context.failure(AuthenticationError.SYSTEM_ERROR.getFlowError(), challengeResponse);
    }
  }

  protected abstract void doAuthenticate(AuthenticationFlowContext context);

  protected String retrieveParameterByString(AuthenticationFlowContext context, String param) {
    MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
    String value = inputData.getFirst(param);
    return value != null ? value : "";
  }

  protected String retrieveParameter(AuthenticationFlowContext context, String param) {
    MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
    String value = inputData.getFirst(getPropertyValue(context, param));
    return value != null ? value : "";
  }

  protected String getPropertyValue(AuthenticationFlowContext context, String key) {
    AuthenticatorConfigModel config = context.getAuthenticatorConfig();
    if (config == null) {
      throw new AuthenticationException(AuthenticationError.CONFIG_UNINITIALIZED_ERROR, loginType.getCode() + " login authenticator");
    }
    return config.getConfig().get(key);
  }

  protected Response getErrorResponse(AuthenticationException e) {
    return getErrorResponse(e.getError(), e.getArgs());
  }

  protected Response getErrorResponse(AuthenticationError error, String... args) {
    String message = args.length > 0 ? String.format(error.getFormatMessage(), args) : error.getDefaultMessage();
    return getErrorResponse(error.getHttpStatus(), error.getCode(), message);
  }

  protected Response getErrorResponse(int status, String error, String errorDescription) {
    OAuth2ErrorRepresentation errorRep = new OAuth2ErrorRepresentation(error, errorDescription);
    return Response.status(status).entity(errorRep).type(MediaType.APPLICATION_JSON_TYPE).build();
  }

  @Override
  public void action(AuthenticationFlowContext context) {
  }

  @Override
  public boolean requiresUser() {
    return false;
  }

  @Override
  public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
  }

  @Override
  public void close() {
  }

  public KeycloakSession getSession() {
    return session;
  }

  public void setSession(KeycloakSession session) {
    this.session = session;
  }

  public LoginType getLoginType() {
    return loginType;
  }

  public void setLoginType(LoginType loginType) {
    this.loginType = loginType;
  }
}
