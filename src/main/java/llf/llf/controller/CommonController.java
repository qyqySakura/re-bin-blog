package llf.llf.controller;

import llf.llf.common.Result;
import llf.llf.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private AvatarService avatarService;

    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                       @RequestParam("role") String role,
                                       @RequestParam("id") Integer id) throws IOException {
        String url = avatarService.uploadAvatar(file, role, id);
        return Result.success(url);
    }
}