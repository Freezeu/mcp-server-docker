package click.taptap.mcp.server.docker.service;

import click.taptap.mcp.server.docker.vo.ContainerInfoVO;
import click.taptap.mcp.server.docker.vo.ImageInfoVO;
import click.taptap.mcp.server.docker.vo.NetworkInfoVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Tool(description = "Create a new container")
    public CreateContainerResponse create_container(
            @ToolParam(description = "imageName") String imageName,
            @ToolParam(description = "containerName") String containerName,
            @ToolParam(description = "command", required = false) String command,
            @ToolParam(description = "environment variables", required = false) String[] env,
            @ToolParam(description = "port bindings", required = false) String[] ports,
            @ToolParam(description = "volume bindings", required = false) String[] volumes,
            @ToolParam(description = "network", required = false) String network,
            @ToolParam(description = "restart policy", required = false) String restartPolicy) {
        
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageName)
                .withName(containerName);

        // 设置命令
        if (StringUtils.isNotBlank(command)) {
            createContainerCmd.withCmd(command.split(" "));
        }

        // 设置环境变量
        if (env != null && env.length > 0) {
            createContainerCmd.withEnv(env);
        }

        // 设置端口映射
        if (ports != null && ports.length > 0) {
            List<PortBinding> portBindings = new ArrayList<>();
            for (String port : ports) {
                String[] parts = port.split(":");
                if (parts.length == 2) {
                    portBindings.add(new PortBinding(
                            Ports.Binding.bindPort(Integer.parseInt(parts[1])),
                            ExposedPort.tcp(Integer.parseInt(parts[0]))
                    ));
                }
            }
            createContainerCmd.withHostConfig(new HostConfig().withPortBindings(portBindings));
        }

        // 设置卷挂载
        if (volumes != null && volumes.length > 0) {
            List<Mount> mounts = new ArrayList<>();
            for (String volume : volumes) {
                String[] parts = volume.split(":");
                if (parts.length == 2) {
                    mounts.add(new Mount()
                            .withType(MountType.BIND)
                            .withSource(parts[0])
                            .withTarget(parts[1]));
                }
            }
            createContainerCmd.withHostConfig(new HostConfig().withMounts(mounts));
        }

        // 设置网络
        if (StringUtils.isNotBlank(network)) {
            createContainerCmd.withHostConfig(new HostConfig().withNetworkMode(network));
        }

        // 设置重启策略
        if (StringUtils.isNotBlank(restartPolicy)) {
            createContainerCmd.withHostConfig(new HostConfig()
                    .withRestartPolicy(RestartPolicy.parse(restartPolicy)));
        }

        return createContainerCmd.exec();
    }

    // ==================== Network Management ====================
    @Tool(description = "List all networks")
    public List<NetworkInfoVO> list_networks() {
        List<Network> networks = dockerClient.listNetworksCmd().exec();
        return BeanUtil.copyToList(networks, NetworkInfoVO.class);
    }

    @Tool(description = "Get network information")
    public Network get_network_info(@ToolParam(description = "networkId") String networkId) {
        return dockerClient.inspectNetworkCmd().withNetworkId(networkId).exec();
    }

    @Tool(description = "Create a new network")
    public CreateNetworkResponse create_network(@ToolParam(description = "networkName") String networkName,
                               @ToolParam(description = "driver") String driver,
                               @ToolParam(description = "subnet", required = false) String subnet,
                               @ToolParam(description = "gateway", required = false) String gateway) {
        Network.Ipam ipam = null;
        if (StringUtils.isNotBlank(subnet)) {
            ipam = new Network.Ipam()
                    .withConfig(new Network.Ipam.Config()
                            .withSubnet(subnet)
                            .withGateway(gateway));
        }
        return dockerClient.createNetworkCmd()
                .withName(networkName)
                .withDriver(driver)
                .withIpam(ipam)
                .exec();
    }

    @Tool(description = "Connect a container to a network")
    public String connect_container_to_network(@ToolParam(description = "containerId") String containerId,
                                             @ToolParam(description = "networkId") String networkId) {
        dockerClient.connectToNetworkCmd()
                .withContainerId(containerId)
                .withNetworkId(networkId)
                .exec();
        return "Container connected to network successfully";
    }

    @Tool(description = "Disconnect a container from a network")
    public String disconnect_container_from_network(@ToolParam(description = "containerId") String containerId,
                                                  @ToolParam(description = "networkId") String networkId) {
        dockerClient.disconnectFromNetworkCmd()
                .withContainerId(containerId)
                .withNetworkId(networkId)
                .exec();
        return "Container disconnected from network successfully";
    }

    @Tool(description = "Remove a network")
    public String remove_network(@ToolParam(description = "networkId") String networkId) {
        dockerClient.removeNetworkCmd(networkId).exec();
        return "Network removed successfully";
    }

    @Tool(description = "Get containers in a network")
    public String get_network_containers(@ToolParam(description = "networkId") String networkId) {
        Network network = dockerClient.inspectNetworkCmd().withNetworkId(networkId).exec();
        return JSONUtil.toJsonStr(network.getContainers());
    }

    // ==================== Image Management ====================
    @Tool(description = "List all images")
    public List<ImageInfoVO> list_images() {
        List<Image> images = dockerClient.listImagesCmd().exec();
        return BeanUtil.copyToList(images, ImageInfoVO.class);
    }

    @Tool(description = "Pull an image from registry")
    public String pull_image(@ToolParam(description = "imageName") String imageName) {
        try {
            ResultCallback.Adapter<PullResponseItem> res = dockerClient.pullImageCmd(imageName)
                    .start()
                    .awaitCompletion();
            return res.toString();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Failed to pull image: " + e.getMessage();
        }
    }

    @Tool(description = "Push an image to registry")
    public String push_image(@ToolParam(description = "imageName") String imageName) {
        try {
            dockerClient.pushImageCmd(imageName)
                    .start()
                    .awaitCompletion();
            return "Image pushed successfully: " + imageName;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Failed to push image: " + e.getMessage();
        }
    }

    @Tool(description = "Remove an image")
    public String remove_image(@ToolParam(description = "imageId") String imageId,
                             @ToolParam(description = "force", required = false) boolean force) {
        dockerClient.removeImageCmd(imageId)
                .withForce(force)
                .exec();
        return "Image removed successfully: " + imageId;
    }

    @Tool(description = "Tag an image with multiple tags")
    public String tag_image(@ToolParam(description = "imageId") String imageId,
                          @ToolParam(description = "repository") String repository,
                          @ToolParam(description = "tags, e.g. ['latest','1.0.0']") String[] tags) {
        StringBuilder result = new StringBuilder();
        for (String tag : tags) {
            dockerClient.tagImageCmd(imageId, repository, tag).exec();
            result.append("Image tagged successfully: ").append(repository).append(":").append(tag).append("\n");
        }
        return result.toString();
    }

    @Tool(description = "Build an image from Dockerfile with multiple tags")
    public String build_image(@ToolParam(description = "dockerfilePath") String dockerfilePath,
                            @ToolParam(description = "repository") String repository,
                            @ToolParam(description = "tags, e.g. ['latest','1.0.0']") String[] tags) {
        try {
            // 创建标签列表
            Set<String> tagSet = new HashSet<>();
            for (String tag : tags) {
                tagSet.add(repository + ":" + tag);
            }

            // 构建镜像
            dockerClient.buildImageCmd()
                    .withDockerfile(new File(dockerfilePath))
                    .withTags(tagSet)  // 使用withTags替代withTag
                    .withPull(true)    // 自动拉取基础镜像
                    .withNoCache(false) // 使用缓存以提高构建速度
                    .start()
                    .awaitCompletion();

            return "Image built successfully with tags: " + String.join(", ", tagSet);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Failed to build image: " + e.getMessage();
        }
    }


    @Tool(description = "Get image details")
    public InspectImageResponse get_image_details(@ToolParam(description = "imageId") String imageId) {
        return dockerClient.inspectImageCmd(imageId).exec();
    }
}
