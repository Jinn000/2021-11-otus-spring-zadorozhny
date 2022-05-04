package ru.zav.storedbooksinfo.dao.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.zav.storedbooksinfo.dao.BookCommentRepository;
import ru.zav.storedbooksinfo.domain.Book;

@RequiredArgsConstructor
@Component
public class BookCascadeSaveMongoEventListener extends AbstractMongoEventListener<Book> {
    private final BookCommentRepository bookCommentRepository;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        super.onBeforeConvert(event);
        final Book book = event.getSource();
        if(book.getComments() != null){
            book.getComments().stream().filter(b-> b.getId() == null).forEach(bookCommentRepository::save);
        }
    }
}
