package com.github.avenderov.testing.junit;

import com.google.common.base.Throwables;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;

/**
 * @author avenderov
 */
public class JettyServer extends ExternalResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JettyServer.class);

    private final int port;

    private final Server server;

    public JettyServer(final int port, final String context, final Class<? extends Servlet> servlet) {
        this.port = port;

        final Server server = new Server(port);

        final ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(servlet, context);

        server.setHandler(servletHandler);

        this.server = server;
    }

    @Override
    protected void before() throws Throwable {
        LOGGER.debug("Starting server...");
        server.start();
    }

    @Override
    protected void after() {
        try {
            server.stop();
        } catch (Exception e) {
            LOGGER.error("Failed to stop server", e);
            throw Throwables.propagate(e);
        }
    }

}
