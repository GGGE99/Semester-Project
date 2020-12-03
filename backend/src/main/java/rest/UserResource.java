package rest;

import DTOs.UserDTO;
import DTOs.UserInfoDTO;
import DTOs.UserInfoListDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entities.User;
import entities.UserInfo;
import errorhandling.InvalidInputException;
import facades.UserFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import security.UserPrincipal;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade facade = UserFacade.getUserFacade(EMF);
    private static Gson GSON = new Gson();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("user")
    public String make() {
        String thisuser = securityContext.getUserPrincipal().getName();
        JsonObject obj = new JsonObject();
        obj.addProperty("name", thisuser);
        JsonArray array = new JsonArray();
        array.add("user");
        boolean s = securityContext.isUserInRole("admin");
        if (s) {
            array.add("admin");
        }
        obj.add("roles", array);

        return obj.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        JsonObject obj = new JsonObject();
        obj.addProperty("name", thisuser);
        JsonArray array = new JsonArray();
        array.add("user");
        boolean s = securityContext.isUserInRole("admin");
        if (s) {
            array.add("admin");
        }
        obj.add("roles", array);

        return obj.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        JsonObject obj = new JsonObject();
        obj.addProperty("name", thisuser);
        JsonArray array = new JsonArray();
        array.add("admin");
        boolean s = securityContext.isUserInRole("user");
        if (s) {
            array.add("user");
        }
        obj.add("roles", array);

        return obj.toString();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("changePW")
    @RolesAllowed({"admin", "user"})
    public String editPW(String jsonString) throws AuthenticationException {
        EntityManager em = EMF.createEntityManager();
        JsonObject obj = GSON.fromJson(jsonString, JsonObject.class);
        String oldPW = obj.get("oldPW").getAsString();
        String newPW = obj.get("newPW").getAsString();
        System.out.println(oldPW);
        String thisuser = securityContext.getUserPrincipal().getName();
        User albert = facade.getVeryfiedUser(thisuser, oldPW);
        albert.setUserPass(newPW);
        em.getTransaction().begin();
        em.merge(albert);
        em.getTransaction().commit();

        System.out.println(GSON.toJson(new UserDTO(albert)));

        return GSON.toJson(new UserDTO(albert));
    }
    
    @POST
    @Path("favorites")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public String makeUserInfo(String userInfoString) throws InvalidInputException {
        String thisuser = securityContext.getUserPrincipal().getName();
        UserInfoDTO userInfoDTO = GSON.fromJson(userInfoString, UserInfoDTO.class);
        EntityManager em = EMF.createEntityManager();
        System.out.println(userInfoString);
        User user = null;

        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userName = :userName", User.class);
            query.setParameter("userName", thisuser);
            user = query.getSingleResult();
            UserInfo userInfo = user.getUserInfo();

            if (userInfo == null) {
                userInfo = new UserInfo();
                user.setUserInfo(userInfo);
            }

            userInfo.setInfo(userInfoDTO);
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {

        }

        return GSON.toJson(new UserDTO(user));
    }
    
     @GET
    @Path("favorites")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin", "user"})
    public String getUserInfo() throws InvalidInputException {
        EntityManager em = EMF.createEntityManager();
        String name = securityContext.getUserPrincipal().getName();
            TypedQuery<UserInfo> query = em.createQuery("SELECT u FROM User u ", UserInfo.class);

            List<UserInfo> users = query.getResultList();
            UserInfoListDTO usersDTO = new UserInfoListDTO(users);
            return GSON.toJson(usersDTO);
}
}
