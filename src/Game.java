
import java.io.*;

public class Game {
    public Game(){
        HashTable = new DoubleHashTable<>();
    }

    public int computeScore(WordInfo wi) {
        return wi.letterSum() * wi.lengthVal() * wi.bonusVal();
    }

    public void playGame(String filename) throws IOException {

        int gameScore = 0;
        int limit = 0;

        File inputFile = new File(filename);

        BufferedReader buff = new BufferedReader(new FileReader(inputFile));

        String inputString;

        System.out.println("Playing " + filename);
        while ((inputString = buff.readLine()) != null) {
            inputString = inputString.toLowerCase();
            WordInfo word = new WordInfo(inputString,0);
            WordInfo find = HashTable.find(word);
            if (find == null) {
                if (!HashTable.insert(word)) {
                    System.out.println("Could not insert the value " + inputString);
                    continue;
                }
                limit += 1;
            }
            else {
                word = find;
            }
            // Included the comment of my debug statement to prove that my word value algorithm is functioning correctly
            // System.out.println("The score for the word " + word.getWord() + " is " + computeScore(word));
            gameScore += computeScore(word);
            word.incrementOccur();
        }


        System.out.println("The score for " + inputFile + " is: " + gameScore);
        System.out.println("----------Hash Table Statistics for " + inputFile + "----------");
        System.out.println("The total number of finds done on the hash table is: " + HashTable.getTotalFinds());
        System.out.println("The total number of probes done on the hash table is: " + HashTable.getTotalProbes());
        System.out.println("The number of items stored in the hash table is: " + HashTable.size());
        System.out.println("The physical length of the hash table is: " + HashTable.capacity());
        System.out.println("The contents of the first 20 non-null entries of the table are: \n" + HashTable.toString(limit));


    }


    private String name;
    private DoubleHashTable<WordInfo> HashTable;



    public static void main( String [ ] args ) {
        try {

            // I've modified the original print statements, as I included a new formatted name of the individual games
            // within the playGame() function output instead. Hence why the toString() method for this object is missing
            // and the function call for it for each of these has been removed.

            System.out.println();
            Game g0 = new Game(  );
            g0.playGame("game0.txt" );
            System.out.println();
            Game g1 = new Game(  );
            g1.playGame("game1.txt" );
            System.out.println();
            Game g2 = new Game(  );
            g2.playGame("game2.txt" );
            System.out.println();
            Game g3 = new Game(  );
            g3.playGame("game3.txt" );
            System.out.println();
            Game g4 = new Game(  );
            g4.playGame("game4.txt" );

        } catch(IOException e){
            e.printStackTrace();
        }


    }

}
