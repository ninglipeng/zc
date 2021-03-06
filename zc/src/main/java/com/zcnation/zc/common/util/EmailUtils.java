package com.zcnation.zc.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.zcnation.zc.domain.ZcUserInfo;




public class EmailUtils {
	
	private static final String FROM = "xhymmc@163.com";

	/**
	 * 注册成功后,向用户发送帐户激活链接的邮件
	 * @param user 未激活的用户
	 */
	public static void sendAccountActivateEmail(ZcUserInfo zcUserInfo) {
		Session session = getSession();
		MimeMessage message = new MimeMessage(session);
		try {
			message.setSubject("帐户激活邮件");
			message.setSentDate(new Date());
			message.setFrom(new InternetAddress(FROM));
			message.setRecipient(RecipientType.TO, new InternetAddress(zcUserInfo.getEmail()));
			message.setContent("<a href='" + GenerateLinkUtils.generateActivateLink(zcUserInfo)+"'>点击激活帐户</a>","text/html;charset=utf-8");
			// 发送邮件
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送重设密码链接的邮件
	 */
	public static void sendResetPasswordEmail(ZcUserInfo zcUserInfo) {
		Session session = getSession();
		MimeMessage message = new MimeMessage(session);
//		hi xhymmc,
//
//		您在美团网申请了验证邮箱，请点击下面的链接，然后根据页面提示完成验证：
//
//
//		http://passport.meituan.com/account/retrievepassword/verifyresult?verifyinfo13=vwBApPRlExariJGtXkpc1wJAdEI1U-If
//
//		--
//		美团网 
		try {
			message.setSubject("找回您的帐户与密码");
			message.setSentDate(new Date());
			message.setFrom(new InternetAddress(FROM));
			message.setRecipient(RecipientType.TO, new InternetAddress(zcUserInfo.getEmail()));
			message.setContent("hi "+zcUserInfo.getUserName()+",<br/>您在小众派申请了验证邮箱，请点击下面的链接，然后根据页面提示完成验证：:<br/><a href='" + GenerateLinkUtils.generateResetPwdLink(zcUserInfo) +"'>"+GenerateLinkUtils.generateResetPwdLink(zcUserInfo)+"</a><br>--<br>小众派","text/html;charset=utf-8");
			// 发送邮件
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Session getSession() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", "smtp.163.com");
		props.setProperty("mail.smtp.port", "25");
		props.setProperty("mail.smtp.auth", "true");
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				String password = null;
				System.out.println(EmailUtils.class);
				System.out.println(EmailUtils.class.getResourceAsStream("password.dat"));
				InputStream is = EmailUtils.class.getResourceAsStream("password.dat");
				byte[] b = new byte[1024];
				try {
					int len = is.read(b);
					password = new String(b,0,len);
					System.out.println(password);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new PasswordAuthentication(FROM, password);
			}
			
		});
		return session;
	}
}
