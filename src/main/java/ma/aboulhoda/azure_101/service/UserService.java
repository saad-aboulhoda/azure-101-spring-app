package ma.aboulhoda.azure_101.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ma.aboulhoda.azure_101.entity.AppUser;
import ma.aboulhoda.azure_101.repository.UserRepo;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepo userRepo;

  public Optional<AppUser> findByUsername(String username) {
    return userRepo.findByUsername(username);
  }

  public AppUser save(AppUser user) {
    return userRepo.save(user);
  }

}
