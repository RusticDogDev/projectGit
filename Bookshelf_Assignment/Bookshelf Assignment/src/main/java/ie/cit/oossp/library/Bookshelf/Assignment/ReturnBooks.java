package ie.cit.oossp.library.Bookshelf.Assignment;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Book;
import ie.cit.oossp.library.Bookshelf.Assignment.domain.Loan;
import ie.cit.oossp.library.Bookshelf.Assignment.repository.BookRepository;
import ie.cit.oossp.library.Bookshelf.Assignment.repository.LoanRepository;

@SpringView(name = ReturnBooks.VIEW_NAME)
public class ReturnBooks extends HorizontalLayout implements View  {	
	@Autowired	
	LoanRepository loanRepo;
	@Autowired	
	BookRepository bookRepo;	
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "ReturnBooks";
	private Notification notif;	
	public final Label extend;
	public final Label payed;
	public final Button backButton;
	public final Button cancelButton;
	public final Button continueButton;
	public final Button cancelPayButton;
	public final Button returnButton;		
	public final Button extendButton;	
	
	public final Grid<Loan> loansGrid;	
	
	public ReturnBooks() {										
		this.notif = new Notification("");
		this.loansGrid = new Grid<>(Loan.class);		
		this.extend = new Label("Due Date(s) extended successfully by two weeks");
		this.payed = new Label("Payment for book rental successfull");
		this.backButton = new Button("Back to Main Menu");
		this.cancelButton = new Button("Cancel");
		this.continueButton = new Button("Confirm");
		this.cancelPayButton = new Button("Cancel Payment");
		this.returnButton = new Button("Return Books Selected");
		this.extendButton = new Button("Extend loan on Books Selected");
	}
	
	@PostConstruct
	void init() {
    {	
		if(VaadinSession.getCurrent().getAttribute("id") != null)
		{			
			List<Loan> loans = loanRepo.findAllForUser((Long) VaadinSession.getCurrent().getAttribute("id"));												
			if(loans.isEmpty()){
				returnButton.setVisible(false);
				extendButton.setVisible(false);
				notif = new Notification("Attention","You are not loaning any books currently", Notification.Type.WARNING_MESSAGE);
        		notif.setPosition(Position.TOP_RIGHT);
    			notif.show(Page.getCurrent());
			}	
			backButton.setEnabled(true);
			loansGrid.setEnabled(true);
			loansGrid.setItems(loans);
			loansGrid.getColumn("loanId").setCaption("Loan Id");		
			loansGrid.getColumn("id").setCaption("User Id");			
			loansGrid.setColumnOrder("loanId", "id", "isbn", "dateTaken", "dateDue");					
			loansGrid.setSelectionMode(SelectionMode.MULTI);
			loansGrid.setWidth("1000px");
			loansGrid.setHeight("225px");			
			loansGrid.addSelectionListener(event -> 
			{
				Set<Loan> selected = event.getAllSelectedItems();
				Notification.show(selected.size() + " items selected");							
				returnButton.setEnabled(selected.size() > 0);		
				extendButton.setEnabled(selected.size() > 0);		
			});	
			HorizontalLayout hl = new HorizontalLayout(returnButton, extendButton);	
			VerticalLayout vl = new VerticalLayout(loansGrid, hl);			
			addComponent(vl);
			
			extendButton.addClickListener(new Button.ClickListener() {							
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					Set<Loan> selected = loansGrid.getSelectedItems();	
					if(!selected.isEmpty())
					{
						boolean valid = true;
						for (Loan l: selected) 
						{						
							Date currentDate = new Date();
							if(l.getDateDue().before(currentDate)){
								valid = false;
							}							
						}
						if(valid)
						{
							for(Loan l: selected)
							{																					
								Long extendLoan = (new Date().getTime()) + (14 * 24 * 3600 * 1000);
								Date dateDue = new Date(extendLoan);
								if(valid == true)
								{
									l.setDateDue(dateDue);
									loanRepo.update(l);
								}																	
							}
							Notification notif = new Notification("Information","Rental due dates extended successfully by two weeks", Notification.Type.HUMANIZED_MESSAGE);    				
			    			notif.setPosition(Position.BOTTOM_RIGHT);
			    			notif.show(Page.getCurrent());
			    			vl.setVisible(false);			    			
			    			VerticalLayout vertical = new VerticalLayout(extend, backButton);
			    			vertical.setCaption("Loan extended successfully by two weeks");
			    			vertical.setCaption("Please confirm payment");
			    			vertical.setSpacing(true);
			    			vertical.setMargin(new MarginInfo(true, true, true, false));
			    			vertical.setSizeUndefined();
			    			vertical.setComponentAlignment(extend, Alignment.MIDDLE_CENTER);
			    			vertical.setComponentAlignment(backButton, Alignment.MIDDLE_CENTER);
			    			Panel panelExtend = new Panel(vertical);			    			
			    			addComponent(panelExtend);
						}
						else{
							notif = new Notification("Error","A book's due date is already late and must be returned and payed in full", Notification.Type.ERROR_MESSAGE);    				
			    			notif.setPosition(Position.BOTTOM_RIGHT);
			    			notif.show(Page.getCurrent());			    						    		
						}						
					}
				}
			});
													
			returnButton.addClickListener(new Button.ClickListener() {				
				private static final long serialVersionUID = 2L;
				
				@Override
				public void buttonClick(ClickEvent event) {
					Set<Loan> selected = loansGrid.getSelectedItems();					
					if(!selected.isEmpty())
					{
						int fine = 0;
						for (Loan l: selected) 
						{						
							Date currentDate = new Date();
							if(l.getDateDue().before(currentDate)){
								fine = fine + 15;
							}
							else{
								fine = fine + 3;
							}
						}						
						Label total = new Label("Total payment due: €" + String.valueOf(fine));
						total.setEnabled(true);
						vl.setVisible(false);
						HorizontalLayout horiz = new HorizontalLayout(cancelPayButton, continueButton);
						continueButton.setEnabled(true);
						cancelPayButton.setEnabled(true);															
						VerticalLayout vert = new VerticalLayout(total, horiz);
						Panel panel = new Panel();
						panel.setEnabled(true);
						vert.setCaption("Please confirm payment");
						vert.setSpacing(true);
						vert.setMargin(new MarginInfo(true, true, true, false));
						vert.setSizeUndefined();
						VerticalLayout vertSuper = new VerticalLayout(vert);						
						vertSuper.setSizeFull();
						vertSuper.setComponentAlignment(vert, Alignment.MIDDLE_CENTER);	
						panel.setContent(vertSuper);												
						panel.setVisible(true);
						addComponent(panel);
						
						cancelPayButton.addClickListener(new Button.ClickListener() {						    
							private static final long serialVersionUID = 3L;
							public void buttonClick(ClickEvent event) {
								vl.setVisible(true);
								panel.setVisible(false);								
						    }
						});
						
						continueButton.addClickListener(new Button.ClickListener() {						    
							private static final long serialVersionUID = 4L;
							public void buttonClick(ClickEvent event) {
								Set<Loan> selected = loansGrid.getSelectedItems();					
								for (Loan l: selected) 
								{															
									Book returnedBook = bookRepo.fineOne(l.getIsbn());
									int remCopies = returnedBook.getRemCopies();
									returnedBook.setRemCopies(remCopies++);
									bookRepo.update(returnedBook);
									loanRepo.deleteOne(l.getLoanId());									
									notif = new Notification("Information","Data successfully saved", Notification.Type.HUMANIZED_MESSAGE);    				
					    			notif.setPosition(Position.BOTTOM_RIGHT);
					    			notif.show(Page.getCurrent());	
					    			vert.setVisible(false);					    			
					    			vertSuper.addComponent(payed);
					    			vertSuper.addComponent(backButton);
								}		
						    }
						});						
					}					
				}
			});
			backButton.addClickListener(new Button.ClickListener() {						    
				private static final long serialVersionUID = 4L;
				public void buttonClick(ClickEvent event) 
				{
					getUI().getNavigator().navigateTo(MainView.VIEW_NAME);								
			    }
			});
		}
    }
	}
}




