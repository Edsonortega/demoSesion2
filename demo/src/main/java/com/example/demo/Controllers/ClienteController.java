package com.example.demo.Controllers;

import com.example.demo.Models.Cliente;
import com.example.demo.Persistence.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<Void> creaCliente(@RequestBody Cliente cliente) {
        Cliente cliente1 = clienteRepository.save(cliente);
        return ResponseEntity.created(URI.create(cliente1.toString())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getCliente(@PathVariable Long id) {
        Optional<Cliente> clienteDB = clienteRepository.findById(id);
        if (!clienteDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro id");
        }
        return ResponseEntity.ok(clienteDB.get());
    }

    @GetMapping
    public List<Cliente> getAllClients() {
        return clienteRepository.findAll();
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Long clienteId) {
        // Verificar si el cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(clienteId);
        return ResponseEntity.ok("Cliente eliminado correctamente");
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<String> editCliente(@PathVariable Long clienteId, @RequestBody Cliente updatedClient){
        // Verificar si el cliente existe
        if (!clienteRepository.existsById(clienteId)) {
            return ResponseEntity.notFound().build();
        }
        Cliente existingClient = clienteRepository.findById(clienteId).get();
        existingClient.setName(updatedClient.getName());
        existingClient.setLastName(updatedClient.getLastName());
        existingClient.setBornDate(updatedClient.getBornDate());
        existingClient.setEmail(updatedClient.getEmail());

        clienteRepository.save(existingClient);
        return ResponseEntity.ok("Cliente actualizado correctamente");
    }
}

