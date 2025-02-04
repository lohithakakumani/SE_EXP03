import javax.swing.*;
import java.awt.*;

public class QuadraticSolver {

    public static void main(String[] args) {
        // Example coefficients for testing
        double a = 1.0;
        double b = -3.0;
        double c = 2.0;

        // Solve the quadratic equation
        solveQuadratic(a, b, c);

        // Plot the quadratic equation
        plotQuadratic(a, b, c);
    }

    // Method to solve the quadratic equation and print solutions
    public static void solveQuadratic(double a, double b, double c) {
        double discriminant = b * b - 4 * a * c;

        System.out.println("Solving equation: " + a + "x^2 + " + b + "x + " + c + " = 0");

        if (discriminant > 0) {
            double x1 = (-b + Math.sqrt(discriminant)) / (2 * a);
            double x2 = (-b - Math.sqrt(discriminant)) / (2 * a);
            System.out.println("Solutions are real and distinct: x1 = " + x1 + ", x2 = " + x2);
        } else if (discriminant == 0) {
            double x1 = -b / (2 * a);
            System.out.println("One real solution: x1 = " + x1);
        } else {
            double realPart = -b / (2 * a);
            double imaginaryPart = Math.sqrt(-discriminant) / (2 * a);
            System.out.println("Complex solutions: x1 = " + realPart + " + " + imaginaryPart + "i, x2 = " + realPart + " - " + imaginaryPart + "i");
        }
    }

    // Method to plot the quadratic function
    public static void plotQuadratic(double a, double b, double c) {
        JFrame frame = new JFrame("Quadratic Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new QuadraticGraph(a, b, c));
        frame.setVisible(true);
    }

    // Custom JPanel for drawing the graph
    static class QuadraticGraph extends JPanel {
        private final double a, b, c;

        public QuadraticGraph(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Draw axes
            g2d.drawLine(0, height / 2, width, height / 2); // X-axis
            g2d.drawLine(width / 2, 0, width / 2, height); // Y-axis

            // Scale factors for the graph
            double scaleX = 20; // Pixels per unit on X-axis
            double scaleY = 20; // Pixels per unit on Y-axis

            // Draw the quadratic function
            g2d.setColor(Color.RED);
            for (int xPixel = 0; xPixel < width; xPixel++) {
                double x = (xPixel - width / 2) / scaleX; // Convert pixel to graph X-coordinate
                double y = a * x * x + b * x + c;         // Calculate Y-coordinate
                int yPixel = (int) (height / 2 - y * scaleY); // Convert graph Y-coordinate to pixel

                if (xPixel > 0) { // Draw lines between points
                    g2d.drawLine(xPixel - 1, prevY, xPixel, yPixel);
                }
                prevY = yPixel;
            }
        }

        private int prevY; // To store the previous Y-pixel for line drawing
    }
}
