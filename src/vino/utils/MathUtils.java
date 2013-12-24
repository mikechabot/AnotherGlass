package vino.utils;

public class MathUtils {

    public static int generateRandomNumber(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    
}
