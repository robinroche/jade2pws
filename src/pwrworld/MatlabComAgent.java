package pwrworld;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.StringACLCodec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * This class acts as a server that handles communication 
 * with Matlab and PowerWorld. 
 * @author rroche
 */
public class MatlabComAgent extends Agent
{

	private static final long serialVersionUID = -4394243932169660776L;

	static final String OPEN_CASE = "open-case";
	static final String EDIT_MODE = "edit-mode";
	static final String RUN_MODE = "run-mode";
	static final String LIST_DEVICES = "list-devices";
	static final String GET_PARAMETERS_SINGLE = "get-parameters-single";
	static final String GET_PARAMETERS_MULTIPLE = "get-parameters-multiple";
	static final String CHANGE_PARAMETERS_SINGLE = "change-parameters-single";
	static final String CHANGE_PARAMETERS_MULTIPLE = "change-parameters-multiple";
	static final String RUN_POWER_FLOW = "run-power-flow";
	static final String SAVE_FILE = "save-file";
	static final String CLOSE_CASE = "close-case";
	static final String END_CONNECTION = "end-connection";


	// TCP connection variables
	ServerSocket srvr = null;
	Socket skt = null;
	BufferedReader in;
	PrintWriter out;
	String ip = "localhost";
	String filePath;
	int port = 1234;


	// Setup method
	protected void setup() 
	{
		System.out.println(getName() + " successfully started");

		// Get arguments
		Object[] args = getArguments();
		filePath = (String) args[0];

		// Create the TCP connection
		try 
		{
			// Create server and socket
			srvr = new ServerSocket(port);
			skt = srvr.accept();
			System.out.println(getLocalName() + ": Server connection initiated");

			// Create writer and reader to send and receive data
			out = new PrintWriter(skt.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		// Send a message to the tester to say its can start sending requests
		sendMessage("Tester","","start-now",ACLMessage.INFORM);

		// Run behavior
		CommWithMatlab commWithMatlab = new CommWithMatlab();
		addBehaviour(commWithMatlab);

	} // End setup


	/**
	 * A behaviour acting as an interface between JADE and Matlab
	 * Matlab itself has scripts acting as an interface between Matlab and PowerWorld
	 * @author Robin
	 */
	class CommWithMatlab extends SimpleBehaviour
	{

		private static final long serialVersionUID = 8966535884137111965L;


		@Override
		public void action() 
		{	

			// Wait for a message from another agent requesting something
			ACLMessage msg = blockingReceive();

			// If this is to open a case
			if(msg.getConversationId().equals(OPEN_CASE))
			{
				openCase(msg.getContent());
			}

			// If this is to switch to edit mode
			if(msg.getConversationId().equals(EDIT_MODE))
			{
				// Prepare the message to send
				String msgContent = EDIT_MODE;

				// Send the message and retrieve the answer
				//System.out.println(getLocalName() + ": Message sent to Matlab: " + msgContent);
				String pwAnswer = callMatlab(msgContent);
				out.flush();

				// Display result if any
				if(pwAnswer.equals(""))
				{
					//System.out.println(getLocalName() + ": Switched to edit mode successfully");
				}
				else
					System.err.println(getLocalName() + ": Switching to edit mode failed: '" + pwAnswer + "'");
			}

			// If this is to switch to edit mode
			if(msg.getConversationId().equals(RUN_MODE))
			{
				// Prepare the message to send
				String msgContent = RUN_MODE;

				// Send the message and retrieve the answer
				//System.out.println(getLocalName() + ": Message sent to Matlab: " + msgContent);
				String pwAnswer = callMatlab(msgContent);
				out.flush();

				// Display result if any
				if(pwAnswer.equals(""))
				{
					//System.out.println(getLocalName() + ": Switched to run mode successfully");
				}
				else
					System.err.println(getLocalName() + ": Switching to run mode failed: '" + pwAnswer + "'");
			}

			// If this is to list the devices of a given type
			if(msg.getConversationId().equals(LIST_DEVICES))
			{
				// Prepare message to send
				String type = msg.getContent();
				String pwRequest = LIST_DEVICES + "," + type;

				// Send the message and retrieve the answer
				//System.out.println(getLocalName() + ": Message sent to Matlab: " + pwRequest);
				String pwAnswer = callMatlab(pwRequest);
				out.flush();

				// Display error if any
				String[] cutAnswer = pwAnswer.split(",");
				if(cutAnswer.length>1)
				{
					//System.out.println(getLocalName() + ": Message received from Matlab: " + pwAnswer);
				}
				else
					System.err.println(getLocalName() + ": ListDevices failed: '" + cutAnswer[0] + "'");

				// Send the answer to the agent that request it
				sendMessage(msg.getSender().getLocalName(),pwAnswer,LIST_DEVICES,ACLMessage.INFORM);
			}

			// If this is to get the parameters of a single element
			if(msg.getConversationId().equals(GET_PARAMETERS_SINGLE))
			{
				// Prepare message to send
				String pwRequest = GET_PARAMETERS_SINGLE + "," + msg.getContent();

				// Send the message and retrieve the answer
				//System.out.println(getLocalName() + ": Message sent to Matlab: " + pwRequest);
				String pwAnswer = callMatlab(pwRequest);
				out.flush();

				// Display error if any
				String[] cutAnswer = pwAnswer.split(",");
				if(cutAnswer.length>1)
				{
					//System.out.println(getLocalName() + ": Message received from Matlab: " + pwAnswer);
				}
				else
					System.err.println(getLocalName() + ": GetParametersSingle failed: '" + cutAnswer[0] + "'");

				// Send the answer to the agent that request it
				sendMessage(msg.getSender().getLocalName(),pwAnswer,GET_PARAMETERS_SINGLE,ACLMessage.INFORM);
			}

			// If this is to get the parameters of multiple elements
			if(msg.getConversationId().equals(GET_PARAMETERS_MULTIPLE))
			{
				// Prepare message to send
				String pwRequest = GET_PARAMETERS_MULTIPLE + "," + msg.getContent();

				// Send the message and retrieve the answer
				//System.out.println(getLocalName() + ": Message sent to Matlab: " + pwRequest);
				String pwAnswer = callMatlab(pwRequest);
				out.flush();

				// Display error if any
				String[] cutAnswer = pwAnswer.split(",");
				if(cutAnswer.length>1)
				{
					//System.out.println(getLocalName() + ": Message received from Matlab: " + pwAnswer);
				}
				else
					System.err.println(getLocalName() + ": GetParametersMultiple failed: '" + cutAnswer[0] + "'");

				// Send the answer to the agent that request it
				sendMessage(msg.getSender().getLocalName(),pwAnswer,GET_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			}

			// If this is to change the parameters of a single element
			if(msg.getConversationId().equals(CHANGE_PARAMETERS_SINGLE))
			{
				// Prepare message to send
				String pwRequest = CHANGE_PARAMETERS_SINGLE + "," + msg.getContent();

				// Send the message and retrieve the answer
				//System.out.println(getLocalName() + ": Message sent to Matlab: " + pwRequest);
				String pwAnswer = callMatlab(pwRequest);
				out.flush();

				if(!pwAnswer.equals(""))
					System.err.println(getLocalName() + ": ChangeParametersSingle failed: '" + pwAnswer + "'");
			}

			// If this is to change the parameters of multiple elements
			if(msg.getConversationId().equals(CHANGE_PARAMETERS_MULTIPLE))
			{
				// Prepare message to send
				String pwRequest = CHANGE_PARAMETERS_MULTIPLE + "," + msg.getContent();

				// Send the message and retrieve the answer
				//System.out.println(getLocalName() + ": Message sent to Matlab: " + pwRequest);
				String pwAnswer = callMatlab(pwRequest);
				out.flush();

				if(!pwAnswer.equals(""))
					System.err.println(getLocalName() + ": ChangeParametersMultiple failed: '" + pwAnswer + "'");
			}

			// If this is to run a power flow analysis
			if(msg.getConversationId().equals(RUN_POWER_FLOW))
			{
				// Prepare message to send
				String pwRequest = RUN_POWER_FLOW;

				// Send the message and retrieve the answer
				//System.out.println(getLocalName() + ": Message sent to Matlab: " + pwRequest);
				String pwAnswer = callMatlab(pwRequest);
				out.flush();

				if(!pwAnswer.equals(""))
					System.err.println(getLocalName() + ": Power flow run failed: '" + pwAnswer + "'");
			}

			// If this is to save the file
			if(msg.getConversationId().equals(SAVE_FILE))
			{
				// Prepare message to send
				String filePath = msg.getContent(); // "c:/users/rroche/Powerworld/ieee14_bis.PWB"
				String msgContent = SAVE_FILE + "," + filePath;

				// Send the message and retrieve the answer
				//System.out.println("Message sent to Matlab: " + msgContent);
				String pwAnswer = callMatlab(msgContent);
				out.flush();

				if(!pwAnswer.equals(""))
					System.err.println(getLocalName() + ": File saving failed: '" + pwAnswer + "'");
			}

			// If this is to close the opened case
			if(msg.getConversationId().equals(CLOSE_CASE))
			{
				// Prepare message to send
				String msgContent = CLOSE_CASE;

				// Send the message and retrieve the answer
				//System.out.println("Message sent to Matlab: " + msgContent);
				String pwAnswer = callMatlab(msgContent);
				out.flush();

				if(!pwAnswer.equals(""))
					System.err.println(getLocalName() + ": Case closing failed: '" + pwAnswer + "'");
			}

			// If this is to end the connection with Matlab
			if(msg.getConversationId().equals(END_CONNECTION))
			{
				myAgent.doDelete();
			}

		} // End action


		@Override
		public boolean done() 
		{
			return false;	
		}

	} // End behavior


	@Override
	protected void takeDown()
	{
		System.out.println("Agent being taken down");

		// Request Matlab to close the connection between Matlab and PowerWorld
		String msgContent = END_CONNECTION;
		String pwAnswer = callMatlab(msgContent);

		if(!pwAnswer.equals(""))
			System.out.println(getLocalName() + ": connection ending failed: '" + pwAnswer + "'");

		// Close TCP writer and socket
		try 
		{
			out.close();
			in.close();
			skt.close();
			srvr.close();
		} 
		catch (IOException e) {	e.printStackTrace(); }
	}


	/**
	 * Sends a message to Matlab and returns an answer
	 * @param msgContent
	 * @return
	 */
	private String callMatlab(String msgContent) 
	{

		ACLMessage msg;
		String matlabAnswer = "";

		// Send the message to Matlab via JADE
		msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID(ip + ":1234", AID.ISGUID));
		msg.setContent(msgContent);

		// Encode message to send as an ACL Message
		StringACLCodec codec = new StringACLCodec(in, out);
		codec.write(msg);
		out.flush();

		// Wait for its answer
		try 
		{
			while (!in.ready()) {}
			matlabAnswer = matlabAnswer + in.readLine().toString();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return matlabAnswer;

	} // End callMatlab


	/**
	 * Sends a message to another agent
	 * @param targetName
	 * @param content
	 * @param conversation
	 * @param type
	 */
	public void sendMessage(String targetName, String content, String conversation, int type)
	{
		ACLMessage message = new ACLMessage(type);
		message.addReceiver(new AID (targetName, AID.ISLOCALNAME));
		message.setContent(content);
		message.setConversationId(conversation);
		this.send(message);
	}


	/**
	 * Opens a case in PowerWorld
	 * @param filePath
	 */
	private void openCase(String filePath) 
	{
		// Prepare the message to send
		String msgContent = OPEN_CASE + "," + filePath;

		// Send the message and retrieve the answer
		//System.out.println(getLocalName() + ": Message sent to Matlab: " + msgContent);
		String pwAnswer = callMatlab(msgContent);
		out.flush();

		// Display result if any
		if(pwAnswer.equals(""))
		{
			//System.out.println(getLocalName() + ": File opened successfully");
		}
		else
			System.err.println(getLocalName() + ": File opening failed: '" + pwAnswer + "'");
	}

}
