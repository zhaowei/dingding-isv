package dingdingisv.web.rest;

import dingdingisv.DingdingisvApp;
import dingdingisv.domain.Isvapp;
import dingdingisv.repository.IsvappRepository;
import dingdingisv.service.IsvappService;
import dingdingisv.repository.search.IsvappSearchRepository;
import dingdingisv.web.rest.dto.IsvappDTO;
import dingdingisv.web.rest.mapper.IsvappMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the IsvappResource REST controller.
 *
 * @see IsvappResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DingdingisvApp.class)
@WebAppConfiguration
@IntegrationTest
public class IsvappResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_USER_NAME = "AAAAA";
    private static final String UPDATED_USER_NAME = "BBBBB";
    private static final String DEFAULT_ISV_KEY = "AAAAA";
    private static final String UPDATED_ISV_KEY = "BBBBB";
    private static final String DEFAULT_SUITE_TOKEN = "AAAAA";
    private static final String UPDATED_SUITE_TOKEN = "BBBBB";
    private static final String DEFAULT_SUITE_ENCODING_AES_KEY = "AAAAA";
    private static final String UPDATED_SUITE_ENCODING_AES_KEY = "BBBBB";
    private static final String DEFAULT_SUITE_KEY = "AAAAA";
    private static final String UPDATED_SUITE_KEY = "BBBBB";
    private static final String DEFAULT_CORP_ID = "AAAAA";
    private static final String UPDATED_CORP_ID = "BBBBB";
    private static final String DEFAULT_SUITE_SECRET = "AAAAA";
    private static final String UPDATED_SUITE_SECRET = "BBBBB";
    private static final String DEFAULT_SUITE_TICKET = "AAAAA";
    private static final String UPDATED_SUITE_TICKET = "BBBBB";
    private static final String DEFAULT_PERMANENT_CODE = "AAAAA";
    private static final String UPDATED_PERMANENT_CODE = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATE_TIME);

    private static final ZonedDateTime DEFAULT_UPDATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATE_TIME_STR = dateTimeFormatter.format(DEFAULT_UPDATE_TIME);

    private static final String DEFAULT_OPEN_ID = "AAAAA";
    private static final String UPDATED_OPEN_ID = "BBBBB";

    @Inject
    private IsvappRepository isvappRepository;

    @Inject
    private IsvappMapper isvappMapper;

    @Inject
    private IsvappService isvappService;

    @Inject
    private IsvappSearchRepository isvappSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIsvappMockMvc;

    private Isvapp isvapp;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IsvappResource isvappResource = new IsvappResource();
        ReflectionTestUtils.setField(isvappResource, "isvappService", isvappService);
        ReflectionTestUtils.setField(isvappResource, "isvappMapper", isvappMapper);
        this.restIsvappMockMvc = MockMvcBuilders.standaloneSetup(isvappResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        isvappSearchRepository.deleteAll();
        isvapp = new Isvapp();
        isvapp.setUserName(DEFAULT_USER_NAME);
        isvapp.setIsvKey(DEFAULT_ISV_KEY);
        isvapp.setSuiteToken(DEFAULT_SUITE_TOKEN);
        isvapp.setSuiteEncodingAesKey(DEFAULT_SUITE_ENCODING_AES_KEY);
        isvapp.setSuiteKey(DEFAULT_SUITE_KEY);
        isvapp.setCorpId(DEFAULT_CORP_ID);
        isvapp.setSuiteSecret(DEFAULT_SUITE_SECRET);
        isvapp.setSuiteTicket(DEFAULT_SUITE_TICKET);
        isvapp.setPermanentCode(DEFAULT_PERMANENT_CODE);
        isvapp.setCreateTime(DEFAULT_CREATE_TIME);
        isvapp.setUpdateTime(DEFAULT_UPDATE_TIME);
        isvapp.setOpenId(DEFAULT_OPEN_ID);
    }

    @Test
    @Transactional
    public void createIsvapp() throws Exception {
        int databaseSizeBeforeCreate = isvappRepository.findAll().size();

        // Create the Isvapp
        IsvappDTO isvappDTO = isvappMapper.isvappToIsvappDTO(isvapp);

        restIsvappMockMvc.perform(post("/api/isvapps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(isvappDTO)))
                .andExpect(status().isCreated());

        // Validate the Isvapp in the database
        List<Isvapp> isvapps = isvappRepository.findAll();
        assertThat(isvapps).hasSize(databaseSizeBeforeCreate + 1);
        Isvapp testIsvapp = isvapps.get(isvapps.size() - 1);
        assertThat(testIsvapp.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testIsvapp.getIsvKey()).isEqualTo(DEFAULT_ISV_KEY);
        assertThat(testIsvapp.getSuiteToken()).isEqualTo(DEFAULT_SUITE_TOKEN);
        assertThat(testIsvapp.getSuiteEncodingAesKey()).isEqualTo(DEFAULT_SUITE_ENCODING_AES_KEY);
        assertThat(testIsvapp.getSuiteKey()).isEqualTo(DEFAULT_SUITE_KEY);
        assertThat(testIsvapp.getCorpId()).isEqualTo(DEFAULT_CORP_ID);
        assertThat(testIsvapp.getSuiteSecret()).isEqualTo(DEFAULT_SUITE_SECRET);
        assertThat(testIsvapp.getSuiteTicket()).isEqualTo(DEFAULT_SUITE_TICKET);
        assertThat(testIsvapp.getPermanentCode()).isEqualTo(DEFAULT_PERMANENT_CODE);
        assertThat(testIsvapp.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME_STR);
        assertThat(testIsvapp.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME_STR);
        assertThat(testIsvapp.getOpenId()).isEqualTo(DEFAULT_OPEN_ID);

        // Validate the Isvapp in ElasticSearch
        Isvapp isvappEs = isvappSearchRepository.findOne(testIsvapp.getId());
        assertThat(isvappEs).isEqualToComparingFieldByField(testIsvapp);
    }

    @Test
    @Transactional
    public void getAllIsvapps() throws Exception {
        // Initialize the database
        isvappRepository.saveAndFlush(isvapp);

        // Get all the isvapps
        restIsvappMockMvc.perform(get("/api/isvapps?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(isvapp.getId().intValue())))
                .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
                .andExpect(jsonPath("$.[*].isvKey").value(hasItem(DEFAULT_ISV_KEY.toString())))
                .andExpect(jsonPath("$.[*].suiteToken").value(hasItem(DEFAULT_SUITE_TOKEN.toString())))
                .andExpect(jsonPath("$.[*].suiteEncodingAesKey").value(hasItem(DEFAULT_SUITE_ENCODING_AES_KEY.toString())))
                .andExpect(jsonPath("$.[*].suiteKey").value(hasItem(DEFAULT_SUITE_KEY.toString())))
                .andExpect(jsonPath("$.[*].corpId").value(hasItem(DEFAULT_CORP_ID.toString())))
                .andExpect(jsonPath("$.[*].suiteSecret").value(hasItem(DEFAULT_SUITE_SECRET.toString())))
                .andExpect(jsonPath("$.[*].suiteTicket").value(hasItem(DEFAULT_SUITE_TICKET.toString())))
                .andExpect(jsonPath("$.[*].permanentCode").value(hasItem(DEFAULT_PERMANENT_CODE.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
                .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
                .andExpect(jsonPath("$.[*].openId").value(hasItem(DEFAULT_OPEN_ID.toString())));
    }

    @Test
    @Transactional
    public void getIsvapp() throws Exception {
        // Initialize the database
        isvappRepository.saveAndFlush(isvapp);

        // Get the isvapp
        restIsvappMockMvc.perform(get("/api/isvapps/{id}", isvapp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(isvapp.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.isvKey").value(DEFAULT_ISV_KEY.toString()))
            .andExpect(jsonPath("$.suiteToken").value(DEFAULT_SUITE_TOKEN.toString()))
            .andExpect(jsonPath("$.suiteEncodingAesKey").value(DEFAULT_SUITE_ENCODING_AES_KEY.toString()))
            .andExpect(jsonPath("$.suiteKey").value(DEFAULT_SUITE_KEY.toString()))
            .andExpect(jsonPath("$.corpId").value(DEFAULT_CORP_ID.toString()))
            .andExpect(jsonPath("$.suiteSecret").value(DEFAULT_SUITE_SECRET.toString()))
            .andExpect(jsonPath("$.suiteTicket").value(DEFAULT_SUITE_TICKET.toString()))
            .andExpect(jsonPath("$.permanentCode").value(DEFAULT_PERMANENT_CODE.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.toString()))
            .andExpect(jsonPath("$.openId").value(DEFAULT_OPEN_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIsvapp() throws Exception {
        // Get the isvapp
        restIsvappMockMvc.perform(get("/api/isvapps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIsvapp() throws Exception {
        // Initialize the database
        isvappRepository.saveAndFlush(isvapp);
        isvappSearchRepository.save(isvapp);
        int databaseSizeBeforeUpdate = isvappRepository.findAll().size();

        // Update the isvapp
        Isvapp updatedIsvapp = new Isvapp();
        updatedIsvapp.setId(isvapp.getId());
        updatedIsvapp.setUserName(UPDATED_USER_NAME);
        updatedIsvapp.setIsvKey(UPDATED_ISV_KEY);
        updatedIsvapp.setSuiteToken(UPDATED_SUITE_TOKEN);
        updatedIsvapp.setSuiteEncodingAesKey(UPDATED_SUITE_ENCODING_AES_KEY);
        updatedIsvapp.setSuiteKey(UPDATED_SUITE_KEY);
        updatedIsvapp.setCorpId(UPDATED_CORP_ID);
        updatedIsvapp.setSuiteSecret(UPDATED_SUITE_SECRET);
        updatedIsvapp.setSuiteTicket(UPDATED_SUITE_TICKET);
        updatedIsvapp.setPermanentCode(UPDATED_PERMANENT_CODE);
        updatedIsvapp.setCreateTime(UPDATED_CREATE_TIME);
        updatedIsvapp.setUpdateTime(UPDATED_UPDATE_TIME);
        updatedIsvapp.setOpenId(UPDATED_OPEN_ID);
        IsvappDTO isvappDTO = isvappMapper.isvappToIsvappDTO(updatedIsvapp);

        restIsvappMockMvc.perform(put("/api/isvapps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(isvappDTO)))
                .andExpect(status().isOk());

        // Validate the Isvapp in the database
        List<Isvapp> isvapps = isvappRepository.findAll();
        assertThat(isvapps).hasSize(databaseSizeBeforeUpdate);
        Isvapp testIsvapp = isvapps.get(isvapps.size() - 1);
        assertThat(testIsvapp.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testIsvapp.getIsvKey()).isEqualTo(UPDATED_ISV_KEY);
        assertThat(testIsvapp.getSuiteToken()).isEqualTo(UPDATED_SUITE_TOKEN);
        assertThat(testIsvapp.getSuiteEncodingAesKey()).isEqualTo(UPDATED_SUITE_ENCODING_AES_KEY);
        assertThat(testIsvapp.getSuiteKey()).isEqualTo(UPDATED_SUITE_KEY);
        assertThat(testIsvapp.getCorpId()).isEqualTo(UPDATED_CORP_ID);
        assertThat(testIsvapp.getSuiteSecret()).isEqualTo(UPDATED_SUITE_SECRET);
        assertThat(testIsvapp.getSuiteTicket()).isEqualTo(UPDATED_SUITE_TICKET);
        assertThat(testIsvapp.getPermanentCode()).isEqualTo(UPDATED_PERMANENT_CODE);
        assertThat(testIsvapp.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIsvapp.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testIsvapp.getOpenId()).isEqualTo(UPDATED_OPEN_ID);

        // Validate the Isvapp in ElasticSearch
        Isvapp isvappEs = isvappSearchRepository.findOne(testIsvapp.getId());
        assertThat(isvappEs).isEqualToComparingFieldByField(testIsvapp);
    }

    @Test
    @Transactional
    public void deleteIsvapp() throws Exception {
        // Initialize the database
        isvappRepository.saveAndFlush(isvapp);
        isvappSearchRepository.save(isvapp);
        int databaseSizeBeforeDelete = isvappRepository.findAll().size();

        // Get the isvapp
        restIsvappMockMvc.perform(delete("/api/isvapps/{id}", isvapp.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean isvappExistsInEs = isvappSearchRepository.exists(isvapp.getId());
        assertThat(isvappExistsInEs).isFalse();

        // Validate the database is empty
        List<Isvapp> isvapps = isvappRepository.findAll();
        assertThat(isvapps).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIsvapp() throws Exception {
        // Initialize the database
        isvappRepository.saveAndFlush(isvapp);
        isvappSearchRepository.save(isvapp);

        // Search the isvapp
        restIsvappMockMvc.perform(get("/api/_search/isvapps?query=id:" + isvapp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isvapp.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].isvKey").value(hasItem(DEFAULT_ISV_KEY.toString())))
            .andExpect(jsonPath("$.[*].suiteToken").value(hasItem(DEFAULT_SUITE_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].suiteEncodingAesKey").value(hasItem(DEFAULT_SUITE_ENCODING_AES_KEY.toString())))
            .andExpect(jsonPath("$.[*].suiteKey").value(hasItem(DEFAULT_SUITE_KEY.toString())))
            .andExpect(jsonPath("$.[*].corpId").value(hasItem(DEFAULT_CORP_ID.toString())))
            .andExpect(jsonPath("$.[*].suiteSecret").value(hasItem(DEFAULT_SUITE_SECRET.toString())))
            .andExpect(jsonPath("$.[*].suiteTicket").value(hasItem(DEFAULT_SUITE_TICKET.toString())))
            .andExpect(jsonPath("$.[*].permanentCode").value(hasItem(DEFAULT_PERMANENT_CODE.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].openId").value(hasItem(DEFAULT_OPEN_ID.toString())));
    }
}
