package group.demo.models;

public class CountryCustomerCount {
    private String country;
    private int count;

    public CountryCustomerCount(String country, int count) {
        this.country = country;
        this.count = count;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
