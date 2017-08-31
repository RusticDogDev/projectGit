package ie.cit.oossp.library.Bookshelf.Assignment.domain;

public class Customer {

	public enum CustType{
		user,
		admin
	}
		
	private Long id;
	private String firstName;
	private String lastName;
	private Long phoneNumber;	
	private String password;
	private String userName;	
	private String custType;

	public Customer(Long id, String firstName, String lastName, Long phoneNumber, String password, String userName, String custType) 
	{
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;		
		this.password = password;
		this.userName = userName;
		this.custType = custType;
	}
		
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Long getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getCustType() {
		return custType;
	}

	public void setCustType(String CustType) {
		this.custType = CustType;
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", password=" + password + ", userName=" + userName + ", custType=" + custType + " ]";
	}	
}
