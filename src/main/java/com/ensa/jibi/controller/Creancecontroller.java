package com.ensa.jibi.controller;


import com.ensa.jibi.dto.CreanceFormDTO;
import com.ensa.jibi.model.Facture;
import com.ensa.jibi.service.CreanceService;
import com.ensa.jibi.service.FactureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping()
public class Creancecontroller {

    @Autowired
    private CreanceService creanceService;
    @Autowired
    private FactureService factureService;

    @GetMapping("/getforms")
    public ResponseEntity<CreanceFormDTO> getCreanceFormDetails(@RequestParam(value = "id", required = true) Long id) {
        CreanceFormDTO creanceFormDTO = creanceService.getCreanceFormDetails(id);
        if (creanceFormDTO != null) {
            return ResponseEntity.ok(creanceFormDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getimpayes")
    public ResponseEntity<List<Facture>> getImpayeFacturesByRefAndCreancier(@RequestParam(value = "ref") String ref, @RequestParam(value = "id") Long creanceId) {
        List<Facture> factures = factureService.getImpayeFacturesByRefAndCreance(ref, creanceId);
        if (factures.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(factures, HttpStatus.OK);
    }


}