package dingdingisv.web.rest;

import com.codahale.metrics.annotation.Timed;
import dingdingisv.auth.AuthHelper;
import dingdingisv.config.Constants;
import dingdingisv.domain.Isvapp;
import dingdingisv.security.SecurityUtils;
import dingdingisv.service.IsvappPermantCodeService;
import dingdingisv.service.IsvappService;
import dingdingisv.web.rest.dto.IsvappDTO;
import dingdingisv.web.rest.dto.IsvappPermantCodeDTO;
import dingdingisv.web.rest.dto.IsvappUpdateDTO;
import dingdingisv.web.rest.dto.OutputDTO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
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

    private final Logger log = LoggerFactory.getLogger(IsvappPermantCodeResource.class);

    @ApiOperation(value = "获取指定企业 accessToken ", notes = "获取指定企业 accessToken", position = 1)
    @RequestMapping(value = "/getCorpToken/{corpId}", method = RequestMethod.GET)
    @Timed
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
                return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_ISV_CORPID_NOT_FOUND, "不存在的 corpId"));
            }

        } else {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_USER_UNAUTH, "用户未授权"));
        }
        return ResponseEntity.ok(new OutputDTO<>(0, "success", accessToken));
    }


    @ApiOperation(value = "设置套件基本信息", notes = "设置套件信息", position = 1)
    @RequestMapping(value = "/isvapps", method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<?> configureIsvapp(@RequestBody IsvappUpdateDTO isvappDTO) throws URISyntaxException {
        log.debug("REST request to update Isvapp : {}", isvappDTO);

        String isvKey = SecurityUtils.getCurrentUserLogin();
        IsvappDTO oldIsvapp = isvappService.findOneByIsvKey(isvKey);

        if (oldIsvapp != null) {
            oldIsvapp.setToken(isvappDTO.getToken());
            oldIsvapp.setSuiteEncodingAesKey(isvappDTO.getSuiteEncodingAesKey());
            oldIsvapp.setSuiteKey(isvappDTO.getSuiteKey());
            oldIsvapp.setSuiteSecret(isvappDTO.getSuiteSecret());
            IsvappDTO result = isvappService.save(oldIsvapp);
            return ResponseEntity.ok(new OutputDTO<>(0, "success", result));

        } else {
            return ResponseEntity.ok(new OutputDTO<>(Constants.ERROR_APPLICATION, "未初始化的数据,请联系管理员"));
        }

    }
}
