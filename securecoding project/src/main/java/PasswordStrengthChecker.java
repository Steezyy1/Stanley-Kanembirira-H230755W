public class PasswordStrengthChecker {
    public static int check(String password) {
        int strength = 0;
        if (password.length() >= 8 && password.length() < 64) strength += 20;
        if (password.matches(".*[A-Z].*")) strength += 20;  // Uppercase
        if (password.matches(".*[a-z].*")) strength += 20;  // Lowercase
        if (password.matches(".*\\d.*")) strength += 20;    // Digit
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) strength += 20;  // Special character
        return strength;
    }
}