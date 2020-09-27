package cs203t10.ryver.cms.content;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    List <Content> findByTitle(String title);

    List<Content> findByApproved(Boolean approved);
}

