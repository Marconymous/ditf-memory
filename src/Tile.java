import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Tile {
  private ImageView imageView;
  private Image front;

  public Tile(ImageView imageView, Image front) {
    this.imageView = imageView;
    this.front = front;
  }

  public Tile(){}

  public ImageView getImageView() {
    return imageView;
  }

  public void setImageView(ImageView imageView) {
    this.imageView = imageView;
  }

  public Image getFront() {
    return front;
  }

  public void setFront(Image front) {
    this.front = front;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tile tile = (Tile) o;
    return Objects.equals(front, tile.front);
  }

  @Override
  public int hashCode() {
    return Objects.hash(imageView, front);
  }
}
