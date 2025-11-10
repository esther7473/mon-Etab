package ci.ada.monetabv2new.controllers;

import ci.ada.monetabv2new.models.forum.CategoryEntity;
import ci.ada.monetabv2new.models.forum.CommentEntity;
import ci.ada.monetabv2new.models.forum.TopicEntity;
import ci.ada.monetabv2new.repositories.CategoryEntityRepository;
import ci.ada.monetabv2new.repositories.CommentEntityRepository;
import ci.ada.monetabv2new.repositories.TopicEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
@RequiredArgsConstructor
public class ForumController {

    private final TopicEntityRepository topicRepo;
    private final CommentEntityRepository commentRepo;
    private final CategoryEntityRepository categoryRepo;

    @GetMapping("/categories")
    public List<CategoryEntity> getCategories() {
        return categoryRepo.findAll();
    }

    @PostMapping("/topics")
    public TopicEntity createTopic(@RequestBody TopicEntity topic) {
        return topicRepo.save(topic);
    }

    @PostMapping("/comments")
    public CommentEntity createComment(@RequestBody CommentEntity comment) {
        return commentRepo.save(comment);
    }
}