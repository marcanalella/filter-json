package filterJson.controller;


import filterJson.service.FilterJsonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1")
public class RestController {

    @Autowired
    private FilterJsonService filterJsonService;

    Logger LOGGER = LoggerFactory.getLogger(RestController.class);

    /**
     * Get JSON filtered.
     *
     * @return JSON filtered
     */
    @PostMapping("/getJson")
    public ResponseEntity<String> getJSONFiltered(@RequestBody Object jsonString, @RequestParam String values) {
        LOGGER.info("Request JSON {} : " + jsonString.toString());
        LOGGER.info("Request Values {} : " + values);

        try {
            String response = filterJsonService.projection(jsonString, values).toString();
            LOGGER.info("Response {} : " + response);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } catch (Exception e) {
            LOGGER.info("Error Parsing JSON: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error Parsing JSON: " + e.getMessage());
        }
    }
}