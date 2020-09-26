package cs203t10.ryver.cms.content;

import java.util.List;


public interface ContentService {
    List<Content> listContents();
    Content getContent(Integer id);
    Content addContent(Content content);
    Content approveContent(Integer id);

}
