package ie.cit.oossp.library.Bookshelf.Assignment.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Loan;

public class LoanRowMapper implements RowMapper<Loan> {

	@Override
	public Loan mapRow(ResultSet rs, int i) throws SQLException {
		return new Loan(rs.getLong("loanId") ,rs.getLong("id"), rs.getLong("isbn"), rs.getDate("dateTaken"), rs.getDate("dateDue"));		
	}
}