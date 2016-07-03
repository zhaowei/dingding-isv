package dingdingisv.web.rest.dto;

/**
 * Created by zhaowei on 16/7/3.
 */
public class IsvappUpdateDTO {

    private String token;
    private String suiteEncodingAesKey;
    private String suiteKey;
    private String suiteSecret;
    private String suiteTicket;

    public IsvappUpdateDTO() {}

    @Override
    public String toString() {
        return "IsvappUpdateDTO{" +
            "token='" + token + '\'' +
            ", suiteEncodingAesKey='" + suiteEncodingAesKey + '\'' +
            ", suiteKey='" + suiteKey + '\'' +
            ", suiteSecret='" + suiteSecret + '\'' +
            ", suiteTicket='" + suiteTicket + '\'' +
            '}';
    }

    public IsvappUpdateDTO(String token, String suiteEncodingAesKey, String suiteKey, String suiteSecret, String suiteTicket) {
        this.token = token;
        this.suiteEncodingAesKey = suiteEncodingAesKey;
        this.suiteKey = suiteKey;
        this.suiteSecret = suiteSecret;
        this.suiteTicket = suiteTicket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
