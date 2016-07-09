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

    @Column(name = "auth_user_info")
    private String authUserInfo;

    @Column(name = "auth_info")
    private String authInfo;

    @Column(name = "agent")
    private String agent;


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
            ", authUserInfo='" + authUserInfo + '\'' +
            ", authInfo='" + authInfo + '\'' +
            ", agent='" + agent + '\'' +
            '}';
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
