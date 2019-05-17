public class Station
{
    private final String name;
    private final String line;
    private  Station previous;
    private  Station next;

    public Station(String name, String line, Station previous, Station next)
    {
        this.name = name;
        this.line = line;
        this.previous = previous;
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public Station getNext() {
        return next;
    }

    public void setNext(Station n)
    {
        next = n;
    }

    public Station getPrevious()
    {
        return previous;
    }

    public void setPrevious(Station p)
    {
        previous = p;
    }

}
