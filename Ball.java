import java.awt.*;

public class Ball
{
    public double radius, mass, bounce, angle;
    public Color color;
    
    public double velocityX, velocityY, velocity, keY, keX;
    public int x, y;
    
    public Ball(double radius, double mass, double bounce, double angle, Color color)
    {
        this.radius = radius;
        this.mass = mass;
        this.bounce = bounce;
        this.angle = angle;
        this.color = color;
    }
    
    public Ball(double radius, Color color)
    {
        this.radius = radius;
        this.color = color;
    }
    
    public void findVelocity(double distanceX, double distanceY, double time)
    {
        velocityX = distanceX / time;
        velocityY = distanceY / time;
        velocity = (Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2))) / time;
        kineticEnergy();
    }
    
    public void ballMiddle(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void addGravity(double gravity, double FPS)
    {
        velocityY += (gravity * (1000/(FPS)));
        kineticEnergy();
    }
    
    public void move()
    {
        x += velocityX;
        y += velocityY;
    }
    
    public void kineticEnergy()
    {
        keY = (0.5 * mass * (Math.pow(velocityY, 2)));
        keX = (0.5 * mass * (Math.pow(velocityX, 2)));
    }
    
    public void hitLeft()   //remove when wall object made
    {
        kineticEnergy();
        keX *= bounce;
        velocityX = (Math.sqrt(keX / (0.5 * mass)));
        kineticEnergy();
    }
    
    public void hitRight()  //remove when wall object made
    {
        kineticEnergy();
        keX *= bounce;
        velocityX = (Math.sqrt(keX / (0.5 * mass)));
        velocityX *= -1;
        kineticEnergy();
    }
    
    public void hitTop()    //remove when wall object made
    {
        kineticEnergy();
        keY *= bounce;
        velocityY = (Math.sqrt(keY / (0.5 * mass)));
        kineticEnergy();
    }
    
    public void hitBottom() //remove when wall object made
    {
        kineticEnergy();
        keY *= bounce;
        velocityY = (Math.sqrt(keY / (0.5 * mass)));
        velocityY *= -1;
        kineticEnergy();
    }
    
    public void frictionLeft(double friction)   //remove when wall object made
    {
        velocityX = velocityX + ((friction / mass) * 0.01);
    }
    
    public void frictionRight(double friction)  //remove when wall object made
    {
        velocityX = velocityX + (((friction * -1) / mass) * 0.01);
    }
    
    public void zeroVelocityX()
    {
        velocityX = 0;
    }
    
    public void fixBottom(int screenY)
    {
        y = screenY - (int)radius;
    }
    
    public void fixTop()
    {
        y = 35 + (int)radius;
    }
    
    public void fixLeft()
    {
        x = (int)radius;
    }
    
    public void fixRight(int screenX)
    {
        x = screenX - (int)radius;
    }
    
    
    public double getRadius()
    {
        return radius;
    }
    
    public double getDiameter()
    {
        return (radius * 2);
    }
    
    public double getMass()
    {
        return mass;
    }
    
    public double getBounce()
    {
        return bounce;
    }
    
    public double getAngle()
    {
        return angle;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public double getVelocityX()
    {
        return velocityX;
    }
    
    public double getVelocityY()
    {
        return velocityY;
    }
    
    public double getVelocity()
    {
        return velocity;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public double getKEY()
    {
        return keY;
    }
    
    public double getKEX()
    {
        return keX;
    }
    
    public void paint(Graphics buffer)
    {
        buffer.fillOval((int)getX() - (int)getRadius(), (int)getY() - (int)getRadius(), (int)getDiameter(), (int)getDiameter());
    }
}