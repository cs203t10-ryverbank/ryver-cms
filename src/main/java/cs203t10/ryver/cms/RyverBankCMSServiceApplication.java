package cs203t10.ryver.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class RyverBankCMSServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RyverBankCMSServiceApplication.class, args);
	}

    @GetMapping("/")
    @Operation(summary = "Check the service name")
    public String getRoot() {
        return "ryver-cms service";
    }

}

