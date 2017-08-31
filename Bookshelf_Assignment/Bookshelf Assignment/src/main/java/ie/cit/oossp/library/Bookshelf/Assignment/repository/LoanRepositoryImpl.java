package ie.cit.oossp.library.Bookshelf.Assignment.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Loan;
import ie.cit.oossp.library.Bookshelf.Assignment.rowmapper.LoanRowMapper;

@Repository
public class LoanRepositoryImpl implements LoanRepository {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	private static final String SQL_INSERT = "insert into Loan (id, isbn, dateTaken, dateDue) values (?, ?, ?, ?)";
	private static final String SQL_UPDATE = "update Loan set id=?, isbn=?, dateTaken=?, dateDue=? where loanId=?";
	private static final String SQL_FIND_ONE = "select * from Loan where loanId = ?";
	private static final String SQL_FIND_ONE_BY_IDS = "select * from Loan where id = ? AND isbn = ?";
	private static final String SQL_IS_lOANING = "select count(*) from Loan where id=? AND isbn = ?";	
	private static final String SQL_IS_Book_lOANING = "select count(*) from Loan where isbn = ?";
	private static final String SQL_FIND_ALL = "select * from Loan order by isbn";
	private static final String SQL_FIND_ALL_FOR_USER = "select * from Loan where id = ? order by isbn";
	private static final String SQL_FIND_ALL_FOR_BOOK = "select * from Loan where isbn = ? order by id";
	private static final String SQL_FIND_ALL_LATE = "select * from Loan where dateDue < CURRENT_TIMESTAMP()";
	private static final String SQL_FIND_ALL_LATE_FOR_USER = "select * from Loan where dateDue < CURRENT_TIMESTAMP() AND id = ?";	
	private static final String SQL_DELETE_ONE = "delete from Loan where loanId = ?";	
		
	@Override
	public List<Loan> findAll() {
		return jdbc.query(SQL_FIND_ALL, new LoanRowMapper());				
	}
	
	@Override
	public List<Loan> findAllLate() {
		return jdbc.query(SQL_FIND_ALL_LATE, new LoanRowMapper());		
	}
	
	@Override
	public List<Loan> findAllLateForUser(Long id) {
		return jdbc.query(SQL_FIND_ALL_LATE_FOR_USER, new LoanRowMapper(), id);				
	}
	
	@Override
	public List<Loan> findAllForUser(Long id) 
	{
		return jdbc.query(SQL_FIND_ALL_FOR_USER, new LoanRowMapper(), id);					
	}

	@Override
	public List<Loan> findAllForBook(Long isbn) 
	{
		return jdbc.query(SQL_FIND_ALL_FOR_BOOK, new LoanRowMapper(), isbn);		
	}

	@Override
	public Loan findOne(Long loanId) {
		return jdbc.queryForObject(SQL_FIND_ONE, new LoanRowMapper(), loanId);		
	}
	
	@Override
	public Loan findOneByIds(Long id, Long isbn) {
		return jdbc.queryForObject(SQL_FIND_ONE_BY_IDS, new LoanRowMapper(), id, isbn);			
	}
	
	@Override
	public Boolean isLoaning(Long id, Long isbn) {
		boolean result = false;		
		int count = jdbc.queryForObject(SQL_IS_lOANING, new Object[] { id, isbn }, Integer.class);                		
		if (count > 0) 
		{
		    result = true;
		}
		return result;
	}
	
	@Override
	public Boolean isBookLoaning(Long isbn) {
		boolean result = false;		
		int count = jdbc.queryForObject(SQL_IS_Book_lOANING, new Object[] { isbn }, Integer.class);                		
		if (count > 0) 
		{
		    result = true;
		}
		return result;
	}	

	@Override
	public Loan save(Loan ln) {
		KeyHolder holder = new GeneratedKeyHolder();			
		int rows = jdbc.update(new PreparedStatementCreator()
		{			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SQL_INSERT, new String[]{"loanId"});								
				ps.setLong(1, ln.getId());
				ps.setLong(2, ln.getIsbn());
				Date sqlDateTaken = new java.sql.Date( ln.getDateTaken().getTime());
				Date sqlDateDue = new java.sql.Date( ln.getDateDue().getTime());								
				ps.setDate(3, sqlDateTaken);				
				ps.setDate(4, sqlDateDue);
				return ps;
			}				
		}, holder);
		if(rows == 1) 
		{
			ln.setLoanId((Long)holder.getKey());
			return ln;
		}
		
		return null;
	}

	@Override
	public int update(Loan ln) {
		return jdbc.update(SQL_UPDATE, ln.getId(), ln.getIsbn(), ln.getDateTaken(), ln.getDateDue(), ln.getLoanId());
	}

	@Override
	public int deleteOne(Long loanId) {
		return jdbc.update(SQL_DELETE_ONE, loanId);			
	}	
}
