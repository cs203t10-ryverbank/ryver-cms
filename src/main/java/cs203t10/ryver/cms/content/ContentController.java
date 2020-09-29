package cs203t10.ryver.cms.content;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import cs203t10.ryver.cms.content.ContentException.ContentNotFoundException;
import cs203t10.ryver.cms.content.ContentException.DuplicateContentException;
import cs203t10.ryver.cms.security.SecurityUtils;


@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/contents")
    @PreAuthorize("principal != null or hasRole('MANAGER')")
    public List<Content> getContents() {
        if (SecurityUtils.isManagerAuthenticated()) {
            return contentService.listContents();
        } else {
            return contentService.listApprovedContents();
        }
         
    }

    @GetMapping("/contents/{id}")
    @PreAuthorize("principal != null or hasRole('MANAGER')")
    public Content getContent(@PathVariable Integer id) {
        Content content = null;
        if (SecurityUtils.isManagerAuthenticated()) {
            content = contentService.getContent(id);
        } else {
            content = contentService.getApprovedContent(id);
        }
        if(content == null) throw new ContentNotFoundException(id);
        return content;
    }

    @PutMapping("/contents/{id}")
    @RolesAllowed("MANAGER")
    public Content approveContent(@PathVariable Integer id){
        Content content= contentService.approveContent(id);
        if(content == null) throw new ContentNotFoundException(id);
        
        return content;
    }

    @PostMapping("/contents")
    @RolesAllowed("ANALYST")
    @ResponseStatus(HttpStatus.CREATED)
    public Content addContent(@Valid @RequestBody Content content){
        Content addedContent = contentService.addContent(content);
        if(addedContent == null) throw new DuplicateContentException(content.getTitle());
        return addedContent;
    }
}

