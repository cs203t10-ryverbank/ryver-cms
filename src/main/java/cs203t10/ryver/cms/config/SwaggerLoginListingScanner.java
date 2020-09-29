package cs203t10.ryver.cms.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class SwaggerLoginListingScanner implements ApiListingScannerPlugin {

    private final CachingOperationNameGenerator operationNames;

    public SwaggerLoginListingScanner(CachingOperationNameGenerator operationNames) {
        this.operationNames = operationNames;
    }

    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        return new ArrayList<>(
                Arrays.asList(
                        new ApiDescription(null, "/login", "login", Collections.singletonList(
                                new OperationBuilder(operationNames)
                                        .method(HttpMethod.POST)
                                        .position(1)
                                        .codegenMethodNameStem("loginPost")
                                        .summary("Generate a JWT")
                                        .notes("Pass a username and password via Basic authentication to receive a JWT token.")
                                        .tags(Set.of("jwt-authentication-filter"))
                                        .authorizations(Arrays.asList(new SecurityReference("basicAuth", new AuthorizationScope[0])))
                                        .responseMessages(responseMessages())
                                        .responseModel(new ModelRef("UserToken"))
                                        .build()
                        ), false)));
    }

    /**
     * @return Set of response messages that overide the default/global response messages
     */
    private Set<ResponseMessage> responseMessages() {
        return Set.of(
                new ResponseMessageBuilder()
                        .code(200)
                        .build(), new ResponseMessageBuilder()
                        .code(401)
                        .build(),
                new ResponseMessageBuilder()
                        .code(403)
                        .build(),
                new ResponseMessageBuilder()
                        .code(404)
                        .build()
        );
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return DocumentationType.SWAGGER_2.equals(delimiter);
    }
}
