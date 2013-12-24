package vino.utils;

import java.util.Arrays;
import java.util.Collections;

public class StringUtils {

    public static String generatePseudoRandomToken() {
        char chars[] = new char[]{'.','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','-','0','1','2','3','4','5','6','7','8','9','~','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','_'};
        Collections.shuffle(Arrays.asList(chars));           
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < 20; i++) {
                int c = MathUtils.generateRandomNumber(0, chars.length-1);
                sb.append(chars[c]);
        }
        return sb.toString();
    }
    
}
