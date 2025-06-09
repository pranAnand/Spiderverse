package spiderman;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 * i. a (int): number of dimensions in the graph
 * ii. b (int): the initial size of the cluster table prior to rehashing
 * iii. c (double): the capacity(threshold) used to rehash the cluster table
 * 2. a lines, each with:
 * i. The dimension number (int)
 * ii. The number of canon events for the dimension (int)
 * iii. The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 * i. The dimension they are currently at (int)
 * ii. The name of the person (String)
 * iii. The dimensional signature of the person (int)
 * 
 * Step 3:
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 * all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */
import java.util.*;
public class Collider {
    public ArrayList<LinkedList<Dimension>> adjList;
    int vertex;
    int edges;

    public Collider(int num) {
        this.vertex = num;
        this.edges = 0;
        this.adjList = new ArrayList<>(num);
        for (int i = 0; i < vertex; i++) {
            adjList.add(new LinkedList<>());
        }
    }

    public void insertVertex(Dimension d, int i) {
        if (i < adjList.size()) {
            adjList.get(i).add(d);
        }

    }

    public int alreadyInIndex(Dimension d) {
        for (int i = 0; i < adjList.size(); i++) {
            if (!adjList.get(i).isEmpty()) {
                if (adjList.get(i).getFirst().getDimensionNumber() == d.getDimensionNumber()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void setHashTable(ArrayList<LinkedList<Dimension>> c) {
        int l = 0;
        int index = 0;
        for (int i = 0; i < c.size(); i++) {
            Dimension d1 = c.get(i).getFirst();
            for (Dimension d2 : c.get(i)) {
                if (d2.getDimensionNumber() != d1.getDimensionNumber()) {
                    if (alreadyInIndex(d2) > -1) {
                        int in = alreadyInIndex(d2);
                        insertVertex(d1, in);
                        insertVertex(d2, index);
                    } else 
                    {
                        insertVertex(d2, l);
                        insertVertex(d1, l);
                        insertVertex(d2, index);
                        l++;
                    }
                }
                else {
                    if (alreadyInIndex(d2) > -1) {
                        index = alreadyInIndex(d2);
                    } else {
                        insertVertex(d1, l);
                        index = l;
                        l++;
                    }
                }
            }
        }
    }

    public Dimension getDimension(int dimNum) {
        for (int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).getFirst().getDimensionNumber() == dimNum){
                return adjList.get(i).getFirst();
            }
        }
        return null;
    }

    public void spiderVerse() {
        int size = StdIn.readInt();
        for (int i = 0; i < size; i++) {
            int currD = StdIn.readInt();
            String name = StdIn.readString();
            int dimSig = StdIn.readInt();
            Person currPerson = new Person(currD, name, dimSig);
            Dimension d = getDimension(currD);
            d.addPersonInDimension(currPerson);
        }

    }

    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
            return;
        }

        String dimInput = args[0];
        String spiderInput = args[1];
        String output = args[2];

        StdIn.setFile(dimInput);

        int a = StdIn.readInt();
        int b = StdIn.readInt();
        double c = StdIn.readDouble();

        Clusters Clusters = new Clusters(b, c);
        Clusters.filer(a);

        Collider pl = new Collider(a);

        ArrayList<LinkedList<Dimension>> list = Clusters.cl;
        pl.setHashTable(list);

        StdIn.setFile(spiderInput);
        pl.spiderVerse();

        StdOut.setFile(output);

        for (int i = 0; i < pl.adjList.size(); i++) {
            for (Dimension d : pl.adjList.get(i)) {
                d.getDimensionNumber();
                StdOut.print(d + " ");
            }
            StdOut.println();
        }
    }
}