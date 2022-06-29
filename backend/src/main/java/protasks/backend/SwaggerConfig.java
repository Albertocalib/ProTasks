package protasks.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerBoardApi() {
        List<Parameter> pars = new ArrayList<>();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Board")
                .select()
                .apis(RequestHandlerSelectors.basePackage("protasks.backend.RestControllers"))
                .paths(regex("/api/board/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("Board API").build())
                .globalOperationParameters(pars);
    }
    @Bean
    public Docket swaggerMessageApi() {
        List<Parameter> pars = new ArrayList<>();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Message")
                .select()
                .apis(RequestHandlerSelectors.basePackage("protasks.backend.RestControllers"))
                .paths(regex("/api/message/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("Message API").build())
                .globalOperationParameters(pars);
    }
    @Bean
    public Docket swaggerTagApi() {
        List<Parameter> pars = new ArrayList<>();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Tag")
                .select()
                .apis(RequestHandlerSelectors.basePackage("protasks.backend.RestControllers"))
                .paths(regex("/api/tag/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("Tag API").build())
                .globalOperationParameters(pars);
    }
    @Bean
    public Docket swaggerTaskListApi() {
        List<Parameter> pars = new ArrayList<>();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("TaskList")
                .select()
                .apis(RequestHandlerSelectors.basePackage("protasks.backend.RestControllers"))
                .paths(regex("/api/list/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("TaskList API").build())
                .globalOperationParameters(pars);
    }
    @Bean
    public Docket swaggerTaskApi() {
        List<Parameter> pars = new ArrayList<>();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Task")
                .select()
                .apis(RequestHandlerSelectors.basePackage("protasks.backend.RestControllers"))
                .paths(regex("/api/task/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("Task API").build())
                .globalOperationParameters(pars);
    }
    @Bean
    public Docket swaggerUserApi() {
        List<Parameter> pars = new ArrayList<>();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("User")
                .select()
                .apis(RequestHandlerSelectors.basePackage("protasks.backend.RestControllers"))
                .paths(regex("/api/user/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("User API").build())
                .globalOperationParameters(pars);
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("ProTasks API")
                .description("ProTasks API reference for developers")
                .contact("acanal@alunmos.urjc.es").license("ProTasks License")
                .licenseUrl("canal@alunmos.urjc.es").version("1.0").build();
    }

}