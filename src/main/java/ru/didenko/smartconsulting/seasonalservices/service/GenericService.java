package ru.didenko.smartconsulting.seasonalservices.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.didenko.smartconsulting.seasonalservices.model.GenericModel;
import ru.didenko.smartconsulting.seasonalservices.repository.GenericRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class GenericService<T extends GenericModel> {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailQueue mailQueue;

    private final GenericRepository<T> repository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GenericService(GenericRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> getList() {
        return repository.findAll();
    }

    public T getOneById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public T create(T object) {
        object.setCreatedWhen(LocalDateTime.now());
        setDeletedAndUpdatedNull(object);
        return repository.save(object);
    }

    public T update(T object) {
        object.setUpdatedWhen(LocalDateTime.now());
        return repository.save(object);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    protected void setDeletedAndUpdatedNull(T object) {
        object.setDeleted(false);
        object.setDeletedWhen(null);
        object.setDeletedBy(null);
        object.setUpdatedBy(null);
        object.setUpdatedWhen(null);
    }

    public void setCreatedAndDeleted(Long sourceId, T destination) {
        T source = getOneById(sourceId);
        destination.setCreatedBy(source.getCreatedBy());
        destination.setCreatedWhen(source.getCreatedWhen());
        destination.setDeletedBy(source.getDeletedBy());
        destination.setDeletedWhen(source.getDeletedWhen());
        destination.setDeleted(source.isDeleted());
    }

    public void sendEmailMessage(String email, String message) {
        SimpleMailMessage provider = new SimpleMailMessage();
        provider.setTo(email);
        provider.setSubject("Сообщение от Сервиса выдачи государственных сезонных услуг");
        provider.setText(message);
        mailQueue.setSender(javaMailSender);
        mailQueue.addMail(provider);
    }
}
