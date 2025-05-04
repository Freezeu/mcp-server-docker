# Docker MCP Server

The Docker MCP Server is a [Model Context Protocol (MCP)](https://modelcontextprotocol.io/introduction)
server that provides seamless integration with Docker Desktop on Windows, enabling advanced
automation and interaction capabilities for developers and tools.

<a href="https://glama.ai/mcp/servers/@Freezeu/mcp-server-docker">
  <img width="380" height="200" src="https://glama.ai/mcp/servers/@Freezeu/mcp-server-docker/badge" />
</a>

## Prerequisites

1. Install Docker Desktop for Windows from Docker's official [download page](https://docs.docker.com/desktop/setup/install/windows-install/).
2. In Docker Desktop, go to **Settings > General** and check the box for  
   **"Expose daemon on tcp://localhost:2375 without TLS"**.  
   This is required for the MCP server to communicate with Docker Desktop.

   > 💡 **Tip:** You can verify that the port is exposed successfully by opening  
   > `http://localhost:2375/version` in your browser. If the setup is correct,  
   > you should see a JSON response with Docker version information.
   > If you're having trouble, check this discussion for a possible solution:
   > [WSL and Docker for Windows: Cannot connect to the Docker daemon](https://forums.docker.com/t/wsl-and-docker-for-windows-cannot-connect-to-the-docker-daemon-at-tcp-localhost-2375-is-the-docker-daemon-running/63571/13)

## Installation

### Usage with Claude Desktop

```json
{
  "mcpServers": {
    "docker-mcp-stdio": {
      "command": "docker",
      "args": [
        "run",
        "-i",
        "--rm",
        "freezeolo/docker-mcp-server"
      ]
    }
  }
}
```


## Tools
✅ **Completed**  🛠️ **In Progress**  ❌ **Plan**  

### Container Lifecycle Management ✅
  - **list_containers** - Get the list of containers ✅
  - **get_container_info** - Get details of a specific container ✅
    - `container_id`: Container ID (string, required)
  - **start_container** - Start a container ✅
    - `container_id`: Container ID (string, required)
  - **restart_container** - Restart a container ✅
    - `container_id`: Container ID (string, required)
  - **stop_container** - Stop a container ✅
    - `container_id`: Container ID (string, required)
  - **remove_container** - Remove a container ✅
    - `container_id`: Container ID (string, required)
  - **create_container** - Create a new container ✅
    - `imageName`: Image name (string, required)
    - `containerName`: Container name (string, required)
    - `command`: Command to run (string, optional)
    - `env`: Environment variables (string[], optional)
    - `ports`: Port bindings (string[], optional)
    - `volumes`: Volume bindings (string[], optional)
    - `network`: Network name (string, optional)
    - `restartPolicy`: Restart policy (string, optional)

### Network Management ✅
  - **list_networks** - List all networks ✅
  - **get_network_info** - Get network information ✅
    - `networkId`: Network ID (string, required)
  - **create_network** - Create a new network ✅
    - `networkName`: Network name (string, required)
    - `driver`: Network driver (string, required)
    - `subnet`: Subnet CIDR (string, required)
    - `gateway`: Gateway IP (string, required)
  - **connect_container_to_network** - Connect container to network ✅
    - `containerId`: Container ID (string, required)
    - `networkId`: Network ID (string, required)
  - **disconnect_container_from_network** - Disconnect container from network ✅
    - `containerId`: Container ID (string, required)
    - `networkId`: Network ID (string, required)
  - **remove_network** - Remove a network ✅
    - `networkId`: Network ID (string, required)
  - **get_network_containers** - Get containers in a network ✅
    - `networkId`: Network ID (string, required)

### Image Management ✅
  - **list_images** - List all images ✅
  - **pull_image** - Pull an image from registry ✅
    - `imageName`: Image name with tag (string, required)
  - **push_image** - Push an image to registry ✅
    - `imageName`: Image name with tag (string, required)
  - **remove_image** - Remove an image ✅
    - `imageId`: Image ID (string, required)
    - `force`: Force removal (boolean, optional)
  - **tag_image** - Tag an image with multiple tags ✅
    - `imageId`: Image ID (string, required)
    - `repository`: Repository name (string, required)
    - `tags`: Array of tags (string[], required)
  - **build_image** - Build an image from Dockerfile ✅
    - `dockerfilePath`: Path to Dockerfile (string, required)
    - `repository`: Repository name (string, required)
    - `tags`: Array of tags (string[], required)
  - **get_image_details** - Get image details ✅
    - `imageId`: Image ID (string, required)

## FAQ

### Q: How do I install the project?
A: Follow the steps in the [Installation](#installation) section. Make sure you have the required dependencies.


## License

This project is licensed under the terms of the MIT open source license. Please refer to [MIT](./LICENSE) for the full terms.
