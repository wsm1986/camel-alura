package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
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
				
			}
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
				
			}*/
			@Override
			public void configure() throws Exception {
				from("file:pedidos?delay=5s&noop=true").
				  	// Guardando Property para passar no header para o metodo GET do book
					setProperty("pedidoId", xpath("/pedido/id/text()")).
				  	setProperty("clienteId", xpath("/pedido/pagamento/email-titular/text()")).
				
					split().
						xpath("/pedido/itens/item").
					filter()
						.xpath("/item/formato[text()='EBOOK']").
						marshal().
						xmljson().
						log("${id} \n ${body}").
						//setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.GET)) 
						//setHeader(Exchange.HTTP_QUERY, constant("clienteId=breno@abc.com&pedidoId=123&ebookId=ARQ"))
						setHeader(Exchange.HTTP_METHOD, HttpMethods.GET).
						setHeader(Exchange.HTTP_QUERY, 
					            simple("clienteId=${property.clienteId}&pedidoId=${property.pedidoId}&ebookId=${property.ebookId}"))
				.to("http4://localhost:8080/webservices/ebook/item");
			}
		});
		context.start(); //aqui camel realmente começa a trabalhar
        Thread.sleep(10000); //esperando um pouco para dar um tempo para camel
	}
}
