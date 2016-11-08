package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
		    	from("file:pedidos").
		    	  log("${id} - ${body}").
		    	to("file:saida");
				
			}
		});
		context.start(); //aqui camel realmente começa a trabalhar
        Thread.sleep(10000); //esperando um pouco para dar um tempo para camel
	}
}
