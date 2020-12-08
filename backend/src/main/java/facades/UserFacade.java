package facades;

import DTOs.UserDTO;
import DTOs.UserInfoDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Role;
import entities.User;
import entities.UserInfo;
import errorhandling.InvalidInputException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private UserFacade() {
    }

    
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;
    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public UserDTO addUser(UserDTO userDTO) throws InvalidInputException {
        EntityManager em = emf.createEntityManager();
        String name = null;
        try {
            Query query = em.createQuery("SELECT u.userName FROM User u WHERE u.userName = :name");
            query.setParameter("name", userDTO.getName());
            name = (String) query.getSingleResult();
        } catch (Exception e) {}

        if (name != null) {
            throw new InvalidInputException(String.format("The name %s is already taken", name));
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO("", "");
        
        User user = new User(userDTO.getName(), userDTO.getPassword());
        for (String role : userDTO.getRoles()) {
            user.addRole(new Role(role));
        }
        user.getUserInfo();
        user.setUserInfo(new UserInfo().setInfo(userInfoDTO));
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        return new UserDTO(user);
    }
    
    


}
