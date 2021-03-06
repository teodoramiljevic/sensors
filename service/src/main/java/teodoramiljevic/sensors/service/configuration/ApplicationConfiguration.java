package teodoramiljevic.sensors.service.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import teodoramiljevic.sensors.service.wrapper.ObjectMapperWrapper;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapperWrapper objectMapper() {
        return new ObjectMapperWrapper(new ObjectMapper());
    }
}
