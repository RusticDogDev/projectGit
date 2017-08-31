package ie.cit.oossp.library.Bookshelf.Assignment;

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

@SpringView(name = RemoveBooks.VIEW_NAME)
public class RemoveBooks extends HorizontalLayout implements View  {	
	@Autowired	
	LoanRepository loanRepo;
	@Autowired	
	BookRepository bookRepo;	
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "RemoveBooks";	
	public final Button deleteButton;	
	public final Grid<Book> bookGrid;	
	
	public RemoveBooks() {				
		this.bookGrid = new Grid<>(Book.class);		
		this.deleteButton = new Button("Delete");
	}
	
	@PostConstruct
    void init() 
    {	
		if(VaadinSession.getCurrent().getAttribute("id") != null)
		{
			List<Book> books = bookRepo.findAll();									
			if(books.isEmpty()){
				deleteButton.setVisible(false);
				Notification notif = new Notification("Warning","There are no books available currenlty", Notification.Type.WARNING_MESSAGE);
        		notif.setPosition(Position.TOP_RIGHT);
    			notif.show(Page.getCurrent());
			}
			bookGrid.setItems(books);
			bookGrid.getColumn("isbn").setCaption("ISBN");						
			bookGrid.setColumnOrder("isbn", "title", "author", "genre", "remCopies");						
			bookGrid.setSelectionMode(SelectionMode.MULTI);
			bookGrid.setWidth("1000px");
			bookGrid.setHeight("225px");			
			bookGrid.addSelectionListener(event -> 
			{
				Set<Book> selected = event.getAllSelectedItems();
				Notification.show(selected.size() + " items selected");
				deleteButton.setEnabled(selected.size() > 0);					
			});						
			VerticalLayout vl = new VerticalLayout(bookGrid, deleteButton);			
			addComponent(vl);
								
			
			deleteButton.addClickListener(new Button.ClickListener() {				
				private static final long serialVersionUID = 1L;
				
				@Override
				public void buttonClick(ClickEvent event) {
					Set<Book> selected = bookGrid.getSelectedItems();
					if(!selected.isEmpty())
					{
						for (Book b: selected) 
						{																		
							boolean isLoaning = loanRepo.isBookLoaning(b.getIsbn());
							if(isLoaning)
							{
								List<Loan> loansToDelete = loanRepo.findAllForBook(b.getIsbn());
								for(Loan l: loansToDelete)
								{
									loanRepo.deleteOne(l.getLoanId());
								}
							}
							bookRepo.deleteOne(b.getIsbn());
						}								    					    					    
						Notification notif = new Notification("Information","Book(s) successfully deleted", Notification.Type.HUMANIZED_MESSAGE);    				
		    			notif.setPosition(Position.BOTTOM_RIGHT);
		    			notif.show(Page.getCurrent());		    					    		
		    			bookGrid.setItems(bookRepo.findAll());
					}					
				}
			});
		}
    }
}