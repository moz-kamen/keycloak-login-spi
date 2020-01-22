package cn.porsche.keycloak.spi.util;

public class AuthenticationException extends RuntimeException {

  private AuthenticationError error;
  private String[] args;

  public AuthenticationException(AuthenticationError error, String... args) {
    this.error = error;
    this.args = args;
  }

  public AuthenticationError getError() {
    return error;
  }

  public void setError(AuthenticationError error) {
    this.error = error;
  }

  public String[] getArgs() {
    return args;
  }

  public void setArgs(String[] args) {
    this.args = args;
  }
}
