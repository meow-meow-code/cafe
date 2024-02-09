package com.badlogic.drop;

import java.util.Objects;

public class CoffeeRecipe {
    int milk;
    int coffee;
    int sugar;

    public CoffeeRecipe(int milk, int coffee, int sugar) {
        this.milk = milk;
        this.coffee = coffee;
        this.sugar = sugar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoffeeRecipe that = (CoffeeRecipe) o;
        return milk == that.milk && coffee == that.coffee && sugar == that.sugar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(milk, coffee, sugar);
    }
}