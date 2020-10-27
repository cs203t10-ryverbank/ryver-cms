package cs203t10.ryver.cms.content;

import java.util.List;

import cs203t10.ryver.cms.content.view.ContentInfo;


public interface ContentService {
    List<Content> listContents();
    List<Content> listApprovedContents();
    Content getContent(Integer id);
    Content getApprovedContent(Integer id);
    Content addContent(Content content);
    Content approveContent(Integer id);
    void deleteContent(Integer id);
    void resetContents();
    Content updateContent(Integer id, ContentInfo newContentInfo);
}
