package spiderman;
import java.util.*;

public class Dimension {
    int dimNum;
    int canonEvents;
    double dimWeight;
    public ArrayList<Person> personDimension;

    public Dimension(int dNum, int cEvent, double dWeight, ArrayList<Person> pDimension) {
        this.dimNum = dNum;
        this.canonEvents = cEvent;
        this.dimWeight = dWeight;
        if (personDimension == null) {
            this.personDimension = new ArrayList<Person>();
        }
        else {
            this.personDimension = pDimension;
        }
    }
    public int getDimensionNumber() {
        return dimNum;
    }
    public ArrayList<Person> getPersonDimension() {
        return personDimension;
    }
    public String toString() {
        return String.valueOf(dimNum);
    }
    public void addPersonInDimension(Person p) {
        personDimension.add(p);
    }
}
