package ie.cit.oossp.library.Bookshelf.Assignment.repository;

import java.util.List;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Book;

public interface BookRepository {

	List<Book> findAll();
	
	List<Book> findAllAvailable();
	
	Book fineOne(Long isbn);
	
	Book save(Book bk);
	
	int update(Book bk);
	
	int deleteOne(Long isbn);
}