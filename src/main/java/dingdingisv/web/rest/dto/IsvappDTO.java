package dingdingisv.web.rest.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A DTO for the Isvapp entity.
 */
public class IsvappDTO implements Serializable {

    private Long id;

    private String userName;

    private String isvKey;

    private String token;
    private String suiteToken;

    private String suiteEncodingAesKey;

    private String suiteKey;

    private String corpId;

    private String suiteSecret;

    private String suiteTicket;

    private String permanentCode;

    private ZonedDateTime createTime;

    private ZonedDateTime updateTime;

    private String openId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getIsvKey() {
        return isvKey;
    }

    public void setIsvKey(String isvKey) {
        this.isvKey = isvKey;
    }
    public String getSuiteToken() {
        return suiteToken;
    }

    public void setSuiteToken(String suiteToken) {
        this.suiteToken = suiteToken;
    }
    public String getSuiteEncodingAesKey() {
        return suiteEncodingAesKey;
    }

    public void setSuiteEncodingAesKey(String suiteEncodingAesKey) {
        this.suiteEncodingAesKey = suiteEncodingAesKey;
    }
    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getSuiteSecret() {
        return suiteSecret;
    }

    public void setSuiteSecret(String suiteSecret) {
        this.suiteSecret = suiteSecret;
    }
    public String getSuiteTicket() {
        return suiteTicket;
    }

    public void setSuiteTicket(String suiteTicket) {
        this.suiteTicket = suiteTicket;
    }
    public String getPermanentCode() {
        return permanentCode;
    }

    public void setPermanentCode(String permanentCode) {
        this.permanentCode = permanentCode;
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
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IsvappDTO isvappDTO = (IsvappDTO) o;

        if ( ! Objects.equals(id, isvappDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IsvappDTO{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", isvKey='" + isvKey + '\'' +
            ", token='" + token + '\'' +
            ", suiteToken='" + suiteToken + '\'' +
            ", suiteEncodingAesKey='" + suiteEncodingAesKey + '\'' +
            ", suiteKey='" + suiteKey + '\'' +
            ", corpId='" + corpId + '\'' +
            ", suiteSecret='" + suiteSecret + '\'' +
            ", suiteTicket='" + suiteTicket + '\'' +
            ", permanentCode='" + permanentCode + '\'' +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", openId='" + openId + '\'' +
            '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
