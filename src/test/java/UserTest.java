import org.hibernate.LazyInitializationException;
import tutorial.Role;
import tutorial.User;
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

    @Test
    public void testNewUserAndAddRole() {

        EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();

        entityManager.getTransaction().begin();

        User user = new User();

        user.setName(Long.toString(new Date().getTime()));

        Role role = new Role();

        role.setName(Long.toString(new Date().getTime()));

        entityManager.persist(user);
        entityManager.persist(role);

        entityManager.getTransaction().commit();


        Assert.assertEquals(0, user.getRoles().size());


        entityManager.getTransaction().begin();

        user.addRole(role);

        entityManager.merge(user);

        entityManager.getTransaction().commit();


        Assert.assertEquals(1, user.getRoles().size());


        entityManager.close();
    }

    @Test
    public void testFindUser() throws Exception {

        EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();

        entityManager.getTransaction().begin();

        User user = new User();

        String name = Long.toString(new Date().getTime());

        user.setName(name);

        Role role = new Role();

        role.setName(name);

        user.addRole(role);

        entityManager.persist(role);
        entityManager.persist(user);

        entityManager.getTransaction().commit();

        entityManager.close();

        entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();

        User foundUser = entityManager.createNamedQuery("User.findByName", User.class).setParameter("name", name)
                .getSingleResult();

        System.out.println(foundUser);

        Assert.assertEquals(name, foundUser.getName());

        Assert.assertEquals(1, foundUser.getRoles().size());

        System.out.println(foundUser.getRoles().getClass());

        entityManager.close();
    }

    @Test(expected = LazyInitializationException.class)
    public void testFindUser1() throws Exception {

        EntityManager entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();

        entityManager.getTransaction().begin();

        User user = new User();

        String name = Long.toString(new Date().getTime());

        user.setName(name);

        Role role = new Role();

        role.setName(name);

        user.addRole(role);

        entityManager.persist(role);
        entityManager.persist(user);

        entityManager.getTransaction().commit();

        entityManager.close();

        entityManager = Persistence.createEntityManagerFactory("tutorialPU").createEntityManager();

        User foundUser = entityManager.createNamedQuery("User.findByName", User.class).setParameter("name", name)
                .getSingleResult();

        entityManager.close();

        Assert.assertEquals(1, foundUser.getRoles().size());
    }
}
