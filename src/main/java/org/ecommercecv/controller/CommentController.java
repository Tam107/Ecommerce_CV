package org.ecommercecv.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.CommentDTO;
import org.ecommercecv.dto.response.ApiResponse;
import org.ecommercecv.model.User;
import org.ecommercecv.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Slf4j(topic = "COMMENT_CONTROLLER")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> addComment(@PathVariable Long productId,
                                                  @AuthenticationPrincipal UserDetails userDetails,
                                                  @Valid @RequestBody CommentDTO commentDTO){
        Long userId = ((User) userDetails).getId();
        CommentDTO newComment = commentService.addComment(productId, userId, commentDTO);
        return ResponseEntity.ok(new ApiResponse(201, "Comment added successfully", newComment));

    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getCommentsByProduct(@PathVariable Long productId){
        var comments = commentService.getCommentsByProduct(productId);
        return ResponseEntity.ok(new ApiResponse(200, "Comments retrieved successfully", comments));
    }

}
