package click.taptap.mcp.server.docker.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageInfoVO {
    @JsonProperty("Id")
    private String id;

    @JsonProperty("RepoTags")
    private String[] repoTags;

    @JsonProperty("Size")
    private Long size;

    @JsonProperty("Created")
    private Long created;

    @JsonProperty("Labels")
    private String[] labels;
} 