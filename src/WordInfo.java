public class WordInfo {
    private final String word;
    private int occurrences;

    public WordInfo(String word, int occurrences){
        this.word = word;
        this.occurrences = occurrences;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (!(obj instanceof WordInfo)) { return false; }
        WordInfo w = (WordInfo) obj;
        return (this.word.compareTo(w.word) == 0);
    }

    public void incrementOccur() { occurrences += 1; }

    public int letterSum(){
        int letterSum = 0;
        // I opted to use nested if's instead. It was just easier.
        for (int i = 0; i < word.length(); i++) {
            // cl = Current Letter
            char cl = word.charAt(i);
            if (cl == 'a' || cl == 'e' || cl == 'i' || cl == 'u' || cl == 'n' || cl == 'r' ||
                    cl == 'o' || cl == 's' || cl == 'l' || cl == 't') {
                letterSum += 1;
            } else if (cl == 'g' || cl == 'd') {
                letterSum += 2;
            } else if (cl == 'm' || cl == 'b' || cl == 'c' || cl == 'p') {
                letterSum += 3;
            } else if (cl == 'y' || cl == 'f' || cl == 'v' || cl == 'w' || cl == 'h') {
                letterSum += 4;
            } else if (cl == 'k') {
                letterSum += 5;
            } else if (cl == 'j' || cl == 'x') {
                letterSum += 8;
            } else {
                letterSum += 10;
            }
        }
        return letterSum;
    }

    public int lengthVal(){
        int tempVal = word.length() - 2;

        if (tempVal <= 0) { tempVal = 0; }
        else if (tempVal >= 6) { tempVal = 6; }

        return tempVal;
    }

    public int bonusVal(){
        int bonusVal;
        if (occurrences == 0) { bonusVal = 5; }
        else if (occurrences > 0 && occurrences < 6) { bonusVal = 4; }
        else if (occurrences > 5 && occurrences < 11) { bonusVal = 3; }
        else if (occurrences > 10 && occurrences < 16) { bonusVal = 2; }
        else { bonusVal = 1; }

        return bonusVal;
    }

    @Override
    public String toString(){
        return "Word: " + word + ", Occurrence: " + occurrences + " times";
    }

    public String getWord(){ return word; }
}
