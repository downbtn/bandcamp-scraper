package bandcampscraper;

class PriceType {
    private final String name;

    private PriceType(String name) { this.name = name; }

    public String toString() {
        return name;
    }

    public static final PriceType USD = new PriceType("US Dollar");
    public static final PriceType AUD = new PriceType("Australian Dollar");
    public static final PriceType GBP = new PriceType("British Pound Sterling");
    public static final PriceType CAD = new PriceType("Canadian Dollar");
    public static final PriceType EUR = new PriceType("Euro");
    public static final PriceType JPY = new PriceType("Japanese Yen");
}