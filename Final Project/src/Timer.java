public class Timer {
    private long last = System.currentTimeMillis();
    private double maxSeconds;

    public Timer(double maxSeconds) {
        this.maxSeconds = maxSeconds;
    }

    public void waitFor() {
        long now = System.currentTimeMillis();
        long end = (long) (1000 * maxSeconds) + last;
        if(end > now) {
            try {
                Thread.sleep(end - now + 1);
            } catch(InterruptedException e) {

            }
        }
        last = System.currentTimeMillis();
    }

    public double getMaxSeconds() {
        return maxSeconds;
    }
}
