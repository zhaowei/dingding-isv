package dingdingisv.web.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.api.model.isv.CorpAgent;
import com.dingtalk.open.client.api.model.isv.CorpAuthInfo;
import com.dingtalk.open.client.api.model.isv.CorpAuthSuiteCode;
import dingdingisv.auth.AuthHelper;
import dingdingisv.config.Constants;
import dingdingisv.domain.Isvapp;
import dingdingisv.service.IsvappPermantCodeService;
import dingdingisv.service.IsvappService;
import dingdingisv.service.ServiceHelper;
import dingdingisv.utils.aes.DingTalkEncryptException;
import dingdingisv.utils.aes.DingTalkEncryptor;
import dingdingisv.web.rest.dto.IsvappDTO;
import dingdingisv.web.rest.dto.IsvappPermantCodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhaowei on 16/6/23.
 */
@RestController
@RequestMapping("/isvReceive")
public class IsvReceiveResource {

    private final Logger log = LoggerFactory.getLogger(IsvappPermantCodeResource.class);

    @Inject
    IsvappService isvappService;

    @Inject
    IsvappPermantCodeService isvappPermantCodeService;

    @RequestMapping(value = "/{isvKey}", method = RequestMethod.POST)
    public ResponseEntity<?> excute(HttpServletRequest request, HttpServletResponse response, @PathVariable String isvKey ) throws Exception {
        try{

            //IsvappPermantCodeDTO isvappPermantCodeDTO2 = isvappPermantCodeService.findOneByIsvFidAndCorpId(1, "ding69aa019cdf8690b8");

            IsvappDTO isvappDTO = isvappService.findOneByIsvKey(isvKey);
            if (isvappDTO == null) {
                return new ResponseEntity<Object>("access denty", HttpStatus.UNAUTHORIZED);
            }

            /** url中的签名 **/
            String msgSignature = request.getParameter("signature");
            /** url中的时间戳 **/
            String timeStamp = request.getParameter("timestamp");
            /** url中的随机字符串 **/
            String nonce = request.getParameter("nonce");

            /** post数据包数据中的加密数据 **/
            ServletInputStream sis = request.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(sis));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }


            JSONObject jsonEncrypt = JSONObject.parseObject(sb.toString());
            String encrypt = "";

            /** 取得JSON对象中的encrypt字段， **/
            try {
                encrypt = jsonEncrypt.getString("encrypt");
            } catch (Exception e) {
                e.printStackTrace();
            }

            /** 对encrypt进行解密 **/
            DingTalkEncryptor dingTalkEncryptor = null;
            String plainText = null;
            DingTalkEncryptException dingTalkEncryptException = null;

            try {
				/*创建加解密类
				 * 第一个参数为注册套件的之时填写的token
				 * 第二个参数为注册套件的之时生成的数据加密密钥（ENCODING_AES_KEY）
				 * 第三个参数，ISV开发传入套件的suiteKey，普通企业开发传Corpid
				 * 具体参数值请查看开发者后台(http://console.d.aliyun.com)
				 *
				 * 注：其中，对于第三个参数，在第一次接受『验证回调URL有效性事件的时候』
				 * 传入Env.CREATE_SUITE_KEY，对于这种情况，已在异常中catch做了处理。
				 * 具体区别请查看文档『验证回调URL有效性事件』
				 */
                dingTalkEncryptor = new DingTalkEncryptor(isvappDTO.getToken(), isvappDTO.getSuiteEncodingAesKey(), isvappDTO.getSuiteKey());
				/*
				 * 获取从encrypt解密出来的明文
				 */
                plainText = dingTalkEncryptor.getDecryptMsg(msgSignature, timeStamp, nonce, encrypt);
            } catch (DingTalkEncryptException e) {
                // TODO Auto-generated catch block
                dingTalkEncryptException = e;
                e.printStackTrace();
            } finally {
                if (dingTalkEncryptException != null) {
                    if (dingTalkEncryptException.code == DingTalkEncryptException.COMPUTE_DECRYPT_TEXT_CORPID_ERROR) {
                        try {
							/*
							 * 第一次创建套件生成加解密类需要传入Env.CREATE_SUITE_KEY，
							 */
                            dingTalkEncryptor = new DingTalkEncryptor(isvappDTO.getToken(), isvappDTO.getSuiteEncodingAesKey(),
                                isvappDTO.getCorpId());
                            plainText = dingTalkEncryptor.getDecryptMsg(msgSignature, timeStamp, nonce, encrypt);
                        } catch (DingTalkEncryptException e) {
                            // TODO Auto-generated catch block
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(dingTalkEncryptException.getMessage());
                        dingTalkEncryptException.printStackTrace();
                    }
                }
            }

        /*
			 *  对从encrypt解密出来的明文进行处理
			 *  不同的eventType的明文数据格式不同
			 */
            JSONObject plainTextJson = JSONObject.parseObject(plainText);
            String eventType = plainTextJson.getString("EventType");
			/* res是需要返回给钉钉服务器的字符串，一般为success
			 * "check_create_suite_url"和"check_update_suite_url"事件为random字段
			 * 具体请查看文档或者对应eventType的处理步骤
			 */
            String res = "success";

            switch (eventType) {
                case "suite_ticket":
                    log.debug("receive suite_ticket event : {}, {}", isvappDTO, request);
				/*"suite_ticket"事件每二十分钟推送一次,数据格式如下
				 * {
					  "SuiteKey": "suitexxxxxx",
					  "EventType": "suite_ticket ",
					  "TimeStamp": 1234456,
					  "SuiteTicket": "adsadsad"
					}
					{"EventType":"suite_ticket","SuiteKey":"suitetnc8ojj9819azjzl","SuiteTicket":"hy1mHrnBe297rMdOorO7Xpb9qQsGI7eyJ5FvbjIZtJUYcMaDgQ2T2OJhmdEyNxtM5btYh4Xfo3ZdrtYW1sEdY7","TimeStamp":"1466914309955"}
				 */
                    isvappDTO.setSuiteTicket(plainTextJson.getString("SuiteTicket"));

                    isvappDTO.setSuiteTicket(isvappDTO.getSuiteTicket());
                    //获取到suiteTicket之后需要换取suiteToken，
                    String suiteToken = ServiceHelper.getSuiteToken(isvappDTO.getSuiteKey(), isvappDTO.getSuiteSecret(), isvappDTO.getSuiteTicket());

                    isvappDTO.setSuiteToken(suiteToken);
                    isvappService.save(isvappDTO);
                    log.debug("save suite_ticket to db : {}, {}", isvappDTO, request);
				/*
				 * ISV应当把最新推送的suiteTicket做持久化存储，
				 * 以防重启服务器之后丢失了当前的suiteTicket
				 *
				 */
//                    JSONObject json = new JSONObject();
//                    json.put("suiteTicket", Constants.suiteTicket);
//                    json.put("suiteToken", suiteToken);
//                    FileUtils.write2File(json, "ticket");

                    break;
                case "tmp_auth_code":
				/*"tmp_auth_code"事件将企业对套件发起授权的时候推送
				 * 数据格式如下
				{
				  "SuiteKey": "suitexxxxxx",
				  "EventType": " tmp_auth_code",
				  "TimeStamp": 1234456,
				  "AuthCode": "adads"
				}
				*/
                    log.debug("receive tmp_auth_code event : {}, {}", isvappDTO, request);
                    Constants.authCode = plainTextJson.getString("AuthCode");
                    //Object value = FileUtils.getValue("ticket", "suiteToken");//获取当前的suiteToken
                    Object value = isvappDTO.getSuiteToken();

                    if (value == null || value.equals("")) {
                        break;
                    }
                    String suiteTokenPerm = value.toString();
				/*
				 * 拿到tmp_auth_code（临时授权码）后，需要向钉钉服务器获取企业的corpId（企业id）和permanent_code（永久授权码）
				 */
                    CorpAuthSuiteCode corpAuthSuiteCode = ServiceHelper.getPermanentCode(Constants.authCode, suiteTokenPerm);
                    String corpId = corpAuthSuiteCode.getAuth_corp_info().getCorpid();
                    String permanent_code = corpAuthSuiteCode.getPermanent_code();
				/*
				 * 将corpId（企业id）和permanent_code（永久授权码）做持久化存储
				 * 之后在获取企业的access_token时需要使用
				 */

                    IsvappPermantCodeDTO isvappPermantCodeDTO = new IsvappPermantCodeDTO();
                    try {
                        isvappPermantCodeDTO = isvappPermantCodeService.findOneByIsvFidAndCorpId(isvappDTO.getId().intValue(), corpId);
                    } catch (Exception e) {

                    } finally {

                        isvappPermantCodeDTO.setIsvFid(isvappDTO.getId().intValue());
                        isvappPermantCodeDTO.setCorpId(corpId);
                        isvappPermantCodeDTO.setPermantCode(permanent_code);
                        isvappPermantCodeService.save(isvappPermantCodeDTO);
                        log.debug("save tmp_auth_code to db : {}, {}", isvappDTO, request.toString());

                    }


//                    JSONObject jsonPerm = new JSONObject();
//                    jsonPerm.put(corpId, permanent_code);
//                    FileUtils.write2File(jsonPerm, "permanentcode");
				/*
				 * 对企业授权的套件发起激活，
				 */
                    ServiceHelper.getActivateSuite(suiteTokenPerm, isvappDTO.getSuiteKey(), corpId, permanent_code);

                    if (permanent_code != null) {
                        break;
                    }
				/*
				 * 获取对应企业的access_token，每一个企业都会有一个对应的access_token，访问对应企业的数据都将需要带上这个access_token
				 * access_token的过期时间为两个小时
				 */
                    try {
                        String accessToken = AuthHelper.getAccessToken(corpId, permanent_code, suiteTokenPerm);
                        Date d = new Date();
                        ZonedDateTime zdt = ZonedDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());

                        if (accessToken != null) {
                            isvappPermantCodeDTO.setAccessToken(accessToken);
                            isvappPermantCodeDTO.setBeginTime(ZonedDateTime.now(ZoneId.systemDefault()).withNano(0));
                            isvappPermantCodeService.save(isvappPermantCodeDTO);
                        }
                        //获取jsticket

                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        System.out.println(e1.toString());
                        e1.printStackTrace();
                    }
                    break;
                case "change_auth":
				/*"change_auth"事件将在企业授权变更消息发生时推送
				 * 数据格式如下
				{
				  "SuiteKey": "suitexxxxxx",
				  "EventType": " change_auth",
				  "TimeStamp": 1234456,
				  "AuthCorpId": "xxxxx"
				}
				*/

                    String corpid = plainTextJson.getString("AuthCorpId");
                    // 由于以下操作需要从持久化存储中获得数据，而本demo并没有做持久化存储（因为部署环境千差万别），所以没有具体代码，只有操作指导。
                    // 1.根据corpid查询对应的permanent_code(永久授权码)
                    // 2.调用『企业授权的授权数据』接口（ServiceHelper.getAuthInfo方法），此接口返回数据具体详情请查看文档。

                    IsvappPermantCodeDTO isvappCode = new IsvappPermantCodeDTO();
                    isvappPermantCodeDTO = isvappPermantCodeService.findOneByIsvFidAndCorpId(isvappDTO.getId().intValue(), corpid);
                    if (isvappPermantCodeDTO == null) {
                        break;
                    }
                    CorpAuthInfo corpAuthInfo = ServiceHelper.getAuthInfo(isvappDTO.getSuiteToken(), isvappDTO.getSuiteKey(), corpid, isvappCode.getPermantCode());
                    isvappPermantCodeDTO.setAuthCorpInfo(JSON.toJSONString(corpAuthInfo.getAuth_corp_info()));
                    isvappPermantCodeDTO.setAuthUserInfo(JSON.toJSONString(corpAuthInfo.getAuth_user_info()));
                    isvappPermantCodeDTO.setAuthInfo(JSON.toJSONString(corpAuthInfo.getAuth_info()));
                    isvappPermantCodeService.save(isvappPermantCodeDTO);


                    // 3.遍历从『企业授权的授权数据』接口中获取所有的微应用信息

                    //CorpAgent corpAgent = ServiceHelper.getAgent(isvappDTO.getSuiteToken(), isvappDTO.getSuiteKey(), corpid, isvappCode.getPermantCode(), "aa");
                    // 4.对每一个微应用都调用『获取企业的应用信息接口』（ServiceHelper.getAgent方法）
				/*
				 * 5.获取『获取企业的应用信息接口』接口返回值其中的"close"参数，才能得知微应用在企业用户做了授权变更之后的状态，有三种状态码
				 * 	分别为0，1，2.含义如下：
				 *  0:禁用（例如企业用户在OA后台禁用了微应用）
				 *  1:正常 (例如企业用户在禁用之后又启用了微应用)
				 *  2:待激活 (企业已经进行了授权，但是ISV还未为企业激活应用)
				 *  再根据具体状态做具体操作。
				 */

                    break;
                case "check_create_suite_url":
				/*"check_create_suite_url"事件将在创建套件的时候推送
				 * {
					  "EventType":"check_create_suite_url",
					  "Random":"brdkKLMW",
					  "TestSuiteKey":"suite4xxxxxxxxxxxxxxx"
					}
				 */
                    //此事件需要返回的"Random"字段，
                    res = plainTextJson.getString("Random");
                    String testSuiteKey = plainTextJson.getString("TestSuiteKey");
                    break;

                case "check_update_suite_url":
				/* "check_update_suite_url"事件将在更新套件的时候推送
				 * {
					  "EventType":"check_update_suite_url",
					  "Random":"Aedr5LMW",
					  "TestSuiteKey":"suited6db0pze8yao1b1y"

					}
				 */
                    res = plainTextJson.getString("Random");
                    break;

                case "suite_relieve":

                    Long isvFid = isvappDTO.getId();
                    String corpIdDel = plainTextJson.getString("AuthCorpId");
                    isvappPermantCodeService.deleteByIsvFidAndCorpId(isvFid.intValue(), corpIdDel);
                    //删除授权
                    break;
                default: // do something
                    break;
            }

            /** 对返回信息进行加密 **/
            long timeStampLong = Long.parseLong(timeStamp);
            Map<String, String> jsonMap = null;
            try {
				/*
				 * jsonMap是需要返回给钉钉服务器的加密数据包
				 */
                jsonMap = dingTalkEncryptor.getEncryptedMap(res, timeStampLong, nonce);
            } catch (DingTalkEncryptException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            JSONObject json = new JSONObject();
            json.putAll(jsonMap);
            //response.getWriter().append(json.toString());
            return new ResponseEntity<String>(json.toString(), HttpStatus.OK);

        } catch (Exception er) {

            er.printStackTrace();

        }

        return new ResponseEntity<String>("test suit", HttpStatus.OK);

    }

}
