package cs203t10.ryver.cms.content;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import lombok.*;

@Entity
@Getter @Setter @Builder(toBuilder = true)
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode @ToString
public class Content {

    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Content's title cannot be null")
    @Size(min = 5, max = 50, message = "Content's title should be between 5 and 50 characters")
    private String title;

    @NotNull(message = "Content's summary cannot be null")
    @Size(min = 5, max = 100, message = "Content's summary should be between 5 and 100 characters")
    private String summary;

    @NotNull(message = "Content's content body cannot be null")
    private String content;
    
    //Questions: Can link be null? Do we need a pattern regex for link?
    private String link;

    @Builder.Default
    private boolean approved = false;

    public boolean getApproved(){
        return approved;
    }

    //Additional: Do we add tags?
}

