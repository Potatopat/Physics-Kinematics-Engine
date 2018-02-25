import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.applet.*;
import java.text.*;

/*
This program was created by Patrick Bartman of Harry D. Jacobs 
High School.
This program is intended for personal use and may only be shared 
with Mr. Maita and all others that he or the programmer sees as
worthy.
*/

public class PhysicsBouncyBall extends Applet implements KeyListener, Runnable, MouseListener, MouseMotionListener, ActionListener
{
    Random generator = new Random();
    Thread mythread;
    public int letter;
    public boolean game = true, kevin = false;
    public int screenX = 1200, screenY = 850, speed = 3, x = 100, y = 200, mx=-1, my = 0;
    public double width = 50, height = 50;
    public String word = "";
    public Image img;
    public Graphics buffer;
    
    public Ball[] balls = new Ball[1];
    public Ball[] tempBalls;
    public Ball[][] previousBalls = new Ball[balls.length][20];
    
    public DecimalFormat fmt = new DecimalFormat("0.##");
    public Label gravityLabel, massLabel, bounceLabel, cofLabel, ballSizeLabel, space;
    public TextField gravity, mass, bounce, cof, ballSize;
    public Button update, newBall;
    
    //public String[] colors = {"Black", "Blue", "Cyan", "Dark Gray", "Gray", "Light Grey", "Green", "Light Green", "Magenta", "Orange", "Pink", "Red", "White", "Yellow"};
    
    public double startX, startY, stopX, stopY, ballXLeft, ballYLeft, ballXRight, ballYRight;
    public long startTime, nowTime;
    public double time, distance, velocity, distanceX, distanceY, velocityX, velocityY, keY, keX; //100 units = 1 meter
    public boolean setStart = true;
    public double f = 0;
    public final int FPS = 10;    //1000 = 1 second
    public boolean up=false, left=false;
    public double b = 0;
    public double GRAVITY = 0.00981, MASS = 1, BOUNCE = 0.8, COF = 0.62, FRICTION = COF * (MASS * (GRAVITY * 1000)); //move when menu is made
    public double[] previousX = new double[20];
    public double[] previousY = new double[previousX.length];
    public double colorAdd = 255 / previousX.length;
    public boolean moveableX = true, moveableY = true;
    public double velocityCheck = 0;
    
    public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    public Dimension dm = getSize();
    
    public int mode = 0;    //change to 0 when menu is made
    public final int MENU = 0, THROW = 1, MOVE = 2;
    
    public Color color = Color.blue;
    
    public void init()
    {
        screenX = d.width - 15;
        screenY = d.height - 120;
        
        setBackground (Color.lightGray);
        setSize(screenX, screenY);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        img = createImage(screenX, screenY);
        buffer = img.getGraphics();
        
        gravity = new TextField ((fmt.format(GRAVITY * 1000)));
        mass = new TextField ((fmt.format(MASS)));
        bounce = new TextField ((fmt.format((BOUNCE * 100))));
        cof = new TextField ((fmt.format(COF)));
        ballSize = new TextField ((fmt.format(((double)width / 100))));
        
        gravityLabel = new Label ("Gravity (m/s/s): ");
        massLabel = new Label ("Mass (kg): ");
        bounceLabel = new Label ("Bounceability (%): ");
        cofLabel = new Label ("Coefficient of Friction: ");
        ballSizeLabel = new Label ("Ball Diameter (m): ");
        space = new Label ("    ");
        
        update = new Button ("Update");
                
        update.addActionListener(this);
        
                
        add(gravityLabel);
        add(gravity);
        add(massLabel);
        add(mass);
        add(bounceLabel);
        add(bounce);
        add(cofLabel);
        add(cof);
        add(ballSizeLabel);
        add(ballSize);
        add(space);
        add(update);
        
        //newBall.addActionListener(this);
        
        mx = x;
        my = 100;
        
        balls[0] = new Ball(25.0, 1.0, .8, 90.0, Color.blue);
        balls[0].ballMiddle(25, 60);
        
        ballYLeft = 35;
        ballYRight = 35 + height;
        
        for (int i = 0; i < previousY.length; i++)
        {
            previousY[i] = 35;
        }
        
        for (int i = 0; i < previousBalls[balls.length - 1].length; i++)
        {
            previousBalls[balls.length - 1][i] = new Ball((width / 2), new Color((((int)colorAdd * i)), ((int)colorAdd * i), (255 - ((int)colorAdd * i))));
            previousBalls[balls.length - 1][i].ballMiddle(25, 60);
        }
        
        nowTime = System.nanoTime();
    }

    public void start() 
    {
        mythread = new Thread(this);
        mythread.start();
    }

    public void run()
    {
        while(game == true)
        {        
            try 
            {
                Thread.sleep(speed); 
            } 
            catch (InterruptedException e) { ; }
            repaint();
        }
    }
    
    public void mouseClicked (MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        
        switch (mode)
        {
            case MENU:
            {
                if (my < screenY)
                {
                    mode = THROW;
                    repaint();
                }
                break;
            }
            case THROW:
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if (setStart)
                {
                    startX = e.getX();
                    startY = e.getY();
                    startTime = System.nanoTime();
                    setStart = false;
                }
                repaint();
                break;
            }
            case MOVE:
            {
                if (my < screenY)
                {
                    setStart = true;
                    moveableX = true;
                    moveableY = true;
                    f = 0;
                    mode = THROW;
                    repaint();
                }
                break;
            }
        }
    }               
    public void mouseReleased (MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        
        switch (mode)
        {
            case MENU:
            {
                if (my < screenY)
                {
                    mode = THROW;
                    repaint();
                }
                break;
            }
            case THROW:
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                stopX = e.getX();
                stopY = e.getY();
                nowTime = System.nanoTime();
                time = ((double)nowTime - (double)startTime) / 1000000000;
                distanceX = (double)stopX - (double)startX;
                distanceX /= 100;
                distanceY= (double)stopY - (double)startY;
                distanceY /= 100;
                balls[(balls.length - 1)].findVelocity(distanceX, distanceY, time);
                balls[(balls.length - 1)].ballMiddle((int)stopX, (int)stopY);
                if (balls[(balls.length - 1)].getVelocityX() < 0)
                {
                    left = true;
                }
                if (balls[(balls.length - 1)].getVelocityY() < 0)
                {
                    up = true;
                }
                mode = MOVE;
                repaint();
                break;
            }
            case MOVE:
            {
                break;
            }
        }
    }
    public void mouseEntered (MouseEvent e){  }
    public void mouseExited (MouseEvent e){  }      
    public void mousePressed (MouseEvent e){  }
    public void mouseDragged (MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        
        switch (mode)
        {
            case MENU:
            {
                if (my < screenY)
                {
                    mode = THROW;
                    repaint();
                }
                break;
            }
            case THROW:
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if (setStart)
                {
                    startX = e.getX();
                    startY = e.getY();
                    startTime = System.nanoTime();
                    setStart = false;
                }
                repaint();
                break;
            }
            case MOVE:
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (my < screenY)
                {
                    setStart = true;
                    moveableX = true;
                    moveableY = true;
                    f = 0;
                    mode = THROW;
                    repaint();
                }
                break;
            }
        }
    }  
    public void mouseMoved (MouseEvent e){  }
    public void keyPressed(KeyEvent e)
    {  
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            String gravityText, massText, bounceText, cofText, ballSizeText;
            
            gravityText = gravity.getText();
            massText = mass.getText();
            bounceText = bounce.getText();
            cofText = cof.getText();
            ballSizeText = ballSize.getText();
            
            GRAVITY = Double.parseDouble(gravityText);
            GRAVITY /= 1000;
            
            MASS = Double.parseDouble(massText);
            
            BOUNCE = Double.parseDouble(bounceText);
            BOUNCE /= 100;
            
            COF = Double.parseDouble(cofText);
            
            width = Double.parseDouble(ballSizeText);
            width *= 100;
            height = width;
            
            ballXRight = ballXLeft + width;
            ballYRight = ballYLeft + height;
            
            FRICTION = COF * (MASS * (GRAVITY * 1000));
            
            update.addActionListener(this);
            
            update.addActionListener(this);
            
            mode = MOVE;
        }
    }
    public void keyReleased(KeyEvent e){  }
    public void keyTyped(KeyEvent e){  }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == update)
        {
            String gravityText, massText, bounceText, cofText, ballSizeText;
            
            gravityText = gravity.getText();
            massText = mass.getText();
            bounceText = bounce.getText();
            cofText = cof.getText();
            ballSizeText = ballSize.getText();
            
            GRAVITY = Double.parseDouble(gravityText);
            GRAVITY /= 1000;
            
            MASS = Double.parseDouble(massText);
            
            BOUNCE = Double.parseDouble(bounceText);
            BOUNCE /= 100;
            
            COF = Double.parseDouble(cofText);
            
            width = Double.parseDouble(ballSizeText);
            width *= 100;
            height = width;
            
            ballXRight = ballXLeft + width;
            ballYRight = ballYLeft + height;
            
            FRICTION = COF * (MASS * (GRAVITY * 1000));
            
            update.addActionListener(this);
            
            update.addActionListener(this);
            
            mode = MOVE;
        }
        else if (e.getSource() == newBall)
        {
            switch (mode)
            {
                case MENU:
                {
                    break;
                }
                case THROW:
                {
                    tempBalls = balls;                      //When do i change to throw?
                    balls = new Ball[(balls.length + 1)];
                    for (int i = 0; i < tempBalls.length; i++)
                    {
                        balls[i] = tempBalls[i];
                    }
                    
                    balls[(balls.length - 1)] = new Ball(((double)width / 2), MASS, BOUNCE, (Math.atan((ballXLeft - previousX[(previousX.length - 1)])/(ballYLeft - previousY[(previousY.length - 1)]))), color);
                    mode = MOVE;
                    break;
                }
                case MOVE:
                {
                    break;
                }
            }
            
            newBall.addActionListener(this);
        }
        
        repaint();
    }
    public int previousScreenX, previousScreenY;
    public void paint (Graphics page)
    {
        dm = getSize();
        previousScreenX = screenX;
        previousScreenY = screenY;
        screenX = dm.width;
        screenY = dm.height;
        if ((previousScreenX != screenX) || (previousScreenY != screenY))
        {
            remove(gravityLabel);
            remove(gravity);
            remove(massLabel);
            remove(mass);
            remove(bounceLabel);
            remove(bounce);
            remove(cofLabel);
            remove(cof);
            remove(ballSizeLabel);
            remove(ballSize);
            remove(update);
        }
        showStatus("(" + mx + ", " + my + ")  " + fmt.format(balls[(balls.length - 1)].getVelocityY()) + "/ " + fmt.format(balls[(balls.length - 1)].getVelocityX()));
        page.drawImage(img,0,0,this);
        nowTime = System.nanoTime();
        
        switch (mode)
        {
            case MENU:
            {
                buffer.setColor(Color.lightGray);
                buffer.fillRect(0, 0, screenX, 35);
                
                gravity = new TextField ((fmt.format(GRAVITY * 1000)));
                gravity.addKeyListener(this);
                mass = new TextField ((fmt.format(MASS)));
                mass.addKeyListener(this);
                bounce = new TextField ((fmt.format((BOUNCE * 100))));
                bounce.addKeyListener(this);
                cof = new TextField ((fmt.format(COF)));
                cof.addKeyListener(this);
                ballSize = new TextField ((fmt.format(((double)width / 100))));
                ballSize.addKeyListener(this);
        
                gravityLabel = new Label ("Gravity (m/s/s): ");
                massLabel = new Label ("Mass (kg): ");
                bounceLabel = new Label ("Bounceability (%): ");
                cofLabel = new Label ("Coefficient of Friction: ");
                ballSizeLabel = new Label ("Ball Diameter (m): ");
                space = new Label ("    ");
        
                update = new Button ("Update");
                
                update.addActionListener(this);
        
                
                add(gravityLabel);
                add(gravity);
                add(massLabel);
                add(mass);
                add(bounceLabel);
                add(bounce);
                add(cofLabel);
                add(cof);
                add(ballSizeLabel);
                add(ballSize);
                add(space);
                add(update);
                
                mode = MOVE;
                repaint();
                break;
            }
            case THROW:
            {
                super.paint(buffer);
                Graphics2D buffer2 = (Graphics2D)buffer;
                buffer.setColor(Color.yellow);
                buffer.fillRect(0,35,screenX,screenY);
                
                buffer.setColor(Color.blue);
                buffer.fillOval(0, 35, (int)balls[(balls.length - 1)].getDiameter(), (int)balls[(balls.length - 1)].getDiameter());
                buffer.setColor(new Color(generator.nextInt(255), generator.nextInt(255), generator.nextInt(255)));
                buffer.fillRect(mx, my, 2, 2);
                repaint();
                break;
            }
            case MOVE:
            {
                try
                {
                    Thread.sleep(FPS);  //1000 = 1 second
                    
                    if (balls[(balls.length - 1)].getVelocityX() < 0)
                    {
                        left = true;
                    }
                    else left = false;
                    if (balls[(balls.length - 1)].getVelocityY() < 0)
                    {
                        up = true;
                    }
                    else up = false;
                    
                    
                    for (int i = 19; i > 0; i--)
                    {
                        try
                        {
                            previousBalls[balls.length - 1][i].ballMiddle(previousBalls[balls.length - 1][i - 1].getX(), previousBalls[balls.length - 1][i - 1].getY());
                        }
                        catch (Exception NullPointerException){}
                    }
                    
                    previousBalls[balls.length - 1][0].ballMiddle(balls[(balls.length - 1)].getX(), balls[(balls.length - 1)].getY());
                    
                    balls[(balls.length - 1)].addGravity(GRAVITY, (double)FPS);  //acceleration of gravity
                    balls[(balls.length - 1)].move();
                    
                    if ((balls[(balls.length - 1)].getY() + balls[(balls.length - 1)].getRadius()) >= screenY && up == false)   //bottom
                    {
                        balls[(balls.length - 1)].hitBottom();
                    }
                    
                    if ((balls[(balls.length - 1)].getY() + balls[(balls.length - 1)].getRadius() <= balls[(balls.length - 1)].getDiameter() + 35) && up == true)  //top
                    {
                        balls[(balls.length - 1)].hitTop();
                    }
                    
                    if ((balls[(balls.length - 1)].getX() - balls[(balls.length - 1)].getRadius()) <= 0 && left == true)    //left
                    {
                        balls[(balls.length - 1)].hitLeft();
                    }
                    
                    if ((balls[(balls.length - 1)].getX() + balls[(balls.length - 1)].getRadius()) >= screenX && left == false)   //right
                    {
                        balls[(balls.length - 1)].hitRight();
                    }
                    
                    
                    if (balls[(balls.length - 1)].getY() + (int)balls[(balls.length - 1)].getRadius() > screenY)    //bottom check
                    {
                        balls[(balls.length - 1)].fixBottom(screenY);
                    }
                    
                    if (balls[(balls.length - 1)].getY() - (int)balls[(balls.length - 1)].getRadius() < 35)     //top check   //35 = bottom of menu
                    {
                       balls[(balls.length - 1)].fixTop();
                    }
                    
                    if (balls[(balls.length - 1)].getX() - (int)balls[(balls.length - 1)].getRadius() < 0)    //left check
                    {
                        balls[(balls.length - 1)].fixLeft();
                    }
                    
                    if (balls[(balls.length - 1)].getX() + (int)balls[(balls.length - 1)].getRadius() > screenX)    //right check
                    {
                        balls[(balls.length - 1)].fixRight(screenX);
                    }
                    
                    
                    
                    if (balls[(balls.length - 1)].getY() == (screenY - (int)balls[(balls.length - 1)].getRadius()) && left == true && balls[(balls.length - 1)].getVelocityX() < 0.0000 && moveableX == true)  //FRICTION left
                    {
                        balls[(balls.length - 1)].frictionLeft(FRICTION);
                        if (balls[(balls.length - 1)].getVelocityX() > -0.05)
                        {
                            moveableX = false;
                            balls[(balls.length - 1)].zeroVelocityX();
                        }
                    }
                    
                    if ((balls[(balls.length - 1)].getY() == (screenY - (int)balls[(balls.length - 1)].getRadius())) && left == false && balls[(balls.length - 1)].getVelocityX() > 0.0000 && moveableX == true)    //FRICTION right
                    {
                        balls[(balls.length - 1)].frictionRight(FRICTION);
                        if (balls[(balls.length - 1)].getVelocityX() < 0.05)
                        {
                            moveableX = false;
                            balls[(balls.length - 1)].zeroVelocityX();
                        }
                    }
                    
                    super.paint(buffer);
                    Graphics2D buffer2 = (Graphics2D)buffer;
                    buffer.setColor(Color.yellow);
                    buffer.fillRect(0,35,screenX,screenY);
                    
                    
                    for (int i = 19; i >= 0; i--)
                    {
                        buffer.setColor(previousBalls[balls.length - 1][i].getColor());
                        previousBalls[balls.length - 1][i].paint(buffer);
                    }
                    
                    buffer.setColor(balls[balls.length - 1].getColor());
                    balls[balls.length - 1].paint(buffer);
                    
                    buffer.setColor(new Color(generator.nextInt(255), generator.nextInt(255), generator.nextInt(255)));
                    buffer.fillRect(mx, my, 2, 2);
                    
                    f++;
                }
                catch (InterruptedException e) {}
                repaint();
                break;
            }
        }
        buffer.setColor(Color.lightGray);
        buffer.fillRect(0, 0, screenX, 35);
        
        buffer.setColor(Color.black);
        buffer.drawString("© Pat Bartman", dm.width - 90, dm.height - 10);
    }
    private Image offScreenImage;
    private Dimension offScreenSize;
    private Graphics offScreenGraphics;
    public final synchronized void update (Graphics g)
    {
        Dimension d = getSize();
        if((offScreenImage == null) || (d.width != offScreenSize.width) || (d.height != offScreenSize.height))
        {
            offScreenImage = createImage(d.width, d.height);
            offScreenSize = d;
            offScreenGraphics = offScreenImage.getGraphics();
        }
        offScreenGraphics.clearRect(0, 0, d.width, d.height);
        paint(offScreenGraphics);
        g.drawImage(offScreenImage, 0, 0, null);
    }
}