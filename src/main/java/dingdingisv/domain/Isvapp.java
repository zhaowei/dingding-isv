package dingdingisv.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Isvapp.
 */
@Entity
@Table(name = "isvapp")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "isvapp")
public class Isvapp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "isv_key")
    private String isvKey;

    @Column(name = "suite_token")
    private String suiteToken;

    @Column(name = "token")
    private String token;

    @Column(name = "suite_encoding_aes_key")
    private String suiteEncodingAesKey;

    @Column(name = "suite_key")
    private String suiteKey;

    @Column(name = "corp_id")
    private String corpId;

    @Column(name = "suite_secret")
    private String suiteSecret;

    @Column(name = "suite_ticket")
    private String suiteTicket;

    @Column(name = "permanent_code")
    private String permanentCode;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @Column(name = "update_time")
    private ZonedDateTime updateTime;

    @Column(name = "open_id")
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
        Isvapp isvapp = (Isvapp) o;
        if(isvapp.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, isvapp.id);
    }

    @Override
    public String toString() {
        return "Isvapp{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", isvKey='" + isvKey + '\'' +
            ", suiteToken='" + suiteToken + '\'' +
            ", token='" + token + '\'' +
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

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
