package ie.cit.oossp.library.Bookshelf.Assignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Book;
import ie.cit.oossp.library.Bookshelf.Assignment.domain.Loan;
import ie.cit.oossp.library.Bookshelf.Assignment.repository.BookRepository;
import ie.cit.oossp.library.Bookshelf.Assignment.repository.LoanRepository;

@SpringView(name = BookView.VIEW_NAME)
public class BookView extends HorizontalLayout implements View  {	
	@Autowired	
	LoanRepository loanRepo;
	@Autowired	
	BookRepository bookRepo;	
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "BookView";	
	public final Button rentButton;	
	public final Grid<Book> avBookGrid;	
	
	public BookView() {				
		this.avBookGrid = new Grid<>(Book.class);		
		this.rentButton = new Button("Rent Selected");
	}
	
	@PostConstruct
    void init() 
    {	
		if(VaadinSession.getCurrent().getAttribute("id") != null)
		{
			List<Book> books = bookRepo.findAllAvailable();			
			List<Book> notLoaning = new ArrayList<Book>();	
			for(int i = 0; i < books.size(); i++)
			{				
				Boolean isLoaning = loanRepo.isLoaning((Long) VaadinSession.getCurrent().getAttribute("id"), books.get(i).getIsbn());
				if(!isLoaning)
				{
					notLoaning.add(books.get(i));
				}
			}			
			books = notLoaning;
			if(books.isEmpty()){
				rentButton.setVisible(false);
				Notification notif = new Notification("Sorry","There are no books available to rent currenlty", Notification.Type.WARNING_MESSAGE);
        		notif.setPosition(Position.TOP_RIGHT);
    			notif.show(Page.getCurrent());
			}
			avBookGrid.setItems(books);
			avBookGrid.getColumn("isbn").setCaption("ISBN");						
			avBookGrid.setColumnOrder("isbn", "title", "author", "genre", "remCopies");						
			avBookGrid.setSelectionMode(SelectionMode.MULTI);
			avBookGrid.setWidth("1000px");
			avBookGrid.setHeight("225px");			
			avBookGrid.addSelectionListener(event -> 
			{
				Set<Book> selected = event.getAllSelectedItems();
				Notification.show(selected.size() + " items selected");
				rentButton.setEnabled(selected.size() > 0);					
			});						
			VerticalLayout vl = new VerticalLayout(avBookGrid, rentButton);			
			addComponent(vl);
								
			
			rentButton.addClickListener(new Button.ClickListener() {				
				private static final long serialVersionUID = 1L;
				
				@Override
				public void buttonClick(ClickEvent event) {
					Set<Book> selected = avBookGrid.getSelectedItems();
					if(!selected.isEmpty())
					{
						for (Book b: selected) 
						{						
							Long twoWeeks = (new Date().getTime()) + (14 * 24 * 3600 * 1000) ;
							Date dateTaken = new Date();
							Date dateDue = new Date(twoWeeks);																
							Loan newLoan = new Loan((long) 1, (Long) VaadinSession.getCurrent().getAttribute("id"), b.getIsbn(), dateTaken, dateDue);
							loanRepo.save(newLoan);
							
							int remCopies = b.getRemCopies();						
							b.setRemCopies(remCopies--);
							bookRepo.update(b);
						}								    					    					    
						Notification notif = new Notification("Information","Data successfully saved", Notification.Type.HUMANIZED_MESSAGE);    				
		    			notif.setPosition(Position.BOTTOM_RIGHT);
		    			notif.show(Page.getCurrent());		    					    		
		    			getUI().getPage().open("http://localhost:8080/#!Rented", "_blank");
		    			getUI().getNavigator().navigateTo(VIEW_NAME);
					}					
				}
			});
		}
    }
}














