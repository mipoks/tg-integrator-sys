package design.kfu.tgintegrator.config;

import design.kfu.tgintegrator.domain.dto.CommentDTO;
import design.kfu.tgintegrator.feign.FeignMessageClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class FeignMessageClientFallback implements FeignMessageClient {


    @Override
    public CommentDTO sendComment(String auth, Long clubId, CommentDTO commentDTO) {
        return null;
    }

    @Override
    public String uploadFile(String commentId, MultipartFile file) {
        return "Upload failed";
    }
}