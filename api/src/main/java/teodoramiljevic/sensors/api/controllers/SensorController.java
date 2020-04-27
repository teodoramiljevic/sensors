package teodoramiljevic.sensors.api.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teodoramiljevic.sensors.api.dto.SensorDataAddValueRequest;
import teodoramiljevic.sensors.api.dto.SensorDataAddValueResponse;
import teodoramiljevic.sensors.api.models.messages.SensorDataResponse;
import teodoramiljevic.sensors.api.services.SensorService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private SensorService sensorService;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(SensorController.class);


    //TODO: Handle invalid model exceptions - global handling
    @PutMapping("/addValue")
    public ResponseEntity<SensorDataAddValueResponse> addValue(@Valid @RequestBody final SensorDataAddValueRequest sensorDataAddValueRequest){
        final Optional<SensorDataResponse> sensorData = sensorService.addValueToSensor(sensorDataAddValueRequest.getValue(), sensorDataAddValueRequest.getId());

        if(sensorData.isPresent()){
            final SensorDataAddValueResponse sensorDataAddValueResponse = modelMapper.map(sensorData.get(), SensorDataAddValueResponse.class);

            logger.debug("Successfully added sensor value: "+ sensorDataAddValueResponse.getValue());
            return ResponseEntity.ok(sensorDataAddValueResponse);
        }

        logger.debug("Sensor service failed to add value for sensor " + sensorDataAddValueRequest.getId());

        //TODO: Handle possible exceptions/empty sensor data with meaningful logs
        return ResponseEntity.status(500).body(null);
    }
}
