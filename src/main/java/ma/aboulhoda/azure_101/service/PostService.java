package ma.aboulhoda.azure_101.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.aboulhoda.azure_101.entity.AppUser;
import ma.aboulhoda.azure_101.entity.Post;
import ma.aboulhoda.azure_101.entity.Vote;
import ma.aboulhoda.azure_101.repository.PostRepo;
import ma.aboulhoda.azure_101.repository.VoteRepo;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepo postRepo;
  private final VoteRepo voteRepo;
  private final AzureBlobService azureBlobService;

  public List<Post> findAllByOrderByScoreDesc() {
    return postRepo.findAllByOrderByScoreDesc();
  }

  public Post createPost(String title, MultipartFile file, AppUser author) throws IOException {
    String imageUrl = azureBlobService.uploadImage(file);

    Post post = new Post();
    post.setTitle(title);
    post.setImageUrl(imageUrl);
    post.setAuthor(author);
    return postRepo.save(post);
  }

  @Transactional
  public void vote(String postId, AppUser user, boolean isUpvote) {
    Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
    int newVoteValue = isUpvote ? 1 : -1;

    Optional<Vote> existingVoteOpt = voteRepo.findByUserAndPost(user, post);

    if (existingVoteOpt.isPresent()) {
      Vote existingVote = existingVoteOpt.get();
      if (existingVote.getVoteValue() == newVoteValue) {
        return;
      }

      if (isUpvote) {
        post.setUpvotes(post.getUpvotes() + 1);
        post.setDownvotes(post.getDownvotes() - 1);
      } else {
        post.setDownvotes(post.getDownvotes() + 1);
        post.setUpvotes(post.getUpvotes() - 1);
      }
      existingVote.setVoteValue(newVoteValue);
      voteRepo.save(existingVote);
    } else {
      // New vote
      Vote vote = new Vote();
      vote.setUser(user);
      vote.setPost(post);
      vote.setVoteValue(newVoteValue);
      voteRepo.save(vote);

      if (isUpvote)
        post.setUpvotes(post.getUpvotes() + 1);
      else
        post.setDownvotes(post.getDownvotes() + 1);
    }
    postRepo.save(post);
  }

}
