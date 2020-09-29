package cs203t10.ryver.cms.content;

import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class ContentServiceImpl implements ContentService {
   
    private ContentRepository contents;
    
    public ContentServiceImpl(ContentRepository contents){
        this.contents = contents;
    }

    @Override
    public List<Content> listContents() {
        return contents.findAll();
    }

    @Override
    public List<Content> listApprovedContents() {
        return contents.findByApproved(true);
    }

    
    @Override
    public Content getContent(Integer id){
        return contents.findById(id).orElse(null);
    }

    @Override
    public Content getApprovedContent(Integer id){
        Content content = contents.findById(id).orElse(null);
        if (content == null || content.getApproved()){
            return content;
        } else{
            //content is not approved
            return null;
        }
    }
    
    @Override
    public Content addContent(Content content) {
        List<Content> sameTitles = contents.findByTitle(content.getTitle());
        if(sameTitles.size() == 0)
            return contents.save(content);
        else
            return null;
    }
    
    @Override
    public Content approveContent(Integer id){
        return contents.findById(id).map( content -> {content.setApproved(true);
            return contents.save(content);
        }).orElse(null);

    }

}