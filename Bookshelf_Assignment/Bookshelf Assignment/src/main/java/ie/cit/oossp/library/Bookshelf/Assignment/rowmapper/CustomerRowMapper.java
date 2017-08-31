package ie.cit.oossp.library.Bookshelf.Assignment.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Customer;

public class CustomerRowMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int i) throws SQLException {
		return new Customer(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getLong("phoneNumber"), rs.getString("password"), rs.getString("userName"), rs.getString("custType"));		
	}
}
