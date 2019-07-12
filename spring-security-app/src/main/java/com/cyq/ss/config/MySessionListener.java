package com.cyq.ss.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Service
@WebListener
public class MySessionListener implements HttpSessionListener {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public synchronized void sessionCreated(HttpSessionEvent se) {
        log.debug("增加session:" + se.getSession().getId());
    }
    @Override
    public synchronized void sessionDestroyed(HttpSessionEvent se) {
        log.debug("销毁session:" + se.getSession().getId());
    }

}
