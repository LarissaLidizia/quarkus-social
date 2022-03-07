package my.groudId.quarkussocial.domain.Repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import my.groudId.quarkussocial.domain.model.Post;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
