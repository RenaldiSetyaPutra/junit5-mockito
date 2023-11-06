import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javassist.NotFoundException;
import jdk.jfr.ContentType;
import org.example.Controller.BookController;
import org.example.Model.Book;
import org.example.Repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();
    ObjectWriter writer = mapper.writer();

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    Book RECORD_1 = new Book(1L, "Renaldi", "Cara Menjadi Sukses", 8);
    Book RECORD_2 = new Book(2L, "Setya", "Cara Menjadi Kaya", 8);
    Book RECORD_3 = new Book(3L, "Putra", "Cara Menjadi Jagoan", 8);

    Book RECORD_4 = new Book();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllBookSuccess() throws Exception{
        List<Book> bookList = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

        Mockito.when(bookRepository.findAll()).thenReturn(bookList);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/book/getAllBook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("Putra")))
                .andExpect(jsonPath("$[1].name", is("Setya")));
    }

    @Test
    public void getBookByIdSuccess() throws Exception{
        Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.of(RECORD_1));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/book/getBookById/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Renaldi")));
    }

    @Test
    public void getBookByIdFailed() throws Exception{
//        Mockito.when(bookRepository.findById(4L)).thenThrow(NotFoundException.class);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/book/getBookById/4")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createBookSuccess() throws Exception{
        Book book = Book.builder()
                .bookId(4L)
                .name("RSP")
                .summary("SS")
                .rating(9)
                .build();

        Mockito.when(bookRepository.save(book)).thenReturn(book);

        String content = writer.writeValueAsString(book);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book/createBook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("RSP")));
    }

    @Test
    public void updateBookSuccess() throws Exception{
        Book updateBook = Book.builder()
                .bookId(1L)
                .name("Updated Book Name")
                .summary("Updated Book Summary")
                .rating(10)
                .build();

        Mockito.when(bookRepository.findById(RECORD_1.getBookId())).thenReturn(Optional.ofNullable(RECORD_1));
        Mockito.when(bookRepository.save(updateBook)).thenReturn(updateBook);

        String updateContent = writer.writeValueAsString(updateBook);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book/updateBook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updateContent);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Updated Book Name")));
    }

    @Test
    public void deleteBookByIdSuccess() throws Exception{
        Mockito.when(bookRepository.findById(RECORD_3.getBookId())).thenReturn(Optional.of(RECORD_3));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/book/deleteBook/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
   }
}