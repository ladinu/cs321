// test var init
// (should print 12)
class Test {
  public static void main(String[] x) {
    Body b = new Body();
    System.out.println(b.i + b.j);
  }
}
class Body {
  int i=5;
  int j=7;
}
