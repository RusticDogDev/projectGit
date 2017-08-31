package ie.cit.oossp.library.Bookshelf.Assignment;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Loan;
import ie.cit.oossp.library.Bookshelf.Assignment.repository.LoanRepository;

@SpringView(name = Rented.VIEW_NAME)
public class Rented extends HorizontalLayout implements View  {
	@Autowired	
	LoanRepository loanRepo;
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "Rented";
	public final Grid<Loan> loansGrid;
	private final Label title;
	
	public Rented() {				
		this.loansGrid = new Grid<>(Loan.class);	
		this.title = new Label("Books now currently loaning");
	}
		
	@PostConstruct
    void init() 
    {	
		if(VaadinSession.getCurrent().getAttribute("id") != null)
		{			
			VerticalLayout vl = new VerticalLayout(title, loansGrid);
			VerticalLayout viewLayout = new VerticalLayout(vl);
			List<Loan> loans = loanRepo.findAllForUser((Long) VaadinSession.getCurrent().getAttribute("id"));	
			title.setEnabled(true);			
			loansGrid.setEnabled(true);			
			loansGrid.setItems(loans);
			loansGrid.getColumn("loanId").setCaption("Loan Id");
			loansGrid.getColumn("id").setCaption("User Id");
			loansGrid.setColumnOrder("loanId", "id", "isbn", "dateTaken", "dateDue");						
			loansGrid.setSelectionMode(SelectionMode.NONE);
			loansGrid.setWidth("700px");
			loansGrid.setHeight("225px");
									
			vl.setSpacing(true);
			vl.setMargin(new MarginInfo(true, true, true, false));
			vl.setSizeUndefined();	        
	        
	        viewLayout.setSizeFull();
	        viewLayout.setComponentAlignment(vl, Alignment.MIDDLE_CENTER);                               
	        addComponent(viewLayout);
			addComponent(vl);
		}
	}
		
		    
}
