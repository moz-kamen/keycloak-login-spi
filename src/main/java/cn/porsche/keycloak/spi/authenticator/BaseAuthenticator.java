package cn.porsche.keycloak.spi.authenticator;

import cn.porsche.keycloak.spi.authenticator.phone.PhoneAuthenticator;
import cn.porsche.keycloak.spi.util.LoginType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.OAuth2ErrorRepresentation;

public abstract class BaseAuthenticator implements Authenticator {

  protected static Logger logger = Logger.getLogger(PhoneAuthenticator.class);

  public static final String PROPERTY_LOGIN_TYPE = "login_type";

  protected KeycloakSession session;

  protected LoginType loginType;

  @Override
  public void authenticate(AuthenticationFlowContext context) {
    String loginTypeCode = retrieveParameterByString(context, PROPERTY_LOGIN_TYPE);
    if (loginType.getCode().equals(loginTypeCode)) {
      doAuthenticate(context);
    } else {
      context.attempted();
    }
  }

  protected abstract void doAuthenticate(AuthenticationFlowContext context);

  protected String retrieveParameterByString(AuthenticationFlowContext context, String param) {
    MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
    return inputData.getFirst(param);
  }

  protected String retrieveParameter(AuthenticationFlowContext context, String param) {
    MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
    return inputData.getFirst(getPropertyValue(context, param));
  }

  protected String getPropertyValue(AuthenticationFlowContext context, String key) {
    AuthenticatorConfigModel config = context.getAuthenticatorConfig();
    return config.getConfig().get(key);
  }

  public Response getErrorResponse(int status, String error, String errorDescription) {
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
