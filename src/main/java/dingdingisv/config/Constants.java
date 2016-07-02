package dingdingisv.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    // Spring profile for development and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";
    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_NO_SWAGGER = "no-swagger";
    // Spring profile used to disable running liquibase
    public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

    public static final String SYSTEM_ACCOUNT = "system";


    public static final String OAPI_HOST = "https://oapi.dingtalk.com";
    public static final String OA_BACKGROUND_URL = "";
    public static final String CORP_ID = "ding8ac44a22eb91c942";

    public static final String SECRET = "";
    public static final String SSO_Secret = "MSpBqNo15e14QksUYw2AKrAOfF2C2oMzNQBPdu0rs-6PvU469_yBc_MNu27h2MbO";


    public static String suiteTicket;
    public static String authCode;
    public static String suiteToken;

    public static final String CREATE_SUITE_KEY = "suitetnc8ojj9819azjzl";
    public static final String SUITE_KEY = "suitetnc8ojj9819azjzl";
    public static final String SUITE_SECRET = "CfGd7WEbhGLAaN3fn58M9fh2r4fAqBrCWmyWZXaxXu51X9mQ8Nqs7ezhCip7gk3t";
    public static final String TOKEN = "badingcom";
    public static final String ENCODING_AES_KEY = "j8clxcmtf9qmks62tytgbm7fo5w99kqrnxnr3qajfaq";



    private Constants() {
    }
}
