package dingdingisv.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A IsvappPermantCode.
 */
@Entity
@Table(name = "isvapp_permant_code")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "isvapppermantcode")
public class IsvappPermantCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "isv_fid")
    private Integer isvFid;

    @Column(name = "corp_id")
    private String corpId;

    @Column(name = "permant_code")
    private String permantCode;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "js_ticket")
    private String jsTicket;

    @Column(name = "begin_time")
    private ZonedDateTime beginTime;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @Column(name = "update_time")
    private ZonedDateTime updateTime;


    @Column(name = "auth_corp_info")
    private String authCorpInfo;

    @Column(name = "invite_code")
    private String inviteCode;

    @Column(name = "industry")
    private String industry;

    @Column(name = "corp_name")
    private String corpName;

    @Column(name = "license_code")
    private String licenseCode;

    @Column(name = "is_authenticated")
    private String isAuthenticated;

    @Column(name = "invite_url")
    private String inviteUrl;

    @Column(name = "auth_user_info")
    private String authUserInfo;

    @Column(name = "corp_logo_url")
    private String corpLogoUrl;

    @Column(name = "auth_info")
    private String authInfo;

    @Column(name = "agent")
    private String agent;

    @Column(name = "agent_id")
    private String agentid;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "app_id")
    private String appid;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAuthCorpInfo() {
        return authCorpInfo;
    }

    @Override
    public String toString() {
        return "IsvappPermantCode{" +
            "id=" + id +
            ", isvFid=" + isvFid +
            ", corpId='" + corpId + '\'' +
            ", permantCode='" + permantCode + '\'' +
            ", accessToken='" + accessToken + '\'' +
            ", jsTicket='" + jsTicket + '\'' +
            ", beginTime=" + beginTime +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", authCorpInfo='" + authCorpInfo + '\'' +
            ", inviteCode='" + inviteCode + '\'' +
            ", industry='" + industry + '\'' +
            ", corpName='" + corpName + '\'' +
            ", licenseCode='" + licenseCode + '\'' +
            ", isAuthenticated='" + isAuthenticated + '\'' +
            ", inviteUrl='" + inviteUrl + '\'' +
            ", authUserInfo='" + authUserInfo + '\'' +
            ", corpLogoUrl='" + corpLogoUrl + '\'' +
            ", authInfo='" + authInfo + '\'' +
            ", agent='" + agent + '\'' +
            ", agentid='" + agentid + '\'' +
            ", agentName='" + agentName + '\'' +
            ", logoUrl='" + logoUrl + '\'' +
            ", appid='" + appid + '\'' +
            '}';
    }

    public void setAuthCorpInfo(String authCorpInfo) {
        this.authCorpInfo = authCorpInfo;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public String getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(String isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public String getInviteUrl() {
        return inviteUrl;
    }

    public void setInviteUrl(String inviteUrl) {
        this.inviteUrl = inviteUrl;
    }

    public String getAuthUserInfo() {
        return authUserInfo;
    }

    public void setAuthUserInfo(String authUserInfo) {
        this.authUserInfo = authUserInfo;
    }

    public String getCorpLogoUrl() {
        return corpLogoUrl;
    }

    public void setCorpLogoUrl(String corpLogoUrl) {
        this.corpLogoUrl = corpLogoUrl;
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

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IsvappPermantCode isvappPermantCode = (IsvappPermantCode) o;
        if(isvappPermantCode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, isvappPermantCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);

    }

}
