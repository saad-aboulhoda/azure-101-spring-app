package ma.aboulhoda.azure_101.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ma.aboulhoda.azure_101.entity.AppUser;
import ma.aboulhoda.azure_101.entity.Post;
import ma.aboulhoda.azure_101.entity.Vote;

public interface VoteRepo extends JpaRepository<Vote, String> {
  Optional<Vote> findByUserAndPost(AppUser user, Post post);
}
