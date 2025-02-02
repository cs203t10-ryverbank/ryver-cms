package cs203t10.ryver.cms.content;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import cs203t10.ryver.cms.content.view.ContentInfo;
import cs203t10.ryver.cms.content.view.ContentInfoUpdatableByAnalyst;
import cs203t10.ryver.cms.content.view.ContentInfoUpdatableByManager;
import cs203t10.ryver.cms.config.DefaultContents;
import cs203t10.ryver.cms.content.ContentException.ContentNotFoundException;
import cs203t10.ryver.cms.content.ContentException.DuplicateContentException;
import cs203t10.ryver.cms.security.SecurityUtils;
import cs203t10.ryver.cms.util.CustomBeanUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import static cs203t10.ryver.cms.content.ContentException.ContentUpdateForbiddenException;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private DefaultContents defaultContents;

    @GetMapping("/contents")
    @Operation(summary = "Get all user content")
    @ApiResponse(responseCode = "200", 
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", 
                schema = @Schema(implementation = Content[].class)))
    public List<Content> getContents() {
        boolean isManager = SecurityUtils.isManagerAuthenticated();
        boolean isAnalyst = SecurityUtils.isAnalystAuthenticated();
        if (isManager || isAnalyst) {
            return contentService.listContents();
        } else {
            return contentService.listApprovedContents();
        }

    }

    @GetMapping("/contents/{id}")
    @Operation(summary = "Get a user's content")
    @ApiResponse(responseCode = "200", 
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", 
                schema = @Schema(implementation = Content.class)))
    public Content getContent(@PathVariable Integer id) {
        Content content = null;
        boolean isManager = SecurityUtils.isManagerAuthenticated();
        boolean isAnalyst = SecurityUtils.isAnalystAuthenticated();
        if (isManager || isAnalyst) {
            content = contentService.getContent(id);
        } else {
            content = contentService.getApprovedContent(id);
        }
        if(content == null) throw new ContentNotFoundException(id);
        return content;
    }

    @PostMapping("/contents")
    @PreAuthorize("hasRole('ANALYST') or hasRole('MANAGER')")
    @Operation(summary = "Add content")
    @ApiResponse(responseCode = "201", 
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", 
                schema = @Schema(implementation = Content.class)))
    @ResponseStatus(HttpStatus.CREATED)
    public Content addContent(@Valid @RequestBody ContentInfoUpdatableByManager contentInfo){

        boolean isManager = SecurityUtils.isManagerAuthenticated();

        ContentInfo updatableInfo = isManager
                ? new ContentInfoUpdatableByManager()
                : new ContentInfoUpdatableByAnalyst();

        Content contentToAdd = new Content();
        CustomBeanUtils.copyNonNullProperties(contentInfo, updatableInfo);
        CustomBeanUtils.copyNonNullProperties(updatableInfo, contentToAdd);
        Content addedContent = contentService.addContent(contentToAdd);
        if(addedContent == null) throw new DuplicateContentException(contentToAdd.getTitle());
        return addedContent;
    }

    @DeleteMapping("/contents/{id}")
    @PreAuthorize("hasRole('ANALYST') or hasRole('MANAGER')")
    @Operation(summary = "Delete content")
    @ApiResponse(responseCode = "200")
    @ResponseStatus(HttpStatus.OK)
    public void deleteContent(@PathVariable Integer id){
        Content content = contentService.getContent(id);
        if(content == null) throw new ContentNotFoundException(id);
        contentService.deleteContent(id);
    }

    @PutMapping("/contents/{id}")
    @PreAuthorize("hasRole('ANALYST') or hasRole('MANAGER')")
    @Operation(summary = "Update content's details")
    @ApiResponse(responseCode = "200", 
                content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", 
                schema = @Schema(implementation = Content.class)))
    public Content updateContent(@PathVariable Integer id,
            @Valid @RequestBody(required = false) ContentInfoUpdatableByManager newContentInfo){

        Content content = contentService.getContent(id);
        Content updatedContent = null;

        boolean isManager = SecurityUtils.isManagerAuthenticated();

        if (newContentInfo == null ){
            if (isManager){
                updatedContent = contentService.approveContent(id);
                if(updatedContent == null) throw new ContentNotFoundException(id);
                return updatedContent;
            }
            throw new ContentUpdateForbiddenException();
        }

        ContentInfo updatableInfo = isManager
                ? new ContentInfoUpdatableByManager()
                : new ContentInfoUpdatableByAnalyst();

        CustomBeanUtils.copyNonNullProperties(newContentInfo, updatableInfo);
        updatedContent = contentService.updateContent(id, updatableInfo);

        if(updatedContent == null) throw new ContentNotFoundException(id);
        return updatedContent;

    }

    @ResponseStatus(HttpStatus.OK)
	@PostMapping("/reset")
    @RolesAllowed("MANAGER")
    @Hidden
	public void resetContents() {
		contentService.resetContents();
        try {
            defaultContents.run();
        } catch (Exception e) {
            System.out.println("Could not add default contents");
        }
	}

}

