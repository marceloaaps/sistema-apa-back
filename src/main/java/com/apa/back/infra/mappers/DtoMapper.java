package com.apa.back.infra.mappers;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.core.domain.entities.EventAnimal;
import com.apa.back.core.domain.entities.EventWorker;
import com.apa.back.core.domain.entities.User;
import com.apa.back.presentation.dtos.animal.AnimalDto;
import com.apa.back.presentation.dtos.user.UsuarioWithIdDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoMapper {


    public List<AnimalDto> mapToAnimalDtos(List<EventAnimal> animaisEvento) {
        return animaisEvento.stream().map(ea -> {
            Animal a = ea.getAnimal();
            return AnimalDto.builder()
                    .id(a.getId())
                    .nome(a.getNome())
                    .idade(a.getIdade())
                    .raca(a.getRaca())
                    .idSaude(a.getIdSaude())
                    .comportamento(a.getComportamento())
                    .historico(a.getHistorico())
                    .dataCadastro(a.getDataCadastro())
                    .disponivelParaAdocao(a.getDisponivelParaAdocao())
                    .build();
        }).toList();
    }

    public List<UsuarioWithIdDto> mapToUsuarioDtos(List<EventWorker> voluntariosEvento) {
        return voluntariosEvento.stream().map(ew -> {
            User u = ew.getIdWorker();
            return new UsuarioWithIdDto(
                    u.getId(),
                    u.getNome(),
                    u.getEmail(),
                    u.getDataNascimento()
            );
        }).toList();
    }


}
