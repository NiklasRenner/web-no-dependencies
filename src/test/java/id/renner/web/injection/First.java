package id.renner.web.injection;

@Inject
public class First {
    private final Second second;

    public First(Second second) {
        this.second = second;
    }

    public Second getSecond() {
        return second;
    }
}