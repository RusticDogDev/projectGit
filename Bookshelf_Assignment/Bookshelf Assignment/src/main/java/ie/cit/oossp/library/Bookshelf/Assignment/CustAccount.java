package ie.cit.oossp.library.Bookshelf.Assignment;

import javax.annotation.PostConstruct;

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
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Customer;
import ie.cit.oossp.library.Bookshelf.Assignment.repository.CustomerRepository;

@SpringView(name = CustAccount.VIEW_NAME)
public class CustAccount extends VerticalLayout implements View {
	@Autowired	
	CustomerRepository custRepo;
	private static final long serialVersionUID = 1L;	
	public static final String VIEW_NAME = "Account";	
	private Notification notif;
	private final TextField fName;
	private final TextField lName;
	private final TextField phoneNum;
	private final PasswordField password;
	private final Button editButton;
	private final Button saveButton;
	
	public CustAccount()
	{							
		this.notif = new Notification("");
		this.fName = new TextField("First Name:");
		this.lName = new TextField("Last Name:");
		this.phoneNum = new TextField("Phone Number:");		
		this.password = new PasswordField("Password:");
		this.editButton = new Button("Edit Account");
		this.saveButton = new Button("Save");
	}
		
	@PostConstruct
    void init() 
    {			
		setSizeFull();		
		saveButton.setVisible(false);
		fName.setWidth("300px");
		lName.setWidth("300px");
		phoneNum.setWidth("300px");
		password.setWidth("300px");			
		
		if(VaadinSession.getCurrent().getAttribute("id") !=null)
		{
			Long id = (Long) VaadinSession.getCurrent().getAttribute("id");				
			Customer cr = custRepo.findOne(id);	
			
			fName.setValue(cr.getFirstName());
			lName.setValue(cr.getLastName());
			phoneNum.setValue(cr.getPhoneNumber().toString());
			password.setValue(cr.getPassword());
		}				
		
		fName.setReadOnly(true);
		lName.setReadOnly(true);
		phoneNum.setReadOnly(true);
		password.setReadOnly(true);
		
		editButton.addClickListener(event -> 
		{    	    
			fName.setReadOnly(false);
			lName.setReadOnly(false);
			phoneNum.setReadOnly(false);
			password.setReadOnly(false);
			fName.setRequiredIndicatorVisible(true);
			lName.setRequiredIndicatorVisible(true);
			password.setRequiredIndicatorVisible(true);		
			editButton.setVisible(false);
			saveButton.setVisible(true);
    	});
		
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
    			Long id = (Long) VaadinSession.getCurrent().getAttribute("id");
    			Long phoneNumber = (long) 0;    			
    			try{
    				phoneNumber = Long.parseLong(phoneNum.getValue());   
    				  // is an integer!
    				} catch (NumberFormatException e) {
    					notif = new Notification("Error","Phone number value entered is not a numeric value", Notification.Type.ERROR_MESSAGE);
    	    			notif.setPosition(Position.TOP_RIGHT);
    	    			notif.show(Page.getCurrent());
    	    			return;	
    				} 
    			
    			Customer cr = new Customer(id, fName.getValue(), lName.getValue(), phoneNumber, password.getValue(), (String) getSession().getAttribute("user"), (String) getSession().getAttribute("custType"));    			
    			custRepo.update(cr);
    			notif = new Notification("Information","Data successfully saved", Notification.Type.HUMANIZED_MESSAGE);    				
    			notif.setPosition(Position.BOTTOM_RIGHT);
    			notif.show(Page.getCurrent());
    			
    			fName.setReadOnly(true);
    			lName.setReadOnly(true);
    			phoneNum.setReadOnly(true);
    			password.setReadOnly(true);
    			editButton.setVisible(true);
    			saveButton.setVisible(false);
    		}
    	});
		
		VerticalLayout fields = new VerticalLayout(fName, lName, phoneNum, password, editButton, saveButton);
        fields.setCaption("Edit your account details here");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();
        
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);                               
        addComponent(viewLayout);															
    }
	
	public void enter(ViewChangeEvent event) {        
    }
}
