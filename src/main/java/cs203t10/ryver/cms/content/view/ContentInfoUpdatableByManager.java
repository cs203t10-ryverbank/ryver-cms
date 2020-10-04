package cs203t10.ryver.cms.content.view;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@Getter @Setter
public class ContentInfoUpdatableByManager implements ContentInfo {

    @JsonProperty("id")
    private Integer id;

    private String title;

    private String summary;

    private String content;

    private String link;
    
    private Boolean approved;

}

