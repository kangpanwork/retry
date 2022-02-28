package code;

public class RetryTest implements Test {

    private int n;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public static void main(String[] args) {
        RetryTest retryTest = new RetryTest();
        RetryInterceptor retryInterceptor = new RetryInterceptor(retryTest, t -> {
            RetryTest retry = (RetryTest) t;
            return retry.getN() > 0;
        }, 4);
        retryTest.setN(10);
        Test test = (Test) retryInterceptor.getProxy();
        test.division();
    }

    public void division() {
        int result = n / 1;
    }
}
