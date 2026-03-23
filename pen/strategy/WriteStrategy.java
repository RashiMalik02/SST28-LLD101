public interface WriteStrategy {
    public void start();
    public void write(String text, String color);
    public void close();

    
}
