package group.demo.restControllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerHelper.API_BASE_URL_v1)
public class HealthController {
    // server health check
    @RequestMapping("/health")
    public String healthCheck() {
        return "ok";
    }
}
