package sample;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Controller {

    @FXML
    private RadioButton uniform;
    @FXML
    private RadioButton normal;
    @FXML
    private TextField rangeA;
    @FXML
    private TextField rangeB;
    @FXML
    private TextField rangeC;
    @FXML
    private TextField catToysToOrder;
    @FXML
    private TextField to;
    @FXML
    private TextField from;
    @FXML
    private TextField expectancy;
    @FXML
    private TextField standardDeviation;
    @FXML
    private TextField flagSalePrice;
    @FXML
    private TextField priceOfCatGivenAsGiftInCaseOfLack;
    @FXML
    private TextField priceOfFlagsSurplusSale;
    @FXML
    private TextField response;
    @FXML
    private TextField semi;




    @FXML
    public void onRadioClicked() {
        if (uniform.isSelected()) {
            ArrayList<Double> resUniformDist = new ArrayList<>(setResUniformDist());
//          System.out.println(resUniformDist.toString());  //  [341.0, 1370.0, 1500.0, 1658.0]
//            ArrayList<Double> dta = new ArrayList<>(setData());
//            ArrayList<Double> cts = new ArrayList<>(setCosts());
//          System.out.println(dta.toString());  //  [15.0, 2.0, 1.0]
//          System.out.println(cts.toString());  //  [6.0, 4.5, 3.0]
            double x0 = resUniformDist.get(0);
            double x1 = resUniformDist.get(1);
            double x2 = resUniformDist.get(2);
            double x3 = resUniformDist.get(3);
            double res1 = calcIntegral(x0, x2) * 10000.0 / 10000.0;
            double res2 = calcIntegral(x2, x3) * 10000.0 / 10000.0;
            double h = 1 / (Double.parseDouble(to.getText()) - Double.parseDouble(from.getText()));
            double resFinal1 = (res1 + res2) * h;
            double res3 = calcIntegral(x0, x1) * 10000.0 / 10000.0;
            double res4 = calcIntegral(x1, x3) * 10000.0 / 10000.0;
            double resFinal2 = (res3 + res4) * h;
            if (resFinal1 > resFinal2) semi.setText("We will recommend Aci to purchase " + x2 + " flags.");
            else semi.setText("We will recommend Aci to purchase " + x1 + " flags.");

            double catToOrderRes = calcAmountOfCatsToOrder(x2, x3) * 10000.0 / 10000.0;
            catToysToOrder.setText("BAR will order " + Math.ceil(catToOrderRes) + " cats.");
        }
        if (normal.isSelected()) naturalDist();

    }

    @FXML
    public double profitExpectancy(double v, double pi, double c, double h) {
        double res = (v + pi - c) / (v + pi - h);
        return res;
    }
    @FXML
    public double profitExpectancy(double c) {
        double v = Double.parseDouble(flagSalePrice.getText());
        double pi = Double.parseDouble(priceOfCatGivenAsGiftInCaseOfLack.getText());
        double h = Double.parseDouble(priceOfFlagsSurplusSale.getText());
        double res = profitExpectancy(v, pi, c, h);
        return res;
    }
    @FXML
    public ArrayList<Double> uniformDist(double uniformFrom, double uniformTo) {
        boolean inRange = false;
        int count = 0;
        ArrayList<Double> js = new ArrayList<>();
        js.add(Double.parseDouble(rangeA.getText()));
        js.add(Double.parseDouble(rangeB.getText()));
        js.add(Double.parseDouble(rangeC.getText()));
        for ( int j=2 ; j>=0; j-- ) {
            double pe = profitExpectancy(js.get(j));
            double res = Math.ceil(uniformFrom + (pe * (uniformTo - uniformFrom)));
            if (res > 1500 && j == 2 && count == 0) {
                inRange = true;
                count++;
            }
            if (res <= 1500 && res > 1000 && j == 1 && count == 0) {
                inRange = true;
                count++;
            }
            if (res > 0 && res <= 1000 && j == 0 && count == 0) {
                inRange = true;
                count++;
            }
            count = 0;
            if (inRange) {
                js.set(j, res);
                if (j > 0) {
                    for (int i=0; i<j;i++) js.remove(i);
                }
                break;
            }
            else if (j == 2)js.set(j, 1500.0);
            else if (j == 1)js.set(j, 1000.0);
            else js.set(j, 0.0);
        }
        return js;
    }

    @FXML
    public void naturalDist() {
        double fi = 0;
        boolean inRange = false;
        double sd = Double.parseDouble(standardDeviation.getText());
        double myu = Double.parseDouble(expectancy.getText());
        int count = 0;
        double res = 0;
        ArrayList<Double> js = new ArrayList<>();
        js.add(Double.parseDouble(rangeA.getText()));
        js.add(Double.parseDouble(rangeB.getText()));
        js.add(Double.parseDouble(rangeC.getText()));

        for ( int j=2 ; j>=0; j-- ) {
            double pe = profitExpectancy(js.get(j));
            if (pe == 0.875) fi = 1.15;
                res = (fi * sd) + myu;
            if (res > 1500 && j == 2 && count == 0) {
                inRange = true;
                count++;
            }
            if (res <= 1500 && res > 1000 && j == 1 && count == 0) {
                inRange = true;
                count++;
            }
            if (res > 0 && res <= 1000 && j == 0 && count == 0) {
                inRange = true;
                count++;
            }
            count = 0;
        }
        if (inRange) response.setText("for the final we will recommend to order " + Math.ceil(res) + " crowns.");
    }

    @FXML
    public ArrayList<Double> setData() {
        ArrayList<Double> data = new ArrayList<>();
        double v = Double.parseDouble(flagSalePrice.getText());
        double pi = Double.parseDouble(priceOfCatGivenAsGiftInCaseOfLack.getText());
        double h = Double.parseDouble(priceOfFlagsSurplusSale.getText());
        data.add(v);
        data.add(pi);
        data.add(h);
        return data;
    }

    @FXML
    public ArrayList<Double> setCosts() {
        ArrayList<Double> costs = new ArrayList<>();
        costs.add(Double.parseDouble(rangeA.getText()));
        costs.add(Double.parseDouble(rangeB.getText()));
        costs.add(Double.parseDouble(rangeC.getText()));
        return costs;
    }

    public ArrayList<Double> setResUniformDist() {
        ArrayList<Double> resUniformDist = new ArrayList<>(uniformDist(Double.parseDouble(from.getText()),
                Double.parseDouble(to.getText())));
        resUniformDist.add(0, Double.parseDouble(from.getText()));
        resUniformDist.add(3, Double.parseDouble(to.getText()));
        return resUniformDist;
    }

    public double f1(double d) {
        ArrayList<Double> cts = new ArrayList<>(setCosts());
        ArrayList<Double> resUniformDist = new ArrayList<>(setResUniformDist());
        double v = Double.parseDouble(flagSalePrice.getText());
        double c = cts.get(2);
        double q = resUniformDist.get(2);
        double h = Double.parseDouble(priceOfFlagsSurplusSale.getText());
        return ((v * ((d * d)/2)) - (c * q) + ((h * q) - ((h * d * d)/2)));
    }

    public double f2(double d) {
        ArrayList<Double> cts = new ArrayList<>(setCosts());
        ArrayList<Double> resUniformDist = new ArrayList<>(setResUniformDist());
        double v = Double.parseDouble(flagSalePrice.getText());
        double c = cts.get(2);
        double q = resUniformDist.get(2);
        double pi = Double.parseDouble(priceOfCatGivenAsGiftInCaseOfLack.getText());
        return ((v * q) - (c * q) - (((pi * d * d)/2) + (pi * q)));
    }

    public double calcIntegral(double a, double b)
    {
        ArrayList<Double> resUniformDist = new ArrayList<>(setResUniformDist());
        double s1 = 0;
        double s2 = 0;
        if(a == resUniformDist.get(0) && b == resUniformDist.get(2)) {
            s1 = f1(b) - f1(a);
        }
        if(a == resUniformDist.get(2) && b == resUniformDist.get(3)) s2 = f2(b) - f2(a);
        return (s1 + s2);
    }

    public double calcAmountOfCatsToOrder(double a, double b) {
        double s = f(b) - f(a);
        double h = 1 / (Double.parseDouble(to.getText()) - Double.parseDouble(from.getText()));
        return (s * h);
    }

    public double f(double d) {
        ArrayList<Double> resUniformDist = new ArrayList<>(setResUniformDist());
        double x2 = resUniformDist.get(2);
        double x3 = resUniformDist.get(3);
        double res = (((d * d)/2) - (x2 * d));
        return res;
    }
}