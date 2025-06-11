package br.com.LiterTech.repository;

import br.com.LiterTech.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTitulo(String titulo);
    List<Livro> findTop10ByOrderByNumeroDownloadsDesc();

    @Query("SELECT l FROM Livro l WHERE l.idiomas ILIKE %:idioma%")
    List<Livro> findByIdioma(@org.springframework.data.repository.query.Param("idioma") String idioma);

    List<Livro> findByAutorNomeContainingIgnoreCase(String nomeAutor);
    long countByIdiomasContainingIgnoreCase(String idioma);

    @Transactional
    long deleteByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByTituloContainingIgnoreCase(String tituloRemover);
}