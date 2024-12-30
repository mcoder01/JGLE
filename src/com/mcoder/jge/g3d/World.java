package com.mcoder.jge.g3d;

import com.mcoder.jge.g3d.scene.light.Light;
import com.mcoder.jge.g3d.model.Model;
import com.mcoder.jge.g3d.scene.Solid;
import com.mcoder.jge.g3d.render.SolidDrawer;
import com.mcoder.jge.g3d.render.shader.Phong;
import com.mcoder.jge.g3d.scene.Camera;
import com.mcoder.jge.g3d.scene.light.Spotlight;
import com.mcoder.jge.math.Vector3D;
import com.mcoder.jge.screen.Drawable;
import com.mcoder.jge.screen.View;
import com.mcoder.jge.util.Texture;

import java.awt.*;
import java.util.LinkedList;

public class World extends View {
    private SolidDrawer drawer;
    private Camera camera;
    private LinkedList<Light> lights;

    @Override
    public void setup() {
        addAll(getLights());
        add(getCamera());

        // Testing
        Model cubeModel = Model.loadFromFile("res/models/cube.obj");
        Texture texture = new Texture("textures/cobblestone.png");
        Phong phongShader = new Phong(this);
        Solid[][][] platform = new Solid[3][1][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                platform[i][0][j] = new Solid(cubeModel, phongShader, i - 1, 0, j - 1, this);
                platform[i][0][j].setTexture(texture);
                //add(platform[i][0][j]);
            }

        Solid cube = new Solid(cubeModel, phongShader, 0, 0, 0, this);
        cube.setTexture(texture);
        //add(cube);

        Model monkeyModel = Model.loadFromFile("res/models/monkey3.obj");
        Texture nullTexture = new Texture("textures/null.png");
        Solid monkey = new Solid(monkeyModel, phongShader, 0, 0, 0, this);
        monkey.setTexture(nullTexture);
        add(monkey);

        Model mountainsModel = Model.loadFromFile("res/models/mountains.obj");
        Solid mountains = new Solid(mountainsModel, phongShader, 0, 0, 0, this);
        //add(mountains);

        Model shipModel = Model.loadFromFile("res/models/ship.obj");
        Solid ship = new Solid(shipModel, phongShader, 0, 0, 0, this);
        //add(ship);

        Model axisModel = Model.loadFromFile("res/models/axis.obj");
        Solid axis = new Solid(axisModel, phongShader, 0, 0, 0, this);
        //add(axis);
        super.setup();
    }

    @Override
    public void show(Graphics2D g2d) {
        for (Drawable drawable : this)
            if (drawable instanceof Solid solid)
                getPipeline().drawSolid(solid);
            else drawable.show(g2d);
    }

    private SolidDrawer getPipeline() {
        if (drawer == null)
            drawer = new SolidDrawer(this);
        return drawer;
    }

    public Camera getCamera() {
        if (camera == null)
            camera = new Camera(0, 0, 3, this);
        return camera;
    }

    public LinkedList<Light> getLights() {
        if (lights == null) {
            lights = new LinkedList<>();
            //lights.add(new AmbientLight(Vector3D.rgbToVec(0xffffff), this));
            lights.add(new Spotlight(Vector3D.rgbToVec(0xff0000), 0, 3, 3, this));
            lights.add(new Spotlight(Vector3D.rgbToVec(0x0000ff), 0, 3, -3, this));
            //lights.add(new Spotlight(Vector3D.rgbToVec(0xffffff), 0, 3, 3, this));
        }

        return lights;
    }
}
