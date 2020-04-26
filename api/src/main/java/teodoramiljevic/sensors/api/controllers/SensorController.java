package teodoramiljevic.sensors.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SensorService sensorService;
    @Autowired
    private ModelMapper modelMapper;


    //TODO: Handle invalid model exceptions - global handling
    @PutMapping("/addValue")
    public ResponseEntity<SensorDataAddValueResponse> addValue(@Valid @RequestBody final SensorDataAddValueRequest sensorDataAddValueRequest){
        final Optional<SensorData> sensorData = sensorService.addValueToSensor(sensorDataAddValueRequest.getValue(), sensorDataAddValueRequest.getId());

        if(sensorData.isPresent()){
            final SensorDataAddValueResponse sensorDataAddValueResponse = modelMapper.map(sensorData.get(), SensorDataAddValueResponse.class);
            return ResponseEntity.ok(sensorDataAddValueResponse);
        }

        //TODO: Handle possible exceptions/empty sensor data with meaningful messages
        return ResponseEntity.status(500).body(null);
    }
}
