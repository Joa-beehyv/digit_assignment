package digit.academy.tutorial.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import digit.academy.tutorial.config.RedisConfig;
import digit.academy.tutorial.repository.AdvocateRepository;
import digit.academy.tutorial.service.AdvocateService;
import digit.academy.tutorial.web.models.Advocate;
import digit.academy.tutorial.web.models.AdvocateRequest;
import digit.academy.tutorial.web.models.AdvocateResponse;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.models.RequestInfoWrapper;
import org.egov.common.contract.models.Workflow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* API tests for AdvocateApiController
*/
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AdvocateApiController.class)
public class AdvocateApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvocateService advocateService;

    @MockBean
    private AdvocateRepository advocateRepository;

    @MockBean
    private RedisConfig redisConfig;

    @MockBean
    RedisConnectionFactory redisConnectionFactory;

    @MockBean
    RedisTemplate redisTemplate;

    @InjectMocks
    private AdvocateApiController advocateApiController;

    @Autowired
    ObjectMapper mapper;

    Workflow workflow = Workflow.builder()
            .action("REGISTER")
            .comments("Advocate Registration")
            .documents(List.of(Document.builder()
                    .fileStore("511d7d16-953b-421f-bccf-da865c015a20")
                    .documentType("image/png")
                    .build()))
            .build();

    Advocate advocate =  Advocate.builder()
            .advocateType("PROSECUTOR")
            .barRegistrationNumber("BCI/MH/2023/12345")
            .tenantId("default")
            .individualId("INDV1")
            .workflow(workflow)
            .build();

    AdvocateRequest advocateRequest = AdvocateRequest.builder()
            .advocates(Collections.singletonList(advocate))
            .build();

    @Test
    void testAdvocateV1CreatePost_Success() throws Exception {

        when(advocateService.create(any()))
                .thenReturn(List.of(Advocate.builder().build()));

        mockMvc.perform(post("/advocate/v1/_create")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(advocateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.advocates").exists());

        // Verify advocate service method was called
        verify(advocateService, times(1)).create(any());
    }

    @Test
    void testAdvocateV1CreatePost_Fail() throws Exception {

        when(advocateService.create(any()))
                .thenReturn(List.of(Advocate.builder().build()));

        mockMvc.perform(post("/advocate/v1/_create")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(advocateRequest.withAdvocates(List.of()))))
                .andExpect(status().is4xxClientError());

        // Verify advocate service method is not called
        verify(advocateService, times(0)).create(any());
    }

    @Test
    void testAdvocateV1UpdatePost_Success() throws Exception {

        when(advocateService.update(any()))
                .thenReturn(AdvocateResponse.builder().build());

        mockMvc.perform(post("/advocate/v1/_update")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(advocateRequest
                                .withAdvocates(List.of(
                                        advocate.withApplicationNumber("ADVOC_000025_2025"))))
                        ))
                .andExpect(status().isOk());

        // Verify advocate service method was called
        verify(advocateService, times(1)).update(any());
    }

    @Test
    void testAdvocateV1UpdatePost_Fail() throws Exception {

        when(advocateService.update(any()))
                .thenReturn(AdvocateResponse.builder().build());

        mockMvc.perform(post("/advocate/v1/_update")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(advocateRequest
                                .withAdvocates(List.of(advocate.withTenantId(null))))))
                .andExpect(status().is4xxClientError());

        // Verify advocate service method was called
        verify(advocateService, times(0)).update(any());
    }


    @Test
    void testAdvocateV1SearchPost_Success() throws Exception {

        when(advocateService.search(any(), any()))
                .thenReturn(AdvocateResponse.builder().build());

        mockMvc.perform(post("/advocate/v1/_search?applicationNumber=ADVOC_000025_2025")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(RequestInfoWrapper.builder()
                                .build())))
                .andExpect(status().isOk());

        // Verify advocate service method was called
        verify(advocateService, times(1)).search(any(), any());
    }

    @Test
    void testAdvocateV1SearchPost_Fail() throws Exception {

        when(advocateService.search(any(), any()))
                .thenReturn(AdvocateResponse.builder().build());

        mockMvc.perform(post("/advocate/v1/_search?applicationNumber=ADVOC_000025_2025")
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());

        // Verify advocate service method was called
        verify(advocateService, times(0)).search(any(), any());
    }
}
