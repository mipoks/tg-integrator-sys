package design.kfu.tgintegrator.feign;

import design.kfu.tgintegrator.config.FeignConfig;
import design.kfu.tgintegrator.config.FeignMessageClientFallback;
import design.kfu.tgintegrator.domain.dto.CommentDTO;
import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.HeaderParam;
import java.io.File;

@FeignClient(value = "tgMessage", url = "http://localhost:8081/",
fallback = FeignMessageClientFallback.class)
public interface FeignMessageClient {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/club/{club_id}/comment", headers = "Content-Type=application/json")
    CommentDTO sendComment(@RequestHeader("Authorization") String auth,
                     @PathVariable("club_id") Long clubId,
                     @RequestBody CommentDTO commentDTO);


    @RequestMapping(method = RequestMethod.POST, value = "/image/comment/{comment_id}", headers = "Content-Type=multipart/form-data")
//    @Headers({"Content-Type: multipart/form-data", "X-DATA_PR: dsadasda"})
    String uploadFile(@PathVariable("comment_id") String commentId, @RequestPart("file") MultipartFile file);

}
