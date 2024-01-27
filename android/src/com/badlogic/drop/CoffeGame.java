package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
//импорт необходимых библиотек

public class CoffeGame extends ApplicationAdapter {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;
    //размеры экрана

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TextureRegion coffeeTexture;
    private Rectangle coffeeBounds;

    private TextureRegion milkTexture;
    private Rectangle milkBounds;

    private TextureRegion sugarTexture;
    private Rectangle sugarBounds;

    private boolean hasCoffee;
    private boolean hasMilk;
    private boolean hasSugar;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        Texture coffeeSheet = new Texture(Gdx.files.internal("coffee_sheet.png"));
        coffeeTexture = new TextureRegion(coffeeSheet, 0, 0, 64, 64);
        coffeeBounds = new Rectangle(100, 100, 64, 64);

        Texture milkSheet = new Texture(Gdx.files.internal("milk_sheet.png"));
        milkTexture = new TextureRegion(milkSheet, 0, 0, 64, 64);
        milkBounds = new Rectangle(200, 100, 64, 64);

        Texture sugarSheet = new Texture(Gdx.files.internal("sugar_sheet.png"));
        sugarTexture = new TextureRegion(sugarSheet, 0, 0, 64, 64);
        sugarBounds = new Rectangle(300, 100, 64, 64);

        hasCoffee = false;
        hasMilk = false;
        hasSugar = false;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(coffeeTexture, coffeeBounds.x, coffeeBounds.y);
        batch.draw(milkTexture, milkBounds.x, milkBounds.y);
        batch.draw(sugarTexture, sugarBounds.x, sugarBounds.y);

        batch.end();

        handleInput();
        update();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();

            if (coffeeBounds.contains(x, y)) {
                hasCoffee = true;
            }
            if (milkBounds.contains(x, y)) {
                hasMilk = true;
            }
            if (sugarBounds.contains(x, y)) {
                hasSugar = true;
            }

            if (hasCoffee && hasMilk && hasSugar) {
                // Игра выиграна!
            }
        }
    }

    private void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}

