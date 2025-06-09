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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */
import java.util.*;
public class Clusters {
    double threshold;
    int tSize;
    int counter;
    public ArrayList<LinkedList<Dimension>> cl;

    public Clusters(int iSize, double t){
        tSize = iSize;
        threshold = t;

        cl = new ArrayList<>(tSize);
        for (int i = 0; i < tSize; i++){
            cl.add(new LinkedList<>());
        }

    }
    public void insertDimension (int dimensionNum, int cannonEvents, int dimensionWeight ){
        Dimension dimen = new Dimension(dimensionNum, cannonEvents, dimensionWeight,null);
        int iss = dimensionNum % tSize;
        cl.get(iss).addFirst(dimen);
        counter++;


        if ((double) counter / tSize >= threshold) {
            rehash();
        }

    }

    public void rehash(){
        ArrayList<LinkedList<Dimension>> nC = new ArrayList<>(tSize * 2);

        for (int i = 0; i < tSize * 2; i++) {
            nC.add(new LinkedList<>());
        }

        for (LinkedList<Dimension> list : cl) {
            for (Dimension d : list) {
                int dim = d.getDimensionNumber();
                int nI = dim % (tSize * 2);
                nC.get(nI).addFirst(d);
            }
        }
        cl = nC;
        tSize *= 2;
    }

    public ArrayList<Dimension> previousClusterList(Dimension d){

        ArrayList<Dimension> listDim = new ArrayList<>(2);

        for(int i = 0; i < cl.size(); i++){
            if(cl.get(i).getFirst().equals(d)){
                int j = i - 1;
                int k = i - 2;

                if(j < 0){
                    j = cl.size() + j;
                }
                if(k < 0){
                    k = cl.size() + k;
                }

                listDim.add(cl.get(j).getFirst());
                listDim.add(cl.get(k).getFirst());

            }
        }
        return listDim;
    }

    public void filer(int a) {
        this.cl.clear();
        this.counter = 0;
        for (int i = 0; i < tSize; i++) {
            this.cl.add(new LinkedList<>());
        }

        for (int i = 0; i < a; i++) {
            int aa = StdIn.readInt();
            int bb = StdIn.readInt();
            int cc = StdIn.readInt();
            insertDimension(aa, bb, cc);
        }

        for(int i = 0; i<cl.size();i++){
            ArrayList<Dimension> listDim2 = previousClusterList(cl.get(i).getFirst());
            cl.get(i).add(listDim2.get(0));
            cl.get(i).add(listDim2.get(1));
        }
    }

    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }

        String input = args[0];
        String output = args[1];

        StdIn.setFile(input);

        int a = StdIn.readInt();
        int b = StdIn.readInt();
        double c = StdIn.readDouble();

        Clusters clus = new Clusters(b, c);
        clus.filer(a);

        StdOut.setFile(output);
        for (int i = 0; i < clus.cl.size(); i++) {
            for (Dimension dimension : clus.cl.get(i)) {
                dimension.getDimensionNumber();
                StdOut.print(dimension + " ");
            }
            StdOut.println();
        }
    }
}


