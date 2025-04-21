package click.taptap.mcp.server.docker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NetworkInfoVO {
    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("Scope")
    private String scope;

    @JsonProperty("Created")
    private String created;
} 