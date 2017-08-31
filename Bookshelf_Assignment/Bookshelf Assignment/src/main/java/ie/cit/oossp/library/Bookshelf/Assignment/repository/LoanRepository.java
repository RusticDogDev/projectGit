package ie.cit.oossp.library.Bookshelf.Assignment.repository;

import java.util.List;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Loan;

public interface LoanRepository {

	List<Loan> findAll();
	
	List<Loan> findAllLate();
	
	List<Loan> findAllLateForUser(Long id);
	
	List<Loan> findAllForUser(Long id);
	
	List<Loan> findAllForBook(Long isbn);
	
	Loan findOne(Long loanId);
	
	Loan findOneByIds(Long id, Long isbn);
	
	Boolean isLoaning(Long id, Long isbn);
	
	Boolean isBookLoaning(Long isbn);
	
	Loan save(Loan ln);
	
	int update(Loan ln);
	
	int deleteOne(Long loanId);
}

