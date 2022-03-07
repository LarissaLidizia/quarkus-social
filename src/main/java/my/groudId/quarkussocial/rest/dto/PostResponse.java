package my.groudId.quarkussocial.rest.dto;

import lombok.Data;
import my.groudId.quarkussocial.domain.model.Post;

import javax.ws.rs.POST;
import java.time.LocalDateTime;

@Data
public class PostResponse {
    private String text;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post){
        PostResponse postResponse = new PostResponse();
        postResponse.setText(post.getText());
        postResponse.setDateTime(post.getDataTime());
        return postResponse;
    }
}
