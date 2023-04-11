package ru.didenko.smartconsulting.seasonalservices.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.UserWithApplicationsDto;
import ru.didenko.smartconsulting.seasonalservices.model.GenericModel;
import ru.didenko.smartconsulting.seasonalservices.model.User;
import ru.didenko.smartconsulting.seasonalservices.repository.ApplicationRepository;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserWithApplicationsMapper extends GenericMapper<User, UserWithApplicationsDto> {

    private final ApplicationRepository applicationRepository;
    private final ApplicationResponseMapper applicationResponseMapper;

    protected UserWithApplicationsMapper(ModelMapper mapper, ApplicationRepository applicationRepository, ApplicationResponseMapper applicationResponseMapper) {
        super(mapper, User.class, UserWithApplicationsDto.class);
        this.applicationRepository = applicationRepository;
        this.applicationResponseMapper = applicationResponseMapper;
    }

    @PostConstruct
    public void setupMapper() {
        super.mapper.createTypeMap(User.class, UserWithApplicationsDto.class)
                .addMappings(m -> m.skip(UserWithApplicationsDto::setApplicationsId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(UserWithApplicationsDto::setApplications)).setPostConverter(toDtoConverter());
    }

    @Override
    void mapSpecificFields(User source, UserWithApplicationsDto destination) {
        destination.setApplicationsId(getIds(source));
        destination.setApplications(
                applicationResponseMapper.toDtos(
                        applicationRepository
                                .findAllByIdIn(destination.getApplicationsId())
                                .stream().toList())
        );
    }

    private Set<Long> getIds(User user) {
        return Objects.isNull(user) || Objects.isNull(user.getId())
                ? null
                : user.getApplications().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
