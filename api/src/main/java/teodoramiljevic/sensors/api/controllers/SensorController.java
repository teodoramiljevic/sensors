package teodoramiljevic.sensors.api.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teodoramiljevic.sensors.api.dto.SensorDataAddValueRequest;
import teodoramiljevic.sensors.api.dto.SensorDataAddValueResponse;
import teodoramiljevic.sensors.api.models.SensorData;
import teodoramiljevic.sensors.api.services.SensorService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    private final SensorService sensorService;

    private final ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(SensorController.class);

    public SensorController(final SensorService sensorService, final ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    //TODO: Handle invalid model exceptions - global handling
    @PutMapping("/addValue")
    public ResponseEntity<SensorDataAddValueResponse> addValue(@Valid @RequestBody final SensorDataAddValueRequest sensorDataAddValueRequest){
        final Optional<SensorData> sensorData = sensorService.addValueToSensor(sensorDataAddValueRequest.getValue(), sensorDataAddValueRequest.getId());

        if(sensorData.isPresent()){
            final teodoramiljevic.sensors.api.dto.SensorDataAddValueResponse sensorDataAddValueResponse = modelMapper.map(sensorData.get(), teodoramiljevic.sensors.api.dto.SensorDataAddValueResponse.class);

            logger.debug("Successfully added sensor value: "+ sensorDataAddValueResponse.getValue());
            return ResponseEntity.ok(sensorDataAddValueResponse);
        }

        logger.debug("Sensor service failed to add value for sensor " + sensorDataAddValueRequest.getId());

        //TODO: Handle possible exceptions/empty sensor data with meaningful logs
        return ResponseEntity.status(500).body(null);
    }
}
