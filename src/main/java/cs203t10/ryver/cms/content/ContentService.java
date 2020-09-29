package cs203t10.ryver.cms.content;

import java.util.List;


public interface ContentService {
    List<Content> listContents();
    List<Content> listApprovedContents();
    Content getContent(Integer id);
    Content getApprovedContent(Integer id);
    Content addContent(Content content);
    Content approveContent(Integer id);
    void deleteContent(Integer id);
    Content updateContent(Integer id, Content newContentInfo);
}
