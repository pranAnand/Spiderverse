package spiderman;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */
import java.util.*;

public class TrackSpot {
    ArrayList<LinkedList<Dimension>> Collider;
    ArrayList<Integer> x = new ArrayList<>();

    public TrackSpot(ArrayList<LinkedList<Dimension>> Collider) {
        this.Collider = Collider;
    }

    public LinkedList<Dimension> childLinked(int dimNumb) {
        for (int i = 0; i < Collider.size(); i++) {
            if (Collider.get(i).getFirst().getDimensionNumber() == dimNumb) {
                return Collider.get(i);
            }
        }
        return null;
    }

    public boolean dfs(int s, int g, Set<Integer> x) {
        x.add(s);
        StdOut.print(s + " ");

        if (s == g) {

            return true;
        }

        LinkedList<Dimension> p = childLinked(s);

        if (p != null) {
            for (Dimension dole : p) {
                if (!x.contains(dole.getDimensionNumber())) {

                    boolean here = dfs(dole.getDimensionNumber(), g, x);
                    if (here) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {

        if (args.length < 4) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
            return;
        }
        String dInput = args[0];
        String spiderInput = args[1];
        String spotInput = args[2];
        String output = args[3];

        StdIn.setFile(dInput);

        int a = StdIn.readInt();
        int b = StdIn.readInt();
        double c = StdIn.readDouble();

        Clusters Clusters = new Clusters(b, c);
        Clusters.filer(a);

        Collider warp = new Collider(a);

        ArrayList<LinkedList<Dimension>> clusters = Clusters.cl;
        warp.setHashTable(clusters);

        StdIn.setFile(spiderInput);
        warp.spiderVerse();

        StdIn.setFile(spotInput);

        int currDim = StdIn.readInt();
        int targetDim = StdIn.readInt();

        StdOut.setFile(output);

        TrackSpot C = new TrackSpot(warp.adjList);
        Set<Integer> v = new HashSet<>();
        C.dfs(currDim, targetDim, v);

        ArrayList<Integer> ty = C.x;

        for (int i = 0; i < ty.size(); i++) {
            ty.get(i);
            StdOut.print(ty.get(i) + " ");
        }
    }
}