package org.example.Controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.Model.Book;
import org.example.Repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
@RequiredArgsConstructor
public class BookController {

    private BookRepository bookRepository;

    @GetMapping("/getAllBook")
    public List<Book> getAllBook(){
        return bookRepository.findAll();
    }

    @GetMapping("/getBookById/{bookId}")
    public Book getBookById(@PathVariable(value = "bookId") Long request) throws NotFoundException{
        if (request == null){
            throw new NotFoundException("BookId must not be null");
        }

        Optional<Book> optionalBook = bookRepository.findById(request);
        if (!optionalBook.isPresent()){
            throw new NotFoundException("Book not found");
        }

        return bookRepository.findById(request).get();
    }

    @PostMapping("/createBook")
    public Book createBook(@RequestBody @Valid Book request){
        return bookRepository.save(request);
    }

    @PutMapping("/updateBook")
    public Book updateBook(@RequestBody @Valid Book request) throws NotFoundException {
        if(request == null || request.getBookId() == null){
            throw new NotFoundException("Book or ID must not be null");
        }
        Optional<Book> optionalBook = bookRepository.findById(request.getBookId());
        if (!optionalBook.isPresent()){
            throw new NotFoundException("Book Id " + request.getBookId() + " not found");
        }

        Book existingBook = optionalBook.get();
        existingBook.setName(request.getName());
        existingBook.setSummary(request.getSummary());
        existingBook.setRating(request.getRating());

        return bookRepository.save(existingBook);
    }

    @DeleteMapping("/deleteBook/{bookId}")
    public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws NotFoundException {
        if (!bookRepository.findById(bookId).isPresent()) {
            throw new NotFoundException("bookId " + bookId + " not found");
        }
        bookRepository.deleteById(bookId);
    }
}
