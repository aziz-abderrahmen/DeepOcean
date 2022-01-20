/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
 * ******************************************************/
package engine;

import tools.HardCodedParameters;
import tools.User;
import tools.Position;
import tools.Sound;

import specifications.EngineService;
import specifications.DataService;
import specifications.RequireDataService;
import specifications.ShrimpService;
import specifications.SharkService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.ArrayList;

public class Engine implements EngineService, RequireDataService {
  private static final double friction = HardCodedParameters.friction, heroesStep = HardCodedParameters.heroesStep,
		  sharkStep = HardCodedParameters.sharkStep;
  private Timer engineClock;
  private DataService data;

  private User.COMMAND command;
  private Random gen;
  public boolean moveLeft, moveRight, moveUp, moveDown;
  private double heroesVX, heroesVY;

  public Engine() {
  }

  @Override
  public void bindDataService(DataService service) {
    data = service; 
  }

  @Override
  public void init() {
    engineClock = new Timer();
    command = User.COMMAND.NONE;
    gen = new Random();
    moveLeft = false;
    moveRight = false;
    moveUp = false;
    moveDown = false;
    heroesVX = 0;
    heroesVY = 0;
  }

  @Override
  public void start() {
    engineClock.schedule(new TimerTask() {

      public void run() {

    	if (data.getGameStatus()) {
    	
        if (gen.nextInt(1000) < data.getSpawnRate())
          spawnShark();
        if (data.getShrimps().size() < 3 && data.getStepNumber() % 100 == 0)
          spawnShrimp();

        updateSpeedHeroes();
        updateCommandHeroes();
        updatePositionHeroes();

        ArrayList<SharkService> sharksRight = new ArrayList<SharkService>();
        ArrayList<SharkService> sharksLeft = new ArrayList<SharkService>();

        data.setSoundEffect(Sound.SOUND.None);
        int point = 0;
        
        if (data.getStepNumber() % 100 == 0) {
        	data.setIsInvulnerable(false);
        }

        for (SharkService p : data.getSharks()) {
          moveLeft(p);

          if (collisionHeroesShark(p) && !data.getIsInvulnerable()) {
//            data.setSoundEffect(Sound.SOUND.HeroesGotHit);
            data.takeDamage(1);
            if (data.getHealthPoints() == 0) {
            	data.setGameStatus(false);
            } else {
            	data.setIsInvulnerable(true);
            }
          } else {

            if (p.getPosition().x > 0 && p.getPosition().x < HardCodedParameters.maxX)
              sharksRight.add(p);
          }
        }
        for (SharkService p : data.getSharks2()) {
          moveRight(p);

          if (collisionHeroesShark(p) && !data.getIsInvulnerable()) {
//            data.setSoundEffect(Sound.SOUND.HeroesGotHit);
            data.takeDamage(1);
            if (data.getHealthPoints() == 0) {
            	data.setGameStatus(false);
            }else {
            	data.setIsInvulnerable(true);
            }
          } else {

            if (p.getPosition().x < HardCodedParameters.maxX - 50 && p.getPosition().x > 0)
              sharksLeft.add(p);
          }
        }

        ArrayList<ShrimpService> shrimps = new ArrayList<ShrimpService>();

        for (ShrimpService f : data.getShrimps()) {
        	if (collisionHeroesShrimp(f)) {
        		data.setSoundEffect(Sound.SOUND.PhantomDestroyed);
        		point++;
        		data.restoreHealth(1);
        	} else {
        		shrimps.add(f);
        	}
        }

        data.setVitesseJeu(data.getVitesseJeu()+point);
        data.setSpawnRate(data.getSpawnRate()+point);
        if (data.getSpawnRate() > 90) data.setSpawnRate(90);
        data.addScore(point);

        data.setShrimps(shrimps);

        data.setStepNumber(data.getStepNumber() + 1);
    	} 
      }
    }, 0, HardCodedParameters.enginePaceMillis);
  }

  @Override
  public void stop() {
    engineClock.cancel();
  }

  @Override
  public void setHeroesCommand(User.COMMAND c) {
    if (c == User.COMMAND.LEFT)
      moveLeft = true;
    if (c == User.COMMAND.RIGHT)
      moveRight = true;
    if (c == User.COMMAND.UP)
      moveUp = true;
    if (c == User.COMMAND.DOWN)
      moveDown = true;
  }

  @Override
  public void releaseHeroesCommand(User.COMMAND c) {
    if (c == User.COMMAND.LEFT)
      moveLeft = false;
    if (c == User.COMMAND.RIGHT)
      moveRight = false;
    if (c == User.COMMAND.UP)
      moveUp = false;
    if (c == User.COMMAND.DOWN)
      moveDown = false;
  }

  private void updateSpeedHeroes() {
    heroesVX *= friction;
    heroesVY *= friction;
  }

  private void updateCommandHeroes() {

    if (moveLeft)
      heroesVX -= heroesStep;
    if (moveRight)
      heroesVX += heroesStep;
    if (moveUp)
      heroesVY -= heroesStep;
    if (moveDown)
      heroesVY += heroesStep;
  }

  private void updatePositionHeroes() {
    data.setHeroesPosition(new Position(data.getHeroesPosition().x + heroesVX, data.getHeroesPosition().y + heroesVY));

    if (data.getHeroesPosition().x - (HardCodedParameters.heroesWidth / (double) 3) < 0)
      data.setHeroesPosition(new Position((int) HardCodedParameters.heroesWidth / 3, data.getHeroesPosition().y));
    if (data.getHeroesPosition().x + (HardCodedParameters.heroesWidth / (double) 2) > HardCodedParameters.maxX+120)
      data.setHeroesPosition(new Position((int) HardCodedParameters.maxX +120- (int) HardCodedParameters.heroesWidth / (double) 2,data.getHeroesPosition().y));
    if (data.getHeroesPosition().y - (HardCodedParameters.heroesHeight / (double) 3) < 0)
      data.setHeroesPosition(new Position(data.getHeroesPosition().x, (int) HardCodedParameters.heroesHeight / (double) 3));
    if (data.getHeroesPosition().y + (HardCodedParameters.heroesHeight / (double) 3) > HardCodedParameters.maxY * .8)
      data.setHeroesPosition(new Position(data.getHeroesPosition().x,(int) HardCodedParameters.maxY * .8 - (int) HardCodedParameters.heroesHeight / (double) 3));

  }

  private void spawnShark() {
    int x = 0;
    int y = 0;
    boolean cont = true;
    while (cont) {
      y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6)) + HardCodedParameters.defaultHeight * .1);
      x = HardCodedParameters.defaultWidth;

      cont = false;
      for (SharkService p : data.getSharks()) {
        if (p.getPosition().equals(new Position(x, y)))
          cont = true;
      }
    }
    data.addShark(new Position(x, y));

    cont = true;
    while (cont) {
      y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6)) + HardCodedParameters.defaultHeight * .1);
      x = HardCodedParameters.minX;

      cont = false;
      for (SharkService p : data.getSharks2()) {
        if (p.getPosition().equals(new Position(x, y)))
          cont = true;
      }
    }
    data.addShark2(new Position(x, y));

  }

  private void spawnShrimp() {
    int x = 0;
    int y = 0;
    boolean cont = true;
    while (cont) {
      y = (int) (gen.nextInt((int) (HardCodedParameters.defaultHeight * .6)) + HardCodedParameters.defaultHeight * .1);
      x = (int) (gen.nextInt((int) (HardCodedParameters.defaultWidth * .6)) + HardCodedParameters.defaultWidth * .1);

      cont = false;
      for (ShrimpService p : data.getShrimps()) {
        if (p.getPosition().equals(new Position(x, y)))
          cont = true;
      }
    }
    data.addShrimp(new Position(x, y));
  }

  private void moveLeft(SharkService p) {
    p.setPosition(new Position(p.getPosition().x - (sharkStep + (data.getVitesseJeu() * .2)), p.getPosition().y));
  }

  private void moveRight(SharkService p) {
    p.setPosition(new Position(p.getPosition().x + (sharkStep + (data.getVitesseJeu() * .2)), p.getPosition().y));

  }


  private boolean collisionHeroesShrimp(ShrimpService p) {
    return (((data.getHeroesPosition().x - p.getPosition().x + 17)
        * (data.getHeroesPosition().x - p.getPosition().x + 17)
        + (data.getHeroesPosition().y - p.getPosition().y + 15)
            * (data.getHeroesPosition().y - p.getPosition().y + 15)) < (data.getHeroesWidth() * .1
                + data.getShrimpWidth()) * (data.getHeroesWidth() * .1 + data.getShrimpWidth()));

  }

  private boolean collisionHeroesShark(SharkService p) {
    return (((data.getHeroesPosition().x - p.getPosition().x - 30)
        * (data.getHeroesPosition().x - p.getPosition().x - 30)
        + (data.getHeroesPosition().y - p.getPosition().y - 60) * (data.getHeroesPosition().y - p.getPosition().y)
        - 60) < (data.getHeroesWidth() * .25 + data.getSharkWidth()) * (data.getHeroesWidth() * .25 + data.getSharkWidth()));

  }

}
