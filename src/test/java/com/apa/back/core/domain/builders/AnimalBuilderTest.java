package com.apa.back.core.domain.builders;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.HistoricoSaude;
import com.apa.back.core.domain.entities.Vacinacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnimalBuilderTest {

    @Test
    @DisplayName("Builder deve criar Animal com apenas campos obrigatórios")
    void builderDeveCriarAnimalComCamposObrigatorios() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .especie("Cachorro")
                .build();

        assertNotNull(animal);
        assertEquals("Rex", animal.getNome());
        assertEquals("Cachorro", animal.getEspecie());
    }

    @Test
    @DisplayName("Builder deve retornar this para permitir fluent interface")
    void builderDeveRetornarThisParaPermitirFluentInterface() {
        AnimalBuilder builder = new AnimalBuilder();

        assertSame(builder, builder.id(1L));
        assertSame(builder, builder.nome("Rex"));
        assertSame(builder, builder.idade(3));
        assertSame(builder, builder.raca("Labrador"));
        assertSame(builder, builder.rgAnimal("RG-001"));
        assertSame(builder, builder.especie("Cachorro"));
        assertSame(builder, builder.sexo("M"));
        assertSame(builder, builder.cor("Marrom"));
        assertSame(builder, builder.comportamento("Calmo"));
        assertSame(builder, builder.historico("Resgatado"));
        assertSame(builder, builder.dataCadastro(LocalDate.now()));
        assertSame(builder, builder.disponivelParaAdocao(true));
        assertSame(builder, builder.deletadoEm(LocalDate.now()));
        assertSame(builder, builder.deletadoPor(1));
        assertSame(builder, builder.historicoSaude(new ArrayList<>()));
        assertSame(builder, builder.vacinacoes(new ArrayList<>()));
    }

    @Test
    @DisplayName("Builder deve construir Animal com todos os campos preenchidos")
    void builderDeveConstruirAnimalComTodosOsCampos() {
        LocalDate dataCadastro = LocalDate.of(2024, 1, 15);
        LocalDate deletadoEm = LocalDate.of(2024, 6, 1);
        List<HistoricoSaude> historicos = new ArrayList<>();
        List<Vacinacao> vacinacoes = new ArrayList<>();

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
                .deletadoEm(deletadoEm)
                .deletadoPor(123)
                .historicoSaude(historicos)
                .vacinacoes(vacinacoes)
                .build();

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
        assertEquals(deletadoEm, animal.getDeletadoEm());
        assertEquals(123, animal.getDeletadoPor());
        assertNotNull(animal.getHistoricoSaude());
        assertNotNull(animal.getVacinacoes());
    }

    @Test
    @DisplayName("Builder deve usar valor padrão para especie se não fornecido")
    void builderDeveUsarValorPadraoParaEspecie() {
        Animal animal = new AnimalBuilder()
                .nome("Miau")
                .build();

        assertEquals("Desconhecida", animal.getEspecie());
    }

    @Test
    @DisplayName("Builder deve sobrescrever valor padrão de especie quando fornecido")
    void builderDeveSobrescreverValorPadraoDeEspecie() {
        Animal animal = new AnimalBuilder()
                .nome("Miau")
                .especie("Gato")
                .build();

        assertEquals("Gato", animal.getEspecie());
    }

    @Test
    @DisplayName("Builder deve aceitar null para campos opcionais")
    void builderDeveAceitarNullParaCamposOpcionais() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .idade(null)
                .raca(null)
                .rgAnimal(null)
                .sexo(null)
                .cor(null)
                .comportamento(null)
                .historico(null)
                .dataCadastro(null)
                .disponivelParaAdocao(null)
                .build();

        assertEquals("Rex", animal.getNome());
        assertNull(animal.getIdade());
        assertNull(animal.getRaca());
        assertNull(animal.getRgAnimal());
        assertNull(animal.getSexo());
        assertNull(animal.getCor());
        assertNull(animal.getComportamento());
        assertNull(animal.getHistorico());
        assertNull(animal.getDataCadastro());
        assertNull(animal.getDisponivelParaAdocao());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas id")
    void builderDeveAceitarApenasId() {
        Animal animal = new AnimalBuilder()
                .id(10L)
                .nome("Rex")
                .build();

        assertEquals(10L, animal.getId());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas nome")
    void builderDeveAceitarApenasNome() {
        Animal animal = new AnimalBuilder()
                .nome("Totó")
                .build();

        assertEquals("Totó", animal.getNome());
        assertNull(animal.getId());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas idade")
    void builderDeveAceitarApenasIdade() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .idade(7)
                .build();

        assertEquals(7, animal.getIdade());
    }

    @Test
    @DisplayName("Builder deve aceitar idade zero")
    void builderDeveAceitarIdadeZero() {
        Animal animal = new AnimalBuilder()
                .nome("Filhote")
                .idade(0)
                .build();

        assertEquals(0, animal.getIdade());
    }

    @Test
    @DisplayName("Builder deve aceitar idade negativa (sem validação)")
    void builderDeveAceitarIdadeNegativa() {
        Animal animal = new AnimalBuilder()
                .nome("Teste")
                .idade(-1)
                .build();

        assertEquals(-1, animal.getIdade());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas raca")
    void builderDeveAceitarApenasRaca() {
        Animal animal = new AnimalBuilder()
                .nome("Bobby")
                .raca("Poodle")
                .build();

        assertEquals("Poodle", animal.getRaca());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas rgAnimal")
    void builderDeveAceitarApenasRgAnimal() {
        Animal animal = new AnimalBuilder()
                .nome("Max")
                .rgAnimal("RG-XYZ-123")
                .build();

        assertEquals("RG-XYZ-123", animal.getRgAnimal());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas sexo")
    void builderDeveAceitarApenasSexo() {
        Animal animal = new AnimalBuilder()
                .nome("Luna")
                .sexo("F")
                .build();

        assertEquals("F", animal.getSexo());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas cor")
    void builderDeveAceitarApenasCor() {
        Animal animal = new AnimalBuilder()
                .nome("Bola")
                .cor("Branco")
                .build();

        assertEquals("Branco", animal.getCor());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas comportamento")
    void builderDeveAceitarApenasComportamento() {
        Animal animal = new AnimalBuilder()
                .nome("Buddy")
                .comportamento("Agressivo")
                .build();

        assertEquals("Agressivo", animal.getComportamento());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas historico")
    void builderDeveAceitarApenasHistorico() {
        Animal animal = new AnimalBuilder()
                .nome("Charlie")
                .historico("Resgatado de enchente")
                .build();

        assertEquals("Resgatado de enchente", animal.getHistorico());
    }

    @Test
    @DisplayName("Builder deve aceitar apenas dataCadastro")
    void builderDeveAceitarApenasDataCadastro() {
        LocalDate data = LocalDate.of(2023, 10, 5);
        Animal animal = new AnimalBuilder()
                .nome("Mel")
                .dataCadastro(data)
                .build();

        assertEquals(data, animal.getDataCadastro());
    }

    @Test
    @DisplayName("Builder deve aceitar disponivelParaAdocao como true")
    void builderDeveAceitarDisponivelParaAdocaoTrue() {
        Animal animal = new AnimalBuilder()
                .nome("Thor")
                .disponivelParaAdocao(true)
                .build();

        assertTrue(animal.getDisponivelParaAdocao());
    }

    @Test
    @DisplayName("Builder deve aceitar disponivelParaAdocao como false")
    void builderDeveAceitarDisponivelParaAdocaoFalse() {
        Animal animal = new AnimalBuilder()
                .nome("Loki")
                .disponivelParaAdocao(false)
                .build();

        assertFalse(animal.getDisponivelParaAdocao());
    }

    @Test
    @DisplayName("Builder deve aceitar deletadoEm")
    void builderDeveAceitarDeletadoEm() {
        LocalDate dataDelecao = LocalDate.of(2024, 12, 1);
        Animal animal = new AnimalBuilder()
                .nome("Removido")
                .deletadoEm(dataDelecao)
                .build();

        assertEquals(dataDelecao, animal.getDeletadoEm());
    }

    @Test
    @DisplayName("Builder deve aceitar deletadoPor")
    void builderDeveAceitarDeletadoPor() {
        Animal animal = new AnimalBuilder()
                .nome("Removido")
                .deletadoPor(999)
                .build();

        assertEquals(999, animal.getDeletadoPor());
    }

    @Test
    @DisplayName("Builder deve aceitar lista vazia de historicoSaude")
    void builderDeveAceitarListaVaziaDeHistoricoSaude() {
        List<HistoricoSaude> historicos = new ArrayList<>();
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .historicoSaude(historicos)
                .build();

        assertNotNull(animal.getHistoricoSaude());
        assertEquals(0, animal.getHistoricoSaude().size());
    }

    @Test
    @DisplayName("Builder deve aceitar lista com historicoSaude")
    void builderDeveAceitarListaComHistoricoSaude() {
        List<HistoricoSaude> historicos = new ArrayList<>();
        HistoricoSaude h1 = new HistoricoSaude();
        h1.setDescricao("Consulta 1");
        historicos.add(h1);

        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .historicoSaude(historicos)
                .build();

        assertEquals(1, animal.getHistoricoSaude().size());
        assertEquals("Consulta 1", animal.getHistoricoSaude().get(0).getDescricao());
    }

    @Test
    @DisplayName("Builder deve aceitar lista vazia de vacinacoes")
    void builderDeveAceitarListaVaziaDeVacinacoes() {
        List<Vacinacao> vacinacoes = new ArrayList<>();
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .vacinacoes(vacinacoes)
                .build();

        assertNotNull(animal.getVacinacoes());
        assertEquals(0, animal.getVacinacoes().size());
    }

    @Test
    @DisplayName("Builder deve aceitar lista com vacinacoes")
    void builderDeveAceitarListaComVacinacoes() {
        List<Vacinacao> vacinacoes = new ArrayList<>();
        Vacinacao v1 = new Vacinacao();
        v1.setNomeVacina("Raiva");
        vacinacoes.add(v1);

        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .vacinacoes(vacinacoes)
                .build();

        assertEquals(1, animal.getVacinacoes().size());
        assertEquals("Raiva", animal.getVacinacoes().get(0).getNomeVacina());
    }

    @Test
    @DisplayName("Builder deve permitir construir múltiplos animais independentes")
    void builderDevePermitirConstruirMultiplosAnimaisIndependentes() {
        AnimalBuilder builder = new AnimalBuilder();

        Animal animal1 = builder
                .nome("Rex")
                .idade(3)
                .build();

        Animal animal2 = new AnimalBuilder()
                .nome("Max")
                .idade(5)
                .build();

        assertEquals("Rex", animal1.getNome());
        assertEquals(3, animal1.getIdade());
        assertEquals("Max", animal2.getNome());
        assertEquals(5, animal2.getIdade());
    }

    @Test
    @DisplayName("Builder deve aceitar strings vazias")
    void builderDeveAceitarStringsVazias() {
        Animal animal = new AnimalBuilder()
                .nome("")
                .raca("")
                .rgAnimal("")
                .especie("")
                .sexo("")
                .cor("")
                .comportamento("")
                .historico("")
                .build();

        assertEquals("", animal.getNome());
        assertEquals("", animal.getRaca());
        assertEquals("", animal.getRgAnimal());
        assertEquals("", animal.getEspecie());
        assertEquals("", animal.getSexo());
        assertEquals("", animal.getCor());
        assertEquals("", animal.getComportamento());
        assertEquals("", animal.getHistorico());
    }

    @Test
    @DisplayName("Builder deve aceitar strings com espaços")
    void builderDeveAceitarStringsComEspacos() {
        Animal animal = new AnimalBuilder()
                .nome("Rex Silva")
                .raca("Golden Retriever")
                .cor("Dourado Claro")
                .build();

        assertEquals("Rex Silva", animal.getNome());
        assertEquals("Golden Retriever", animal.getRaca());
        assertEquals("Dourado Claro", animal.getCor());
    }

    @Test
    @DisplayName("Builder deve aceitar caracteres especiais em strings")
    void builderDeveAceitarCaracteresEspeciaisEmStrings() {
        Animal animal = new AnimalBuilder()
                .nome("Rex-2024")
                .rgAnimal("RG/2024/001")
                .comportamento("Calmo & Amigável")
                .historico("Encontrado em 01/01/2024 às 10h")
                .build();

        assertEquals("Rex-2024", animal.getNome());
        assertEquals("RG/2024/001", animal.getRgAnimal());
        assertEquals("Calmo & Amigável", animal.getComportamento());
        assertTrue(animal.getHistorico().contains("01/01/2024"));
    }

    @Test
    @DisplayName("Builder deve aceitar datas passadas")
    void builderDeveAceitarDatasPassadas() {
        LocalDate dataPassada = LocalDate.of(2020, 1, 1);
        Animal animal = new AnimalBuilder()
                .nome("Velho")
                .dataCadastro(dataPassada)
                .build();

        assertEquals(dataPassada, animal.getDataCadastro());
        assertTrue(animal.getDataCadastro().isBefore(LocalDate.now()));
    }

    @Test
    @DisplayName("Builder deve aceitar datas futuras")
    void builderDeveAceitarDatasFuturas() {
        LocalDate dataFutura = LocalDate.of(2030, 12, 31);
        Animal animal = new AnimalBuilder()
                .nome("Futuro")
                .dataCadastro(dataFutura)
                .build();

        assertEquals(dataFutura, animal.getDataCadastro());
        assertTrue(animal.getDataCadastro().isAfter(LocalDate.now()));
    }

    @Test
    @DisplayName("Builder deve aceitar data de hoje")
    void builderDeveAceitarDataDeHoje() {
        LocalDate hoje = LocalDate.now();
        Animal animal = new AnimalBuilder()
                .nome("Hoje")
                .dataCadastro(hoje)
                .build();

        assertEquals(hoje, animal.getDataCadastro());
    }

    @Test
    @DisplayName("Builder deve permitir sobrescrever valores")
    void builderDevePermitirSobrescreverValores() {
        Animal animal = new AnimalBuilder()
                .nome("Rex")
                .nome("Max")  // Sobrescreve
                .idade(3)
                .idade(5)     // Sobrescreve
                .build();

        assertEquals("Max", animal.getNome());
        assertEquals(5, animal.getIdade());
    }

    @Test
    @DisplayName("Builder deve construir animal minimalista")
    void builderDeveConstruirAnimalMinimalista() {
        Animal animal = new AnimalBuilder()
                .nome("Min")
                .build();

        assertNotNull(animal);
        assertEquals("Min", animal.getNome());
        assertEquals("Desconhecida", animal.getEspecie());
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
    }
}

