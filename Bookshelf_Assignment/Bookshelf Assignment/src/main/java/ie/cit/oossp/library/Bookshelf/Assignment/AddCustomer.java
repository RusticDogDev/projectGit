package ie.cit.oossp.library.Bookshelf.Assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Customer;
import ie.cit.oossp.library.Bookshelf.Assignment.domain.Customer.CustType;
import ie.cit.oossp.library.Bookshelf.Assignment.repository.CustomerRepository;

@SpringView(name = AddCustomer.VIEW_NAME)
public class AddCustomer extends VerticalLayout implements View {
	@Autowired	
	CustomerRepository custRepo;
	private static final long serialVersionUID = 1L;	
	public static final String VIEW_NAME = "AddUser";	
	private Notification notif;	
	private NativeSelect<String> userType;
	private final TextField userName;
	private final TextField fName;
	private final TextField lName;
	private final TextField phoneNum;	
	private TextField password;	
	private final Button editButton;
	private final Button saveButton;
	
	public AddCustomer()
	{									
		this.notif = new Notification("");
		this.userType = new NativeSelect<String>("Genre:");	
		this.userName = new TextField("Email Address:");
		this.fName = new TextField("First Name:");
		this.lName = new TextField("Last Name:");
		this.phoneNum = new TextField("Phone Number:");		
		this.password = new TextField("Password:");
		this.editButton = new Button("Edit");
		this.saveButton = new Button("Create");
	}
		
	@PostConstruct
    void init() 
    {			
		setSizeFull();		
		editButton.setVisible(false);
		userName.setWidth("300px");
		fName.setWidth("300px");
		lName.setWidth("300px");
		phoneNum.setWidth("300px");		
		password.setWidth("300px");		
		
		userName.setRequiredIndicatorVisible(true);
		fName.setRequiredIndicatorVisible(true);
		lName.setRequiredIndicatorVisible(true);
		password.setRequiredIndicatorVisible(true);
		userType.setRequiredIndicatorVisible(true);				
		
		if(VaadinSession.getCurrent().getAttribute("id") !=null)
		{
			String[] custTypes = getNames(CustType.class);
			ArrayList<String> custTypeList = new ArrayList<String>(Arrays.asList(custTypes));					
			userType = new NativeSelect<String>("User Type:", custTypeList);
			userType.setRequiredIndicatorVisible(true);	
			userType.setEmptySelectionAllowed(false);
			userType.setSelectedItem(custTypeList.get(0));
			userType.setWidth("300px");
			saveButton.addClickListener(event -> 
			{    	    
				if(fName.getValue().contentEquals("") || lName.getValue().contentEquals("") || password.getValue().contentEquals(""))
	    		{	    			
	    			notif = new Notification("Error","All fields must be filled in correctly", Notification.Type.ERROR_MESSAGE);
	    			notif.setPosition(Position.TOP_RIGHT);
	    			notif.show(Page.getCurrent());
	    			return;	    			
	    		}
	    		else
	    		{   	    			
	    			List<Customer> custList = custRepo.findAll();	    				    				    				    
	    			Long phoneNumber = (long) 0;
	    			try 
	            	{
	            		  InternetAddress emailAddr = new InternetAddress(userName.getValue());
	            		  emailAddr.validate();
	            	} 	
	            	catch (AddressException ex) 
	            	{
	            		notif = new Notification("Error","Not a valid E-mail Address", Notification.Type.ERROR_MESSAGE);
	            		notif.setPosition(Position.TOP_RIGHT);
	        			notif.show(Page.getCurrent());
	            	}  
	    			if(phoneNum.getValue() != ""){
	    				try{
		    				phoneNumber = Long.parseLong(phoneNum.getValue());   
		    				  // is an integer!
		    				} 
		    			catch (NumberFormatException e) {
	    					notif = new Notification("Error","Phone number value entered is not a numeric value", Notification.Type.ERROR_MESSAGE);
	    	    			notif.setPosition(Position.TOP_RIGHT);
	    	    			notif.show(Page.getCurrent());
	    	    			return;	
	    				}
	    			}	    			
	    			String word = password.getValue();        	    
	            	if (word != null && (word.length() < 8 || !word.matches(".*\\d.*"))) 
	            	{                
	            		notif = new Notification("Error","Password must be at least 8 characters long and contain at least one number", Notification.Type.ERROR_MESSAGE);
	            		notif.setPosition(Position.TOP_RIGHT);
	        			notif.show(Page.getCurrent());
	        			return;
	        		}     
	    			if(!saveButton.getCaption().contentEquals("Save")){boolean valid = true;
	    			for(Customer cr : custList){
	    				if(cr.getUserName() == userName.getValue())
	    				{
	    					valid = false;
	    				}
	    			}
	    			if(!valid){
	    				notif = new Notification("Error","Username already exists", Notification.Type.ERROR_MESSAGE);
		    			notif.setPosition(Position.TOP_RIGHT);
		    			notif.show(Page.getCurrent());
		    			return;
	    			}}	    				    		
	    			notif = new Notification("Information","User was successfully saved", Notification.Type.HUMANIZED_MESSAGE);    				
	    			notif.setPosition(Position.BOTTOM_RIGHT);
	    			notif.show(Page.getCurrent());	    				    					
	    			Customer newCustomer = new Customer((long) 1, fName.getValue(), lName.getValue(), phoneNumber, password.getValue(), userName.getValue(), userType.getValue());    			
	    			if(saveButton.getCaption().contentEquals("Save"))
	    			{
	    				custRepo.update(newCustomer);
	    			}
	    			else{
	    				custRepo.save(newCustomer);
	    			}	    			
	    			userType.setReadOnly(true);
	    			userName.setReadOnly(true);
	    			fName.setReadOnly(true);
	    			lName.setReadOnly(true);
	    			phoneNum.setReadOnly(true);
	    			password.setReadOnly(true);
	    			editButton.setVisible(true);
	    			saveButton.setCaption("Save");
	    			saveButton.setVisible(false);
	    		}
	    	});
			editButton.addClickListener(event -> 
			{				
				userType.setReadOnly(false);    			
    			fName.setReadOnly(false);
    			lName.setReadOnly(false);
    			phoneNum.setReadOnly(false);
    			password.setReadOnly(false);
    			editButton.setVisible(false);
    			saveButton.setVisible(true);
			});
			VerticalLayout fields = new VerticalLayout(userName, password, userType, fName, lName, phoneNum, editButton, saveButton);
	        fields.setCaption("Create a user here");
	        fields.setSpacing(true);
	        fields.setMargin(new MarginInfo(true, true, true, false));
	        fields.setSizeUndefined();
	        
	        VerticalLayout viewLayout = new VerticalLayout(fields);
	        viewLayout.setSizeFull();
	        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);                               
	        addComponent(viewLayout);					
		}																						
    }
	public static String[] getNames(Class<? extends Enum<?>> e) {
	    return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}
	public void enter(ViewChangeEvent event) {        
    }
}
