package com.cynen.sms.utils;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.http.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SmsUtils {
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    /**
     * env是可以获取到application.yml中的所有的变量值.
     */
    @Autowired
    private Environment env;


    /**
     * 发送短信.
     * @param mobile  手机号
     * @param TemplateCode  模板代码
     * @param TemplateParam 模板中的参数值
     * @param SignName  模板签名.
     */
    public void sendSms(String mobile,String TemplateCode,String TemplateParam ,String SignName) {

            String accessKeyId = env.getProperty("aliyun.sms.accessKeyId");
            String accessSecret = env.getProperty("aliyun.sms.accessSecret");
            DefaultProfile profile = DefaultProfile.getProfile("", accessKeyId, accessSecret);
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain(domain);
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("PhoneNumbers", mobile);
            request.putQueryParameter("TemplateCode", TemplateCode);
            request.putQueryParameter("TemplateParam", TemplateParam);
            request.putQueryParameter("SignName", SignName);
            try {
                CommonResponse response = client.getCommonResponse(request);
                System.out.println(response.getData());
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
    }

}
