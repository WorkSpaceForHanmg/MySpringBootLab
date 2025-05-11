package com.rookies3.myspringbootlab.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Getter @Setter
@DynamicUpdate
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true,nullable = false)
    private String isbn;

    @Column(nullable = false)
    @Positive    //가격 음수를 방지 하기 위함
    private Integer price;

    @Column(nullable = false)
    private LocalDate publishDate;


}
