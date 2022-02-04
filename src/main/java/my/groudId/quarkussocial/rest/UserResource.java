package my.groudId.quarkussocial.rest;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import my.groudId.quarkussocial.domain.Repository.UserRepository;
import my.groudId.quarkussocial.domain.model.User;
import my.groudId.quarkussocial.rest.dto.CreateUserRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private  UserRepository repository;

    @Inject
    public UserResource(UserRepository repository){
        this.repository = repository;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest  userRequest ){
        User user = new User();
        user.setAge(userRequest.getAge());
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        System.out.print(user);
        repository.persist(user);

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser( @PathParam("id") Long id){
        User user = repository.findById(id);

        if(user != null){
            repository.delete(user);
            return  Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userData){
        User user = repository.findById(id);

        if(user != null){
            user.setAge(userData.getAge());
            user.setName(userData.getName());
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
