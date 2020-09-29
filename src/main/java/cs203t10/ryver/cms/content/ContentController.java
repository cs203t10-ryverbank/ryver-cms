package cs203t10.ryver.cms.content;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import cs203t10.ryver.cms.content.view.ContentInfoViewableByAnalyst;
import cs203t10.ryver.cms.content.ContentException.ContentNotFoundException;
import cs203t10.ryver.cms.content.ContentException.DuplicateContentException;
import cs203t10.ryver.cms.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;

import static cs203t10.ryver.cms.content.ContentException.ContentUpdateForbiddenException;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/contents")
    @PreAuthorize("principal != null or hasRole('MANAGER')")
    @ApiOperation(value = "Get all user content")
    public List<Content> getContents() {
        if (SecurityUtils.isManagerAuthenticated()) {
            return contentService.listContents();
        } else {
            return contentService.listApprovedContents();
        }
         
    }

    @GetMapping("/contents/{id}")
    @PreAuthorize("principal != null or hasRole('MANAGER')")
    @ApiOperation(value = "Get a user's content")
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

    @PutMapping("/contents/manager/{id}")
    @RolesAllowed("MANAGER")
    @ApiOperation(value = "Update content's approval status")
    public Content approveContent(@PathVariable Integer id){
        Content content= contentService.approveContent(id);
        if(content == null) throw new ContentNotFoundException(id);
        return content;
    }

    @PostMapping("/contents")
    @RolesAllowed("ANALYST")
    @ApiOperation(value = "Add content")
    @ResponseStatus(HttpStatus.CREATED)
    public Content addContent(@Valid @RequestBody Content content){
        Content addedContent = contentService.addContent(content);
        if(addedContent == null) throw new DuplicateContentException(content.getTitle());
        return addedContent;
    }

    @DeleteMapping("/contents/{id}")
    @RolesAllowed("ANALYST")
    @ApiOperation(value = "Delete content")
    @ResponseStatus(HttpStatus.OK)
    public void deleteContent(@PathVariable Integer id){
        Content content = contentService.getContent(id);
        if(content == null) throw new ContentNotFoundException(id);
        contentService.deleteContent(id);
    }

    @PutMapping("/contents/{id}")
    @PreAuthorize("hasRole('ANALYST')")
    @ApiOperation(value = "Update content's details",
            notes = "Only fields defined in the request body will be updated.",
            response = ContentInfoViewableByAnalyst.class)
    public Content updateContent(@PathVariable Integer id, 
            @Valid @RequestBody Content newContentInfo){
        Content content = contentService.getContent(id);
        content = contentService.updateContent(id, newContentInfo);
        if(content == null) throw new ContentNotFoundException(id);
        return content;
    }
}

