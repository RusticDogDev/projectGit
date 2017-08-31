package ie.cit.oossp.library.Bookshelf.Assignment.domain;

import java.util.Date;

public class Loan {
	private Long loanId;
	private Long id;
	private Long isbn;
	private Date dateTaken;
	private Date dateDue;	
	
	public Loan(Long loanId, Long id, Long isbn, Date dateTaken, Date dateDue) 
	{
		super();
		this.setLoanId(loanId);
		this.setId(id);
		this.setIsbn(isbn);
		this.setDateTaken(dateTaken);
		this.setDateDue(dateDue);		
	}
	
	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public Date getDateTaken() {
		return dateTaken;
	}

	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}

	public Date getDateDue() {
		return dateDue;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}
	
	@Override
	public String toString() {
		return "Loan [id=" + id + ", isbn=" + isbn + ", dateTaken=" + dateTaken + ", dateDue=" + dateDue + "]";
	}	
}
