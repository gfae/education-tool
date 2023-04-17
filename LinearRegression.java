import java.util.ArrayList;

public class LinearRegression {

    ArrayList<Integer> xCords;
    ArrayList<Integer> yCords;
    double xBar;
    double yBar;
    int aboveAverage = 0;
    int belowAverage = 0;

    LinearRegression(ArrayList<Integer> xCords, ArrayList<Integer> yCords) {
        this.xCords = xCords;
        this.yCords = yCords;
        this.xBar = meanOfPoints(this.xCords);
        this.yBar = meanOfPoints(this.yCords);
    }

    double meanOfPoints(ArrayList<Integer> cords) {
        int sum = 0;
        for (int i : cords) {
            sum += i;
        }
        return sum/cords.size();
    }

    double gradient() {
        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < xCords.size(); i++) {
            double xTemp;
            double yTemp;
            xTemp = xCords.get(i) - xBar;
            yTemp = yCords.get(i) - yBar;
            numerator += xTemp * yTemp;
            denominator += xTemp * xTemp;
        }

        return numerator/denominator;
    }

    int[][] getLinearCords() {
        double gradiant = gradient();
        double intercept = yBar - (gradiant * xBar);
        getAverageRatio(intercept, gradiant);
        int[] point1 = {0, (int) intercept};
        int[] point2 = {100, (int) ((gradiant * 100) + intercept)};
        return new int[][]{point1, point2};
    }

    void getAverageRatio(double intercept, double gradiant) {
        for (int i = 0; i < xCords.size(); i++) {
            double y = (gradiant * xCords.get(i)) + intercept;
            if (y > yCords.get(i)) {
                belowAverage++;
            } else {
                aboveAverage++;
            }
        }
    }

    int getAboveAverage() {
        return aboveAverage;
    }
    int getBelowAverage() {
        return belowAverage;
    }
}
