import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private LinkedList<Case> processedCases;
    private LinkedList<Case> unprocessedCases;
    private File trainingSet;
    private File testingSet;


    public Main(String trainingSetPath, String testingSetPath, int k) {
        trainingSet = new File(trainingSetPath);
        testingSet = new File(testingSetPath);
        processedCases = new LinkedList<>();
        unprocessedCases = new LinkedList<>();
        getCasesFromFile(trainingSet,processedCases);
        getCasesFromFile(testingSet,unprocessedCases);

        int guessed= 0;
        for (Case testCase: unprocessedCases) {
            LinkedList<Pair<Case, Double>> distances = calculateKNearestDistances(k,processedCases, testCase);

            HashMap<String, Integer> map = getAppearenceCount(distances);
            //System.out.println(distances);
            //System.out.println(map);
            //System.out.println(map.entrySet().stream().max((a, b) -> a.getValue() - b.getValue()).get().getKey());
            if (testCase.getDecisionAttribute().equals( map.entrySet().stream().max((a, b) -> a.getValue() - b.getValue()).get().getKey())){
               System.out.println("true" + map.entrySet().stream().max((a, b) -> a.getValue() - b.getValue()).get().getKey());
                guessed++;
            }
            else System.out.println("false" + map.entrySet().stream().max((a, b) -> a.getValue() - b.getValue()).get().getKey());


        }
        double guessedperc = (double)guessed/30*100;
        System.out.println("guessed " + guessed + "/30");
//        System.out.println(guessedperc);
        while (true){
            addCaseManually(new Scanner(System.in), k);
        }


    }

    public HashMap<String, Integer> getAppearenceCount(LinkedList<Pair<Case, Double>> distances) {
        HashMap<String, Integer> map = new HashMap<>();
        for (Pair<Case, Double> p : distances) {
            String s = p.getKey().getDecisionAttribute();
            if (map.containsKey(s)) {
                int i = map.get(s);
                i++;
                map.replace(s, i);
            } else {
                map.put(s, 1);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter k");
        int k = scanner.nextInt();

            new Main("src/train.txt", "src/test.txt", k);

    }

    public void addCaseManually(Scanner scanner,int k) {
        System.out.println("Please enter new data");
        String data = scanner.nextLine();
        Case added = new Case();
        added.setAttributes(data);

        LinkedList<Pair<Case, Double>> distances = calculateKNearestDistances(k,processedCases, added);
        HashMap<String, Integer> map = getAppearenceCount(distances);
        System.out.println("It's probably: " + map.entrySet().stream().max((a, b) -> a.getValue() - b.getValue()).get().getKey());
    }

    public LinkedList<Pair<Case, Double>> calculateKNearestDistances(int k, LinkedList<Case> training,Case testCase) {
        LinkedList<Pair<Case, Double>> distances = new LinkedList<>();
        for (Case c : training) {
            Double dist = testCase.calculateDistanceBetween(c.getAttributes());
            if (dist != 0) {
                if (distances.size() <=  k) {
                    distances.add(new Pair<>(c, dist));

                } else if (distances.getLast().getValue() > dist) {
                    distances.removeLast();
                    distances.add(new Pair<>(c, dist));
                }
                distances.sort(new DistanceComparator());


            }


        }
        return distances;
    }


    public void getCasesFromFile(File file, LinkedList linkedList) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                linkedList.add(new Case(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}

//class DistanceComparator implements Comparator<Case> {
//    public int compare(Case s1, Case s2) {
//        double x = s1.getDistance() - s2.getDistance();
//        if(x == 0)return 0;
//        if(x<0) return  -1;
//        return 1;
//    }

class DistanceComparator implements Comparator<Pair<Case, Double>> {
    public int compare(Pair<Case, Double> s1, Pair<Case, Double> s2) {
        Double x = s1.getValue() - s2.getValue();
        if (x == 0) return 0;
        if (x < 0) return -1;
        return 1;
    }
}