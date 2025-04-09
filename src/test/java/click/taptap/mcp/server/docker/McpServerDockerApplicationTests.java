package click.taptap.mcp.server.docker;

import click.taptap.mcp.server.docker.service.DockerService;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class McpServerDockerApplicationTests {
    @Resource
    private DockerService dockerService;

    @Test
    void contextLoads() {
        System.out.println(dockerService.list_containers());
    }

}
