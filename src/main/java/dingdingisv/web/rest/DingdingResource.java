package dingdingisv.web.rest;

import dingdingisv.auth.AuthHelper;
import dingdingisv.domain.Isvapp;
import dingdingisv.security.SecurityUtils;
import dingdingisv.service.IsvappPermantCodeService;
import dingdingisv.service.IsvappService;
import dingdingisv.web.rest.dto.IsvappDTO;
import dingdingisv.web.rest.dto.IsvappPermantCodeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by zhaowei on 16/7/3.
 */

@RestController
@RequestMapping("/dingding")
public class DingdingResource {

    @Inject
    private IsvappService isvappService;

    @Inject
    private IsvappPermantCodeService isvappPermantCodeService;

    public static final long cacheTime = 1000 * 60 * 55 * 2;

    @RequestMapping(value = "/getCorpToken/{corpId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccessToken(@PathVariable String corpId) {

        String isvKey = SecurityUtils.getCurrentUserLogin();
        IsvappDTO isvappDTO = isvappService.findOneByIsvKey(isvKey);

        String accessToken = null;
        if (isvappDTO != null) {
            IsvappPermantCodeDTO isvappPermantCodeDTO = isvappPermantCodeService.findOneByIsvFidAndCorpId(isvappDTO.getId().intValue(), corpId);
            if (isvappPermantCodeDTO !=null && isvappPermantCodeDTO.getId()>0) {
                Long curTime = System.currentTimeMillis();
                Long beginTime = 0l;
                if (isvappPermantCodeDTO.getBeginTime() != null) {
                    beginTime = isvappPermantCodeDTO.getBeginTime().toInstant().toEpochMilli();
                }
                if (curTime - beginTime > cacheTime) {
                    //重新获取accesstoken
                    try {
                        accessToken = AuthHelper.getAccessToken(corpId, isvappPermantCodeDTO.getPermantCode() , isvappDTO.getSuiteToken());

                        if (accessToken != null) {
                            isvappPermantCodeDTO.setAccessToken(accessToken);
                            isvappPermantCodeDTO.setBeginTime(ZonedDateTime.now(ZoneId.systemDefault()).withNano(0));
                            isvappPermantCodeService.save(isvappPermantCodeDTO);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    accessToken = isvappPermantCodeDTO.getAccessToken();
                }
            } else {
                return new ResponseEntity<Object>("不存在的 corpId", HttpStatus.OK);
            }

        } else {
            return new ResponseEntity<>("未授权的请求", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Object>(accessToken, HttpStatus.OK);
    }
}
