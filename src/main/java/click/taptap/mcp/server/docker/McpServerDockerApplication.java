package click.taptap.mcp.server.docker;

import click.taptap.mcp.server.docker.service.DockerService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerDockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerDockerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider dockerTools(DockerService dockerService) {
        return MethodToolCallbackProvider.builder().toolObjects(dockerService).build();
    }

}
