package IPC;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsServer implements Runnable, ExceptionListener {
	public void run() {
		try {

			// Create a ConnectionFactory
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

			// Create a Connection
			Connection connection = connectionFactory.createConnection();
			connection.start();

			connection.setExceptionListener(this);

			// Create a Session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// Create the destination (Topic or Queue)
			Destination destination = session.createQueue("TEST.FOO");

			// Create a MessageConsumer from the Session to the Topic or Queue
			MessageConsumer consumer = session.createConsumer(destination);

			// Wait for a message
			Message message = consumer.receive(1000);

			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				System.out.println("Received: " + text);
			} else {
				System.out.println("Received: " + message);
			}

			consumer.close();
			session.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
		}
	}

	public synchronized void onException(JMSException ex) {
		System.out.println("JMS Exception occured.  Shutting down client.");
	}

	public static void main(String[] args) throws Exception {
		thread(new JmsClient(), false);
		thread(new JmsClient(), false);
		thread(new JmsServer(), false);
		Thread.sleep(1000);
		thread(new JmsServer(), false);
		thread(new JmsClient(), false);
		thread(new JmsServer(), false);
		thread(new JmsClient(), false);
		Thread.sleep(1000);
		thread(new JmsServer(), false);
		thread(new JmsClient(), false);
		thread(new JmsServer(), false);
		thread(new JmsServer(), false);
		thread(new JmsClient(), false);
		thread(new JmsClient(), false);
		Thread.sleep(1000);
		thread(new JmsClient(), false);
		thread(new JmsServer(), false);
		thread(new JmsServer(), false);
		thread(new JmsClient(), false);
		thread(new JmsServer(), false);
		thread(new JmsClient(), false);
		thread(new JmsServer(), false);
		thread(new JmsClient(), false);
		thread(new JmsServer(), false);
		thread(new JmsServer(), false);
		thread(new JmsClient(), false);
	}

	public static void thread(Runnable runnable, boolean daemon) {
		Thread brokerThread = new Thread(runnable);
		brokerThread.setDaemon(daemon);
		brokerThread.start();
	}
}