public enum Shape {
    CIRCLE(1), SEMICIRCLE(2), TRIANGLE(3), SQUARE(4), PENTAGON(5);

    private int value;

    Shape(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
