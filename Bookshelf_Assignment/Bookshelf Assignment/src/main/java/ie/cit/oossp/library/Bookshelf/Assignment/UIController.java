package ie.cit.oossp.library.Bookshelf.Assignment;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Customer.CustType;

@Theme("valo")
@SpringUI
public class UIController extends UI {
    
	private static final long serialVersionUID = 1L;	
    @Autowired
    private SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest request) {
    	final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        final CssLayout navBar = new CssLayout();
        navBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navBar.addComponent(createNavigationButton("Logout", Login.VIEW_NAME));
        navBar.addComponent(createNavigationButton("Home", MainView.VIEW_NAME));
        navBar.addComponent(createNavigationButton("Available To Rent", BookView.VIEW_NAME));
        navBar.addComponent(createNavigationButton("Return Books / Extend Loans", ReturnBooks.VIEW_NAME));
        navBar.addComponent(createNavigationButton("Account Settings", CustAccount.VIEW_NAME));
        navBar.setVisible(false);
        root.addComponent(navBar);
        
        final CssLayout adminBar = new CssLayout();
        adminBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        adminBar.addComponent(createNavigationButton("Logout", Login.VIEW_NAME));
        adminBar.addComponent(createNavigationButton("Home", MainView.VIEW_NAME)); 
        adminBar.addComponent(createNavigationButton("Add Book", AddBook.VIEW_NAME)); 
        adminBar.addComponent(createNavigationButton("Remove Books", RemoveBooks.VIEW_NAME)); 
        adminBar.addComponent(createNavigationButton("Add User", AddCustomer.VIEW_NAME));         
        adminBar.addComponent(createNavigationButton("Account Settings", CustAccount.VIEW_NAME));
        adminBar.setVisible(false);
        root.addComponent(adminBar);               
                     
        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);        
        
        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);
        
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            
		private static final long serialVersionUID = 1L;

		@Override
        public boolean beforeViewChange(ViewChangeEvent event) 
		{
            
            boolean isLoggedIn = getSession().getAttribute("user") != null;
            boolean isLoginView = event.getNewView() instanceof Login;              
            if (!isLoggedIn && !isLoginView) {                           	
                getNavigator().navigateTo(Login.VIEW_NAME);
                return false;
            } 
            else if (isLoggedIn && isLoginView) 
            {                
                return false;
            }
            else if(isLoggedIn)
            {            	            
            	String cust = (String) getSession().getAttribute("custType");
            	String admin = CustType.admin.toString();
            	if(cust.contentEquals(admin))
            	{
            		adminBar.setVisible(true);
            	}
            	else
            	{
            		navBar.setVisible(true);
            	}        		
            }
            else if(!isLoggedIn)
            {
            	navBar.setVisible(false);
            	adminBar.setVisible(false);
            }
            return true;
        }
            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        });
    }
        
	private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);        
        button.addClickListener(event -> 
    	{
        	if(caption == "Logout")
        	{
        		getSession().setAttribute("user", null);
        		getUI().getNavigator().navigateTo("");
        	}
        	else
        	{
        		getUI().getNavigator().navigateTo(viewName);
        	}
    		
    	});
        return button;
    }
}
