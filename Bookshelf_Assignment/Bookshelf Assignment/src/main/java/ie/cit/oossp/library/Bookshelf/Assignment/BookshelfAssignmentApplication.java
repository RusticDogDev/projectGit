package ie.cit.oossp.library.Bookshelf.Assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.vaadin.spring.annotation.EnableVaadin;

@EnableVaadin
@Configuration
@SpringBootApplication

public class BookshelfAssignmentApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BookshelfAssignmentApplication.class, args);
	}
}
