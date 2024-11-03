package dev.coem.post;

import org.springframework.data.repository.ListCrudRepository;

public interface PostRepository extends ListCrudRepository<Post, Integer> {

    Post findByTitle(String title);
}
