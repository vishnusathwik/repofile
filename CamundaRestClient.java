package com.drytadpole.codebase;

package com.example.migration.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class CamundaRestClient {

    private final WebClient webClient;

    public CamundaRestClient(WebClient.Builder builder, RestProperties props) {
        this.webClient = builder
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getToken())
                .build();
    }

    public void migrateProcessInstance(long processInstanceKey,
                                       long targetProcessDefinitionKey,
                                       List<Map<String, String>> mappingInstructions) {

        Map<String, Object> body = Map.of(
                "migrationPlan", Map.of(
                        "targetProcessDefinitionKey", targetProcessDefinitionKey,
                        "mappingInstructions", mappingInstructions
                )
        );

        // POST /process-instances/:processInstanceKey/migration. [[Migrate REST](https://docs.camunda.io/docs/apis-tools/camunda-api-rest/specifications/migrate-process-instance/)]
        webClient.post()
                .uri("/process-instances/{key}/migration", processInstanceKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
