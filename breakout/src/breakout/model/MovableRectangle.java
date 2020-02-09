package breakout.model;


import org.w3c.dom.css.Rect;

import java.util.List;
import java.util.Random;
import static breakout.model.Breakout.BALL_SPEED_FACTOR;

abstract class MovableRectangle extends Rectangle {

    private double velocity;
    private double dx;
    private double dy;

    public MovableRectangle(double x, double y) {
        super(x, y);

        this.velocity = BALL_SPEED_FACTOR;

        Random rnd = new Random();
        dx = (rnd.nextDouble() - 0.5);
        dy = - Math.abs(rnd.nextDouble() - 0.5);

        dy = Math.min(dy, -0.5);
        double norm = Math.sqrt( Math.pow(this.dx, 2) + Math.pow(this.dy, 2) );
        this.dx = (dx/norm)*velocity;
        this.dy = (dy/norm)*velocity;

        //this.dx = 0;
        //this.dy = -2;
    }

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public double getDx() {
        return this.dx;
    }

    public double getDy() {
        return this.dy;
    }

    public double getVelocity() {
        return this.velocity;
    }

    public void move() {
        double x = this.getX();
        double y = this.getY();
        double dx = this.getDx();
        double dy = this.getDy();
        double v = this.getVelocity();

        this.x = x + (dx * v);
        this.y = y + (dy * v);

        //System.out.println("Dy is:" + dy);
    }

    public void changeDirection(Rectangle hitted_item) {
        HitDirection hit_direction = this.getHitDirection(hitted_item);
        if ( (hit_direction == HitDirection.FROM_BELOW) || (hit_direction == HitDirection.FROM_ABOVE) ) {
            this.dy = - this.dy;
        } else {
            this.dx = - this.dx;
        }
    }

    private HitDirection getHitDirection(Rectangle hitted_item) {

        boolean moving_right = this.dx > 0;
        boolean moving_down = this.dy > 0;

        if ( (this.getX() + this.getWidth() == hitted_item.getX()) && (moving_right) ) {
            return HitDirection.FROM_LEFT;
        } else if ( (this.getX() == hitted_item.getX() + hitted_item.getWidth()) && (!moving_right) ) {
            return HitDirection.FROM_RIGHT;
        } else if ( (this.getY() + this.getHeight() == hitted_item.getY()) && (moving_down) ) {
            return HitDirection.FROM_ABOVE;
        } else {
            //todo uncertain if this is only the other case
            return HitDirection.FROM_BELOW;
        }
    }

    public void adjustIllegalPosition(Paddle paddle) {
        if (this.getY() < paddle.getY()) {
            // from above
            this.setY(paddle.getY() - this.getHeight());
        } else if (this.getX() < paddle.getX()) {
            // from left
            this.setX(paddle.getX() - this.getWidth());
        } else if (this.getX() + this.getWidth() > paddle.getX() + paddle.getWidth()){
            this.setX(paddle.getX() + paddle.getWidth());
        }
    }

    private enum HitDirection {
        FROM_BELOW, FROM_ABOVE, FROM_LEFT, FROM_RIGHT
    }
}
