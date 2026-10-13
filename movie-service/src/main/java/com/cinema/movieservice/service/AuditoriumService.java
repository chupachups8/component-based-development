package com.cinema.movieservice.service;

import com.cinema.movieservice.dto.AuditoriumRequest;
import com.cinema.movieservice.exception.ResourceAlreadyExistsException;
import com.cinema.movieservice.exception.ResourceNotFoundException;
import com.cinema.movieservice.model.Auditorium;
import com.cinema.movieservice.repository.AuditoriumRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditoriumService {

    private final AuditoriumRepository auditoriumRepository;

    public AuditoriumService(
            AuditoriumRepository auditoriumRepository) {
        this.auditoriumRepository = auditoriumRepository;
    }

    public List<Auditorium> getAllAuditoriums() {
        return auditoriumRepository.findAll();
    }

    public Auditorium getAuditoriumById(Integer id) {
        return auditoriumRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auditorium with id " + id + " not found"
                        )
                );
    }

    public Auditorium createAuditorium(
            AuditoriumRequest request) {

        if (auditoriumRepository
                .existsByNameIgnoreCase(request.getName())) {

            throw new ResourceAlreadyExistsException(
                    "Auditorium with name "
                            + request.getName()
                            + " already exists"
            );
        }

        Auditorium auditorium = new Auditorium();

        auditorium.setName(request.getName());
        auditorium.setCapacity(request.getCapacity());

        return auditoriumRepository.save(auditorium);
    }

    public Auditorium updateAuditorium(
            Integer id,
            AuditoriumRequest request) {

        Auditorium auditorium = getAuditoriumById(id);

        if (!auditorium.getName()
                .equalsIgnoreCase(request.getName())
                && auditoriumRepository
                .existsByNameIgnoreCase(request.getName())) {

            throw new ResourceAlreadyExistsException(
                    "Auditorium with name "
                            + request.getName()
                            + " already exists"
            );
        }

        auditorium.setName(request.getName());
        auditorium.setCapacity(request.getCapacity());

        return auditoriumRepository.save(auditorium);
    }

    public void deleteAuditorium(Integer id) {

        Auditorium auditorium = getAuditoriumById(id);

        auditoriumRepository.delete(auditorium);
    }

}
