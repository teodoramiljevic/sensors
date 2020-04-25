package teodoramiljevic.sensors.api.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensor")
public class SensorController {

    @PutMapping("/{id}/addValue")
    public String addValue(@RequestBody double value, @PathVariable long id){

        return "Got: "+ value;
    }
}
