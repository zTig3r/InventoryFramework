package io.github.ztig3r.IF.data;

import lombok.Getter;

/**
 * Represents a pattern for inventory layouts.
 */
public class Pattern {

    /**
     * The pattern represented as a 2D array of integers.
     */
    @Getter
    protected int[][] pattern;

    /**
     * Creates a pattern from the given string array. Each string represents a row in the pattern,
     * and each character in the string represents a slot in that row. The character's ASCII value
     * is stored in the pattern array.
     *
     * @param pattern Array of strings representing the pattern
     */
    public Pattern(String... pattern) {
        this.pattern = new int[pattern.length][9];
        for (int row = 0; row < pattern.length; row++) {
            String line = pattern[row];
            for (int slot = 0; slot < line.length(); slot++) {
                this.pattern[row][slot] = line.charAt(slot);
            }
        }
    }
}
