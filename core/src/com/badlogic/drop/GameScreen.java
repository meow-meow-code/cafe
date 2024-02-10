package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.*;
import java.util.HashMap;
import java.util.Random;

public class GameScreen implements Screen {
    private CoffeGame game;
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

    private Texture sellButtonTexture;
    private Rectangle sellButtonBounds;

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
    private Texture randomCoffeeTexture;
    private Rectangle randomCoffeeBounds;

    private String orderText;

    private float secondsLeft;
    private int moneyCount;

    private Stage stage;
    private String[] coffeeNames = {"Latte", "Cappuccino"};

    private HashMap<String, CoffeeRecipe> menuMap;

    private CoffeeRecipe currentCoffee;

    public GameScreen(CoffeGame game){
        this.game = game;
        create();
        createMenuMap();
        orderText = getRandomOrderText();
        secondsLeft = 60;
        moneyCount = 0;
        currentCoffee = new CoffeeRecipe(0,0,0);
    }

    private void createMenuMap(){
        menuMap = new HashMap<>();
        menuMap.put("Latte", new CoffeeRecipe(2, 1, 1));
        menuMap.put("Cappuccino", new CoffeeRecipe(1, 2, 1));
    }

    private String getRandomOrderText(){
        int rand = new Random().nextInt(menuMap.size());
        return (String) menuMap.keySet().stream().toArray()[rand];
    }

    public void create() {

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        randomCoffeeTexture = new Texture(Gdx.files.internal("random_coffee1_sheet.png"));
        randomCoffeeBounds = new Rectangle(400, 100, 64, 64);

        coffeeTexture = new Texture(Gdx.files.internal("coffee_sheet.png"));
        coffeeBounds = new Rectangle(150, 200, 64, 64);

        milkTexture = new Texture(Gdx.files.internal("milk_sheet.png"));
        milkBounds = new Rectangle(350, 200, 64, 64);

        sugarTexture = new Texture(Gdx.files.internal("sugar_sheet.png"));
        sugarBounds = new Rectangle(560, 200, 64, 64);

        sellButtonBounds = new Rectangle(500, 1, 200, 200);
        sellButtonTexture = new Texture(Gdx.files.internal("sell_button.png"));

        hasCoffee = false;
        hasMilk = false;
        hasSugar = false;
        hasRandomCoffee = false;

        gameWon = false;
        Texture victoryScreenTexture = new Texture(Gdx.files.internal("victory_screen.jpg"));
        victoryTexture = new TextureRegion(victoryScreenTexture, 0, 0, WIDTH, HEIGHT);
        victoryBounds = new Rectangle(0, 0, WIDTH, HEIGHT);

        /*Random random = new Random();
        randomCoffeeType = random.nextInt(2) + 1;

        if (randomCoffeeType == 1){
            randomCoffeeTexture = coffeeTexture;
            randomCoffeeBounds = coffeeBounds;
        } else if (randomCoffeeType == 2){
            randomCoffeeTexture = milkTexture;
            randomCoffeeBounds = milkBounds;
        }*/
    }

    public void renderGame() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        secondsLeft -= Gdx.graphics.getDeltaTime();
        if(secondsLeft <= 0){
            game.setScreen(new EndgameScreen(game, moneyCount));
        }

        batch.begin();
        BitmapFont font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
        font.draw(batch, orderText, 150,425);
        font.draw(batch, (int)secondsLeft + "", 580, 425);
        font.draw(batch, moneyCount + "$", 160,120);

        //batch.draw(randomCoffeeTexture, randomCoffeeBounds.x, randomCoffeeBounds.y);



        if (coffeeAnimating) {
            coffeeAlpha -= Gdx.graphics.getDeltaTime() * animSpeed;
            coffeeOffsetY += Gdx.graphics.getDeltaTime() * animSpeed * 100;
            if (coffeeAlpha <= 0f) {
                coffeeAnimating = false;
                coffeeAlpha = 0f;
                hasCoffee = false;
                coffeeOffsetY = 0f;
                coffeeAlpha = 1;
            }
        }

        if (milkAnimating) {
            milkAlpha -= Gdx.graphics.getDeltaTime() * animSpeed;
            milkOffsetY += Gdx.graphics.getDeltaTime() * animSpeed * 100;
            if (milkAlpha <= 0f) {
                milkAnimating = false;
                hasMilk = false;
                milkOffsetY = 0f;
                milkAlpha = 1;
            }
        }

        if (sugarAnimating) {
            sugarAlpha -= Gdx.graphics.getDeltaTime() * animSpeed;
            sugarOffsetY += Gdx.graphics.getDeltaTime() * animSpeed * 100;
            if (sugarAlpha <= 0f) {
                sugarAnimating = false;
                sugarAlpha = 1f;
                hasSugar = false;
                sugarOffsetY = 0f;
            }
        }

        batch.setColor(1f, 1f, 1f, coffeeAlpha);
        batch.draw(coffeeTexture, coffeeBounds.x, coffeeBounds.y + coffeeOffsetY, coffeeBounds.width, coffeeBounds.height);

        batch.setColor(1f, 1f, 1f, milkAlpha);
        batch.draw(milkTexture, milkBounds.x, milkBounds.y + milkOffsetY, milkBounds.width, milkBounds.height);

        batch.setColor(1f, 1f, 1f, sugarAlpha);
        batch.draw(sugarTexture, sugarBounds.x, sugarBounds.y + sugarOffsetY, sugarBounds.width, sugarBounds.height);
        batch.setColor(1, 1, 1, 1);

        batch.draw(sellButtonTexture, sellButtonBounds.x, sellButtonBounds.y, sellButtonBounds.width, sellButtonBounds.height);

        if (gameWon) {
            batch.draw(victoryTexture, victoryBounds.x, victoryBounds.y);
        }
        batch.end();
        handleInput();
        update();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {

            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchPos = camera.unproject(touchPos);
            float x = touchPos.x;
            float y = touchPos.y;

            if (coffeeBounds.contains(x, y)) {
                if(!hasCoffee){
                    currentCoffee.coffee++;
                    System.out.println("Coffee");
                    coffeeAnimating = true;
                    hasCoffee = true;
                }
            }

            if (milkBounds.contains(x, y)) {
                if(!hasMilk){
                    System.out.println("Milk");
                    milkAnimating = true;
                    hasMilk = true;
                    currentCoffee.milk++;
                }

            }

            if (sugarBounds.contains(x, y)) {
                if(!hasSugar){
                    currentCoffee.sugar++;
                    sugarAnimating = true;
                    hasSugar = true;
                    System.out.println("Sugar");
                }
            }

            if (sellButtonBounds.contains(x, y)){
                if(checkIsOrderRight()){
                    moneyCount += 10;
                }
                currentCoffee = new CoffeeRecipe(0,0,0);
                orderText = getRandomOrderText();
            }
        }
    }

    private boolean checkIsOrderRight(){
        return menuMap.get(orderText).equals(currentCoffee);
    }

    private void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        renderGame();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}