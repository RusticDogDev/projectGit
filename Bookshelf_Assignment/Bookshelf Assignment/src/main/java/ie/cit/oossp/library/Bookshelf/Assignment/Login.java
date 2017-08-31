package ie.cit.oossp.library.Bookshelf.Assignment;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
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

@SpringView(name = Login.VIEW_NAME)
public class Login extends VerticalLayout implements View {   
	@Autowired	
	CustomerRepository custRepo;
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "";	
	private Notification notif;	
	private final TextField user;
	private final PasswordField password;	
	private final Button loginButton;
	
	public Login()
	{					
		this.notif = new Notification("");
		this.user = new TextField("E-mail:");
		this.password = new PasswordField("Password:");
		this.loginButton = new Button("Login");
	}	
    
	@PostConstruct
    void init() 
    {
    	Binder<String> binder = new Binder<String>();    	
    	loginButton.addClickListener(event -> {
    		List<Customer> customerList = custRepo.findAll();
    		String username = this.user.getValue();
            String password = this.password.getValue();        
    		for(int i =0; i < customerList.size(); i++)
    		{
				if(username.contentEquals(customerList.get(i).getUserName()) && password.contentEquals(customerList.get(i).getPassword()) )
				{
					getSession().setAttribute("user", username);
					getSession().setAttribute("id", customerList.get(i).getId());
					getSession().setAttribute("custType", customerList.get(i).getCustType());
					getUI().getNavigator().navigateTo(MainView.VIEW_NAME);
					return;
				}
				else
				{
					notif = new Notification("Error","Username or Password were entred incorrectly", Notification.Type.ERROR_MESSAGE);
	        		notif.setPosition(Position.TOP_RIGHT);
	    			notif.show(Page.getCurrent());
				}
    		}                                 
    	});
    	
    	setSizeFull();
		user.setWidth("300px");        
        user.setRequiredIndicatorVisible(true);
        
        password.setWidth("300px");               
        password.setValue("");
        
        binder.forField(user).asRequired("This is a required Field");
        user.addValueChangeListener(event -> 
        {
        	String email = event.getValue();        	
        	try 
        	{
        		  InternetAddress emailAddr = new InternetAddress(email);
        		  emailAddr.validate();
        	} 	
        	catch (AddressException ex) 
        	{
        		notif = new Notification("Error","Not a valid E-mail Address", Notification.Type.ERROR_MESSAGE);
        		notif.setPosition(Position.TOP_RIGHT);
    			notif.show(Page.getCurrent());
        	}        	        	
        });
        binder.forField(password).asRequired("This is a required Field");
        password.addValueChangeListener(event -> 
        {
        	String word = event.getValue();        	    
        	if (word != null && (word.length() < 8 || !word.matches(".*\\d.*"))) 
        	{                
        		notif = new Notification("Error","Password must be at least 8 characters long and contain at least one number", Notification.Type.ERROR_MESSAGE);
        		notif.setPosition(Position.TOP_RIGHT);
    			notif.show(Page.getCurrent());
    		}            
        });
        VerticalLayout fields = new VerticalLayout(user, password, loginButton);
        fields.setCaption("Please enter your details to log into the application");
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();
        
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);                               
        addComponent(viewLayout);
    }

    public void enter(ViewChangeEvent event) {
        user.focus();
    }    	
}
