/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/Data.java 2015-03-11 buixuan.
 * ******************************************************/
package data;

import tools.HardCodedParameters;
import tools.Position;
import tools.Sound;

import specifications.DataService;
import specifications.ShrimpService;
import specifications.SharkService;


import data.ia.Shrimp;
import data.ia.MoveLeftShark;


import java.util.ArrayList;

public class Data implements DataService{
  //private Heroes hercules;
  private Position heroesPosition;
  private int stepNumber, score;
  private ArrayList<ShrimpService> shrimps;
  private ArrayList<SharkService> sharks;
  private ArrayList<SharkService> sharks2;



  private double heroesWidth,heroesHeight,shrimpWidth,shrimpHeight,sharkWidth,sharkHeight;
  private Sound.SOUND sound;
  double minX,maxX,minY,maxY;
  

  public Data(){}

  @Override
  public void init(){
    //hercules = new Heroes;
    heroesPosition = new Position(HardCodedParameters.heroesStartX,HardCodedParameters.heroesStartY);
    shrimps = new ArrayList<ShrimpService>();
    sharks = new ArrayList<SharkService>();
    sharks2 = new ArrayList<SharkService>();


    stepNumber = 0;
    score = 0;
    heroesWidth = HardCodedParameters.heroesWidth;
    heroesHeight = HardCodedParameters.heroesHeight;
    shrimpWidth = HardCodedParameters.shrimpWidth;
    shrimpHeight = HardCodedParameters.shrimpHeight;
    sharkWidth = HardCodedParameters.sharkWidth;
    sharkHeight = HardCodedParameters.sharkHeight;
    sound = Sound.SOUND.None;
    minX = HardCodedParameters.minX;
    maxX = HardCodedParameters.maxX;
    minY = HardCodedParameters.minY;
    maxY = HardCodedParameters.maxY;


  }

  @Override
  public Position getHeroesPosition(){ return heroesPosition; }
  
  @Override
  public double getHeroesWidth(){ return heroesWidth; }
  
  @Override
  public double getHeroesHeight(){ return heroesHeight; }
  
  @Override
  public double getShrimpWidth(){ return shrimpWidth; }
  
  @Override
  public double getShrimpHeight(){ return shrimpHeight; }

  @Override
  public double getSharkWidth(){ return sharkWidth; }
  
  @Override
  public double getSharkHeight(){ return sharkHeight; }

  @Override
  public int getStepNumber(){ return stepNumber; }
  
  @Override
  public int getScore(){ return score; }

  @Override
  public ArrayList<ShrimpService> getShrimps(){ return shrimps; }
  
  @Override
  public ArrayList<SharkService> getSharks(){ return sharks; }
  
  @Override
  public ArrayList<SharkService> getSharks2(){ return sharks2; }
  
  @Override
  public Sound.SOUND getSoundEffect() { return sound; }

  @Override
  public void setHeroesPosition(Position p) { heroesPosition=p; }
  
  @Override
  public void setStepNumber(int n){ stepNumber=n; }



  @Override
  public void addScore(int score){ this.score+=score; }

  @Override
  public void addShrimp(Position p) { shrimps.add(new Shrimp(p)); }
  
  @Override
  public void setShrimps(ArrayList<ShrimpService> shrimps) { this.shrimps=shrimps; }
  
    
  @Override
  public void addShark(Position p) { sharks.add(new MoveLeftShark(p)); }
  
  @Override
  public void addShark2(Position p) { sharks.add(new MoveLeftShark(p)); }
  

  
  
  
  @Override
  public void setSoundEffect(Sound.SOUND s) { sound=s; }
}
