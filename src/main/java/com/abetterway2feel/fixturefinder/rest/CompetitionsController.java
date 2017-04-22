package com.abetterway2feel.fixturefinder.rest;

import com.abetterway2feel.fixturefinder.repository.competition.CompetitionRepository;
import com.abetterway2feel.fixturefinder.rest.dto.CompetitionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Paths.COMPETITIONS)
public class CompetitionsController {

    private CompetitionRepository competitionRepository;

    @Autowired
    public CompetitionsController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<CompetitionDTO>> getAll() {
        return new ResponseEntity<>(
                competitionRepository
                        .getAll()
                        .stream()
                        .map(CompetitionDTO::from)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

}
