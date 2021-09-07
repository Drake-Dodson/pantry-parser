package JVExperiment.demo;

import com.sun.tools.javac.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/hello-world")
@RestController
public class HelloWorldController {

    @GetMapping
    public List<String> getHelloWorld(){
        return List.of("Hello", "World");
    }
}
