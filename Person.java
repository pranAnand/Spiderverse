package spiderman;

public class Person {
    int dimension;
    String name;
    int dimSignature;

    public Person(){
        
    }
    public Person(int dim, String n, int sign){
        dimension = dim;
        name = n;
        dimSignature = sign;
    }

    public int getDimension() {
        return dimension;
    }

    public String getPersonName() {
        return name;
    }

    public int getDimensionalSignature() {
        return dimSignature;
    }
}
    

