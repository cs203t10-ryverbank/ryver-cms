package cs203t10.ryver.cms.content;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/contents")
    public List<Content> getContents() {
        return contentService.listApprovedContents(true);
    }

    // @GetMapping("/contents")
    // @RolesAllowed("MANAGER")
    // public List<Content> getContents() {
    //      return contentService.listContents();
    // }

    @PutMapping("/contents/{id}")
    @RolesAllowed("MANAGER")
    public Content approveContent(@PathVariable Integer id){
        Content content= contentService.approveContent(id);
        if(content == null) throw new ContentNotFoundException(id);
        
        return content;
    }
}

