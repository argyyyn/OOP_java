package product.checkout;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    public static class Line {
        public final String name;
        public final double value;
        public Line(String name, double value){ this.name=name; this.value=value; }
    }
    private final List<Line> lines = new ArrayList<>();
    private double total;

    public void add(String name, double value) { lines.add(new Line(name, value)); }
    public void setTotal(double total) { this.total = total; }
    public List<Line> lines(){ return lines; }
    public double total(){ return total; }
}

