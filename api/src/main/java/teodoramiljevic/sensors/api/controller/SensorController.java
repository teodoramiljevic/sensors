package teodoramiljevic.sensors.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teodoramiljevic.sensors.api.dto.SensorValue;
import teodoramiljevic.sensors.api.dto.addValue.AddValueRequest;
import teodoramiljevic.sensors.api.dto.addValue.AddValueResponse;
import teodoramiljevic.sensors.api.dto.getLatest.GetLatestResponse;
import teodoramiljevic.sensors.api.dto.getValues.GetValuesResponse;
import teodoramiljevic.sensors.api.model.SensorData;
import teodoramiljevic.sensors.api.model.SensorDataList;
import teodoramiljevic.sensors.api.service.SensorService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
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
    //TODO: Make better responses with message keys
    @PutMapping("/add-value")
    public ResponseEntity<AddValueResponse> addValue(@Valid @RequestBody final AddValueRequest addValueRequest){
        final Optional<SensorData> sensorData = sensorService.addValue(addValueRequest.getSensorId(),new SensorData(addValueRequest.getValue()));

        if(sensorData.isPresent()){
            logger.debug("Successfully added - "+ sensorData.get().getValue());
            return ResponseEntity.ok(modelMapper.map(sensorData.get().getValue(), AddValueResponse.class));
        }

        logger.debug("Sensor service failed to add value for sensor " + addValueRequest.getSensorId());
        return ResponseEntity.status(500).body(null);
    }

    @GetMapping("/get-latest/{id}")
    public ResponseEntity<GetLatestResponse> getLatest(@Valid @NotEmpty @PathVariable final String id){
        final Optional<SensorData> sensorData = sensorService.getLatest(id);

        if(sensorData.isPresent()){
            logger.debug("Successfully retrieved latest value");
            return ResponseEntity.ok(modelMapper.map(sensorData.get().getValue(), GetLatestResponse.class));
        }

        logger.debug("Failed to get latest value for sensor " + id);
        return ResponseEntity.status(500).body(null);
    }

    @GetMapping("/get-values/{id}")
    public ResponseEntity<GetValuesResponse> getValues(@Valid @NotEmpty @PathVariable final String id){
        final SensorDataList sensorDataList = sensorService.getValues(id);
        final List<SensorValue> resultValues = Arrays.asList(modelMapper.map(sensorDataList.getValues(), SensorValue[].class));

        return ResponseEntity.ok(new GetValuesResponse(resultValues));
    }
}
