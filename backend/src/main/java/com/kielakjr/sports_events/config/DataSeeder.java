package com.kielakjr.sports_events.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.kielakjr.sports_events.model.Competition;
import com.kielakjr.sports_events.model.Event;
import com.kielakjr.sports_events.model.EventParticipant;
import com.kielakjr.sports_events.model.EventResult;
import com.kielakjr.sports_events.model.EventStatus;
import com.kielakjr.sports_events.model.Sport;
import com.kielakjr.sports_events.model.Team;
import com.kielakjr.sports_events.model.Venue;
import com.kielakjr.sports_events.repo.CompetitionRepo;
import com.kielakjr.sports_events.repo.EventParticipantRepo;
import com.kielakjr.sports_events.repo.EventRepo;
import com.kielakjr.sports_events.repo.EventResultRepo;
import com.kielakjr.sports_events.repo.SportRepo;
import com.kielakjr.sports_events.repo.TeamRepo;
import com.kielakjr.sports_events.repo.VenueRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final SportRepo sportRepo;
    private final VenueRepo venueRepo;
    private final CompetitionRepo competitionRepo;
    private final TeamRepo teamRepo;
    private final EventRepo eventRepo;
    private final EventParticipantRepo eventParticipantRepo;
    private final EventResultRepo eventResultRepo;

    @Override
    public void run(ApplicationArguments args) {
        if (eventRepo.count() > 0) {
            return;
        }

        Sport football = findOrSaveSport("Football");
        Sport basketball = findOrSaveSport("Basketball");
        Sport tennis = findOrSaveSport("Tennis");
        Sport iceHockey = findOrSaveSport("Ice Hockey");

        Venue bernabeu = findOrSaveVenue("Santiago Bernabeu", "Madrid");
        Venue campNou = findOrSaveVenue("Camp Nou", "Barcelona");
        Venue wembley = findOrSaveVenue("Wembley Stadium", "London");
        Venue allianz = findOrSaveVenue("Allianz Arena", "Munich");
        Venue staples = findOrSaveVenue("Staples Center", "Los Angeles");
        Venue msg = findOrSaveVenue("Madison Square Garden", "New York");
        Venue rolandGarros = findOrSaveVenue("Roland Garros", "Paris");
        Venue tdGarden = findOrSaveVenue("TD Garden", "Boston");

        Competition laLiga = findOrSaveCompetition("La Liga");
        Competition premierLeague = findOrSaveCompetition("Premier League");
        Competition championsLeague = findOrSaveCompetition("Champions League");
        Competition nba = findOrSaveCompetition("NBA Regular Season");
        Competition frenchOpen = findOrSaveCompetition("French Open");
        Competition bundesliga = findOrSaveCompetition("Bundesliga");
        Competition nhl = findOrSaveCompetition("NHL Regular Season");

        Team realMadrid = findOrSaveTeam("Real Madrid", "Real Madrid Club de Futbol", "RMA", "ESP", football);
        Team barcelona = findOrSaveTeam("Barcelona", "Futbol Club Barcelona", "BAR", "ESP", football);
        Team manCity = findOrSaveTeam("Manchester City", "Manchester City Football Club", "MCI", "GBR", football);
        Team arsenal = findOrSaveTeam("Arsenal", "Arsenal Football Club", "ARS", "GBR", football);
        Team bayern = findOrSaveTeam("Bayern Munich", "FC Bayern Munich", "BAY", "DEU", football);
        Team dortmund = findOrSaveTeam("Borussia Dortmund", "Ballspielverein Borussia 09 e.V. Dortmund", "BVB", "DEU", football);

        Team lakers = findOrSaveTeam("Lakers", "Los Angeles Lakers", "LAL", "USA", basketball);
        Team celtics = findOrSaveTeam("Celtics", "Boston Celtics", "BOS", "USA", basketball);
        Team knicks = findOrSaveTeam("Knicks", "New York Knicks", "NYK", "USA", basketball);
        Team warriors = findOrSaveTeam("Warriors", "Golden State Warriors", "GSW", "USA", basketball);

        Team djokovic = findOrSaveTeam("Djokovic", "Novak Djokovic", "DJO", "SRB", tennis);
        Team alcaraz = findOrSaveTeam("Alcaraz", "Carlos Alcaraz", "ALC", "ESP", tennis);
        Team sinner = findOrSaveTeam("Sinner", "Jannik Sinner", "SIN", "ITA", tennis);

        Team bruins = findOrSaveTeam("Bruins", "Boston Bruins", "BRU", "USA", iceHockey);
        Team rangers = findOrSaveTeam("Rangers", "New York Rangers", "NYR", "USA", iceHockey);

        Event elClasico = eventRepo.save(new Event(null, LocalDate.of(2025, 10, 26), LocalTime.of(20, 0), "2025/2026", EventStatus.COMPLETED, "Matchday 10", laLiga, bernabeu));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", elClasico, realMadrid),
            new EventParticipant(null, "AWAY", elClasico, barcelona)
        ));
        eventResultRepo.saveAll(List.of(
            new EventResult(null, 3, true, realMadrid, elClasico),
            new EventResult(null, 1, false, barcelona, elClasico)
        ));

        Event cityArsenal = eventRepo.save(new Event(null, LocalDate.of(2025, 11, 2), LocalTime.of(16, 30), "2025/2026", EventStatus.COMPLETED, "Matchday 11", premierLeague, wembley));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", cityArsenal, manCity),
            new EventParticipant(null, "AWAY", cityArsenal, arsenal)
        ));
        eventResultRepo.saveAll(List.of(
            new EventResult(null, 1, false, manCity, cityArsenal),
            new EventResult(null, 1, false, arsenal, cityArsenal)
        ));

        Event derKlassiker = eventRepo.save(new Event(null, LocalDate.of(2025, 11, 9), LocalTime.of(18, 30), "2025/2026", EventStatus.COMPLETED, "Matchday 11", bundesliga, allianz));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", derKlassiker, bayern),
            new EventParticipant(null, "AWAY", derKlassiker, dortmund)
        ));
        eventResultRepo.saveAll(List.of(
            new EventResult(null, 2, true, bayern, derKlassiker),
            new EventResult(null, 1, false, dortmund, derKlassiker)
        ));

        Event clRealCity = eventRepo.save(new Event(null, LocalDate.of(2026, 4, 8), LocalTime.of(21, 0), "2025/2026", EventStatus.SCHEDULED, "Quarter-Final", championsLeague, bernabeu));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", clRealCity, realMadrid),
            new EventParticipant(null, "AWAY", clRealCity, manCity)
        ));

        Event clBarcaArsenal = eventRepo.save(new Event(null, LocalDate.of(2026, 4, 9), LocalTime.of(21, 0), "2025/2026", EventStatus.SCHEDULED, "Quarter-Final", championsLeague, campNou));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", clBarcaArsenal, barcelona),
            new EventParticipant(null, "AWAY", clBarcaArsenal, arsenal)
        ));

        Event lakersCeltics = eventRepo.save(new Event(null, LocalDate.of(2025, 12, 25), LocalTime.of(20, 0), "2025/2026", EventStatus.COMPLETED, "Regular Season", nba, staples));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", lakersCeltics, lakers),
            new EventParticipant(null, "AWAY", lakersCeltics, celtics)
        ));
        eventResultRepo.saveAll(List.of(
            new EventResult(null, 108, false, lakers, lakersCeltics),
            new EventResult(null, 112, true, celtics, lakersCeltics)
        ));

        Event knicksWarriors = eventRepo.save(new Event(null, LocalDate.of(2026, 3, 23), LocalTime.of(19, 30), "2025/2026", EventStatus.ONGOING, "Regular Season", nba, msg));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", knicksWarriors, knicks),
            new EventParticipant(null, "AWAY", knicksWarriors, warriors)
        ));

        Event lakersKnicks = eventRepo.save(new Event(null, LocalDate.of(2026, 4, 1), LocalTime.of(19, 0), "2025/2026", EventStatus.SCHEDULED, "Regular Season", nba, msg));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", lakersKnicks, knicks),
            new EventParticipant(null, "AWAY", lakersKnicks, lakers)
        ));

        Event frenchOpenFinal = eventRepo.save(new Event(null, LocalDate.of(2026, 6, 7), LocalTime.of(15, 0), "2026", EventStatus.SCHEDULED, "Final", frenchOpen, rolandGarros));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", frenchOpenFinal, djokovic),
            new EventParticipant(null, "AWAY", frenchOpenFinal, alcaraz)
        ));

        Event frenchOpenSemi = eventRepo.save(new Event(null, LocalDate.of(2026, 6, 6), LocalTime.of(14, 0), "2026", EventStatus.SCHEDULED, "Semi-Final", frenchOpen, rolandGarros));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", frenchOpenSemi, sinner),
            new EventParticipant(null, "AWAY", frenchOpenSemi, alcaraz)
        ));

        Event bruinsRangers = eventRepo.save(new Event(null, LocalDate.of(2026, 1, 15), LocalTime.of(19, 0), "2025/2026", EventStatus.COMPLETED, "Regular Season", nhl, tdGarden));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", bruinsRangers, bruins),
            new EventParticipant(null, "AWAY", bruinsRangers, rangers)
        ));
        eventResultRepo.saveAll(List.of(
            new EventResult(null, 4, true, bruins, bruinsRangers),
            new EventResult(null, 2, false, rangers, bruinsRangers)
        ));

        Event rangersBruins = eventRepo.save(new Event(null, LocalDate.of(2026, 4, 5), LocalTime.of(19, 0), "2025/2026", EventStatus.SCHEDULED, "Regular Season", nhl, msg));
        eventParticipantRepo.saveAll(List.of(
            new EventParticipant(null, "HOME", rangersBruins, rangers),
            new EventParticipant(null, "AWAY", rangersBruins, bruins)
        ));
    }

    private Sport findOrSaveSport(String name) {
        return sportRepo.findByName(name)
            .orElseGet(() -> sportRepo.save(new Sport(null, name)));
    }

    private Venue findOrSaveVenue(String name, String city) {
        return venueRepo.findByName(name)
            .orElseGet(() -> venueRepo.save(new Venue(null, name, city)));
    }

    private Competition findOrSaveCompetition(String name) {
        return competitionRepo.findByName(name)
            .orElseGet(() -> competitionRepo.save(new Competition(null, name)));
    }

    private Team findOrSaveTeam(String name, String officialName, String abbreviation, String countryCode, Sport sport) {
        return teamRepo.findByName(name)
            .orElseGet(() -> teamRepo.save(new Team(null, name, officialName, abbreviation, countryCode, sport)));
    }
}
