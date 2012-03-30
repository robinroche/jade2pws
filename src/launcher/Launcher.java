package launcher;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

/**
 * Launches 2 JADE agents for testing the communication with Matlab and Powerworld
 * @author rroche
 */
public class Launcher
{

	static ContainerController cController;

	static String PWRWORLD_NAME = "matlabComAgent"; 
	static String PWRWORLD_CLASS = "pwrworld.MatlabComAgent";
	static String PWRWORLD_TESTER_NAME = "Tester"; 
	static String PWRWORLD_TESTER_CLASS = "pwrworld.MatlabComAgentTest"; 
	 

	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args)
	{		
		try 
		{
			runJade();
		} 
		catch (StaleProxyException e) {} 
		catch (ControllerException e) {}	
	}


	/**
	 * Runs JADE and starts the initial agents
	 * @throws ControllerException
	 */
	public static void runJade() throws ControllerException
	{
		// Launch JADE platform
		Runtime rt = Runtime.instance();
		Profile p;
		p = new ProfileImpl();
		cController = rt.createMainContainer(p);			
		rt.setCloseVM(true);
		
		// Launch Powerworld interface agent
		addAgent(PWRWORLD_NAME, PWRWORLD_CLASS, null);
		addAgent(PWRWORLD_TESTER_NAME, PWRWORLD_TESTER_CLASS, null);
	}


	/**
	 * Creates and starts an agent
	 * @param name
	 * @param type
	 * @throws ControllerException
	 */
	private static void addAgent(String name, String type, String arg) throws ControllerException 
	{		
		Object[] argsObj = {arg};
		AgentController ac = cController.createNewAgent(name, type, argsObj);
		ac.start();
	}


} // End class


