package com.cynen.sms.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.cynen.sms.utils.SmsUtils;

/**
 * 监听 activemq中的消息，进行消费和发送短信。
 * @author myth
 *
 */
@Component
public class UserSmsListener {
	
	@Autowired
	private SmsUtils smsUtils;
	
    // 从配置文件直接读取参数
    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;
    
	// 监听ActiveMQ上的用户注册的验证码。
	@JmsListener(destination="pinyougou.user.sms")
	public void sendMsg(Map map) {
		String mobile = (String) map.get("mobile");
		String checkcode = (String) map.get("checkcode");
		System.out.println("手机号：" +mobile+",验证码是 ：" + checkcode );
		// 只接受 map类型的参数。
		String TemplateParam = "{\"code\":\""+checkcode+"\"}";
		smsUtils.sendSms(mobile, template_code, TemplateParam, sign_name);
	}
	
	
}
