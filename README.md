# Docker MCP Server

The Docker MCP Server is a [Model Context Protocol (MCP)](https://modelcontextprotocol.io/introduction)
server that provides seamless integration with Docker Desktop on Windows, enabling advanced
automation and interaction capabilities for developers and tools.


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

### Container Lifecycle Management 🛠️
  - **list_containers** - Get the list of containers ✅
  - **get_container_info** - Get details of a specific container ✅
    - `container_id`: Container ID (string, required)
  - **start_container** - Start a container ✅
    - `container_id`: Container ID (string, required)
  - **stop_container** - Stop a container ✅
    - `container_id`: Container ID (string, required)
  - **remove_container** - Remove a container ✅
    - `container_id`: Container ID (string, required)

  - create_container
  - restart_container
  - Create, start, stop, restart, and remove containers
  - Batch container operations management
  - Container health status monitoring

### Image Management❌
  - Image pull, push, build, and removal
  - Image version control and tag management
  - Custom image registry integration

### Network Management❌
  - Create and manage Docker networks
  - Network connection configuration
  - Port mapping management

### Volume Management❌
  - Create, mount, and remove data volumes
  - Data volume permission management
  - Data volume backup and recovery

## FAQ

### Q: How do I install the project?
A: Follow the steps in the [Installation](#installation) section. Make sure you have the required dependencies.


## License

This project is licensed under the terms of the MIT open source license. Please refer to [MIT](./LICENSE) for the full terms.
