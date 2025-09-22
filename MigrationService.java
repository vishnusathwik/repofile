package com.drytadpole.codebase;


import com.example.migration.client.CamundaRestClient;
import com.example.migration.client.OperateClient;
import com.example.migration.dto.ProcessDefinitionSearchResponse;
import com.example.migration.dto.ProcessInstanceSearchResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MigrationService {

    private final OperateClient operate;
    private final CamundaRestClient camundaRest;

    public MigrationService(OperateClient operate, CamundaRestClient camundaRest) {
        this.operate = operate;
        this.camundaRest = camundaRest;
    }

    public int migrateAllToLatest(String bpmnProcessId) {
        // 1) Resolve latest process definition key
        var defRes = operate.searchLatestDefinition(bpmnProcessId);
        if (defRes == null || defRes.items().isEmpty()) {
            throw new IllegalStateException("No process definition found for bpmnProcessId=" + bpmnProcessId);
        }
        long latestKey = defRes.items().get(0).key();
        int latestVersion = defRes.items().get(0).version();

        // 2) Page through active instances, filter those not on latest version
        int pageSize = 200;
        int from = 0;
        int migratedCount = 0;

        while (true) {
            ProcessInstanceSearchResponse page = operate.searchActiveInstancesForProcess(bpmnProcessId, pageSize, from);
            if (page == null || page.items() == null || page.items().isEmpty()) break;

            for (var inst : page.items()) {
                if (inst.processVersion() == latestVersion) continue; // already latest

                // 3) Collect active element IDs
                List<String> activeElementIds = operate.fetchActiveElementIds(inst.key());
                if (activeElementIds.isEmpty()) continue; // nothing active; skip (migration requires active)

                // 4) Build same-ID mapping instructions
                List<Map<String, String>> mappings = new ArrayList<>();
                for (String id : new HashSet<>(activeElementIds)) {
                    mappings.add(Map.of("sourceElementId", id, "targetElementId", id));
                }

                // 5) Call migrate REST
                try {
                    camundaRest.migrateProcessInstance(inst.key(), latestKey, mappings);
                    migratedCount++;
                } catch (Exception e) {
                    // Handle/record rejection or transient failures.
                    // Rejections are expected if limitations are violated. [[Limitations](https://docs.camunda.io/docs/components/concepts/process-instance-migration/#limitations)]
                    System.err.println("Migration failed for instance " + inst.key() + ": " + e.getMessage());
                }
            }

            if (page.items().size() < pageSize) break;
            from += pageSize;
        }

        return migratedCount;
    }
}