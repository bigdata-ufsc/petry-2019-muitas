package br.com.lucasmaypetry.utils;

import java.util.ArrayList;
import java.util.List;

public class PrimeUtils {

	private static int index = 0;
	private static int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79,
			83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139,
			149, 151, 157, 163, 167, 173, 179 };

	private PrimeUtils() {
	}

	public synchronized static final int nextPrime() {
		return primes[index++];
	}

	public static List<Integer> factorsOf(int number) {
		int theNumber = number;
		int curPrime = 0;
		List<Integer> factors = new ArrayList<>();

		while (primes[curPrime] <= theNumber) {
			if (theNumber % primes[curPrime] == 0) {
				factors.add(primes[curPrime]);
				theNumber /= primes[curPrime];
			}

			curPrime++;
		}

		if (factors.isEmpty()) // Number is prime
			factors.add(number);

		return factors;
	}

}
