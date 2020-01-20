package cn.porsche.keycloak.spi.authenticator;

import cn.porsche.keycloak.spi.authenticator.phone.PhoneAuthenticatorFactory;
import cn.porsche.keycloak.spi.util.LoginType;
import java.lang.reflect.InvocationTargetException;
import org.jboss.logging.Logger;
import org.keycloak.Config.Scope;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public abstract class BaseAuthenticatorFactory implements AuthenticatorFactory {

  protected static Logger logger = Logger.getLogger(PhoneAuthenticatorFactory.class);

  @Override
  public Authenticator create(KeycloakSession session) {
    LoginType loginType = getLoginType();
    BaseAuthenticator authenticator = createAuthenticator(loginType.getAuthenticatorClazz());
    authenticator.setSession(session);
    authenticator.setLoginType(getLoginType());
    return authenticator;
  }

  private BaseAuthenticator createAuthenticator(Class<BaseAuthenticator> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("创建" + clazz.getSimpleName() + "失败!");
  }

  protected abstract LoginType getLoginType();

  public static final Requirement[] REQUIREMENT_CHOICES = {
      Requirement.REQUIRED,
      Requirement.ALTERNATIVE,
      Requirement.DISABLED
  };

  @Override
  public String getId() {
    return getLoginType().getCode() + "-login-authenticator";
  }

  @Override
  public Requirement[] getRequirementChoices() {
    return REQUIREMENT_CHOICES;
  }

  @Override
  public String getDisplayType() {
    return getLoginType().getCode() + " login authenticator";
  }

  @Override
  public String getReferenceCategory() {
    return getLoginType().getCode() + " login authenticator";
  }

  @Override
  public String getHelpText() {
    return getLoginType().getName() + "校验器";
  }

  @Override
  public boolean isUserSetupAllowed() {
    return true;
  }

  @Override
  public boolean isConfigurable() {
    return true;
  }

  @Override
  public void init(Scope scope) {
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
  }

  @Override
  public void close() {
  }
}
