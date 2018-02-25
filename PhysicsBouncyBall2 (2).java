import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.applet.*;
import java.text.*;

public class PhysicsBouncyBall2 extends Applet implements KeyListener, Runnable, MouseListener, MouseMotionListener, ActionListener
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
    
    public Ball[] balls, tempBalls;
    
    public DecimalFormat fmt = new DecimalFormat("0.####");
    public Label gravityLabel, massLabel, bounceLabel, cofLabel, ballSizeLabel;
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
        screenX = d.width;
        screenY = d.width;
        
        setBackground (Color.yellow);
        setSize(screenX, screenY);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        img = createImage(screenX, screenY);
        buffer = img.getGraphics();
        
        //newBall.addActionListener(this);
        
        mx = x;
        my = y;
        
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
                velocityX = distanceX / time;
                distanceY= (double)stopY - (double)startY;
                distanceY /= 100;
                velocityY = distanceY / time;
                distance = Math.sqrt(Math.pow(distance, 2) + Math.pow(distanceY, 2));
                velocity = distance / time;
                //setStart = true;    //remove when MOVE is created
                ballXLeft = stopX - (width / 2);
                ballYLeft = stopY - (height / 2);
                ballXRight = stopX + (width / 2);
                ballYRight = stopY + (height / 2);
                if (velocityX < 0)
                {
                    left = true;
                }
                if (velocityY < 0)
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
    public void keyPressed(KeyEvent e){  }
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

    public void paint (Graphics page)
    {
        dm = getSize();
        screenX = dm.width;
        screenY = dm.height;
        showStatus("(" + mx + ", " + my + ")  " + fmt.format(velocityY) + "/ " + fmt.format(velocityX));
        page.drawImage(img,0,0,this);
        nowTime = System.nanoTime();
        
        switch (mode)
        {
            case MENU:
            {
                buffer.setColor(Color.yellow);
                buffer.fillRect(0, screenY, screenX, screenY + 100);
                
                
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
                buffer.fillRect(0,0,screenX,screenY);
                
                buffer.setColor(Color.blue);
                buffer.fillOval(mx - 25, my - 25, (int)width, (int)height);
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
                    
                    if (velocityX < 0)
                    {
                        left = true;
                    }
                    else left = false;
                    if (velocityY < 0)
                    {
                        up = true;
                    }
                    else up = false;
                    
                    
                    for (int i = 19; i > 0; i--)
                    {
                        try
                        {
                            previousX[i] = previousX[i - 1];
                        }
                        catch (Exception NullPointerException){}
                    }
                    
                    previousX[0] = ballXLeft;
                    
                    for (int i = 19; i > 0; i--)
                    {
                        try
                        {
                            previousY[i] = previousY[i - 1];
                        }
                        catch (Exception NullPointerException){}
                    }
                    
                    previousY[0] = ballYLeft;
                    
                    
                    velocityY += (GRAVITY * (1000/((double)FPS)));  //acceleration of gravity
                    ballYLeft += velocityY;
                    ballYRight += velocityY;
                    ballXLeft += velocityX;
                    ballXRight += velocityX;
                    
                    keY = (0.5 * MASS * (Math.pow(velocityY, 2)));
                    keX = (0.5 * MASS * (Math.pow(velocityX, 2)));
                    
                    if ((ballYRight >= screenY || ballYLeft >= screenY) && up == false)   //bottom
                    {
                        keY *= BOUNCE;
                        velocityY = (Math.sqrt(keY / (0.5 * MASS)));
                        /*if (velocityCheck == velocityY)
                        {
                            velocityY *= .8;
                        }
                        velocityCheck = velocityY;*/
                        velocityY *= -1;
                        //add in code to prevent endless bouncing
                    }
                    
                    if ((ballYLeft <= 0 || ballYRight <= 0) && up == true)  //top
                    {
                        keY *= BOUNCE;
                        velocityY = (Math.sqrt(keY / (0.5 * MASS)));
                        //add in code to prevent endless bouncing
                    }
                    
                    if ((ballXLeft <= 0 || ballXRight <= 0) && left == true)    //left
                    {
                        keX *= BOUNCE;
                        velocityX = (Math.sqrt(keX / (0.5 * MASS)));
                    }
                    
                    if ((ballXLeft >= screenX || ballXRight >= screenX) && left == false)   //right
                    {
                        keX *= BOUNCE;
                        velocityX = (Math.sqrt(keX / (0.5 * MASS)));
                        velocityX *= -1;
                    }
                    
                    
                    if (ballYRight > screenY || ballYLeft > screenY)    //bottom check
                    {
                        ballYRight = screenY;
                        ballYLeft = screenY - height;
                    }
                    
                    if (ballYRight < 0 || ballYLeft < 0)    //top check
                    {
                        ballYRight = height;
                        ballYLeft = 0;
                    }
                    
                    if (ballXLeft < 0 || ballXRight < 0)    //left check
                    {
                        ballXRight = width;
                        ballXLeft = 0;
                    }
                    
                    if (ballXLeft > screenX || ballXRight > screenX)    //right check
                    {
                        ballXRight = screenX;
                        ballXLeft = screenX - width;
                    }
                    
                    
                    
                    if ((ballYLeft == (screenY - 50) || ballYRight == screenY) && left == true && velocityX < 0.0000 && moveableX == true)  //FRICTION left
                    {
                        velocityX = velocityX + ((FRICTION / MASS) * 0.01);
                        if (velocityX > -0.05)
                        {
                            moveableX = false;
                            velocityX = 0;
                        }
                    }
                    
                    if ((ballYLeft == (screenY - 50) || ballYRight == screenY) && left == false && velocityX > 0.0000 && moveableX == true)    //FRICTION right
                    {
                        velocityX = velocityX + (((FRICTION * -1) / MASS) * 0.01);
                        if (velocityX < 0.05)
                        {
                            moveableX = false;
                            velocityX = 0;
                        }
                    }
                    
                    
                    super.paint(buffer);
                    Graphics2D buffer2 = (Graphics2D)buffer;
                    buffer.setColor(Color.yellow);
                    buffer.fillRect(0,0,screenX,screenY);
                    
                    
                    for (int i = 19; i >= 0; i--)
                    {
                        buffer.setColor(new Color((((int)colorAdd * i)), ((int)colorAdd * i), (255 - ((int)colorAdd * i))));
                        buffer.fillOval((int)previousX[i], (int)previousY[i], (int)width, (int)height);
                    }
                    
                    buffer.setColor(color);
                    buffer.fillOval((int)ballXLeft, (int)ballYLeft, (int)width, (int)height);
                    
                    
                    
                    buffer.setColor(new Color(generator.nextInt(255), generator.nextInt(255), generator.nextInt(255)));
                    buffer.fillRect(mx, my, 2, 2);
                    
                    f++;
                }
                catch (InterruptedException e) {}
                repaint();
                break;
            }
        }
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