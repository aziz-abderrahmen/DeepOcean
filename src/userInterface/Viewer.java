/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: userInterface/Viewer.java 2015-03-11 buixuan.
 * ******************************************************/
package userInterface;

import javafx.scene.paint.LinearGradient;
import tools.HardCodedParameters;
import tools.User;



import specifications.ViewerService;
import specifications.ReadService;
import specifications.RequireReadService;
import specifications.ShrimpService;
import specifications.SharkService;


import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

import java.util.ArrayList;

public class Viewer implements ViewerService, RequireReadService{
  private static final int spriteSlowDownRate=HardCodedParameters.spriteSlowDownRate;
  private static final double defaultMainWidth=HardCodedParameters.defaultWidth,
                              defaultMainHeight=HardCodedParameters.defaultHeight;
  private ReadService data;
  private ReadService data2;

  private ImageView heroesAvatar;
  private ImageView sharkAvatar;
  private ImageView sharkAvatarR;




  private ArrayList<Rectangle> heroesAvatarViewports;
  private ArrayList<Circle> sharkAvatarViewports;

  private ArrayList<Integer> heroesAvatarXModifiers;
  private ArrayList<Integer> heroesAvatarYModifiers;
  private ArrayList<Integer> sharkAvatarXModifiers;
  private ArrayList<Integer> sharkAvatarYModifiers;
  private int heroesAvatarViewportIndex;
  private int sharkAvatarViewportIndex;

  private double xShrink,yShrink,shrink,xModifier,yModifier,heroesScale,sharkScale;
  private boolean moveLeft,moveRight,moveUp,moveDown;
  Image avatarRight=new Image("file:src/images/clown-fish-r.png");
  Image avatarLeft=new Image("file:src/images/clown-fish.png");
  Image avatarUp=new Image("file:src/images/clown-fish-up.png");
  Image avatarDown=new Image("file:src/images/clown-fish-down.png");
  Image sharkImg=new Image("file:src/images/shark.png");
  Image sharkImgR=new Image("file:src/images/sharki.png");



  public Viewer(){}
  
 @Override
  public void bindReadService(ReadService service){
    data=service;
  }


  @Override
  public void init(){
    xShrink=1;
    yShrink=1;
    xModifier=0;
    yModifier=0;
    moveLeft = false;
    moveRight = false;
    moveUp = false;
    moveDown = false;

    //Yucky hard-conding
 

    //heroesSpriteSheet =  cyclope;
 
    heroesAvatar = new ImageView(avatarRight);
    sharkAvatar = new ImageView(sharkImg);
    sharkAvatarR = new ImageView(sharkImgR);



    //heroesAvatar = new Circle(20,  Color.rgb(255,238,0));
    //heroesAvatar.setEffect(new Lighting());
    heroesAvatar.setTranslateX(data.getHeroesPosition().x);
    heroesAvatar.setTranslateY(data.getHeroesPosition().y);

    heroesAvatarViewports = new ArrayList<Rectangle>();
    heroesAvatarXModifiers = new ArrayList<Integer>();
    heroesAvatarYModifiers = new ArrayList<Integer>();

    heroesAvatarViewportIndex=0;

    sharkAvatarViewports = new ArrayList<Circle>();
    sharkAvatarXModifiers = new ArrayList<Integer>();
    sharkAvatarYModifiers = new ArrayList<Integer>();

    sharkAvatarViewportIndex=0;
    


    heroesAvatarViewports.add(new Rectangle(HardCodedParameters.heroesHeight,HardCodedParameters.heroesWidth,  Color.rgb(255,238,0)));

    heroesAvatarXModifiers.add(0);heroesAvatarYModifiers.add(0);
    double radius=.5*Math.min(shrink*data.getSharkWidth(),shrink*data.getSharkHeight());

    sharkAvatarViewports.add(new Circle(radius+2000,  Color.rgb(255,238,0)));

    sharkAvatarXModifiers.add(0);sharkAvatarYModifiers.add(0);

  }

  @Override
  public Parent getPanel(){
    if(moveLeft ){
        heroesAvatar = new ImageView(avatarLeft);
    }
    if(moveRight ){
        heroesAvatar = new ImageView(avatarRight);
    }
    if(moveUp ){
        heroesAvatar = new ImageView(avatarUp);
    } 
    if(moveDown ){
        heroesAvatar = new ImageView(avatarDown);
    }
    shrink=Math.min(xShrink,yShrink);
    xModifier=.01*shrink*defaultMainHeight;
    yModifier=.01*shrink*defaultMainHeight;

    //Yucky hard-conding
    Stop[] stops = new Stop[] { new Stop(0, Color.rgb(137, 212, 207)), new Stop(1, Color.rgb( 116, 74, 232))};
    LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
    Rectangle map = new Rectangle(-2*xModifier+shrink*defaultMainWidth+130,
                                  -.2*shrink*defaultMainHeight+shrink*defaultMainHeight);
    map.setFill(lg1);
    map.setStroke(Color.rgb(217, 83, 79));
    map.setStrokeWidth(.01*shrink*defaultMainHeight);
    map.setArcWidth(.04*shrink*defaultMainHeight);
    map.setArcHeight(.04*shrink*defaultMainHeight);
    map.setTranslateX(xModifier);
    map.setTranslateY(yModifier);
    map.setOpacity(20);
    
    Text greets = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
                           -0.1*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "\n DEEP OCEAN");
    greets.setFont(new Font(.05*shrink*defaultMainHeight));
    
    greets.setFill(Color.rgb(255, 238, 173));

    
    Text score = new Text(-0.1*shrink*defaultMainHeight+.5*shrink*defaultMainWidth,
                           -0.05*shrink*defaultMainWidth+shrink*defaultMainHeight,
                           "\n Earnings : "+data.getScore());
    score.setFont(new Font(.05*shrink*defaultMainHeight));
    score.setFill(Color.rgb(255, 238, 173));

    
    int index=heroesAvatarViewportIndex/spriteSlowDownRate;
    heroesScale=data.getHeroesHeight()*shrink/heroesAvatarViewports.get(index).getHeight();
    sharkScale=data.getSharkHeight()*shrink/sharkAvatarViewports.get(index).getRadius();
    heroesAvatar.setFitHeight(data.getHeroesHeight()*shrink);
    heroesAvatar.setPreserveRatio(true);
    heroesAvatar.setTranslateX(shrink*data.getHeroesPosition().x+
                               shrink*xModifier+
                               -heroesScale*0.5*heroesAvatarViewports.get(index).getHeight()+
                               shrink*heroesScale*heroesAvatarXModifiers.get(index)
                              );
    heroesAvatar.setTranslateY(shrink*data.getHeroesPosition().y+
                               shrink*yModifier+
                               -heroesScale*0.5*heroesAvatarViewports.get(index).getWidth()+
                               shrink*heroesScale*heroesAvatarYModifiers.get(index)
                              );
    heroesAvatarViewportIndex=(heroesAvatarViewportIndex+1)%(heroesAvatarViewports.size()*spriteSlowDownRate);

    sharkAvatarViewportIndex=(sharkAvatarViewportIndex+1)%(sharkAvatarViewports.size()*spriteSlowDownRate);
//

    double healthBarScale = data.getHeroesHeight()*shrink/heroesAvatarViewports.get(index).getHeight();
    Rectangle healthBarLose = new Rectangle(90 * healthBarScale, 10*healthBarScale);
    healthBarLose.setFill(Color.RED);
    healthBarLose.setTranslateX(shrink*data.getHeroesPosition().x+
            shrink*xModifier+
            -heroesScale*0.5*heroesAvatarViewports.get(index).getWidth()+
            shrink*heroesScale*heroesAvatarXModifiers.get(index) - 5*healthBarScale
           );
    healthBarLose.setTranslateY(shrink*data.getHeroesPosition().y+
            shrink*yModifier+
            -heroesScale*0.5*heroesAvatarViewports.get(index).getHeight()+
            shrink*heroesScale*heroesAvatarYModifiers.get(index) + 90*healthBarScale);
    
    Rectangle healthBar = new Rectangle(data.getHealthPoints()*30 * healthBarScale, 10*healthBarScale);
    healthBar.setFill(Color.GREEN);
    healthBar.setTranslateX(shrink*data.getHeroesPosition().x+
            shrink*xModifier+
            -heroesScale*0.5*heroesAvatarViewports.get(index).getWidth()+
            shrink*heroesScale*heroesAvatarXModifiers.get(index) - 5*healthBarScale
           );
    healthBar.setTranslateY(shrink*data.getHeroesPosition().y+
            shrink*yModifier+
            -heroesScale*0.5*heroesAvatarViewports.get(index).getHeight()+
            shrink*heroesScale*heroesAvatarYModifiers.get(index) + 90*healthBarScale);
    
    Group panel = new Group();
    panel.getChildren().addAll(map,greets,score,heroesAvatar, healthBarLose, healthBar);
    
    ArrayList<ShrimpService> shrimps = data.getShrimps();
    ShrimpService f;


    for (int j=0; j<shrimps.size();j++){
      f=shrimps.get(j);
      double radius=.5*Math.min(shrink*data.getSharkWidth(),shrink*data.getSharkHeight());

      Circle shrimpAvatar = new Circle(radius+1,Color.YELLOW);
      shrimpAvatar.setEffect(new Lighting());
      shrimpAvatar.setTranslateX(shrink*f.getPosition().x+shrink*xModifier-radius);
      shrimpAvatar.setTranslateY(shrink*f.getPosition().y+shrink*yModifier-radius);
      panel.getChildren().add(shrimpAvatar);
    }

    ArrayList<SharkService> sharks = data.getSharks();
    ArrayList<SharkService> sharks2 = data.getSharks2();

    SharkService p;
    SharkService p2;


    for (int i=0; i<sharks.size();i++){
      p=sharks.get(i);
      p2=sharks2.get(i);

      

      sharkAvatar = new ImageView(sharkImgR);
      sharkAvatar.setFitHeight(data.getSharkHeight()*shrink*3);
      sharkAvatar.setPreserveRatio(true);
      sharkAvatar.setTranslateX(shrink*p2.getPosition().x+
              shrink*xModifier+
              -sharkScale*0.5*sharkAvatarViewports.get(index).getRadius()+
              shrink*sharkScale*sharkAvatarXModifiers.get(index)
             );
sharkAvatar.setTranslateY(shrink*p2.getPosition().y+
              shrink*yModifier+
              -sharkScale*0.5*sharkAvatarViewports.get(index).getRadius()+
              shrink*sharkScale*sharkAvatarYModifiers.get(index)
             );



      panel.getChildren().add(sharkAvatar);
      
      sharkAvatar = new ImageView(sharkImg);
      sharkAvatar.setFitHeight(data.getSharkHeight()*shrink*3);
      sharkAvatar.setPreserveRatio(true);
      sharkAvatar.setTranslateX(shrink*p.getPosition().x+
              shrink*xModifier+
              -sharkScale*0.5*sharkAvatarViewports.get(index).getRadius()+
              shrink*sharkScale*sharkAvatarXModifiers.get(index)
             );
sharkAvatar.setTranslateY(shrink*p.getPosition().y+
              shrink*yModifier+
              -sharkScale*0.5*sharkAvatarViewports.get(index).getRadius()+
              shrink*sharkScale*sharkAvatarYModifiers.get(index)
             );



      panel.getChildren().add(sharkAvatar);
      
    }
    



    return panel;
  
  }

  @Override
  public void setMainWindowWidth(double width){
    xShrink=width/defaultMainWidth;
  }
  
  @Override
  public void setMainWindowHeight(double height){
    yShrink=height/defaultMainHeight;
  }


  @Override
  public void setHeroesCommand(User.COMMAND c){
    if (c==User.COMMAND.LEFT) moveLeft=true;
    if (c==User.COMMAND.RIGHT) moveRight=true;
    if (c==User.COMMAND.UP) moveUp=true;
    if (c==User.COMMAND.DOWN) moveDown=true;
  }
  
  @Override
  public void releaseHeroesCommand(User.COMMAND c){
    if (c==User.COMMAND.LEFT) moveLeft=false;
    if (c==User.COMMAND.RIGHT) moveRight=false;
    if (c==User.COMMAND.UP) moveUp=false;
    if (c==User.COMMAND.DOWN) moveDown=false;
  }

}
