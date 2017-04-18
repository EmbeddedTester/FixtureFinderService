package com.abetterway2feel.fixturefinder.rest;

import com.abetterway2feel.fixturefinder.domain.MatchDay;
import com.abetterway2feel.fixturefinder.domain.Team;
import com.abetterway2feel.fixturefinder.repository.fixtures.FixtureRepository;
import com.abetterway2feel.fixturefinder.repository.matchday.MatchDayRepository;
import com.abetterway2feel.fixturefinder.rest.dto.FixtureDTO;
import com.abetterway2feel.fixturefinder.rest.dto.MatchDayDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/fixtures")
public class FixturesController {
    private FixtureRepository fixtureRepository;
    private MatchDayRepository matchDayRepository;
    private Clock clock;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MatchDayDTO> getFixturesFor() {
        MatchDay matchDay = matchDayRepository.fetchFor(LocalDate.now(clock));
        return new ResponseEntity<>(MatchDayDTO.createResponseFor(matchDay), HttpStatus.OK);
    }

    @RequestMapping(value = "/{matchDate}", method = RequestMethod.GET)
    public ResponseEntity<MatchDayDTO> getFixturesFor(@PathVariable("matchDate") String matchDate) {
        LocalDate parsedMatchDate;
        try{
            parsedMatchDate = LocalDate.parse(matchDate);
        }
        catch (DateTimeParseException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MatchDay matchDay = matchDayRepository.fetchFor(parsedMatchDate);
        return new ResponseEntity<>(MatchDayDTO.createResponseFor(matchDay), HttpStatus.OK);
    }

    @RequestMapping(value = "/byTeam/{team}", method = RequestMethod.GET)
    public ResponseEntity<Collection<FixtureDTO>> getFixturesByTeam(@PathVariable("team") String team) {
        LocalDateTime now = LocalDate.now().atStartOfDay();
        LocalDate start = now.minus(12, ChronoUnit.MONTHS).toLocalDate();
        LocalDate end = now.plus(12, ChronoUnit.MONTHS).toLocalDate();

        List<FixtureDTO> fixtureList = fixtureRepository
                .getFor(Team.builder().name(team).build(), start, end)
                .stream()
                .map(FixtureDTO::from)
                .collect(Collectors.toList());

        return new ResponseEntity<>(fixtureList, HttpStatus.OK);
    }

}
