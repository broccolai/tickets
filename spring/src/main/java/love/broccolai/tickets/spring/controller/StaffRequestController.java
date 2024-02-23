package love.broccolai.tickets.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffRequestController {

    @GetMapping("/staff/list")
    public int list() {
        return -1;
    }
}
