package ma.enset.gestioncontrats.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String home() {
        return "Gestion Contrats API is running";
    }
}