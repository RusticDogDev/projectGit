package ie.cit.oossp.library.Bookshelf.Assignment.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ie.cit.oossp.library.Bookshelf.Assignment.domain.Book;
import ie.cit.oossp.library.Bookshelf.Assignment.rowmapper.BookRowMapper;

@Repository
public class BookRepositoryImpl implements BookRepository {

	@Autowired
	private JdbcOperations jdbc;
		
	private static final String SQL_INSERT = "insert into Book (isbn, title, author, genre, remCopies) values (?, ?, ?, ?, ?)";
	private static final String SQL_UPDATE = "update Book set title=?, author=?, genre=?, remCopies=? where isbn=?";
	private static final String SQL_FIND_ONE = "select * from Book where isbn = ?";
	private static final String SQL_FIND_ALL = "select * from Book order by author";
	private static final String SQL_FIND_ALL_AVAILABLE = "select * from Book where remCopies > 0 order by author";
	private static final String SQL_DELETE_ONE = "delete from Book where isbn = ?";
	
	
	@Override
	public List<Book> findAll() {
		return jdbc.query(SQL_FIND_ALL, new BookRowMapper());		
	}
	
	@Override
	public List<Book> findAllAvailable() {
		return jdbc.query(SQL_FIND_ALL_AVAILABLE, new BookRowMapper());			
	}
	
	@Override
	public Book fineOne(Long isbn) {		
		return jdbc.queryForObject(SQL_FIND_ONE, new BookRowMapper() ,isbn);		
	}

	@Override
	public Book save(final Book bk) {
		KeyHolder holder =  new GeneratedKeyHolder();
		int rows = jdbc.update(new PreparedStatementCreator(){

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SQL_INSERT, new String[]{"isbn"});
				ps.setLong(1, bk.getIsbn());
				ps.setString(2, bk.getTitle());
				ps.setString(3, bk.getAuthor());
				ps.setString(4, bk.getGenre());
				ps.setInt(5, bk.getRemCopies());				
				return ps;
			}				
		}, holder);
		if (rows == 1)
		{
			bk.setIsbn((Long)holder.getKey());
			return bk;
		}
		
		return null;			
	}

	@Override
	public int update(Book bk) {
		return jdbc.update(SQL_UPDATE, bk.getIsbn(), bk.getTitle(), bk.getAuthor(), bk.getGenre(), bk.getRemCopies());
	}
	
	@Override
	public int deleteOne(Long isbn) {
		return jdbc.update(SQL_DELETE_ONE, isbn);		
	}	
}
