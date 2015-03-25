package tutorial;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Giau on 3/25/2015.
 */
public class UsersController {
    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping("/users")
    public String users(Model model) {

        model.addAttribute("users", entityManager.createQuery("select u from User u").getResultList());

        return "users";
    }
}
