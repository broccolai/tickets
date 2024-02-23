package love.broccolai.tickets.spring.controller;

import com.google.inject.Injector;
import love.broccolai.tickets.api.model.Ticket;
import love.broccolai.tickets.api.service.ModificationService;
import love.broccolai.tickets.api.service.StorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
public class UserRequestController {

    private final StorageService storageService;
    private final ModificationService modificationService;

    public UserRequestController(final Injector injector) {
        this.storageService = injector.getInstance(StorageService.class);
        this.modificationService = injector.getInstance(ModificationService.class);
    }

    @GetMapping("/user/create")
    public int create() {
        Ticket createdTicket =  this.storageService.createTicket(UUID.randomUUID(), "test");

        return createdTicket.id();
    }
}
