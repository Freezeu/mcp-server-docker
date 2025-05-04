package click.taptap.mcp.server.docker.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DockerServiceTest {

    @Autowired
    private DockerService dockerService;

    @Test
    void testListNetworks() {
        assertDoesNotThrow(() -> {
            var networks = dockerService.list_networks();
            assertNotNull(networks);
        });
    }

    @Test
    void testGetNetworkInfo() {
        assertDoesNotThrow(() -> {
            var networks = dockerService.list_networks();
            if (!networks.isEmpty()) {
                var network = dockerService.get_network_info(networks.getFirst().getId());
                assertNotNull(network);
            }
        });
    }

    @Test
    void testCreateNetwork() {
        assertDoesNotThrow(() -> {
            var response = dockerService.create_network(
                    "test-network",
                    "bridge",
                    null,
                    null
            );
            assertNotNull(response);
            assertNotNull(response.getId());
            dockerService.remove_network(response.getId());
        });
    }

    @Test
    void testListImages() {
        assertDoesNotThrow(() -> {
            var images = dockerService.list_images();
            assertNotNull(images);
        });
    }

    @Test
    void testPullImage() {
        assertDoesNotThrow(() -> {
            String result = dockerService.pull_image("hello-world:latest");
            assertTrue(result.contains("successfully"));
        });
    }

    @Test
    void testTagImage() {
        assertDoesNotThrow(() -> {
            var images = dockerService.list_images();
            if (!images.isEmpty()) {
                String result = dockerService.tag_image(
                    images.getFirst().getId(),
                    "test-repo",
                    new String[]{"test-tag"}
                );
                assertTrue(result.contains("successfully"));
            }
        });
    }

    @Test
    void testBuildImage() {
        assertDoesNotThrow(() -> {
            String result = dockerService.build_image(
                "src/test/resources/Dockerfile",
                "test-build",
                new String[]{"latest"}
            );
            assertTrue(result.contains("successfully"));
        });
    }

    @Test
    void testGetImageDetails() {
        assertDoesNotThrow(() -> {
            var images = dockerService.list_images();
            if (!images.isEmpty()) {
                var details = dockerService.get_image_details(images.getFirst().getId());
                assertNotNull(details);
            }
        });
    }

    @Test
    void testRemoveImage() {
        assertDoesNotThrow(() -> {
            var images = dockerService.list_images();
            if (!images.isEmpty()) {
                String result = dockerService.remove_image(images.getFirst().getId(), false);
                assertTrue(result.contains("successfully"));
            }
        });
    }

    @Test
    void get_container_logs() {
        assertDoesNotThrow(() -> {
            var containers = dockerService.list_containers();
            if (!containers.isEmpty()) {
                var logs = dockerService.get_container_logs(containers.getFirst().getId(), false, true, null, null, null);
                System.out.println(logs);
                assertNotNull(logs);
            }
        });
    }
}