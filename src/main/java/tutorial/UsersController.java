package tutorial;

/**
 * Created by Giau on 3/23/2015.
 */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UsersController {

    @RequestMapping("/users")
    public String users(Model model) {
        return "users";
    }
}
