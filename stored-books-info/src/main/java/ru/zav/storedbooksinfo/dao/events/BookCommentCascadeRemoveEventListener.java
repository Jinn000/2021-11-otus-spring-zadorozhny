package ru.zav.storedbooksinfo.dao.events;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.zav.storedbooksinfo.dao.BookCommentRepository;
import ru.zav.storedbooksinfo.domain.Book;

@RequiredArgsConstructor
@Component
public class BookCommentCascadeRemoveEventListener extends AbstractMongoEventListener<Book> {
    private final BookCommentRepository bookCommentRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        final Document source = event.getSource();
        final String commentId = source.get("_id").toString();
        bookCommentRepository.removeCommentsByBookId(commentId);
    }
}
