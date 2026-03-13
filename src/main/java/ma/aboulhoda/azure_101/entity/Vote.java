package ma.aboulhoda.azure_101.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "votes")
public class Vote {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  private AppUser user;

  @ManyToOne
  private Post post;

  private int voteValue;
}
