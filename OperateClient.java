package com.drytadpole.codebase;


import com.example.migration.dto.ProcessDefinitionSearchResponse;
import com.example.migration.dto.ProcessInstanceSearchResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class OperateClient {

    private final WebClient webClient;

    public OperateClient(WebClient.Builder builder,
                         OperateProperties props) {
        this.webClient = builder
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.getToken())
                .build();
    }

    public ProcessDefinitionSearchResponse searchLatestDefinition(String bpmnProcessId) {
        Map<String, Object> body = Map.of(
                "filter", Map.of("bpmnProcessId", bpmnProcessId),
                "size", 1,
                "sort", new Object[]{ Map.of("field", "version", "order", "DESC") }
        );
        // POST /v1/process-definitions/search returns the latest first by sorting DESC.
        // This resolves bpmnProcessId -> process definition key. [[Forum mapping](https://forum.camunda.io//t/64486)]
        return webClient.post()
                .uri("/v1/process-definitions/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ProcessDefinitionSearchResponse.class)
                .block();
    }

    public ProcessInstanceSearchResponse searchActiveInstancesForProcess(
            String bpmnProcessId, int pageSize, int pageFrom) {

        Map<String, Object> filter = Map.of(
                "bpmnProcessId", bpmnProcessId,
                "state", "ACTIVE"
        );
        Map<String, Object> body = Map.of(
                "filter", filter,
                "size", pageSize,
                "from", pageFrom,
                "sort", new Object[]{ Map.of("field", "processVersion", "order", "ASC") }
        );

        // POST /v1/process-instances/search with filters. Pattern per Operate API "search".
        return webClient.post()
                .uri("/v1/process-instances/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ProcessInstanceSearchResponse.class)
                .block();
    }

    public java.util.List<String> fetchActiveElementIds(long processInstanceKey) {
        // You may gather all active element IDs via flownode-instances search:
        // POST /v1/flownode-instances/search with filter { processInstanceKey, state: "ACTIVE" }
        Map<String, Object> body = Map.of(
                "filter", Map.of("processInstanceKey", processInstanceKey, "state", "ACTIVE"),
                "size", 1000
        );
        record FlownodeSearchResponse(java.util.List<Item> items) { record Item(String flowNodeId) {} }

        FlownodeSearchResponse response = webClient.post()
                .uri("/v1/flownode-instances/search")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(FlownodeSearchResponse.class)
                .block();

        // Extract unique flowNodeIds to satisfy "all active elements require a mapping".
        return response == null ? java.util.List.of() :
                response.items().stream().map(FlownodeSearchResponse.Item::flowNodeId).distinct().toList();
        // Searching flownode-instances is documented. [[Search flownodes](https://docs.camunda.io/docs/apis-tools/operate-api/specifications/search-4/)]
    }
}