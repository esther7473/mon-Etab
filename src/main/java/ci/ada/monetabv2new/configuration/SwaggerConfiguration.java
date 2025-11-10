package ci.ada.monetabv2new.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI getOpenAPI() {
        final Info info = new Info()
                .title("MonetaB API")
                .version("0.0.1")
                .description("MonetaB API");
        return new OpenAPI().info(info);
    }
}
