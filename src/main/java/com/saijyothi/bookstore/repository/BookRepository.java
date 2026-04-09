package com.saijyothi.bookstore.repository;

import com.saijyothi.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("select distinct b.category from Book b order by b.category asc")
    List<String> findDistinctCategories();

    List<Book> findAllByOrderByIdAsc();

    long countByStockGreaterThan(Integer stock);

    boolean existsByIsbn(String isbn);
}
