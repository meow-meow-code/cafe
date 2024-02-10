package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Font {

    private Texture fontTexture;
    private TextureRegion[] fontRegion;

    public Font(String fontPath, int charWidth, int charHeight){
        fontTexture = new Texture(Gdx.files.internal(fontPath));
        int regionWidth = fontTexture.getWidth();
        int regionHeight = fontTexture.getHeight();
        fontRegion = new TextureRegion[regionWidth * regionHeight];

        for (int y = 0; y < regionHeight; y++){
            for (int x = 0; x < regionWidth; x++){
                fontRegion[y * regionWidth + x] = new TextureRegion(fontTexture, x * charWidth, y * charHeight, charWidth, charHeight);

            }
        }
    }
    public void drawText(SpriteBatch batch, String text, float x, float y){
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i);
            int charWidth = 2;
            if (c >= 32 && c <= 127){
                batch.draw(fontRegion[c - 32], x + i * charWidth, y);
            }
        }
    }
    public void dispose(){
        fontTexture.dispose();
    }
}
