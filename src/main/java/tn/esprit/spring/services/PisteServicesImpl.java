package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PisteServicesImpl implements  IPisteServices{

    private IPisteRepository pisteRepository;

    @Override
    public List<Piste> retrieveAllPistes() {
        return pisteRepository.findAll();
    }

    @Override
    public Piste addPiste(Piste piste) {
        if (piste.getNamePiste() == null || piste.getNamePiste().isEmpty() || piste.getLength() <= 0 || piste.getSlope() < 0) {
            throw new IllegalArgumentException("Invalid Piste data");
        }
        return pisteRepository.save(piste);
    }



    @Override
    public boolean removePiste(Long numPiste) {
        if (pisteRepository.existsById(numPiste)) {
            pisteRepository.deleteById(numPiste);
            return true; // Deletion successful
        }
        throw new IllegalArgumentException("Piste not found"); // Throw exception if not found
    }


    @Override
    public Piste retrievePiste(Long numPiste) {
        return pisteRepository.findById(numPiste).orElse(null);
    }

    public Piste retrievePisteByName(String namePiste) {
        return pisteRepository.findByNamePiste(namePiste).orElse(null);
    }
    @Override
    public Piste updatePiste(Long numPiste, Piste newPisteData) {
        Optional<Piste> optionalPiste = pisteRepository.findById(numPiste);
        if (optionalPiste.isPresent()) {
            Piste existingPiste = optionalPiste.get();
            // Update the fields with newPisteData values, checking for nulls
            if (newPisteData.getNamePiste() != null) {
                existingPiste.setNamePiste(newPisteData.getNamePiste());
            }
            if (newPisteData.getColor() != null) {
                existingPiste.setColor(newPisteData.getColor());
            }
            if (newPisteData.getLength() != null) {
                existingPiste.setLength(newPisteData.getLength());
            }
            if (newPisteData.getSlope() != null) {
                existingPiste.setSlope(newPisteData.getSlope());
            }
            // Save the updated Piste entity
            return pisteRepository.save(existingPiste);
        } else {
            throw new IllegalArgumentException("Piste not found");
        }
    }

}

