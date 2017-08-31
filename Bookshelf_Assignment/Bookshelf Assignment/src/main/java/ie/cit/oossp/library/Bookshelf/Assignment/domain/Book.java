package ie.cit.oossp.library.Bookshelf.Assignment.domain;

public class Book {
	public enum GenreType{
		SciFi,
		COA,
		Fiction,
		Horror,
		Crime
	}
	private Long isbn;
	private String title;
	private String author;	
	private String genre;
	private int remCopies;

	public Book(Long isbn, String title, String author, String genre, int remCopies) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.setRemCopies(remCopies);
		this.setGenre(genre);
	}
	
	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public int getRemCopies() {
		return remCopies;
	}

	public void setRemCopies(int remCopies) {
		this.remCopies = remCopies;
	}	

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + ", author=" + author + ", genre=" + genre + ", remCopies=" + remCopies + "]";
	}
}
