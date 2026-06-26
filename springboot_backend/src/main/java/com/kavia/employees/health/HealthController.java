package com.kavia.employees.health;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  @GetMapping("${HEALTHCHECK_PATH:/healthz}")
  public Map<String, String> healthz() {
    return Map.of("status", "ok");
  }
}
