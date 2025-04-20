package click.taptap.mcp.server.docker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.ContainerPort;
import lombok.Data;

@Data
public class ContainerInfoVO {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("ImageID")
    private String imageId;

    @JsonProperty("Names")
    private String[] names;

    @JsonProperty("Ports")
    public ContainerPort[] ports;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Created")
    private Long created;
}
