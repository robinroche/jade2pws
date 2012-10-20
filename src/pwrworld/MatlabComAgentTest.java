package pwrworld;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


/**
 * This agent sends requests to PowerWorld through the other agent
 * that adapts and forwards its requests to Matlab.
 * @author rroche
 */
public class MatlabComAgentTest extends Agent
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

	static String PWRWORLD_NAME = "matlabComAgent"; 
	String matlabAgent = PWRWORLD_NAME;
	
	static String powerWorldFilePath;
	static String powerWorldFilePath2;
	
	
	
	// Setup method
	protected void setup() 
	{
		System.out.println(getName() + " successfully started");

		// Get files path
		// Change this path to the one of the file you want to open: TODO
		try 
		{
			String path = new java.io.File(".").getCanonicalPath();
			powerWorldFilePath = path + "/powerworld/ieee14.pwb";
			powerWorldFilePath2 = path + "/powerworld/ieee14_saved.pwb";
		} 
		catch (IOException e) {e.printStackTrace();}
		
		// Wait for a message from the server agent to start sending request
		MessageTemplate mt = MessageTemplate.MatchConversationId("start-now");
		blockingReceive(mt).getContent();
		System.out.println(getLocalName() + ": Starting communication...");
		
		// Run behavior
		CommWithMatlab commWithMatlab = new CommWithMatlab();
		addBehaviour(commWithMatlab);

	} // End setup


	/**
	 * Handles communication between the other agents and Matlab
	 * @author rroche
	 */
	class CommWithMatlab extends SimpleBehaviour
	{

		private static final long serialVersionUID = 8966535884137111965L;


		@Override
		public void action() 
		{	

			// Local variables
			String answer = "";
			int deviceId = 0;
			int deviceId2 = 0;
			int deviceId3 = 3;
			int nbFields = 0;
			int nbElements = 0;
			String fields = "";
			String values = "";
			String type = "";
			String request = "";
			
			
			/* INITIALIZE */
			
			System.out.println("*******************************");
			System.out.println("TESTING IF CASES CAN BE OPENED AND SWITCH TO EDIT MODE");
			System.out.println("*******************************");
			
			// Open case
			sendMessage(matlabAgent,powerWorldFilePath,OPEN_CASE,ACLMessage.INFORM);
			
			// Switch to edit mode
			sendMessage(matlabAgent,"",EDIT_MODE,ACLMessage.INFORM);
			
			
			/* LIST DEVICES */
			
			System.out.println("*******************************");
			System.out.println("TESTING IF DEVICE LISTING WORKS");
			System.out.println("*******************************");
			
			// List buses
			sendMessage(matlabAgent,"Bus",LIST_DEVICES,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": List buses answer = " + answer);
			Object[][] busesListData = parseAnswerString(answer);
			printDataArray(busesListData);
			
			// List branches
			sendMessage(matlabAgent,"Branch",LIST_DEVICES,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": List branches answer = " + answer);
			Object[][] branchesListData = parseAnswerString(answer);
			printDataArray(branchesListData);
			
			// List generators
			sendMessage(matlabAgent,"Gen",LIST_DEVICES,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": List generators answer = " + answer);
			Object[][] gensListData = parseAnswerString(answer);
			printDataArray(gensListData);
			
			// List loads
			sendMessage(matlabAgent,"Load",LIST_DEVICES,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": List loads answer = " + answer);
			Object[][] loadsListData = parseAnswerString(answer);
			printDataArray(loadsListData);
			
			
			/* GET PARAMETERS MULTIPLE */
				
			System.out.println("*******************************");
			System.out.println("TESTING IF GETTING PARAMETERS FOR MULTIPLE ELEMENTS WORKS");
			System.out.println("*******************************");
			
			// Get parameters of all buses
			type = "Bus";
			fields = "BusNum,BusName,BusNomVolt,BusSlack";
			request = type + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": Get buses parameters answer = " + answer);
			Object[][] busesParamsData = parseAnswerString(answer);
			printDataArray(busesParamsData);
			
			// Get parameters of all branches
			type = "Branch";
			fields = "BusNum,BusNum:1,LineCircuit,LineStatus";
			request = type + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": Get branches parameters answer = " + answer);
			Object[][] branchesParamsData = parseAnswerString(answer);
			printDataArray(branchesParamsData);

			// Get parameters of all generators
			type = "Gen";
			fields = "BusNum,GenID,GenMW,GenMVR,GenStatus";
			request = type + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": Get generators parameters answer = " + answer);
			Object[][] gensParamsData = parseAnswerString(answer);
			printDataArray(gensParamsData);
			
			// Get parameters of all loads
			type = "Load";
			fields = "BusNum,LoadID,LoadMW,LoadMVR,LoadStatus";
			request = type + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": Get loads parameters answer = " + answer);
			Object[][] loadsParamsData = parseAnswerString(answer);
			printDataArray(loadsParamsData);
			
			
			/* GET PARAMETERS SINGLE */
			
			System.out.println("*******************************");
			System.out.println("TESTING IF GETTING PARAMETERS FOR A SINGLE ELEMENT WORKS");
			System.out.println("*******************************");
			
			// Get parameters for one bus (bus 1)
			type = "Bus";
			deviceId = 1;
			//fields = "pwbusnum,pwbusname,pwbusvolt,pwbusangle";
			fields = "BusNum,BusName,BusVoltLimLow,BusVoltLimHigh,BusRad";
			request = type + "," + deviceId + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_SINGLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": Get bus parameters answer = " + answer);
			Object[][] busParamsData = parseAnswerString(answer);
			printDataArray(busParamsData);
			
			// Get parameters for one branch (branch between buses 1 and 2)
			type = "Branch";
			deviceId = 1;
			deviceId2 = 2;
			deviceId3 = 1;
			fields = "BusNum,BusNum:1,LineCircuit,LineStatus";
			request = type + "," + deviceId + "," + deviceId2 + "," + deviceId3 + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_SINGLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": Get branch parameters answer = " + answer);
			Object[][] branchParamsData = parseAnswerString(answer);
			printDataArray(branchParamsData);

			// Get parameters for one generator (generator on bus 6)
			type = "Gen";
			deviceId = 6;
			deviceId2 = 1;
			fields = "BusNum,GenID,GenMW";
			request = type + "," + deviceId + "," + deviceId2 + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_SINGLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": Get generator parameters answer = " + answer);
			Object[][] genParamsData = parseAnswerString(answer);
			printDataArray(genParamsData);
			
			// Get parameters for one load (load on bus 2)
			type = "Load";
			deviceId = 2;
			deviceId2 = 1;
			fields = "BusNum,LoadID,LoadMW,LoadMVA";
			request = type + "," + deviceId + "," + deviceId2 + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_SINGLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			System.out.println(getLocalName() + ": Get load parameters answer = " + answer);
			Object[][] loadParamsData = parseAnswerString(answer);
			printDataArray(loadParamsData);
			
			
			/* CHANGE PARAMETERS MULTIPLE */
			
			System.out.println("*******************************");
			System.out.println("TESTING IF CHANGING PARAMETERS FOR SEVERAL ELEMENTS WORKS");
			System.out.println("*******************************");
			
			// Change parameters for several buses (changes names for buses 1 and 2) 
			type = "Bus";
			nbFields = 2;
			fields = "BusNum,BusName";
			nbElements = 2;
			values = "1,Bus Machin,2,Bus Truc";
			request = type + "," + nbElements + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			
			// Change parameters for several branches (change line status (to open) for branches 1-2 and 2-3)
			type = "Branch";
			nbFields = 4;
			fields = "BusNum,BusNum:1,LineCircuit,LineStatus";
			nbElements = 2;
			values = "1,2,1,Open,2,3,1,Open";
			request = type + "," + nbElements + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			
			// Change parameters for several generators (change the MW output of generators on buses 1 and 2 to 10 MW)
			type = "Gen";
			nbFields = 3;
			fields = "BusNum,GenID,GenMW";
			nbElements = 2;
			values = "1,1,10,2,1,10";
			request = type + "," + nbElements + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			
			// Change parameters for several loads (change the MW demand of loads on buses 2 and 3 to 1.5 MW)
			type = "Load";
			nbFields = 3;
			fields = "BusNum,LoadID,LoadMW";
			nbElements = 2;
			values = "2,1,1.5,3,1,1.5";
			request = type + "," + nbElements + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_MULTIPLE,ACLMessage.INFORM);
			
			
			/* CHANGE PARAMETERS SINGLE */
			
			System.out.println("*******************************");
			System.out.println("TESTING IF CHANGING PARAMETERS FOR A SINGLE ELEMENT WORKS");
			System.out.println("*******************************");
			
			// Change parameters for a bus (change name of bus 1)
			type = "Bus";
			nbFields = 2;
			fields = "BusNum,BusName";
			values = "1,Bus Machin";
			request = type + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_SINGLE,ACLMessage.INFORM);
			
			// Change parameters for a branch (change status of branch 1-2 to open)
			type = "Branch";
			nbFields = 4;
			fields = "BusNum,BusNum:1,LineCircuit,LineStatus";
			values = "1,2,1,Open";
			request = type + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_SINGLE,ACLMessage.INFORM);
			
			// Change parameters for a generator (change MW output of generator on bus 1 to 10 MW)
			type = "Gen";
			nbFields = 3;
			fields = "BusNum,GenID,GenMW";
			values = "1,1,10";
			request = type + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_SINGLE,ACLMessage.INFORM);
			
			// Change parameters for a load (change MW demand of load on bus 2 to 1.5 MW)
			type = "Load";
			nbFields = 3;
			fields = "BusNum,LoadID,LoadMW";
			values = "2,1,1.5";
			request = type + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_SINGLE,ACLMessage.INFORM);
			
			
			/* TEST CHANGES */
			
			System.out.println("*******************************");
			System.out.println("TESTING IF CHANGES ARE REALLY WORKING");
			System.out.println("*******************************");
			
			// Get parameters for a load (load on bus 2)
			System.out.println("*** Read initial values:");
			type = "Load";
			deviceId = 2;
			deviceId2 = 1;
			fields = "BusNum,LoadID,LoadMW,LoadMVR,LoadStatus";
			request = type + "," + deviceId + "," + deviceId2 + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_SINGLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			//System.out.println(getLocalName() + ": Get load parameters answer = " + answer);
			Object[][] loadParamsData2 = parseAnswerString(answer);
			printDataArray(loadParamsData2);
			
			// Change parameters for a load (change MW demand of load on bus 2 to 1.5 MW, 0.5 MVAR)
			System.out.println("*** Change values...");
			type = "Load";
			nbFields = 4;
			fields = "BusNum,LoadID,LoadMW,LoadMVR";
			values = "2,1,2.5,0.5";
			request = type + "," + nbFields + "," + fields + "," + values;
			sendMessage(matlabAgent,request,CHANGE_PARAMETERS_SINGLE,ACLMessage.INFORM);
			
			// Get parameters for a load (load on bus 2)
			System.out.println("*** Values after change (you should see different values):");
			type = "Load";
			deviceId = 2;
			deviceId2 = 1;
			fields = "BusNum,LoadID,LoadMW,LoadMVR,LoadStatus";
			request = type + "," + deviceId + "," + deviceId2 + "," + fields;
			sendMessage(matlabAgent,request,GET_PARAMETERS_SINGLE,ACLMessage.INFORM);
			answer = blockingReceive().getContent();
			//System.out.println(getLocalName() + ": Get load parameters answer = " + answer);
			Object[][] loadParamsData3 = parseAnswerString(answer);
			printDataArray(loadParamsData3);
			
			
			/* SCRIPT COMMANDS */
			
			System.out.println("*******************************");
			System.out.println("TESTING SWITCHING TO RUN MODE AND RUNNING A POWER FLOW");
			System.out.println("*******************************");
			
			// Switch to run mode
			sendMessage(matlabAgent,"",RUN_MODE,ACLMessage.INFORM);
			
			// Run power flow
			sendMessage(matlabAgent,"",RUN_POWER_FLOW,ACLMessage.INFORM);
			
			
			/* END CONNECTION */
			
			System.out.println("*******************************");
			System.out.println("TESTING SAVING THE FILE, CLOSING IT AND CLOSING THE CONNECTION");
			System.out.println("*******************************");
			
			// Save case
			sendMessage(matlabAgent,powerWorldFilePath2,SAVE_FILE,ACLMessage.INFORM);
			
			// Close case
			sendMessage(matlabAgent,"",CLOSE_CASE,ACLMessage.INFORM);
			
			// End connection
			sendMessage(matlabAgent,"",END_CONNECTION,ACLMessage.INFORM);
			
			// Kill agent
			myAgent.doDelete();
			
			
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
	}


	/**
	 * Parse an answer string received from Matlab
	 * Return a 2D array containing the data 
	 * @param answer
	 * @param types
	 * @return
	 */
	private Object[][] parseAnswerString(String answer)
	{
		// Split the incoming string
		String[] splitAnswer = answer.split(",");
		int nbElements = Integer.parseInt(splitAnswer[0]);
		int nbParams = Integer.parseInt(splitAnswer[1]);

		// Create the output data array
		Object[][] data = new Object[nbElements][nbParams];

		for(int i=0;i<nbElements;i++)
		{
			for(int j=0;j<nbParams;j++)
			{
				data[i][j] = splitAnswer[2+i*nbParams+j];
			}
		}

		return data;
	}


	/**
	 * Prints an array of values
	 * @param array
	 */
	private void printDataArray(Object[][] array)
	{
		for(int i=0;i<array.length;i++)
		{
			for(int j=0;j<array[0].length;j++)
			{
				if(array[i][j].equals(" "))
					System.out.print(Double.NaN + "\t");
				else
					System.out.print(array[i][j] + "\t");
			}
			System.out.println();
		}
	}


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

}
