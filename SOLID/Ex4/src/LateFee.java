public class LateFee implements FeeComponent {
    private final Money fee;

    public LateFee(Money fee) {
        this.fee = fee;
    }

    @Override
    public Money monthlyFee() {
        return fee;
    }
}
