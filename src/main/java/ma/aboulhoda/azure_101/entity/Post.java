package ma.aboulhoda.azure_101.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

@Entity
@Data
@Table(name = "posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String title;

  @Column(nullable = false)
  private String imageUrl;

  private int upvotes = 0;
  private int downvotes = 0;

  @Formula("upvotes - downvotes")
  private int score;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private AppUser author;
}
