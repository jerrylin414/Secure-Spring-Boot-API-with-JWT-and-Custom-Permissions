package com.lzy.jshow.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;

public class RandomUtils {

	private static Random _rnd = new Random();

	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static int randomRound(double number) throws Exception {
		double percentage = (number - Math.floor(number)) * 100;

		if (percentage == 0) {
			return ((int) number);
		} else {
			int r = _rnd.nextInt(100);
			if (r < percentage) {
				return ((int) number + 1);
			} else {
				return ((int) number);
			}
		}
	}
	
	public static int randomInt(int n) throws Exception {
		if(n <= 0){
			return 0;
		}
		return _rnd.nextInt(n);
	}
	
	public static int randomRange(int n1, int n2) throws Exception {
		int max = Math.max(n1, n2);
		int min = Math.min(n1, n2);
		if(min < 0){
			min = 0;
		}
		if(max < 0){
			max = 0;
		}
		
		return _rnd.nextInt(max - min + 1) + min;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(randomRange(0,10000));
	}
	

	public static BigDecimal randomRange(BigDecimal min, BigDecimal max, Integer scale, RoundingMode roundingMode){
    	if(max == null){
    		return BigDecimal.ZERO.add(min);
    	}
    	BigDecimal range = max.subtract(min);
		MathContext mathContext = new MathContext(scale, roundingMode);
		BigDecimal randomFraction = new BigDecimal(new Random().nextDouble(), mathContext);
        BigDecimal val = min.add(range.multiply(randomFraction, mathContext), mathContext).setScale(scale, roundingMode);
        return val;
    }
}
