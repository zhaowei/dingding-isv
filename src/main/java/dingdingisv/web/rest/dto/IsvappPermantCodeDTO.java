package dingdingisv.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the IsvappPermantCode entity.
 */
public class IsvappPermantCodeDTO implements Serializable {

    private Long id;

    private Integer isvFid;

    private String corpId;

    private String permantCode;

    private String accessToken;

    private String jsTicket;

    private String authCorpInfo;

    private String authUserInfo;

    private String authInfo;

    private String agent;


    private ZonedDateTime createTime;

    private ZonedDateTime updateTime;

    private ZonedDateTime beginTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsvFid() {
        return isvFid;
    }

    public void setIsvFid(Integer isvFid) {
        this.isvFid = isvFid;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getPermantCode() {
        return permantCode;
    }

    public void setPermantCode(String permantCode) {
        this.permantCode = permantCode;
    }
    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }
    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IsvappPermantCodeDTO isvappPermantCodeDTO = (IsvappPermantCodeDTO) o;

        if ( ! Objects.equals(id, isvappPermantCodeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getJsTicket() {
        return jsTicket;
    }

    public void setJsTicket(String jsTicket) {
        this.jsTicket = jsTicket;
    }

    public ZonedDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(ZonedDateTime beginTime) {

        this.beginTime = beginTime;
    }

    @Override
    public String toString() {
        return "IsvappPermantCodeDTO{" +
            "id=" + id +
            ", isvFid=" + isvFid +
            ", corpId='" + corpId + '\'' +
            ", permantCode='" + permantCode + '\'' +
            ", accessToken='" + accessToken + '\'' +
            ", jsTicket='" + jsTicket + '\'' +
            ", authCorpInfo='" + authCorpInfo + '\'' +
            ", authUserInfo='" + authUserInfo + '\'' +
            ", authInfo='" + authInfo + '\'' +
            ", agent='" + agent + '\'' +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", beginTime=" + beginTime +
            '}';
    }

    public String getAuthCorpInfo() {
        return authCorpInfo;
    }

    public void setAuthCorpInfo(String authCorpInfo) {
        this.authCorpInfo = authCorpInfo;
    }

    public String getAuthUserInfo() {
        return authUserInfo;
    }

    public void setAuthUserInfo(String authUserInfo) {
        this.authUserInfo = authUserInfo;
    }

    public String getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(String authInfo) {
        this.authInfo = authInfo;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
