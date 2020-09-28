package cs203t10.ryver.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cs203t10.ryver.cms.content.Content;
import cs203t10.ryver.cms.content.ContentRepository;
import cs203t10.ryver.cms.content.ContentService;
@Component
@Order(2)
public class DefaultContents implements CommandLineRunner {

    @Autowired
    private ContentService contentService;

    
    /*
    Content item info:
        {
            "id": (auto-generated by your api, int value),
            "title":"The title of the advisory or news",
            "summary":"The short summary of the content item",
            "content": "The text of the content item",
            "link":"https://link.to.externalcontent",
            "approved": true (or false)
        }
     */

    @Override
    public void run(String... args) throws Exception {
        addDefaultContent(Content.builder()
            .title("BoomerProperty stocks are booming")
            .summary("Property prices have been going through the roof")
            .content("Buy stocks from BoomerProperty")
            .approved(true)
            .build());
        addDefaultContent(Content.builder()
            .title("WestProperty prices are plunging")
            .summary("Due to management issues and poor maintenance, investors are selling stocks fast")
            .content("Sell stocks from WestProperty")
            .approved(true)
            .build());
        addDefaultContent(Content.builder()
            .title("Conspiracy Theory: Property Market Collapse")
            .summary("Property tax is slated to increase and housing loans are rumoured to double")
            .content("Don't buy anything!")
            .approved(false)
            .build());
    }

    public void addDefaultContent(Content content) {
        contentService.addContent(content);
        System.out.println("[Add content]: " + content.getTitle());
    }
}

