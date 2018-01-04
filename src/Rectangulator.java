public class Rectangulator {
  public static void main(String[] args) {
    int length = Integer.parseInt(args[0]);
    int width = Integer.parseInt(args[1]);

    Rectangle rectangle = new Rectangle(length, width);
    int area = rectangle.getArea();
    int perimeter = rectangle.getPerimeter();

    String output = String.format("*** Your Rectangle ***\n\nLength: %d\nWidth: %d\nArea: %d\nPerimeter: %d\n\n", rectangle.length, rectangle.width, area, perimeter);

    System.out.println(output);
  }
}
