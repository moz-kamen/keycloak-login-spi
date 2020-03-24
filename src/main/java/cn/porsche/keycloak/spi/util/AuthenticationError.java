package cn.porsche.keycloak.spi.util;

import org.apache.http.HttpStatus;
import org.keycloak.authentication.AuthenticationFlowError;

public enum AuthenticationError {
  CONFIG_UNINITIALIZED_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, AuthenticationFlowError.IDENTITY_PROVIDER_ERROR,
      "1001", "配置文件未初始化: %s", "配置文件未初始化"),
  CONFIG_INVALID_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, AuthenticationFlowError.IDENTITY_PROVIDER_ERROR,
      "1002", "配置文件参数异常: 不可用的[%s]-[]", "配置文件参数异常"),

  USER_NOT_FOUNF_ERROR(HttpStatus.SC_UNAUTHORIZED, AuthenticationFlowError.INVALID_USER,
      "2001", "未查询到用户: %s[%s]", "未查询到用户"),

  PASSWORD_INVALID_ERROR(HttpStatus.SC_UNAUTHORIZED, AuthenticationFlowError.INVALID_CREDENTIALS,
      "3001", "用户密码不匹配", "用户密码不匹配"),
  PHONE_CODE_INVALID_ERROR(HttpStatus.SC_UNAUTHORIZED, AuthenticationFlowError.INVALID_CREDENTIALS,
      "3002", "手机验证码校验失败", "手机验证码校验失败"),

  SMS_REQUEST_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, AuthenticationFlowError.IDENTITY_PROVIDER_ERROR,
      "8001", "短信服务调用失败: %s", "短信服务调用失败"),
  EMS_REQUEST_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, AuthenticationFlowError.IDENTITY_PROVIDER_ERROR,
      "8002", "邮件服务调用失败: %s", "邮件服务调用失败"),

  PARAM_NOT_CHECKED_ERROR(HttpStatus.SC_BAD_REQUEST, AuthenticationFlowError.IDENTITY_PROVIDER_ERROR,
      "9001", "参数校验异常: %s", "参数校验异常"),

  SYSTEM_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, AuthenticationFlowError.IDENTITY_PROVIDER_ERROR,
      "9999", "未知内部异常: %s", "未知内部异常")
  ;

  private int httpStatus;
  private AuthenticationFlowError flowError;
  private String code;
  private String formatMessage;
  private String defaultMessage;

  AuthenticationError(int httpStatus, AuthenticationFlowError flowError, String code, String formatMessage, String defaultMessage) {
    this.httpStatus = httpStatus;
    this.flowError = flowError;
    this.code = code;
    this.formatMessage = formatMessage;
    this.defaultMessage = defaultMessage;
  }

  public int getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(int httpStatus) {
    this.httpStatus = httpStatus;
  }

  public AuthenticationFlowError getFlowError() {
    return flowError;
  }

  public void setFlowError(AuthenticationFlowError flowError) {
    this.flowError = flowError;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFormatMessage() {
    return formatMessage;
  }

  public void setFormatMessage(String formatMessage) {
    this.formatMessage = formatMessage;
  }

  public String getDefaultMessage() {
    return defaultMessage;
  }

  public void setDefaultMessage(String defaultMessage) {
    this.defaultMessage = defaultMessage;
  }
}
