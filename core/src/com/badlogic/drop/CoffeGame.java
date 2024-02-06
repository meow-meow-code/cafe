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
import com.badlogic.gdx.math.Vector3;

import java.util.Random;
//импорт необходимых библиотек

public class CoffeGame extends ApplicationAdapter {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 480;
    //размеры экрана

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture coffeeTexture;
    private Rectangle coffeeBounds;

    private Texture milkTexture;
    private Rectangle milkBounds;

    private Texture sugarTexture;
    private Rectangle sugarBounds;

    private boolean hasCoffee;
    private boolean hasMilk;
    private boolean hasSugar;

    private boolean gameWon;
    private TextureRegion victoryTexture;
    private Rectangle victoryBounds;
    private float coffeeAlpha = 1f;
    private float milkAlpha = 1f;
    private float sugarAlpha = 1f;

    private float coffeeOffsetY = 0f;
    private float milkOffsetY = 0f;
    private float sugarOffsetY = 0f;

    private float animSpeed = 2f;

    private boolean coffeeAnimating = false;
    private  boolean milkAnimating = false;
    private boolean sugarAnimating = false;

    private boolean hasRandomCoffee;
    private int randomCoffeeType;
    // 1 - капучино, 2 - латте
    private TextureRegion randomCoffeeTexture;
    private Rectangle randomCoffeeBounds;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        Texture randomCoffeeSheet = new Texture(Gdx.files.internal("random_coffee1_sheet.png"));
        randomCoffeeTexture = new TextureRegion(randomCoffeeSheet, 0, 0, 64, 64);
        randomCoffeeBounds = new Rectangle(400, 100, 64, 64);

        coffeeTexture = new Texture(Gdx.files.internal("coffee_sheet.png"));
        coffeeBounds = new Rectangle(100, 100, 64, 64);

        milkTexture = new Texture(Gdx.files.internal("milk_sheet.png"));
        milkBounds = new Rectangle(200, 100, 64, 64);

        sugarTexture = new Texture(Gdx.files.internal("sugar_sheet.png"));
        sugarBounds = new Rectangle(300, 100, 64, 64);

        hasCoffee = false;
        hasMilk = false;
        hasSugar = false;
        hasRandomCoffee = false;

        gameWon = false;
        Texture victoryScreenTexture = new Texture(Gdx.files.internal("victory_screen.jpg"));
        victoryTexture = new TextureRegion(victoryScreenTexture, 0, 0, WIDTH, HEIGHT);
        victoryBounds = new Rectangle(0, 0, WIDTH, HEIGHT);

        Random random = new Random();
        randomCoffeeType = random.nextInt(2) + 1;

        if (randomCoffeeType == 1){
            randomCoffeeTexture = coffeeTexture;
            randomCoffeeBounds = coffeeBounds;
        } else if (randomCoffeeType == 2){
            randomCoffeeTexture = milkTexture;
            randomCoffeeBounds = milkBounds;
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (gameWon) {
            batch.begin();
            batch.draw(coffeeTexture, coffeeBounds.x, coffeeBounds.y);
            batch.draw(milkTexture, milkBounds.x, milkBounds.y);
            batch.draw(sugarTexture, sugarBounds.x, sugarBounds.y);
            batch.draw(randomCoffeeTexture, randomCoffeeBounds.x, randomCoffeeBounds.y);

            if (coffeeAnimating) {
                coffeeAlpha -= Gdx.graphics.getDeltaTime() * animSpeed;
                coffeeOffsetY += Gdx.graphics.getDeltaTime() * animSpeed * 100;
                if (coffeeAlpha <= 0f) {
                    coffeeAnimating = false;
                    coffeeAlpha = 0f;
                }
            }

            if (milkAnimating) {
                milkAlpha -= Gdx.graphics.getDeltaTime() * animSpeed;
                milkOffsetY += Gdx.graphics.getDeltaTime() * animSpeed * 100;
                if (milkAlpha <= 0f) {
                    milkAnimating = false;
                    milkAlpha = 0f;
                }
            }

            if (sugarAnimating) {
                sugarAlpha -= Gdx.graphics.getDeltaTime() * animSpeed;
                sugarOffsetY += Gdx.graphics.getDeltaTime() * animSpeed * 100;
                if (sugarAlpha <= 0f) {
                    sugarAnimating = false;
                    sugarAlpha = 0f;
                }
            }

            batch.setColor(1f, 1f, 1f, coffeeAlpha);
            batch.draw(coffeeTexture, coffeeBounds.x, coffeeBounds.y + coffeeOffsetY, coffeeBounds.width, coffeeBounds.height);

            batch.setColor(1f, 1f, 1f, milkAlpha);
            batch.draw(milkTexture, milkBounds.x, milkBounds.y + milkOffsetY, milkBounds.width, milkBounds.height);

            batch.setColor(1f, 1f, 1f, sugarAlpha);
            batch.draw(sugarTexture, sugarBounds.x, sugarBounds.y + sugarOffsetY, sugarBounds.width, sugarBounds.height);
            batch.setColor(1, 1, 1, 1);
            if (gameWon) {
                batch.draw(victoryTexture, victoryBounds.x, victoryBounds.y);
            }
            batch.end();

            handleInput();
            update();
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {

            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos = camera.unproject(touchPos);
            float x = touchPos.x;
            float y = touchPos.y;
            System.out.println(x + " " + y);
            System.out.println(milkBounds);

            if (coffeeBounds.contains(x, y)) {
                System.out.println("Coffee");
                coffeeAnimating = true;
                hasCoffee = true;
            }

            if (milkBounds.contains(x, y)) {
                System.out.println("Milk");
                milkAnimating = true;
                hasMilk = true;
            }

            if (sugarBounds.contains(x, y)) {
                sugarAnimating = true;
                hasSugar = true;
                System.out.println("Sugar");
            }

            if (hasCoffee && hasMilk && hasSugar) {
                // Игра выигранa
                gameWon = true;
                System.out.println("Won");
            }
        }
    }

    private void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}

