package com.apa.back.core.domain.entities;

import com.apa.back.core.domain.builder.AnimalBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    @DisplayName("Deve criar Animal com todos os campos obrigatórios")
    void deveCriarAnimalComCamposObrigatorios() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .especie("Cachorro")
                .build();

        assertNotNull(animal);
        assertEquals("Rex", animal.getNome());
        assertEquals("Cachorro", animal.getEspecie());
    }

    @Test
    @DisplayName("Deve criar Animal com especie padrão quando não informado")
    void deveCriarAnimalComEspeciePadrao() {
        Animal animal = new AnimalBuilder()
                .nome("Miau")
                .build();

        assertEquals("Desconhecida", animal.getEspecie());
    }

    @Test
    @DisplayName("Deve criar Animal com todos os campos preenchidos")
    void deveCriarAnimalComTodosOsCampos() {
        LocalDate dataCadastro = LocalDate.of(2024, 1, 15);

        Animal animal = new AnimalBuilder()
                .id(1L)
                .nome("Rex")
                .idade(3)
                .raca("Labrador")
                .rgAnimal("RG-2024-001")
                .especie("Cachorro")
                .sexo("M")
                .cor("Marrom")
                .comportamento("Calmo e amigável")
                .historico("Encontrado na rua")
                .dataCadastro(dataCadastro)
                .disponivelParaAdocao(true)
                .build();

        assertNotNull(animal);
        assertEquals(1L, animal.getId());
        assertEquals("Rex", animal.getNome());
        assertEquals(3, animal.getIdade());
        assertEquals("Labrador", animal.getRaca());
        assertEquals("RG-2024-001", animal.getRgAnimal());
        assertEquals("Cachorro", animal.getEspecie());
        assertEquals("M", animal.getSexo());
        assertEquals("Marrom", animal.getCor());
        assertEquals("Calmo e amigável", animal.getComportamento());
        assertEquals("Encontrado na rua", animal.getHistorico());
        assertEquals(dataCadastro, animal.getDataCadastro());
        assertTrue(animal.getDisponivelParaAdocao());
    }

    @Test
    @DisplayName("Deve criar Animal com campos nullable nulos")
    void deveCriarAnimalComCamposNullableNulos() {
        Animal animal = new AnimalBuilder()
                .nome("Miau")
                .especie("Gato")
                .build();

        assertNotNull(animal);
        assertNull(animal.getId());
        assertNull(animal.getIdade());
        assertNull(animal.getRaca());
        assertNull(animal.getRgAnimal());
        assertNull(animal.getSexo());
        assertNull(animal.getCor());
        assertNull(animal.getComportamento());
        assertNull(animal.getHistorico());
        assertNull(animal.getDataCadastro());
        assertNull(animal.getDisponivelParaAdocao());
        assertNull(animal.getDeletadoEm());
        assertNull(animal.getDeletadoPor());
    }

    @Test
    @DisplayName("Deve criar Animal apenas com idade")
    void deveCriarAnimalApenasComIdade() {
        Animal animal = new AnimalBuilder()
                .nome("Totó")
                .idade(5)
                .build();

        assertEquals("Totó", animal.getNome());
        assertEquals(5, animal.getIdade());
        assertNull(animal.getRaca());
    }

    @Test
    @DisplayName("Deve criar Animal apenas com raca")
    void deveCriarAnimalApenasComRaca() {
        Animal animal = new AnimalBuilder()
                .nome("Bobby")
                .raca("Poodle")
                .build();

        assertEquals("Bobby", animal.getNome());
        assertEquals("Poodle", animal.getRaca());
        assertNull(animal.getIdade());
    }

    @Test
    @DisplayName("Deve criar Animal apenas com rgAnimal")
    void deveCriarAnimalApenasComRgAnimal() {
        Animal animal = new AnimalBuilder()
                .nome("Max")
                .rgAnimal("RG-001")
                .build();

        assertEquals("Max", animal.getNome());
        assertEquals("RG-001", animal.getRgAnimal());
    }

    @Test
    @DisplayName("Deve criar Animal apenas com sexo")
    void deveCriarAnimalApenasComSexo() {
        Animal animal = new AnimalBuilder()
                .nome("Luna")
                .sexo("F")
                .build();

        assertEquals("Luna", animal.getNome());
        assertEquals("F", animal.getSexo());
    }

    @Test
    @DisplayName("Deve criar Animal apenas com cor")
    void deveCriarAnimalApenasComCor() {
        Animal animal = new AnimalBuilder()
                .nome("Bola")
                .cor("Branco")
                .build();

        assertEquals("Bola", animal.getNome());
        assertEquals("Branco", animal.getCor());
    }

    @Test
    @DisplayName("Deve criar Animal apenas com comportamento")
    void deveCriarAnimalApenasComComportamento() {
        Animal animal = new AnimalBuilder()
                .nome("Buddy")
                .comportamento("Agitado")
                .build();

        assertEquals("Buddy", animal.getNome());
        assertEquals("Agitado", animal.getComportamento());
    }

    @Test
    @DisplayName("Deve criar Animal apenas com historico")
    void deveCriarAnimalApenasComHistorico() {
        Animal animal = new AnimalBuilder()
                .nome("Charlie")
                .historico("Resgatado de maus tratos")
                .build();

        assertEquals("Charlie", animal.getNome());
        assertEquals("Resgatado de maus tratos", animal.getHistorico());
    }

    @Test
    @DisplayName("Deve criar Animal apenas com dataCadastro")
    void deveCriarAnimalApenasComDataCadastro() {
        LocalDate data = LocalDate.of(2024, 5, 20);
        Animal animal = new AnimalBuilder()
                .nome("Mel")
                .dataCadastro(data)
                .build();

        assertEquals("Mel", animal.getNome());
        assertEquals(data, animal.getDataCadastro());
    }

    @Test
    @DisplayName("Deve criar Animal com disponivelParaAdocao true")
    void deveCriarAnimalDisponivelParaAdocao() {
        Animal animal = new AnimalBuilder()
                .nome("Thor")
                .disponivelParaAdocao(true)
                .build();

        assertTrue(animal.getDisponivelParaAdocao());
    }

    @Test
    @DisplayName("Deve criar Animal com disponivelParaAdocao false")
    void deveCriarAnimalNaoDisponivelParaAdocao() {
        Animal animal = new AnimalBuilder()
                .nome("Loki")
                .disponivelParaAdocao(false)
                .build();

        assertFalse(animal.getDisponivelParaAdocao());
    }

    @Test
    @DisplayName("Deve realizar softDelete corretamente")
    void deveRealizarSoftDeleteCorretamente() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .disponivelParaAdocao(true)
                .build();

        animal.softDelete(123);

        assertFalse(animal.getDisponivelParaAdocao());
        assertNotNull(animal.getDeletadoEm());
        assertEquals(LocalDate.now(), animal.getDeletadoEm());
        assertEquals(123, animal.getDeletadoPor());
    }

    @Test
    @DisplayName("Deve adicionar historico de saude corretamente")
    void deveAdicionarHistoricoSaudeCorretamente() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .build();

        HistoricoSaude evento = new HistoricoSaude();
        evento.setDescricao("Consulta veterinária");
        evento.setTipoEvento("Consulta");

        animal.addHistoricoSaude(evento);

        assertEquals(1, animal.getHistoricoSaude().size());
        assertEquals(evento, animal.getHistoricoSaude().get(0));
        assertEquals(animal, evento.getAnimal());
    }

    @Test
    @DisplayName("Deve remover historico de saude corretamente")
    void deveRemoverHistoricoSaudeCorretamente() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .build();

        HistoricoSaude evento = new HistoricoSaude();
        evento.setDescricao("Consulta veterinária");

        animal.addHistoricoSaude(evento);
        assertEquals(1, animal.getHistoricoSaude().size());

        animal.removeHistoricoSaude(evento);
        assertEquals(0, animal.getHistoricoSaude().size());
        assertNull(evento.getAnimal());
    }

    @Test
    @DisplayName("Deve adicionar vacinacao corretamente")
    void deveAdicionarVacinacaoCorretamente() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .build();

        Vacinacao vacinacao = new Vacinacao();
        vacinacao.setNomeVacina("Raiva");
        vacinacao.setDataAplicacao(LocalDate.now());

        animal.addVacinacao(vacinacao);

        assertEquals(1, animal.getVacinacoes().size());
        assertEquals(vacinacao, animal.getVacinacoes().get(0));
        assertEquals(animal, vacinacao.getAnimal());
    }

    @Test
    @DisplayName("Deve remover vacinacao corretamente")
    void deveRemoverVacinacaoCorretamente() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .build();

        Vacinacao vacinacao = new Vacinacao();
        vacinacao.setNomeVacina("Raiva");

        animal.addVacinacao(vacinacao);
        assertEquals(1, animal.getVacinacoes().size());

        animal.removeVacinacao(vacinacao);
        assertEquals(0, animal.getVacinacoes().size());
        assertNull(vacinacao.getAnimal());
    }

    @Test
    @DisplayName("Deve criar Animal com listas de historico e vacinacoes via builder")
    void deveCriarAnimalComListasViaBuilder() {
        List<HistoricoSaude> historicos = new ArrayList<>();
        HistoricoSaude h1 = new HistoricoSaude();
        h1.setDescricao("Consulta 1");
        historicos.add(h1);

        List<Vacinacao> vacinacoes = new ArrayList<>();
        Vacinacao v1 = new Vacinacao();
        v1.setNomeVacina("V8");
        vacinacoes.add(v1);

        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .historicoSaude(historicos)
                .vacinacoes(vacinacoes)
                .build();

        assertEquals(1, animal.getHistoricoSaude().size());
        assertEquals(1, animal.getVacinacoes().size());
    }

    @Test
    @DisplayName("Deve criar Animal usando construtor padrão")
    void deveCriarAnimalComConstrutorPadrao() {
        Animal animal = new Animal();

        assertNotNull(animal);
        assertNull(animal.getNome());
        assertEquals("Desconhecida", animal.getEspecie());
    }

    @Test
    @DisplayName("Deve criar Animal usando construtor completo")
    void deveCriarAnimalComConstrutorCompleto() {
        LocalDate data = LocalDate.of(2024, 1, 1);
        List<HistoricoSaude> historicos = new ArrayList<>();
        List<Vacinacao> vacinacoes = new ArrayList<>();

        Animal animal = new Animal(
                1L,
                "Rex",
                3,
                "Labrador",
                "RG-001",
                "Cachorro",
                "M",
                "Marrom",
                "Calmo",
                "Resgatado",
                data,
                true,
                null,
                null,
                historicos,
                vacinacoes
        );

        assertEquals(1L, animal.getId());
        assertEquals("Rex", animal.getNome());
        assertEquals(3, animal.getIdade());
        assertEquals("Labrador", animal.getRaca());
        assertEquals("RG-001", animal.getRgAnimal());
        assertEquals("Cachorro", animal.getEspecie());
        assertEquals("M", animal.getSexo());
        assertEquals("Marrom", animal.getCor());
        assertEquals("Calmo", animal.getComportamento());
        assertEquals("Resgatado", animal.getHistorico());
        assertEquals(data, animal.getDataCadastro());
        assertTrue(animal.getDisponivelParaAdocao());
        assertNull(animal.getDeletadoEm());
        assertNull(animal.getDeletadoPor());
    }

    @Test
    @DisplayName("Deve permitir alterar valores usando setters")
    void devePermitirAlterarValoresUsandoSetters() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .build();

        animal.setNome("Max");
        animal.setIdade(5);
        animal.setRaca("Poodle");
        animal.setRgAnimal("RG-999");
        animal.setEspecie("Cachorro");
        animal.setSexo("M");
        animal.setCor("Preto");
        animal.setComportamento("Brincalhão");
        animal.setHistorico("Adotado anteriormente");
        animal.setDataCadastro(LocalDate.now());
        animal.setDisponivelParaAdocao(false);

        assertEquals("Max", animal.getNome());
        assertEquals(5, animal.getIdade());
        assertEquals("Poodle", animal.getRaca());
        assertEquals("RG-999", animal.getRgAnimal());
        assertEquals("Cachorro", animal.getEspecie());
        assertEquals("M", animal.getSexo());
        assertEquals("Preto", animal.getCor());
        assertEquals("Brincalhão", animal.getComportamento());
        assertEquals("Adotado anteriormente", animal.getHistorico());
        assertNotNull(animal.getDataCadastro());
        assertFalse(animal.getDisponivelParaAdocao());
    }

    @Test
    @DisplayName("Deve aceitar idade como Integer null")
    void deveAceitarIdadeComoIntegerNull() {
        Animal animal = new AnimalBuilder()
                .nome("Filhote")
                .idade(null)
                .build();

        assertNull(animal.getIdade());
    }

    @Test
    @DisplayName("Deve aceitar Boolean null para disponivelParaAdocao")
    void deveAceitarBooleanNullParaDisponivelParaAdocao() {
        Animal animal = new AnimalBuilder()
                .nome("Indeterminado")
                .disponivelParaAdocao(null)
                .build();

        assertNull(animal.getDisponivelParaAdocao());
    }

    @Test
    @DisplayName("Deve aceitar valores extremos de idade")
    void deveAceitarValoresExtremosDeIdade() {
        Animal animalNovo = new AnimalBuilder()
                .nome("Filhote")
                .idade(0)
                .build();

        Animal animalVelho = new AnimalBuilder()
                .nome("Idoso")
                .idade(20)
                .build();

        assertEquals(0, animalNovo.getIdade());
        assertEquals(20, animalVelho.getIdade());
    }

    @Test
    @DisplayName("Deve aceitar strings vazias para campos opcionais")
    void deveAceitarStringsVazias() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .raca("")
                .cor("")
                .comportamento("")
                .historico("")
                .build();

        assertEquals("", animal.getRaca());
        assertEquals("", animal.getCor());
        assertEquals("", animal.getComportamento());
        assertEquals("", animal.getHistorico());
    }

    @Test
    @DisplayName("Deve manter listas vazias quando não há histórico ou vacinações")
    void deveManterListasVazias() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .build();

        assertNotNull(animal.getHistoricoSaude());
        assertNotNull(animal.getVacinacoes());
        assertEquals(0, animal.getHistoricoSaude().size());
        assertEquals(0, animal.getVacinacoes().size());
    }

    @Test
    @DisplayName("Deve adicionar múltiplos históricos de saúde")
    void deveAdicionarMultiplosHistoricos() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .build();

        HistoricoSaude h1 = new HistoricoSaude();
        h1.setDescricao("Consulta 1");

        HistoricoSaude h2 = new HistoricoSaude();
        h2.setDescricao("Consulta 2");

        HistoricoSaude h3 = new HistoricoSaude();
        h3.setDescricao("Consulta 3");

        animal.addHistoricoSaude(h1);
        animal.addHistoricoSaude(h2);
        animal.addHistoricoSaude(h3);

        assertEquals(3, animal.getHistoricoSaude().size());
    }

    @Test
    @DisplayName("Deve adicionar múltiplas vacinações")
    void deveAdicionarMultiplasVacinacoes() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .build();

        Vacinacao v1 = new Vacinacao();
        v1.setNomeVacina("Raiva");

        Vacinacao v2 = new Vacinacao();
        v2.setNomeVacina("V8");

        Vacinacao v3 = new Vacinacao();
        v3.setNomeVacina("V10");

        animal.addVacinacao(v1);
        animal.addVacinacao(v2);
        animal.addVacinacao(v3);

        assertEquals(3, animal.getVacinacoes().size());
    }

    @Test
    @DisplayName("Deve aceitar sexo com um caractere")
    void deveAceitarSexoComUmCaractere() {
        Animal animalMacho = new AnimalBuilder()
                .nome("Rex")
                .sexo("M")
                .build();

        Animal animalFemea = new AnimalBuilder()
                .nome("Luna")
                .sexo("F")
                .build();

        assertEquals("M", animalMacho.getSexo());
        assertEquals("F", animalFemea.getSexo());
    }

    @Test
    @DisplayName("Deve permitir rgAnimal com até 50 caracteres")
    void devePermitirRgAnimalAte50Caracteres() {
        String rgLongo = "RG-2024-" + "0".repeat(42); // 50 caracteres total

        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .rgAnimal(rgLongo)
                .build();

        assertEquals(50, animal.getRgAnimal().length());
    }

    @Test
    @DisplayName("Deve permitir especie com até 100 caracteres")
    void devePermitirEspecieAte100Caracteres() {
        String especieLonga = "Cachorro " + "X".repeat(91); // 100 caracteres (9 + 91)

        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .especie(especieLonga)
                .build();

        assertEquals(100, animal.getEspecie().length());
    }
}

