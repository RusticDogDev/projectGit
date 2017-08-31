package ie.cit.oossp.library.Bookshelf.Assignment.repository;

import java.util.List;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Customer;

public interface CustomerRepository {
	
	List<Customer> findAll();
	
	Customer findOne(Long id);
	
	Customer save(Customer cr);
	
	int update(Customer cr);
	
	int deleteOne(Long id);
}
