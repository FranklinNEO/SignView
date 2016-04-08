package com.neo.libray;

import java.io.Serializable;

/**
 * Created by Neo on 16/4/8.
 */
public class CoordinateInfo implements Serializable {
    private float x;
    private float y;

    public CoordinateInfo(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
