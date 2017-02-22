package com.abetterway2feel.fixturefinder.rest;

import com.abetterway2feel.fixturefinder.domain.MatchDay;
import com.abetterway2feel.fixturefinder.repository.MatchDayRepository;
import com.abetterway2feel.fixturefinder.rest.dto.MatchDayDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDate;

@RestController
@RequestMapping("/fixtures")
public class FixturesController {
    static Logger log = Logger.getLogger(FixturesController.class.getName());

    private MatchDayRepository matchDayRepository;

    private Clock clock;

    @Autowired
    public FixturesController(MatchDayRepository matchDayRepository, Clock clock) {
        this.matchDayRepository = matchDayRepository;
        this.clock = clock;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MatchDayDTO> getFixturesFor() {
        MatchDay matchDay = matchDayRepository.getFor(LocalDate.now(clock));
        return new ResponseEntity<>(MatchDayDTO.createResponseFor(matchDay), HttpStatus.OK);
    }

    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    public ResponseEntity<MatchDayDTO> getFixturesFor(@PathVariable("date") String date) {
        MatchDay matchDay = matchDayRepository.getFor(LocalDate.parse(date));
        return new ResponseEntity<>(MatchDayDTO.createResponseFor(matchDay), HttpStatus.OK);
    }

}
