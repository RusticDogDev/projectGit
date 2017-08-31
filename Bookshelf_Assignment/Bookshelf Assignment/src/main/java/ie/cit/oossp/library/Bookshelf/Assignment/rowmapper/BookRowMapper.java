package ie.cit.oossp.library.Bookshelf.Assignment.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Book;

public class BookRowMapper implements RowMapper<Book> {

	@Override
	public Book mapRow(ResultSet rs, int i) throws SQLException {
		return new Book(rs.getLong("isbn"), rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getInt("remCopies"));		
	}	
}