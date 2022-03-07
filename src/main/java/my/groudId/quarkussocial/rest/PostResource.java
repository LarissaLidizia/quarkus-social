package my.groudId.quarkussocial.rest;


import io.quarkus.panache.common.Sort;
import my.groudId.quarkussocial.domain.Repository.FollowerRepository;
import my.groudId.quarkussocial.domain.Repository.PostRepository;
import my.groudId.quarkussocial.domain.Repository.UserRepository;
import my.groudId.quarkussocial.domain.model.Post;
import my.groudId.quarkussocial.domain.model.User;
import my.groudId.quarkussocial.rest.dto.CreatePostRequest;
import my.groudId.quarkussocial.rest.dto.PostResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;

    @Inject
    public PostResource (
            UserRepository userRepository,
            PostRepository postRepository, FollowerRepository followerRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request){
        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);
        //post.setDateTime(LocalDateTime.now());

        postRepository.persist(post);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(
            @PathParam("userId") Long userId,
            @HeaderParam("followerId") Long followerId){
        User user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(followerId == null){
           return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("You forgot the header followerId")
                    .build();
        }

        User follower =  userRepository.findById(followerId);

        if(follower ==  null){
            return  Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Inexistent followerId")
                    .build();
        }
        boolean follows = followerRepository.follows(follower, user);
        if(!follows){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You can't see these posts")
                    .build();
        }


        var query = postRepository.find(
                "user", Sort.by("dataTime", Sort.Direction.Descending), user);
        var list = query.list();

        var postResponseList= list.stream()
               // .map(post -> PostResponse.fromEntity(post))
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
