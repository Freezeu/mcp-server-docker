package click.taptap.mcp.server.docker.service;

import click.taptap.mcp.server.docker.vo.ContainerInfoVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DockerService {
    private final DockerClient dockerClient;

    // ==================== Container Lifecycle Management ====================
    @Tool(description = "Get the list of containers")
    public List<ContainerInfoVO> list_containers() {
        List<Container> containerList = dockerClient.listContainersCmd().withShowAll(true).exec();
        return BeanUtil.copyToList(containerList, ContainerInfoVO.class);
    }

    @Tool(description = "Get container information")
    public String get_container_info(@ToolParam(description = "containerId") String containerId) {
        InspectContainerResponse exec = dockerClient.inspectContainerCmd(containerId).exec();
        return JSONUtil.toJsonStr(exec);
    }

    @Tool(description = "Start a container")
    public String start_container(@ToolParam(description = "containerId") String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
        return "Container started successfully";
    }

    @Tool(description = "Restart a container")
    public String restart_container(@ToolParam(description = "containerId") String containerId) {
        dockerClient.restartContainerCmd(containerId).exec();
        return "Container started successfully";
    }

    @Tool(description = "Stop a container")
    public String stop_container(@ToolParam(description = "containerId") String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
        return "Container stopped successfully";
    }

    @Tool(description = "Remove a container")
    public String remove_container(@ToolParam(description = "containerId") String containerId) {
        dockerClient.removeContainerCmd(containerId).withForce(false).exec();
        return "Container removed successfully";
    }

    // @Tool(description = "Create a container")
    public String create_container(@ToolParam(description = "imageName") String imageName,
                                   @ToolParam(description = "containerName") String containerName,
                                   @ToolParam(description = "command") String command) {
        CreateContainerResponse exec = dockerClient.createContainerCmd(imageName)
                .withName(containerName)
                .withCmd(command)
                .exec();
        return JSONUtil.toJsonStr(exec);
    }

    public String get_container_logs(@ToolParam(description = "containerId") String containerId) {
        return null;
    }

    public String get_container_stats() {
        return null;
    }

    public String restart_container() {
        return null;
    }
}
