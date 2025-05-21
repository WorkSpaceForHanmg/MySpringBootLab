package com.rookies3.myspringbootlab.service;

import com.rookies3.myspringbootlab.controller.dto.BookDTO;
import com.rookies3.myspringbootlab.controller.dto.PublisherDTO;
import com.rookies3.myspringbootlab.entity.Publisher;
import com.rookies3.myspringbootlab.repository.PublisherRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public List<PublisherDTO.SimpleResponse> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(p -> PublisherDTO.SimpleResponse.fromEntityWithCount(p, (long) p.getBooks().size()))
                .toList();
    }

    public PublisherDTO.Response getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with id: " + id));
        return PublisherDTO.Response.fromEntity(publisher);
    }

    public PublisherDTO.Response getPublisherByName(String name) {
        Publisher publisher = publisherRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with name: " + name));
        return PublisherDTO.Response.fromEntity(publisher);
    }

    @Transactional
    public PublisherDTO.Response createPublisher(PublisherDTO.Request request) {
        if (publisherRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Publisher with the same name already exists.");
        }

        Publisher publisher = Publisher.builder()
                .name(request.getName())
                .establishedDate(request.getEstablishedDate())
                .address(request.getAddress())
                .build();

        Publisher savedPublisher = publisherRepository.save(publisher);
        return PublisherDTO.Response.fromEntity(savedPublisher);
    }

    @Transactional
    public void updatePublisher(Long id, PublisherDTO.Request request) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));

        if (!publisher.getName().equals(request.getName()) &&
                publisherRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Another publisher with this name already exists.");
        }

        publisher.setName(request.getName());
        publisher.setEstablishedDate(request.getEstablishedDate());
        publisher.setAddress(request.getAddress());
    }

    @Transactional
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found"));

        if (!publisher.getBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete publisher with books.");
        }

        publisherRepository.delete(publisher);
    }

    @Transactional(readOnly = true)
    public List<BookDTO.Response> getBooksByPublisher(Long publisherId) {
        Publisher publisher = publisherRepository.findByIdWithBooks(publisherId)
                .orElseThrow(() -> new EntityNotFoundException("Publisher not found with id: " + publisherId));
        return publisher.getBooks().stream()
                .map(BookDTO.Response::fromEntity)  // BookDTO.Response.fromEntity 메서드는 Book 엔티티 → DTO 변환
                .collect(Collectors.toList());
    }
}
