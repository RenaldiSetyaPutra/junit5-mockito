package org.example.Model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "book_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    @NonNull
    private String name;

    @NonNull
    private String summary;

    private int rating;

    public Book(long bookId, String name, String summary, int rating) {
        this.bookId = bookId;
        this.name = name;
        this.summary = summary;
        this.rating = rating;
    }
}
