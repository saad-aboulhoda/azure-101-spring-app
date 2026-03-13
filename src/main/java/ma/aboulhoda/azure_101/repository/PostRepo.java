package ma.aboulhoda.azure_101.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import ma.aboulhoda.azure_101.entity.Post;

public interface PostRepo extends JpaRepository<Post, String> {
  List<Post> findAllByOrderByScoreDesc();
}
