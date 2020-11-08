package cs203t10.ryver.content.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cs203t10.ryver.cms.content.Content;
import cs203t10.ryver.cms.content.ContentRepository;
import cs203t10.ryver.cms.content.ContentServiceImpl;
import cs203t10.ryver.cms.content.view.ContentInfoUpdatableByManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ContentServiceTest {

    @Mock
    private ContentRepository contents;

    @InjectMocks
    private ContentServiceImpl contentService;


    @Test
    void addContent_NewTitle_ReturnSavedContent(){
        // arrange ***
        Content content = Content.builder()
            .title("[New!] Property stocks in the green")
            .summary("Property stocks are selling like hot potatoes")
            .content("Buy stocks from BoomerProperty")
            .build();

        // stubbing ***
        when(contents.findByTitle(any(String.class))).thenReturn(new ArrayList<Content>());
        when(contents.save(any(Content.class))).thenReturn(content);

        // act ***
        Content savedContent = contentService.addContent(content);

        // assert ***
        assertNotNull(savedContent);
        verify(contents).findByTitle(content.getTitle());
        verify(contents).save(content);
    }

    @Test
    void addContent_SameTitle_ReturnNull(){
        //arrange ***
        Content content =  Content.builder()
            .title("[Duplicated] Property stocks in the red")
            .summary("Property stocks are not selling at all")
            .content("Buy buy property stocks")
            .build();

        List<Content> sameTitles = new ArrayList<Content>();
        sameTitles.add(content);

        //stubbing ***
        when(contents.findByTitle(content.getTitle())).thenReturn(sameTitles);

        //act ***
        Content savedContent = contentService.addContent(content);

        //assert ***
        assertNull(savedContent);
        verify(contents).findByTitle(content.getTitle());

    }

    @Test
    void approveContent_NotFound_ReturnNull(){
        Content content = Content.builder()
            .title("[Not Found] Property stocks stagnating")
            .summary("Property stocks are not selling at all")
            .content("Don't buy property stocks")
            .build();

        //stubbing ***
        when(contents.findById(content.getId())).thenReturn(Optional.empty());

        //act ***
        Content updatedContent = contentService.approveContent(content.getId());

        //assert ***
        assertNull(updatedContent);
        verify(contents).findById(content.getId());
    }

    @Test
    void updateContent_NewContent_ReturnUpdatedContent(){
        //arrange ***
        Content content = Content.builder()
            .title("[Pending Approval] Property stocks stagnating")
            .summary("Property stocks are not selling at all")
            .content("Don't buy property stocks")
            .build();


        ContentInfoUpdatableByManager contentUpdate = new ContentInfoUpdatableByManager();
        contentUpdate.setTitle("Property stocks stagnating");


        //stubbing ***
        when(contents.findById(content.getId())).thenReturn(Optional.of(content));
        when(contents.save(any(Content.class))).thenReturn(content);

        //act ***
        Content updatedContent = contentService.updateContent(content.getId(), contentUpdate);

        ///assert***
        assertNotNull(updatedContent);
        assertEquals(updatedContent.getTitle(), content.getTitle());
        verify(contents).findById(content.getId());
        verify(contents).save(content);
    }

}
