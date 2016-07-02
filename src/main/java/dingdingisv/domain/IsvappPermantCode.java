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

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @Column(name = "update_time")
    private ZonedDateTime updateTime;

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

    @Override
    public String toString() {
        return "IsvappPermantCode{" +
            "id=" + id +
            ", isvFid=" + isvFid +
            ", corpId='" + corpId + '\'' +
            ", permantCode='" + permantCode + '\'' +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }
}
