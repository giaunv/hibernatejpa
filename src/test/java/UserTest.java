import model.User;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Date;

/**
 * Created by Giau on 3/22/2015.
 */
public class UserTest extends AbstractTest {
    @Test
    public void testNewUser() {

        EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();

        entityManager.getTransaction().begin();

        User user = new User();

        user.setName(Long.toString(new Date().getTime()));

        entityManager.persist(user);

        entityManager.getTransaction().commit();

        // see that the ID of the user was set by Hibernate
        System.out.println("user=" + user + ", user.id=" + user.getId());

        User foundUser = entityManager.find(User.class, user.getId());

        // note that foundUser is the same instance as user and is a concrete class (not a proxy)
        System.out.println("foundUser=" + foundUser);

        Assert.assertEquals(user.getName(), foundUser.getName());

        entityManager.close();
    }

    @Test(expected = Exception.class)
    public void testNewUserWithTxn() throws Exception {

        EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();

        entityManager.getTransaction().begin();
        try {
            User user = new User();

            user.setName(Long.toString(new Date().getTime()));

            entityManager.persist(user);

            if (true) {
                throw new Exception();
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }

        entityManager.close();
    }
}
