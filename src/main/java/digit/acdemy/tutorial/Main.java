package digit.acdemy.tutorial;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.acdemy.tutorial.util.MdmsUtil;
import digit.acdemy.tutorial.web.models.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

@Configuration
@Import({TracerConfiguration.class})
@SpringBootApplication
@ComponentScan(basePackages = {"digit", "digit.web.controllers", "digit.config"})
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private  UserType userType;

    @Autowired
    private  MdmsUtil mdmsUtil;

    @Bean
    public UserType usertype() throws JsonProcessingException {
        // Example JSON string
        JsonNode mdms = mdmsUtil.fetchMdmsData(
                RequestInfo.builder().build(),
                "joa",
                "masters.UserType",
                Set.of());
        try {
            JsonNode userTypesNode = mdms.get("UserType");
            if (userTypesNode != null && userTypesNode.isArray()) {
                for (JsonNode userTypeNode : userTypesNode) {
                    String code = userTypeNode.get("code").asText();
                    userType.addValue(code);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DynamicEnum", e);
        }
        //log.info("user type loaded: {}", userType.getValues());
        return userType;
    }
}
