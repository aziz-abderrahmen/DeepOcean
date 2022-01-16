
package data.ia;

import tools.Position;

import specifications.ShrimpService;

public class Shrimp implements ShrimpService {
  private Position position;

  public Shrimp(Position p) {
    position = p;
  }

  @Override
  public Position getPosition() {
    return position;
  }

  @Override
  public ShrimpService.MOVE getAction() {
    return ShrimpService.MOVE.LEFT;
  }

  @Override
  public void setPosition(Position p) {
    position = p;
  }
}
