package ru.didenko.smartconsulting.seasonalservices.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.UserDto;
import ru.didenko.smartconsulting.seasonalservices.model.GenericModel;
import ru.didenko.smartconsulting.seasonalservices.model.User;
import ru.didenko.smartconsulting.seasonalservices.repository.ApplicationRepository;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper extends GenericMapper<User, UserDto> {

    private final ApplicationRepository applicationRepository;

    protected UserMapper(ModelMapper mapper, ApplicationRepository applicationRepository) {
        super(mapper, User.class, UserDto.class);
        this.applicationRepository = applicationRepository;
    }

    @PostConstruct
    public void setupMapper() {
        super.mapper.createTypeMap(User.class, UserDto.class)
                .addMappings(m -> m.skip(UserDto::setApplicationsId)).setPostConverter(toDtoConverter());
        super.mapper.createTypeMap(UserDto.class, User.class)
                .addMappings(m -> m.skip(User::setApplications)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(UserDto source, User destination) {
        if (!Objects.isNull(source.getApplicationsId())) {
            destination.setApplications(applicationRepository.findAllByIdIn(source.getApplicationsId()));
        } else {
            destination.setApplications(null);
        }
    }

    @Override
    void mapSpecificFields(User source, UserDto destination) {
        if (!Objects.isNull(source.getApplications())) {
            destination.setApplicationsId(getIds(source));
        } else {
            destination.setApplicationsId(null);
        }
    }

    private Set<Long> getIds(User user) {
        return Objects.isNull(user) || Objects.isNull(user.getId())
                ? null
                : user.getApplications().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }
}
