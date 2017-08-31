package ie.cit.oossp.library.Bookshelf.Assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Book;
import ie.cit.oossp.library.Bookshelf.Assignment.domain.Book.GenreType;
import ie.cit.oossp.library.Bookshelf.Assignment.repository.BookRepository;

@SpringView(name = AddBook.VIEW_NAME)
public class AddBook extends VerticalLayout implements View {
	@Autowired	
	BookRepository bookRepo;
	private static final long serialVersionUID = 1L;	
	public static final String VIEW_NAME = "AddBook";	
	private Notification notif;	
	private final TextField isbn;
	private final TextField title;
	private final TextField author;
	private final TextField remCopies;
	private NativeSelect<String> genre;	
	private final Button editButton;
	private final Button saveButton;
	
	public AddBook()
	{									
        this.notif = new Notification(" ");				
		this.isbn = new TextField("ISBN Number:");
		this.title = new TextField("Title:");
		this.author = new TextField("Author:");		
		this.remCopies = new TextField("Number of copies: ");
		this.genre = new NativeSelect<String>("Genre:");		
		this.editButton = new Button("Edit");
		this.saveButton = new Button("Create");
	}
		
	@PostConstruct
    void init() 
    {			
		setSizeFull();		
		editButton.setVisible(false);
		isbn.setWidth("300px");
		title.setWidth("300px");
		author.setWidth("300px");		
		remCopies.setWidth("300px");
		
		isbn.setRequiredIndicatorVisible(true);
		title.setRequiredIndicatorVisible(true);
		author.setRequiredIndicatorVisible(true);
		remCopies.setRequiredIndicatorVisible(true);				
		
		if(VaadinSession.getCurrent().getAttribute("id") !=null)
		{
			String[] genres = getNames(GenreType.class);
			ArrayList<String> genreList = new ArrayList<String>(Arrays.asList(genres));
						
			genre = new NativeSelect<String>("Genre:", genreList);
			genre.setRequiredIndicatorVisible(true);	
			genre.setEmptySelectionAllowed(false);
			genre.setSelectedItem(genreList.get(1));
			genre.setWidth("300px");
			saveButton.addClickListener(event -> 
			{    	    
				if(isbn.getValue().contentEquals("") || title.getValue().contentEquals("") || author.getValue().contentEquals("") || genre.getValue().contentEquals("") || remCopies.getValue().contentEquals(""))
	    		{	    			
	    			notif = new Notification("Error","All fields must be filled in correctly", Notification.Type.ERROR_MESSAGE);
	    			notif.setPosition(Position.TOP_RIGHT);
	    			notif.show(Page.getCurrent());
	    			return;	    			
	    		}
	    		else
	    		{   	    			
	    			List<Book> books = bookRepo.findAll();
	    			
	    			Long isbnNum =(long) 0;
	    			int copies = 0;
	    			if(isbn.getValue().length() < 10 || isbn.getValue().length() >  13 )
	    			{
	    				notif = new Notification("Error","ISBN value entered is not a valid ISBN number \n ISBN number must have between 10 and 13 digits", Notification.Type.ERROR_MESSAGE);
    	    			notif.setPosition(Position.TOP_RIGHT);
    	    			notif.show(Page.getCurrent());
    	    			return;	
	    			}	    			
	    			try{
	    				isbnNum = Long.parseLong(isbn.getValue());	    
	    				  // is an integer!
	    				} catch (NumberFormatException e) {
	    					notif = new Notification("Error","ISBN value entered is not a numeric value", Notification.Type.ERROR_MESSAGE);
	    	    			notif.setPosition(Position.TOP_RIGHT);
	    	    			notif.show(Page.getCurrent());
	    	    			return;	
	    				} 	    	
	    			try{
	    				copies = Integer.parseInt(remCopies.getValue());
	    				  // is an integer!
	    				} catch (NumberFormatException e) {
	    					notif = new Notification("Error","Number of copies value entered is not a numeric value", Notification.Type.ERROR_MESSAGE);
	    	    			notif.setPosition(Position.TOP_RIGHT);
	    	    			notif.show(Page.getCurrent());
	    	    			return;	
	    				}	    	
	    			if(!saveButton.getCaption().contentEquals("Save")){
	    				boolean valid = true;
		    			for(Book b : books){
		    				if(b.getIsbn() == isbnNum)
		    				{
		    					valid = false;
		    				}
		    			}
		    			if(!valid){
		    				notif = new Notification("Error","Book ISBN already exists", Notification.Type.ERROR_MESSAGE);
			    			notif.setPosition(Position.TOP_RIGHT);
			    			notif.show(Page.getCurrent());
			    			return;
		    			}
	    			}	    				    			
	    			notif = new Notification("Information","Data successfully saved", Notification.Type.HUMANIZED_MESSAGE);    				
	    			notif.setPosition(Position.BOTTOM_RIGHT);
	    			notif.show(Page.getCurrent());	    				    					
	    			Book newBook = new Book(isbnNum, title.getValue(), author.getValue(), genre.getValue(), copies);
	    			if(saveButton.getCaption().contentEquals("Save"))
	    			{
	    				bookRepo.update(newBook);
	    			}
	    			else{
	    				bookRepo.save(newBook);
	    			}	    			
	    			isbn.setReadOnly(true);
	    			title.setReadOnly(true);
	    			author.setReadOnly(true);
	    			genre.setReadOnly(true);
	    			remCopies.setReadOnly(true);
	    			editButton.setVisible(true);
	    			saveButton.setCaption("Save");
	    			saveButton.setVisible(false);
	    		}
	    	});
			editButton.addClickListener(event -> 
			{				
    			title.setReadOnly(false);
    			author.setReadOnly(false);
    			genre.setReadOnly(false);
    			remCopies.setReadOnly(false);
    			editButton.setVisible(false);
    			saveButton.setVisible(true);
			});
			VerticalLayout fields = new VerticalLayout(isbn, title, author, remCopies, genre, editButton, saveButton);
	        fields.setCaption("Create a new book here");
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
