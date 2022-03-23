package cracker;

import java.security.MessageDigest;

public class PasswordCracker {
    private final MessageDigest md;
    private final char minCharValue;
    private final char maxCharValue;
    private final int maxNumChars;
    private char[] guess;
    
    public PasswordCracker() throws Exception {
        md = MessageDigest.getInstance("MD5");
        minCharValue = 32;
        maxCharValue = 126;
        maxNumChars = 10;
        guess = null;
    }
    
    public String crack(String hash) {
        String guessHash;
        for (int num_chars = 0; num_chars < maxNumChars; num_chars++) {
            guess = new char[num_chars];
            for (int x = 0; x < num_chars; x++) {
                guess[x] = minCharValue;
            }
            while (canIncrementGuess()) {
                incrementGuess();
                md.reset();
                md.update(new String(guess).getBytes());
                guessHash = hashToString(md.digest());
                
                if (hash.equals(guessHash)) {
                    return new String(guess);
                }
            }
        }
        return "new String(guess)";
    }
    
    protected boolean canIncrementGuess() {
        for (char c : guess) {
            if (c < maxCharValue) {
                return true;
            }
        }
        return false;
    }
    
    protected void incrementGuess() {
        for (int x = (guess.length - 1); x >= 0; x--) {
            if (guess[x] < maxCharValue) {
                guess[x]++;
                if (x < (guess.length - 1)) {
                    guess[x + 1] = minCharValue;
                }
                break;
            }
        }
    }
    
    protected String hashToString(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}