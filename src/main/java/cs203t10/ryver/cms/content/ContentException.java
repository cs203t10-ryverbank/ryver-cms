package cs203t10.ryver.cms.content;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



public class ContentException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ContentNotFoundException extends RuntimeException{

        private static final long serialVersionUID = 1L;

        public ContentNotFoundException(Integer id) {
            super("Could not find book of id: " + id);
        }
        
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class DuplicateContentException extends RuntimeException{

        private static final long serialVersionUID = 1L;

        public DuplicateContentException(String title) {
            super("Post: " + title + " already exists");
        }
        
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Property update forbidden")
    public static class ContentUpdateForbiddenException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

}