public class EmptyOrderException extends RuntimeException {
    public EmptyOrderException() {
        super("Cannot place an empty order. Please add at least one item.");
    }
}
