package tn.esprit.spring.services;

import tn.esprit.spring.entities.Piste;

import java.util.List;

public interface IPisteServices {

    List<Piste> retrieveAllPistes();

    Piste  addPiste(Piste  piste);

    boolean removePiste (Long numPiste);

    Piste retrievePiste (Long numPiste);

    Piste retrievePisteByName (String namePiste);

    Piste updatePiste(Long numPiste, Piste newPisteData);

}
