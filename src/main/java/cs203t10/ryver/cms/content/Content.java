package cs203t10.ryver.cms.content;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
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
    @Lob
    private String title;

    @NotNull(message = "Content's summary cannot be null")
    @Lob
    private String summary;

    @NotNull(message = "Content's content body cannot be null")
    @Lob
    private String content;

    @Lob
    private String link;

    @Builder.Default
    private Boolean approved = false;

    public Boolean getApproved(){
        return approved;
    }

}

