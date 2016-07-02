package dingdingisv.web.rest;

import dingdingisv.DingdingisvApp;
import dingdingisv.domain.IsvappPermantCode;
import dingdingisv.repository.IsvappPermantCodeRepository;
import dingdingisv.service.IsvappPermantCodeService;
import dingdingisv.repository.search.IsvappPermantCodeSearchRepository;
import dingdingisv.web.rest.dto.IsvappPermantCodeDTO;
import dingdingisv.web.rest.mapper.IsvappPermantCodeMapper;

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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the IsvappPermantCodeResource REST controller.
 *
 * @see IsvappPermantCodeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DingdingisvApp.class)
@WebAppConfiguration
@IntegrationTest
public class IsvappPermantCodeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_ISV_FID = 1;
    private static final Integer UPDATED_ISV_FID = 2;
    private static final String DEFAULT_CORP_ID = "AAAAA";
    private static final String UPDATED_CORP_ID = "BBBBB";
    private static final String DEFAULT_PERMANT_CODE = "AAAAA";
    private static final String UPDATED_PERMANT_CODE = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATE_TIME);

    private static final ZonedDateTime DEFAULT_UPDATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATE_TIME_STR = dateTimeFormatter.format(DEFAULT_UPDATE_TIME);

    @Inject
    private IsvappPermantCodeRepository isvappPermantCodeRepository;

    @Inject
    private IsvappPermantCodeMapper isvappPermantCodeMapper;

    @Inject
    private IsvappPermantCodeService isvappPermantCodeService;

    @Inject
    private IsvappPermantCodeSearchRepository isvappPermantCodeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIsvappPermantCodeMockMvc;

    private IsvappPermantCode isvappPermantCode;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IsvappPermantCodeResource isvappPermantCodeResource = new IsvappPermantCodeResource();
        ReflectionTestUtils.setField(isvappPermantCodeResource, "isvappPermantCodeService", isvappPermantCodeService);
        ReflectionTestUtils.setField(isvappPermantCodeResource, "isvappPermantCodeMapper", isvappPermantCodeMapper);
        this.restIsvappPermantCodeMockMvc = MockMvcBuilders.standaloneSetup(isvappPermantCodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        isvappPermantCodeSearchRepository.deleteAll();
        isvappPermantCode = new IsvappPermantCode();
        isvappPermantCode.setIsvFid(DEFAULT_ISV_FID);
        isvappPermantCode.setCorpId(DEFAULT_CORP_ID);
        isvappPermantCode.setPermantCode(DEFAULT_PERMANT_CODE);
        isvappPermantCode.setCreateTime(DEFAULT_CREATE_TIME);
        isvappPermantCode.setUpdateTime(DEFAULT_UPDATE_TIME);
    }

    @Test
    @Transactional
    public void createIsvappPermantCode() throws Exception {
        int databaseSizeBeforeCreate = isvappPermantCodeRepository.findAll().size();

        // Create the IsvappPermantCode
        IsvappPermantCodeDTO isvappPermantCodeDTO = isvappPermantCodeMapper.isvappPermantCodeToIsvappPermantCodeDTO(isvappPermantCode);

        restIsvappPermantCodeMockMvc.perform(post("/api/isvapp-permant-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(isvappPermantCodeDTO)))
                .andExpect(status().isCreated());

        // Validate the IsvappPermantCode in the database
        List<IsvappPermantCode> isvappPermantCodes = isvappPermantCodeRepository.findAll();
        assertThat(isvappPermantCodes).hasSize(databaseSizeBeforeCreate + 1);
        IsvappPermantCode testIsvappPermantCode = isvappPermantCodes.get(isvappPermantCodes.size() - 1);
        assertThat(testIsvappPermantCode.getIsvFid()).isEqualTo(DEFAULT_ISV_FID);
        assertThat(testIsvappPermantCode.getCorpId()).isEqualTo(DEFAULT_CORP_ID);
        assertThat(testIsvappPermantCode.getPermantCode()).isEqualTo(DEFAULT_PERMANT_CODE);
        assertThat(testIsvappPermantCode.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testIsvappPermantCode.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);

        // Validate the IsvappPermantCode in ElasticSearch
        IsvappPermantCode isvappPermantCodeEs = isvappPermantCodeSearchRepository.findOne(testIsvappPermantCode.getId());
        assertThat(isvappPermantCodeEs).isEqualToComparingFieldByField(testIsvappPermantCode);
    }

    @Test
    @Transactional
    public void getAllIsvappPermantCodes() throws Exception {
        // Initialize the database
        isvappPermantCodeRepository.saveAndFlush(isvappPermantCode);

        // Get all the isvappPermantCodes
        restIsvappPermantCodeMockMvc.perform(get("/api/isvapp-permant-codes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(isvappPermantCode.getId().intValue())))
                .andExpect(jsonPath("$.[*].isv_fid").value(hasItem(DEFAULT_ISV_FID)))
                .andExpect(jsonPath("$.[*].corpId").value(hasItem(DEFAULT_CORP_ID.toString())))
                .andExpect(jsonPath("$.[*].permantCode").value(hasItem(DEFAULT_PERMANT_CODE.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME_STR)));
    }

    @Test
    @Transactional
    public void getIsvappPermantCode() throws Exception {
        // Initialize the database
        isvappPermantCodeRepository.saveAndFlush(isvappPermantCode);

        // Get the isvappPermantCode
        restIsvappPermantCodeMockMvc.perform(get("/api/isvapp-permant-codes/{id}", isvappPermantCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(isvappPermantCode.getId().intValue()))
            .andExpect(jsonPath("$.isv_fid").value(DEFAULT_ISV_FID))
            .andExpect(jsonPath("$.corpId").value(DEFAULT_CORP_ID.toString()))
            .andExpect(jsonPath("$.permantCode").value(DEFAULT_PERMANT_CODE.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME_STR))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingIsvappPermantCode() throws Exception {
        // Get the isvappPermantCode
        restIsvappPermantCodeMockMvc.perform(get("/api/isvapp-permant-codes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIsvappPermantCode() throws Exception {
        // Initialize the database
        isvappPermantCodeRepository.saveAndFlush(isvappPermantCode);
        isvappPermantCodeSearchRepository.save(isvappPermantCode);
        int databaseSizeBeforeUpdate = isvappPermantCodeRepository.findAll().size();

        // Update the isvappPermantCode
        IsvappPermantCode updatedIsvappPermantCode = new IsvappPermantCode();
        updatedIsvappPermantCode.setId(isvappPermantCode.getId());
        updatedIsvappPermantCode.setIsvFid(UPDATED_ISV_FID);
        updatedIsvappPermantCode.setCorpId(UPDATED_CORP_ID);
        updatedIsvappPermantCode.setPermantCode(UPDATED_PERMANT_CODE);
        updatedIsvappPermantCode.setCreateTime(UPDATED_CREATE_TIME);
        updatedIsvappPermantCode.setUpdateTime(UPDATED_UPDATE_TIME);
        IsvappPermantCodeDTO isvappPermantCodeDTO = isvappPermantCodeMapper.isvappPermantCodeToIsvappPermantCodeDTO(updatedIsvappPermantCode);

        restIsvappPermantCodeMockMvc.perform(put("/api/isvapp-permant-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(isvappPermantCodeDTO)))
                .andExpect(status().isOk());

        // Validate the IsvappPermantCode in the database
        List<IsvappPermantCode> isvappPermantCodes = isvappPermantCodeRepository.findAll();
        assertThat(isvappPermantCodes).hasSize(databaseSizeBeforeUpdate);
        IsvappPermantCode testIsvappPermantCode = isvappPermantCodes.get(isvappPermantCodes.size() - 1);
        assertThat(testIsvappPermantCode.getIsvFid()).isEqualTo(UPDATED_ISV_FID);
        assertThat(testIsvappPermantCode.getCorpId()).isEqualTo(UPDATED_CORP_ID);
        assertThat(testIsvappPermantCode.getPermantCode()).isEqualTo(UPDATED_PERMANT_CODE);
        assertThat(testIsvappPermantCode.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testIsvappPermantCode.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);

        // Validate the IsvappPermantCode in ElasticSearch
        IsvappPermantCode isvappPermantCodeEs = isvappPermantCodeSearchRepository.findOne(testIsvappPermantCode.getId());
        assertThat(isvappPermantCodeEs).isEqualToComparingFieldByField(testIsvappPermantCode);
    }

    @Test
    @Transactional
    public void deleteIsvappPermantCode() throws Exception {
        // Initialize the database
        isvappPermantCodeRepository.saveAndFlush(isvappPermantCode);
        isvappPermantCodeSearchRepository.save(isvappPermantCode);
        int databaseSizeBeforeDelete = isvappPermantCodeRepository.findAll().size();

        // Get the isvappPermantCode
        restIsvappPermantCodeMockMvc.perform(delete("/api/isvapp-permant-codes/{id}", isvappPermantCode.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean isvappPermantCodeExistsInEs = isvappPermantCodeSearchRepository.exists(isvappPermantCode.getId());
        assertThat(isvappPermantCodeExistsInEs).isFalse();

        // Validate the database is empty
        List<IsvappPermantCode> isvappPermantCodes = isvappPermantCodeRepository.findAll();
        assertThat(isvappPermantCodes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIsvappPermantCode() throws Exception {
        // Initialize the database
        isvappPermantCodeRepository.saveAndFlush(isvappPermantCode);
        isvappPermantCodeSearchRepository.save(isvappPermantCode);

        // Search the isvappPermantCode
        restIsvappPermantCodeMockMvc.perform(get("/api/_search/isvapp-permant-codes?query=id:" + isvappPermantCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(isvappPermantCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].isv_fid").value(hasItem(DEFAULT_ISV_FID)))
            .andExpect(jsonPath("$.[*].corpId").value(hasItem(DEFAULT_CORP_ID.toString())))
            .andExpect(jsonPath("$.[*].permantCode").value(hasItem(DEFAULT_PERMANT_CODE.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME_STR)))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME_STR)));
    }
}
