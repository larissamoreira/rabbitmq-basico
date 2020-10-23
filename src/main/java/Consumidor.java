import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class Consumidor {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection conexao = connectionFactory.newConnection();
        Channel canal = conexao.createChannel();

        String NOME_FILA = "plica"
                + "";
        canal.queueDeclare(NOME_FILA, false, false, false, null);

        canal.basicQos(1);

        DeliverCallback callback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody());
            System.out.println("Eu, Larissa de Sousa Moreira Gusmão, recebi: " + mensagem);
            try {
                doWork(mensagem);
            } finally {
                System.out.println("Done");
                canal.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        // fila, noAck, callback, callback em caso de cancelamento (por exemplo, a fila foi deletada)
        canal.basicConsume(NOME_FILA, true, callback, consumerTag -> {
            System.out.println("Cancelaram a fila: " + NOME_FILA);
        });


    }
    private static void doWork(String msg) {
        for(char ch: msg.toCharArray()) {
            if(ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}


