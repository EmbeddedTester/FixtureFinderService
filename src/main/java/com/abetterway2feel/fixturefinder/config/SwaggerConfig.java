package com.abetterway2feel.fixturefinder.config;

import com.abetterway2feel.fixturefinder.rest.Paths;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.abetterway2feel.fixturefinder.rest"))
                .paths(PathSelectors.ant(Paths.APP_ROOT + "/**/*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Fixture Finder Service",
                "Search for the fixtures that are played today or this year by your favourite team. " +
                        "Also check which competitions are currently supported in our service",
                "0.0.1",
                "https://termsfeed.com/terms-service/54c8fa503590c1577ea31611638813c2",
                new Contact("Philip Quinn", "https://github.com/abetterway2feel/FixtureFinderService", "philip.a.quinn@hotmail.co.uk"),
                "MIT_License",
                "https://en.wikipedia.org/wiki/MIT_License"
        );
    }
}
