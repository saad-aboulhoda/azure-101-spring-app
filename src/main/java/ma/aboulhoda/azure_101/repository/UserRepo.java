package ma.aboulhoda.azure_101.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ma.aboulhoda.azure_101.entity.AppUser;
import java.util.Optional;

public interface UserRepo extends JpaRepository<AppUser, String> {
  Optional<AppUser> findByUsername(String username);
}