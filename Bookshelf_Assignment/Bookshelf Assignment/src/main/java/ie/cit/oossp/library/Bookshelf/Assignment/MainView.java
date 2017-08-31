package ie.cit.oossp.library.Bookshelf.Assignment;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = MainView.VIEW_NAME)
public class MainView extends VerticalLayout implements View {   
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "Main";
	
	@PostConstruct
    void init() 
    {		
        setMargin(true);
        setSpacing(true);        
    }

    @Override
    public void enter(ViewChangeEvent event) {        
        String username = String.valueOf(getSession().getAttribute("user"));
        addComponent(new Label("Welcome " + username));    
        
    }
}