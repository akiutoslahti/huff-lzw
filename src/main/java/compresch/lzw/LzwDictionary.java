/*
 * MIT License
 *
 * Copyright (c) 2018 Aki Utoslahti
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package compresch.lzw;

import compresch.ds.LzwEntry;
import compresch.ds.LzwHashTable;

/**
 * Dictionary used for encoding and decoding with LZW.
 */
public class LzwDictionary {

    private LzwHashTable dictionary;
    private String[] index;
    private int currentSize;
    private int maxSize;
    private int codewordLength;

    /**
     * Constructs and initiates new dictionary.
     */
    LzwDictionary() {
        this.maxSize = 4096;
        this.codewordLength = 12;
        this.dictionary = new LzwHashTable();
        this.index = new String[this.maxSize];
        initDictionary();
    }

    /**
     * Constructs and initiates new dictionary.
     */
    LzwDictionary(int codewordLength) {
        this.codewordLength = codewordLength;
        this.maxSize = (int) (Math.pow(2, codewordLength));
        this.dictionary = new LzwHashTable();
        this.index = new String[this.maxSize];
        initDictionary();
    }

    /**
     * Initiates dictionary with base symbols.
     */
    private void initDictionary() {
        for (int i = 0; i < 256; i++) {
            addEntry((char) (i) + "");
        }
    }

    /**
     * Adds new entry to dictionary.
     * @param symbol String entry to be added to dictionary.
     * @return true if adding was successful, false if dictionary is full.
     */
    boolean addEntry(String symbol) {
        if (this.currentSize < this.maxSize - 1) {
            this.dictionary.insert(new LzwEntry(symbol, this.currentSize));
            this.index[this.currentSize] = symbol;
            currentSize++;
            return true;
        }
        return false;
    }

    /**
     * Returns symbol corresponding to parameter codeword.
     * @param codeword Integer codeword to be searched from dictionary.
     * @return String symbol for given codeword from dictionary.
     */
    public String getSymbol(int codeword) throws IllegalArgumentException {
        if (codeword < 0 || codeword > this.maxSize - 1) {
            throw new IllegalArgumentException();
        }
        if (codeword >= this.currentSize) {
            return null;
        }
        return this.index[codeword];
    }

    int getCodeword(String symbol) {
        return this.dictionary.search(symbol);
    }

    int getPseudoEof() {
        return this.maxSize - 1;
    }

    int getCodewordLength() {
        return this.codewordLength;
    }

}