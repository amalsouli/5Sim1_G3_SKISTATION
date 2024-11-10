package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "\uD83C\uDFBF Piste Management")
@RestController
@RequestMapping("/piste")
@RequiredArgsConstructor
public class PisteRestController {

    private final IPisteServices pisteServices;

    @Validated
    @Operation(description = "Add Piste")
    @PostMapping("/add")
    public ResponseEntity<Piste> addPiste(@Valid @RequestBody Piste piste) {
        Piste savedPiste = pisteServices.addPiste(piste);
        return new ResponseEntity<>(savedPiste, HttpStatus.CREATED);
    }

    @Operation(description = "Retrieve fall Pistes")
    @GetMapping("/all")
    public ResponseEntity<List<Piste>> getAllPistes() {
        List<Piste> pistes = pisteServices.retrieveAllPistes();
        return ResponseEntity.ok(pistes);
    }

    @Operation(description = "Retrieve Piste by Id")
    @GetMapping("/get/{idPiste}")
    public ResponseEntity<Piste> getById(@PathVariable("idPiste") Long numPiste) {
        Piste piste = pisteServices.retrievePiste(numPiste);
        if (piste == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Piste not found");
        }
        return ResponseEntity.ok(piste);
    }

    @Operation(description = "Delete Piste by Id")
    @DeleteMapping("/delete/{idPiste}")
    public ResponseEntity<Void> deleteById(@PathVariable("idPiste") Long numPiste) {
        try {
            // Call the service method to remove the Piste
            boolean isRemoved = pisteServices.removePiste(numPiste);

            if (isRemoved) {
                return ResponseEntity.noContent().build(); // Return 204 No Content
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found for other exceptions
        }
    }

    @Operation(description = "Retrieve Piste by Name")
    @GetMapping("/name/{namePiste}")
    public ResponseEntity<Piste> getPisteByName(@PathVariable String namePiste) {
        Optional<Piste> piste = Optional.ofNullable(pisteServices.retrievePisteByName(namePiste));
        return piste.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @Operation(description = "Update Piste by Id")
    @PutMapping("/update/{idPiste}")
    public ResponseEntity<Piste> updatePiste(
            @PathVariable("idPiste") Long idPiste,
            @Valid @RequestBody Piste newPisteData) {
        if (newPisteData == null) {
            return ResponseEntity.badRequest().build(); // Return a bad request if no data is provided
        }
        try {
            Piste updatedPiste = pisteServices.updatePiste(idPiste, newPisteData);
            return ResponseEntity.ok(updatedPiste);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Piste not found", e);
        }
    }

}
