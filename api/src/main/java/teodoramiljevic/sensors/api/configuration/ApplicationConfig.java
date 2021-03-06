package teodoramiljevic.sensors.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import teodoramiljevic.sensors.api.wrapper.ObjectMapperWrapper;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public ObjectMapperWrapper objectMapper(){
        return new ObjectMapperWrapper(new ObjectMapper());
    }
}
