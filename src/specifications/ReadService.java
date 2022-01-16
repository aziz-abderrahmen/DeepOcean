/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/ReadService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Position;
import tools.Sound;

import java.util.ArrayList;

public interface ReadService {
  public Position getHeroesPosition();
  public double getHeroesWidth();
  public double getHeroesHeight();
  public double getShrimpWidth();
  public double getShrimpHeight();
  public double getSharkWidth();
  public double getSharkHeight();
  public int getStepNumber();
  public int getScore();
  public ArrayList<ShrimpService> getShrimps();
  public ArrayList<SharkService> getSharks();
  public ArrayList<SharkService> getSharks2();


  public Sound.SOUND getSoundEffect();
}
