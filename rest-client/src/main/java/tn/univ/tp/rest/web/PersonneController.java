package tn.univ.tp.rest.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tn.univ.tp.rest.dto.DeleteResponse;
import tn.univ.tp.rest.dto.PersonneDto;
import tn.univ.tp.rest.dto.PersonneRequest;
import tn.univ.tp.rest.service.PersonneService;

@RestController
@RequestMapping("/api/personnes")
public class PersonneController {

    private final PersonneService personneService;

    public PersonneController(PersonneService personneService) {
        this.personneService = personneService;
    }

    @GetMapping
    public List<PersonneDto> getAll() {
        return personneService.getAll();
    }

    @GetMapping("/{id}")
    public PersonneDto getById(@PathVariable("id") Long id) {
        return personneService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonneDto create(@RequestBody PersonneRequest request) {
        return personneService.create(request);
    }

    @PutMapping("/{id}")
    public PersonneDto update(@PathVariable("id") Long id, @RequestBody PersonneRequest request) {
        return personneService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse delete(@PathVariable("id") Long id) {
        return personneService.delete(id);
    }
}
