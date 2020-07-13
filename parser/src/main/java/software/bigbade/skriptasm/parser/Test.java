package software.bigbade.skriptasm.parser;

public class Test {
    private Test() {
        System.out.println("Test");
    }

    public void test() {
        if(isTrue()) {
            System.out.println("Yep");
        } else {
            System.out.println("Nope");
        }
    }

    private boolean isTrue() {
        return true;
    }
}
