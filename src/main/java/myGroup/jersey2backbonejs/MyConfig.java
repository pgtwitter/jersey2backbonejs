package myGroup.jersey2backbonejs;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("webapi")
public class MyConfig extends ResourceConfig {
	public MyConfig() {
		packages(this.getClass().getPackage().getName());
	}
}