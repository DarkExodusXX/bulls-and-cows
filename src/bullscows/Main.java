package bullscows;

import com.sun.source.tree.ArrayAccessTree;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    final static String[] POSSIBLE_SYMBOLS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
            "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static String[] deleteSymbol(String[] availableSymbols, int numberToDelete) {
        String[] newAvailableSymbols = new String[availableSymbols.length - 1];
        for (int i = 0; i < numberToDelete; i++) {
            newAvailableSymbols[i] = availableSymbols[i];
        }
        for (int i = numberToDelete; i < availableSymbols.length - 1; i++) {
            newAvailableSymbols[i] = availableSymbols[i+1];
        }
        return newAvailableSymbols;
    }

    public static void tryGuess(String secretCode, int turn, int lengthOfNumber) {
        System.out.printf("Turn %d:\n", turn);

        String input = scanner.next();
        if (!(input.length() == lengthOfNumber)) {
            System.out.println("Error: input length is not equal the length of the code");
            tryGuess(secretCode, turn, lengthOfNumber);
        }
        int cow = 0;
        int bull = 0;

        String[] inputArray = input.split("");

        for (int i = 0; i < secretCode.length(); i++) {
            if (secretCode.charAt(i) == input.charAt(i) && !isCountYet(input, i)) {
                bull++;
            } else {
                if (secretCode.contains(inputArray[i]) && !isCountYet(input, i)) {
                    cow++;
                }
            }
        }

        if (cow > 0 && bull > 0) {
            System.out.printf("Grade: %d bull(s) and %d cow(s).\n", bull, cow);
        } else {
            if (cow > 0) {
                System.out.printf("Grade: %d cow(s)\n", cow);
            }
            if (bull > 0) {
                System.out.printf("Grade: %d bull(s)\n", bull);
            }
        }
        if (cow == 0 && bull == 0) {
            System.out.println("Grade: None");
        }

        if (bull == lengthOfNumber) {
            System.out.println("Congratulations! You guessed the secret code.");
        } else {
            tryGuess(secretCode, ++turn, lengthOfNumber);
        }
    }

    public static boolean isCountYet(String input, int symbolIndex) {
        boolean isCountYet = false;
        for (int i = 0; i < symbolIndex; i++) {
            if (input.charAt(i) == input.charAt(symbolIndex)) {
                isCountYet = true;
                break;
            }
        }
        return isCountYet;
    }

    public static StringBuilder generateSecret(int lengthOfNumber, int rangeOfPossibleCharacters) {
        Random random = new Random(System.nanoTime());
        String[] availableSymbols = Arrays.copyOfRange(POSSIBLE_SYMBOLS, 0, rangeOfPossibleCharacters);
        StringBuilder secretCode = new StringBuilder();
        if (lengthOfNumber > rangeOfPossibleCharacters) {
            System.out.printf("Error: can't generate a secret number with a length of %d because there aren't enough unique digits.\n", lengthOfNumber);
            return null;
        } else {
            for (int i = 0; i < lengthOfNumber; i++) {
                int randomNumber = random.nextInt(availableSymbols.length);
                secretCode.append(availableSymbols[randomNumber]);
                availableSymbols = deleteSymbol(availableSymbols, randomNumber);
            }
        }
        return secretCode;
    }

    public static void startGame() {
        System.out.println("Input the length of the secret code:");
        int lengthOfNumber;
        try {
            lengthOfNumber = Integer.parseInt(scanner.next());
        } catch (Exception e) {
            System.out.println("Error: it isn't a valid number.");
            return;
        }
        System.out.println("Input the number of possible symbols in the code:");
        int rangeOfPossibleCharacters;
        try {
            rangeOfPossibleCharacters = Integer.parseInt(scanner.next());
        } catch (Exception e) {
            System.out.println("Error: it isn't a valid number.");
            return;
        }
        if (rangeOfPossibleCharacters < lengthOfNumber) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols./n", lengthOfNumber, rangeOfPossibleCharacters);
            return;
        }
        if (lengthOfNumber > 36) {
            System.out.println("Error: maximum length is 36.");
            return;
        }
        if (rangeOfPossibleCharacters > 36) {
            System.out.println("Error: maximum number of possible symbols is 36 (0-9, a-z).");
            return;
        }
        if (lengthOfNumber <= 0) {
            System.out.println("Error: length may not be less than or equal to 0");
            return;
        }
        StringBuilder secretCode = generateSecret(lengthOfNumber, rangeOfPossibleCharacters);
        String rangeString;
        if (rangeOfPossibleCharacters <= 10) {
            rangeString = String.format("(0-%d)", rangeOfPossibleCharacters - 1);
        } else {
            rangeString = String.format(" (0-9, a-%s)", POSSIBLE_SYMBOLS[rangeOfPossibleCharacters - 1]);
        }
        System.out.println("The secret is prepared: " + "*".repeat(lengthOfNumber) + rangeString);
        if (secretCode != null) {
            System.out.println("Okay, let's start a game!");
            tryGuess(secretCode.toString(), 1, lengthOfNumber);
        }
    }

    public static void main(String[] args) {
        while (true) {
            startGame();
        }
    }
}
