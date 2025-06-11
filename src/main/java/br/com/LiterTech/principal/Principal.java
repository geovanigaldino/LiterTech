package br.com.LiterTech.principal;

import br.com.LiterTech.model.Autor;
import br.com.LiterTech.model.DadosAutor;
import br.com.LiterTech.model.DadosLivro;
import br.com.LiterTech.model.DadosRespostaAPI;
import br.com.LiterTech.model.Livro;
import br.com.LiterTech.repository.AutorRepository;
import br.com.LiterTech.repository.LivroRepository;
import br.com.LiterTech.service.ConsumoAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;


@SpringBootApplication(scanBasePackages = "br.com.LiterTech")
public class Principal implements CommandLineRunner {

    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private AutorRepository autorRepository;
    private ConsumoAPI consumo = new ConsumoAPI();
    private ObjectMapper mapper = new ObjectMapper();
    private Scanner leitura = new Scanner(System.in);

    private static final String BASE_URL = "https://gutendex.com/books/";

    public static void main(String[] args) {
        SpringApplication.run(Principal.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        exibirMenu();
    }

    private void exibirMenu() {
        int opcao = -1;
        while(opcao != 0) {
            String menu = """
                    \n
                    === LITERTECH - CATÁLOGO DE LIVROS ===
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Listar TOP 10 livros por downloads
                    7 - Buscar autor por nome
                    8 - Listar livros de um autor específico
                    9 - Exibir estatísticas de idiomas
                    10 - Remover livro por título
                    0 - Sair
                    Escolha uma opção:
                    """;

            System.out.print(menu);

            try {
                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao) {
                    case 1:
                        buscarLivroPorTitulo();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosNoAno();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 6:
                        listarTop10LivrosPorDownloads();
                        break;
                    case 7:
                        buscarAutorPorNome();
                        break;
                    case 8:
                        listarLivrosDeAutorEspecifico();
                        break;
                    case 9:
                        exibirEstatisticasDeIdiomas();
                        break;
                    case 10:
                        removerLivroPorTitulo();
                        break;
                    case 0:
                        System.out.println("\nSaindo do LiterTech. Até mais!");
                        break;
                    default:
                        System.out.println("\nOpção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nEntrada inválida. Por favor, digite um número.");
                leitura.nextLine();
                opcao = -1;
            } catch (Exception e) {
                System.err.println("\nOcorreu um erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void buscarLivroPorTitulo() {
        System.out.print("Digite o título para busca: ");
        String tituloBusca = leitura.nextLine();
        String urlBusca = BASE_URL + "?search=" + tituloBusca.replace(" ", "%20");

        String jsonRetorno = consumo.obterDados(urlBusca);

        try {
            DadosRespostaAPI respostaAPI = mapper.readValue(jsonRetorno, DadosRespostaAPI.class);
            List<DadosLivro> dadosLivros = respostaAPI.resultados();

            if (dadosLivros.isEmpty()) {
                System.out.println("\nNenhum livro encontrado com o título: " + tituloBusca);
            } else {
                dadosLivros.forEach(dadosLivro -> {
                    System.out.println("\n--- Livro Encontrado ---");
                    System.out.println("Título: " + dadosLivro.titulo());
                    System.out.println("Idiomas: " + dadosLivro.idiomas());
                    System.out.println("Downloads: " + dadosLivro.numeroDownloads());

                    Autor autor = null;
                    if (!dadosLivro.autores().isEmpty()) {
                        DadosAutor dadosAutor = dadosLivro.autores().get(0);

                        Optional<Autor> autorExistente = autorRepository.findByNome(dadosAutor.nome());
                        if (autorExistente.isPresent()) {
                            autor = autorExistente.get();
                            System.out.println("Autor já existe: " + autor.getNome());
                        } else {
                            autor = new Autor(dadosAutor);
                            autorRepository.save(autor);
                            System.out.println("Novo Autor salvo: " + autor.getNome());
                        }
                    }

                    Optional<Livro> livroExistente = livroRepository.findByTitulo(dadosLivro.titulo());
                    if (livroExistente.isEmpty()) {
                        Livro livro = new Livro(dadosLivro);
                        livro.setAutor(autor);
                        livroRepository.save(livro);
                        System.out.println("Novo Livro salvo no banco de dados: " + livro.getTitulo());
                    } else {
                        System.out.println("Livro já existe no banco de dados, pulando: " + dadosLivro.titulo());
                    }
                    System.out.println("--------------------");
                });
            }

        } catch (Exception e) {
            System.err.println("\nErro ao buscar e processar livro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listarLivrosRegistrados() {
        System.out.println("\n--- Todos os Livros Registrados ---");
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("\nNenhum livro registrado ainda.");
        } else {
            livros.forEach(livro -> {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                System.out.println("Idioma(s): " + livro.getIdiomas());
                System.out.println("Downloads: " + livro.getNumeroDownloads());
                System.out.println("--------------------");
            });
        }
    }

    private void listarAutoresRegistrados() {
        System.out.println("\n--- Todos os Autores Registrados ---");
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("\nNenhum autor registrado ainda.");
        } else {
            autores.forEach(autor -> {
                System.out.println("Nome: " + autor.getNome());
                System.out.println("Ano de Nascimento: " + (autor.getAnoNascimento() != null ? autor.getAnoNascimento() : "Desconhecido"));
                System.out.println("Ano de Falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Vivo/Desconhecido"));
                System.out.println("--------------------");
            });
        }
    }

    private void listarAutoresVivosNoAno() {
        System.out.print("\nDigite o ano para buscar autores vivos: ");
        int anoBusca;
        try {
            anoBusca = leitura.nextInt();
            leitura.nextLine();

            System.out.println("\n--- Autores vivos em " + anoBusca + " ---");
            List<Autor> autoresVivos = autorRepository.findAutoresVivosNoAno(anoBusca);

            if (autoresVivos.isEmpty()) {
                System.out.println("\nNenhum autor encontrado vivo em " + anoBusca + ".");
            } else {
                autoresVivos.forEach(autor -> {
                    System.out.println("Nome: " + autor.getNome());
                    System.out.println("Ano de Nascimento: " + autor.getAnoNascimento());
                    System.out.println("Ano de Falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Vivo"));
                    System.out.println("--------------------");
                });
            }
        } catch (InputMismatchException e) {
            System.out.println("\nEntrada inválida. Por favor, digite um ano válido.");
            leitura.nextLine();
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.print("Digite o idioma (Ex: en, pt, fr): ");
        String idiomaBusca = leitura.nextLine().toLowerCase();

        System.out.println("\n--- Livros no idioma '" + idiomaBusca + "' ---");
        List<Livro> livrosPorIdioma = livroRepository.findByIdioma(idiomaBusca); // Usando o método do repositório

        if (livrosPorIdioma.isEmpty()) {
            System.out.println("\nNenhum livro encontrado no idioma '" + idiomaBusca + "'.");
        } else {
            livrosPorIdioma.forEach(livro -> {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                System.out.println("--------------------");
            });
        }
    }

    private void listarTop10LivrosPorDownloads() {
        System.out.println("\n--- Top 10 Livros por Downloads ---");
        List<Livro> topLivros = livroRepository.findTop10ByOrderByNumeroDownloadsDesc();

        if (topLivros.isEmpty()) {
            System.out.println("\nNenhum livro encontrado. Tente buscar e salvar alguns primeiro.");
        } else {
            topLivros.forEach(livro -> {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                System.out.println("Downloads: " + livro.getNumeroDownloads());
                System.out.println("--------------------");
            });
        }
    }

    private void buscarAutorPorNome() {
        System.out.print("Digite o nome (ou parte dele) do autor para buscar: ");
        String nomeBusca = leitura.nextLine();

        System.out.println("\n--- Autores encontrados com '" + nomeBusca + "' ---");
        List<Autor> autoresEncontrados = autorRepository.findByNomeContainingIgnoreCase(nomeBusca);

        if (autoresEncontrados.isEmpty()) {
            System.out.println("\nNenhum autor encontrado com o nome '" + nomeBusca + "' no banco de dados.");
        } else {
            autoresEncontrados.forEach(autor -> {
                System.out.println("Nome: " + autor.getNome());
                System.out.println("Ano de Nascimento: " + (autor.getAnoNascimento() != null ? autor.getAnoNascimento() : "Desconhecido"));
                System.out.println("Ano de Falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Vivo/Desconhecido"));
                System.out.println("--------------------");
            });
        }
    }

    private void listarLivrosDeAutorEspecifico() {
        System.out.print("Digite o nome (ou parte dele) do autor para listar seus livros: ");
        String nomeAutorBusca = leitura.nextLine();

        System.out.println("\n--- Livros do autor com nome '" + nomeAutorBusca + "' ---");
        List<Livro> livrosDoAutor = livroRepository.findByAutorNomeContainingIgnoreCase(nomeAutorBusca);

        if (livrosDoAutor.isEmpty()) {
            System.out.println("\nNenhum livro encontrado para o autor com nome '" + nomeAutorBusca + "' no banco de dados.");
        } else {
            livrosDoAutor.forEach(livro -> {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + (livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido"));
                System.out.println("Idioma(s): " + livro.getIdiomas());
                System.out.println("Downloads: " + livro.getNumeroDownloads());
                System.out.println("--------------------");
            });
        }
    }

    private void exibirEstatisticasDeIdiomas() {
        System.out.println("\n--- Estatísticas de Livros por Idioma ---");

        long countEnglish = livroRepository.countByIdiomasContainingIgnoreCase("en");
        System.out.println("Quantidade de livros em Inglês (en): " + countEnglish);

        long countFrench = livroRepository.countByIdiomasContainingIgnoreCase("fr");
        System.out.println("Quantidade de livros em Francês (fr): " + countFrench);

        long countPortuguese = livroRepository.countByIdiomasContainingIgnoreCase("pt");
        System.out.println("Quantidade de livros em Português (pt): " + countPortuguese);

        System.out.println("------------------------------------------");
    }

    private void removerLivroPorTitulo() {
        System.out.print("\nDigite o título (ou parte dele) do livro que deseja REMOVER: ");
        String tituloRemover = leitura.nextLine();

        List<Livro> livrosParaRemover = livroRepository.findByTituloContainingIgnoreCase(tituloRemover);

        if (livrosParaRemover.isEmpty()) {
            System.out.println("\nNenhum livro encontrado com o título '" + tituloRemover + "' para remoção.");
        } else {
            System.out.println("\n--- Livros ENCONTRADOS para remoção ---");
            livrosParaRemover.forEach(livro -> System.out.println("- " + livro.getTitulo() + " (ID: " + livro.getId() + ")"));
            System.out.print("\nConfirma a remoção desses livros? (sim/não): ");
            String confirmacao = leitura.nextLine().trim().toLowerCase();

            if (confirmacao.equals("sim")) {
                long removidos = livroRepository.deleteByTituloContainingIgnoreCase(tituloRemover);
                System.out.println("\n" + removidos + " livro(s) removido(s) com sucesso.");
            } else {
                System.out.println("\nRemoção cancelada.");
            }
        }
    }
}