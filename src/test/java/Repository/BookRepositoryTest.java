package Repository;

import org.assertj.core.api.Assertions;
import org.example.Model.Book;
import org.example.Repository.BookRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    Book book;

    @BeforeEach
    void setUp(){
        book = new Book(1L, "Renaldi", "Cara Menjadi Sukses", 8);
        bookRepository.save(book);
    }

    @AfterEach
    void tearDown(){
        book = null;
        bookRepository.deleteAll();
    }

    // Test Success
    @Test
    public void getBookByIdSuccess(){
        Optional<Book> bookData = bookRepository.findById(1L);
        Assertions.assertThat(bookData.get().getName()).isEqualTo(book.getName());
    }

    // Test Fail
    @Test
    public void getBookByIdNotFound(){
        Optional<Book> bookData = bookRepository.findById(2L);
        Assertions.assertThat(bookData.isEmpty()).isTrue();
    }


}
