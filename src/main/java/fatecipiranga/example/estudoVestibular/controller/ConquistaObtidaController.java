package fatecipiranga.example.estudoVestibular.controller;

import fatecipiranga.example.estudoVestibular.model.Conquista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import fatecipiranga.example.estudoVestibular.model.ConquistaObtida;
import fatecipiranga.example.estudoVestibular.repository.ConquistaObtidaRepository;

import java.util.List;
import java.util.Optional;

@RestController
public class ConquistaObtidaController {

  @Autowired
  ConquistaObtidaRepository conquistasObtRepo;

  @PostMapping("/api/conquista_obtida")
  public ResponseEntity<Void> gravar(@RequestBody ConquistaObtida conquistaObtida) {
    // Verifica se o item já existe no Banco de Dados
    if(conquistasObtRepo.procurarConquistaObtida(
            conquistaObtida.getAluno().getId(),
            conquistaObtida.getConquista().getId()
    ).isEmpty()){
      conquistasObtRepo.save(conquistaObtida); // Salva o objeto no Banco de Dados
      return ResponseEntity.status(HttpStatus.CREATED).build(); // Retorna 201
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Retorna 400
  }

    /*
    @PutMapping("/api/conquista_obtida")
    public ResponseEntity<Void> alterar(@RequestBody ConquistaObtida conquistaObtida) {
        conquistasObtRepo.save(conquistaObtida); // Salva o objeto no Banco de Dados
        return ResponseEntity.status(HttpStatus.CREATED).build(); // Retorna 201
    }
    */

  @GetMapping("/api/conquista_obtida/{conquistaObtidaId}")
  public ResponseEntity<ConquistaObtida> carregar(@PathVariable Long conquistaObtidaId) {
    // Procura item por ID
    return conquistasObtRepo.findById(conquistaObtidaId)
            .map(ResponseEntity::ok) // Retorna 200 + o item pesquisado
            .orElse(ResponseEntity.notFound().build()); // Retorna 404
  }

  @GetMapping("/api/conquistas_obtidas/aluno/{alunoId}")
  public ResponseEntity<List<ConquistaObtida>> listarTodasConquistasPorAluno(@PathVariable Long alunoId){
    Optional<List<ConquistaObtida>> conquistasObtidasPorAluno = conquistasObtRepo.procurarConquistasObtidasPorAluno(alunoId);

    return conquistasObtidasPorAluno
            .map(ResponseEntity::ok) // Retorna 200 + a lista de itens pesquisados
            .orElse(ResponseEntity.notFound().build()); // Retorna 404
  }

  @GetMapping("/api/conquistas_obtidas/aluno/{alunoId}/vestibular/{vestibularId}")
  public ResponseEntity<List<ConquistaObtida>> listarTodasConquistasPorAlunoPorVestibular(
          @PathVariable Long alunoId,
          @PathVariable Long vestibularId
  ) {
    Optional<List<ConquistaObtida>> conquistasObtidasPorAlunoPorVestibular = conquistasObtRepo.procurarConquistasObtidasPorAlunoPorVestibular(alunoId, vestibularId);

    return conquistasObtidasPorAlunoPorVestibular
            .map(ResponseEntity::ok) // Retorna 200 + a lista de itens pesquisados
            .orElse(ResponseEntity.notFound().build()); // Retorna 404
  }

  @DeleteMapping("/api/conquista_obtida/{conquistaObtidaId}")
  public ResponseEntity<Void> remover(@PathVariable Long conquistaObtidaId) {
    // Checa se o item existe por ID
    if(conquistasObtRepo.existsById(conquistaObtidaId)){
      // Deleta o item por ID
      conquistasObtRepo.deleteById(conquistaObtidaId);
      return ResponseEntity.noContent().build(); // Retorna 204
    }
    else{
      return ResponseEntity.notFound().build(); // Retorna 404
    }
  }
}