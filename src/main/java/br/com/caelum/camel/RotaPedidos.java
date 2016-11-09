package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			
			/* Rota Simples
			@Override
			public void configure() throws Exception {
				  from("file:pedidos?delay=5s&noop=true").
				  	marshal().xmljson().
		    	  log("${id} - ${body}").
		    	to("file:saida");
				
			}*/
			// Rota de com filter 
			@Override
			public void configure() throws Exception {
				  from("file:pedidos?delay=5s&noop=true").
				  	split().
				  		xpath("/pedido/itens/item").
				  	filter().
				  		xpath("/item/formato[text()='EBOOK']").
				  	log("${id} - ${body}").
				  	marshal().xmljson().
				  	setHeader(Exchange.FILE_NAME, simple("${file:name.noext}-${header.CamelSplitIndex}.json")).
		    	to("file:saida");
				
			}
		});
		context.start(); //aqui camel realmente começa a trabalhar
        Thread.sleep(10000); //esperando um pouco para dar um tempo para camel
	}
}
