package smith.c195v2;

public class Customer {

    public Customer(){}

    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;

    private int divisionID;

    public Customer(int customerID, String customerName, String address, String postalCode, String phone, int divisionID){
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    public void setCustomerID(int customerID){
        this.customerID = customerID;
    }
    public int getCustomerID(){
        return this.customerID;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public String getCustomerName(){
        return this.customerName;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    public String getPostalCode(){
        return this.postalCode;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){return this.phone;}
    public void setDivisionID(int divisionID){
        this.divisionID = divisionID;
    }

    public int getDivisionID(){
        return this.divisionID;
    }

}
