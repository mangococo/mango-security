# 向spring容器注册以下接口的实现，可以替换默认的处理逻辑

- 密码加密解密策略
<br/>org.springframework.security.crypto.password.PasswordEncoder

- 表单登录用户信息读取逻辑
<br/>org.springframework.security.core.userdetails.UserDetailsService

- 社交登录用户信息读取逻辑
<br/>org.springframework.social.security.SocialUserDetailsService

- Session失效时的处理策略
<br/>org.springframework.security.web.session.InvalidSessionStrategy

- 并发登录导致前一个session失效时的处理策略配置
<br/>org.springframework.security.web.session.SessionInformationExpiredStrategy

- 退出时的处理逻辑
<br/>org.springframework.security.web.authentication.logout.LogoutSuccessHandler

- 短信发送的处理逻辑
<br/>stu.mango.security.core.validate.code.sms.SmsCodeSender

- 向spring容器注册名为imageValidateCodeGenerator的bean，可以替换默认的图片验证码生成逻辑,bean必须实现以下接口
<br/>stu.mango.security.core.validate.code.ValidateCodeGenerator

- 如果spring容器中有下面这个接口的实现，则在社交登录无法确认用户时，用此接口的实现自动注册用户，不会跳到注册页面
<br/>org.springframework.social.connect.ConnectionSignUp