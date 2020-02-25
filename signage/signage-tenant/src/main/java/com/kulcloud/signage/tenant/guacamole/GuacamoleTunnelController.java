package com.kulcloud.signage.tenant.guacamole;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.net.SimpleGuacamoleTunnel;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.apache.guacamole.servlet.GuacamoleHTTPTunnelServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuacamoleTunnelController extends GuacamoleHTTPTunnelServlet {

	private static final long serialVersionUID = 1L;
	@Value("${guacamole.guacd.hostname:localhost}")
	private String guacdHostname;
	@Value("${guacamole.guacd.port:4822}")
	private int guacdPort;

	@Override
	protected GuacamoleTunnel doConnect(HttpServletRequest request) throws GuacamoleException {
		// Create our configuration
        GuacamoleConfiguration config = new GuacamoleConfiguration();
        config.setProtocol("rdp");
        config.setParameter("hostname", "10.1.100.180");
//        config.setParameter("port", "3389");
        config.setParameter("username", "gui");
        config.setParameter("password", "kulcloud");
        config.setParameter("ignore-cert", "true");
        
        // Connect to guacd - everything is hard-coded here.
        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket(guacdHostname, guacdPort),
                config
        );

        // Return a new tunnel which uses the connected socket
        return new SimpleGuacamoleTunnel(socket);
	}

	@Override
	@RequestMapping(path = "tunnel", method = { RequestMethod.POST, RequestMethod.GET })
	protected void handleTunnelRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		super.handleTunnelRequest(request, response);
	}

}
