package ru.didenko.smartconsulting.seasonalservices.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**Class-service with mail queue logic*/
@Log4j2
@Service
public class MailQueue implements Runnable {
    private JavaMailSender sender;
    private boolean isRunning = false;

    private final ConcurrentLinkedQueue<SimpleMailMessage> mailsToSend = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<SimpleMailMessage> errorRun = new ConcurrentLinkedQueue<>();
    private final Map<SimpleMailMessage, MailException> mailsWithErrors = new ConcurrentHashMap<>();

    private static final int WAIT_FAILURE_TIME = 60000;
    private static final int MAX_THREADS_SEND_MAIL = 4;
    private static final int MAX_ELEMENTS_BEFORE_NEW_THREAD = 25;
    private static final AtomicInteger CURRENT_THREADS_SEND_MAIL = new AtomicInteger(0);

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    /**
     * Method starts the main algorithm of checking queue and sending mails from it in concurrent mode
     * After sending all mails from main queue tries to send mails from error queue
     */
    @Override
    public void run() {
        isRunning = true;
        CURRENT_THREADS_SEND_MAIL.getAndIncrement();
        while (isRunning) {
            while (mailsToSend.peek() != null) {
                int currentThreads = CURRENT_THREADS_SEND_MAIL.get();
                if (currentThreads < MAX_THREADS_SEND_MAIL && mailsToSend.size()
                        > (MAX_ELEMENTS_BEFORE_NEW_THREAD * currentThreads)) {
                    new Thread(this).start();
                }
                SimpleMailMessage message = mailsToSend.remove();
                sendMessage(message);
            }
        }
        if (CURRENT_THREADS_SEND_MAIL.decrementAndGet() < 1) {
            getErrorThread().start();
        }
        isRunning = false;
    }

    /**
     * Adding a mail to the Queue.
     * When Queue is not started, it will start.
     *
     * @param message to send.
     * @return true is mail is successfully added to the Queue
     */
    public boolean addMail(SimpleMailMessage message) {
        boolean result = mailsToSend.add(message);
        if (!isRunning) {
            new Thread(this).start();
        }
        return result;
    }

    /**
     * Manual remove a specific mail from the error list.
     *
     * @param message to remove
     */
    public void removeMailFromError(SimpleMailMessage message) {
        MailException mailException = mailsWithErrors.remove(message);
        log.info("The mail to " + Objects.requireNonNull(message.getTo())[0] +
                "\nwith title : " + message.getSubject() + " was removed from error queue." +
                "\nError was : " + mailException.getMessage());
    }

    /**
     * Manual start to try sending the mails with errors again.
     */
    public void startErrorThread() {
        getErrorThread().start();
    }

    /**
     * Manual try to send specific mail from error list.
     *
     * @param message to send
     * @return True if mail was send.
     */
    public boolean trySingleErrorMail(SimpleMailMessage message) {
        if (sendMessage(message)) {
            log.info("Mail with error was successfully sent.");
            return true;
        }
        return false;
    }

    private Thread getErrorThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                wait(WAIT_FAILURE_TIME);
                tryErrorsAgain();
            }

            private void wait(int time) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                    log.error("sleep interrupted.", ex);
                }
            }
        });
    }

    private void tryErrorsAgain() {
        errorRun.addAll(mailsWithErrors.keySet());
        while (errorRun.peek() != null) {
            SimpleMailMessage message = errorRun.remove();
            if (sendMessage(message)) {
                MailException exception = mailsWithErrors.remove(message);
                if (exception != null) {
                    log.trace("Mail with error was successfully sent.", exception);
                }
            }
        }
    }

    private boolean sendMessage(SimpleMailMessage message) {
        MailException exception;
        try {
            sender.send(message);
            return true;
        } catch (MailException e) {
            log.error("sending mail failed " + Objects.requireNonNull(message.getTo())[0] + e.getMessage());
            exception = mailsWithErrors.put(message, e);
            if (exception != null) {
                log.trace("Added duplicated mail in errors", e);
            }
        }
        return false;
    }

    /**
     * Shows mails with sending errors
     */
    public Map<SimpleMailMessage, MailException> getMailsWithErrors() {
        return mailsWithErrors;
    }

    /**
     * Shows if messages are being sent
     */
    public boolean isRunning() {
        return isRunning;
    }
}
