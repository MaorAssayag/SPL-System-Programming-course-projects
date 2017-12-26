package bgu.spl.a2;

public class DummyCallback implements callback {
    public boolean wasCalled = false;

    public void call() {
        this.wasCalled = true;
    }
}