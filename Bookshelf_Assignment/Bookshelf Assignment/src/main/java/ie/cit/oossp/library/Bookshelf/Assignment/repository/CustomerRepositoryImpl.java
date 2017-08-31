package ie.cit.oossp.library.Bookshelf.Assignment.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Customer;
import ie.cit.oossp.library.Bookshelf.Assignment.rowmapper.CustomerRowMapper;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

	@Autowired
	private JdbcTemplate jdbc;
		
	private static final String SQL_INSERT = "insert into Customer (id, firstName, lastName, phoneNumber, password, userName, custType ) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE = "update Customer set firstName=?, lastName=?, phoneNumber=?, password=?, custType=? where id=?";
	private static final String SQL_FIND_ONE = "select * from Customer where id = ?";
	private static final String SQL_FIND_ALL = "select * from Customer order by id";
	private static final String SQL_DELETE_ONE = "delete from Customer where id = ?";
	
	@Override
	public List<Customer> findAll() {
		return jdbc.query(SQL_FIND_ALL, new CustomerRowMapper());			
	}
	
	@Override
	public Customer findOne(Long id) {
		return jdbc.queryForObject(SQL_FIND_ONE, new CustomerRowMapper() ,id);		
	}

	@Override
	public Customer save(Customer cr) {
		KeyHolder holder =  new GeneratedKeyHolder();
		int rows = jdbc.update(new PreparedStatementCreator(){

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SQL_INSERT, new String[]{"id"});
				ps.setLong(1, cr.getId());
				ps.setString(2, cr.getFirstName());
				ps.setString(3, cr.getLastName());
				ps.setLong(4, cr.getPhoneNumber());				
				ps.setString(5, cr.getPassword());
				ps.setString(6, cr.getUserName());
				ps.setString(7, cr.getCustType());
				return ps;
			}				
		}, holder);
		if (rows == 1)
		{
			cr.setId((Long)holder.getKey());
			return cr;
		}
		
		return null;			
	}	

	@Override
	public int update(Customer cr) 
	{
		return jdbc.update(SQL_UPDATE, cr.getFirstName(), cr.getLastName(), cr.getPhoneNumber(), cr.getPassword(), cr.getCustType(), cr.getId());		
	}	
	
	@Override
	public int deleteOne(Long id) {
		return jdbc.update(SQL_DELETE_ONE, id);				
	}	
}
