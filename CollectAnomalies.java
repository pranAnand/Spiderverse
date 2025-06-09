package spiderman;

import java.util.*;

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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 * i. The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 * is at the same Dimension (if one exists, space separated) followed by
 * the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    int hart;
    ArrayList<LinkedList<Dimension>> adjList;

    public CollectAnomalies(int h, ArrayList<LinkedList<Dimension>> adjList) {
        this.hart = h;
        this.adjList = adjList;
    }

   
    public boolean containsSp(int k) {
        for (int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).getFirst().getDimensionNumber() == k) {
                if (!(adjList.get(i).getFirst().getPersonDimension().isEmpty())) {
                    for (Person p : adjList.get(i).getFirst().getPersonDimension()) {
                        if (p.getDimension() == p.getDimensionalSignature()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }

    public String getSpider(int k){
        for(int i = 0; i < adjList.size(); i++){
            if(adjList.get(i).getFirst().getDimensionNumber() == k){
                for(Person p : adjList.get(i).getFirst().getPersonDimension()){
                    if(p.getDimensionalSignature() == p.getDimension()){
                        return p.getPersonName();
                    }
                }
            }
        }
        return "";
    }

    public void process() {
        ArrayList<Person> p = getAnomalies();
        ArrayList<Integer> trs = new ArrayList<>();
        Stack<Integer> s = new Stack<>();

        for (int i = 0; i < p.size(); i++) {
            if (containsSp(p.get(i).getDimension())) {
                trs = bfsFindPath(hart, p.get(i).getDimension());
                StdOut.print(p.get(i).getPersonName() + " ");
                StdOut.print(getSpider(p.get(i).getDimension())+" ");

                for (int j = 0; j < trs.size(); j++) {
                    s.add(trs.get(j));
                }

                while(!s.isEmpty()){
                    StdOut.print(s.pop()+" ");
                }

            } else {
                trs = bfsFindPath(hart, p.get(i).getDimension());

                StdOut.print(p.get(i).getPersonName() + " ");

                ArrayList<Integer> three = new ArrayList<>();
                for (int j = 0; j < trs.size(); j++) {
                    three.add(trs.get(j));
                    StdOut.print(trs.get(j) + " ");
                    s.add(trs.get(j));
                }

                s.pop();
                while(!s.isEmpty()){
                    StdOut.print(s.pop()+" ");
                }

            }
            StdOut.println();
        }

    }

    public ArrayList<Person> getAnomalies() {
        ArrayList<Person> p = new ArrayList<>();

        for (int i = 0; i < adjList.size(); i++) {
            if (!adjList.get(i).getFirst().getPersonDimension().isEmpty()) {
                for (Person ps : adjList.get(i).getFirst().getPersonDimension()) {
                    if (ps.getDimension() != ps.getDimensionalSignature()
                            && ps.getDimension() != hart) {
                        p.add(ps);
                    }
                }
            }
        }
        return p;
    }

    public LinkedList<Dimension> getNode(int curr) {
        for (int i = 0; i < adjList.size(); i++) {
            if(adjList.get(i).getFirst().getDimensionNumber() == curr){
                return adjList.get(i);
            }
        }
        return new LinkedList<Dimension>();
    }

    public ArrayList<Integer> bfsFindPath(int s, int g) {
        ArrayList<Integer> v = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> no = new HashMap<>();

        queue.add(s);
        v.add(s);
        no.put(s, null);

        while (!queue.isEmpty()) {
            Integer in = queue.poll();
            LinkedList<Dimension> xx = getNode(in);
            
            for (Dimension d : xx) {
                if (!v.contains(d.getDimensionNumber())) {
                    queue.add(d.getDimensionNumber());
                    v.add(d.getDimensionNumber());
                    no.put(d.getDimensionNumber(), in);
                }
            }

            if (v.contains(g)) {
                no.put(g, in);
                return sPath(no, g);
            }

        }
        return new ArrayList<Integer>();
    }

    private ArrayList<Integer> sPath(Map<Integer, Integer> prevNodes, int goal) {
        ArrayList<Integer> trop = new ArrayList<>();
        for (Integer at = goal; at != null; at = prevNodes.get(at)) {
            trop.add(at);
        }
        Collections.reverse(trop);
        return trop;
    }

    public static void main(String[] args) {

        if (args.length < 4) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
            return;
        }
        
        String dInput = args[0];
        String spiderInput = args[1];
        String hInput = args[2];
        String output = args[3];

        StdIn.setFile(dInput);

        int a = StdIn.readInt();
        int b = StdIn.readInt();
        double c = StdIn.readDouble();

        Clusters Clusters = new Clusters(b, c);
        Clusters.filer(a);

        Collider blaster = new Collider(a);

        ArrayList<LinkedList<Dimension>> clusters = Clusters.cl;
        blaster.setHashTable(clusters);

        StdIn.setFile(spiderInput);
        blaster.spiderVerse();

        StdIn.setFile(hInput);
        int startingHub = StdIn.readInt();

        StdOut.setFile(output);

        CollectAnomalies anomalies = new CollectAnomalies(startingHub, blaster.adjList);
        anomalies.process();

    }

}
