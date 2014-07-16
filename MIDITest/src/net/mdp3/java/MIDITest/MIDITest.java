/**
 * 
 */
package net.mdp3.java.MIDITest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * @author Mikel
 *
 */
public class MIDITest {
	
	private BufferedReader br;
	private LinkedList<Receiver> receivers = new LinkedList<Receiver>();
	private LinkedList<MidiDevice> openDevices = new LinkedList<MidiDevice>();
	private LinkedList<Transmitter> transmitters = new LinkedList<Transmitter>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MIDITest mt = new MIDITest();
	}
	
	public MIDITest() {
		loadDevices();
		
		br = new BufferedReader(new InputStreamReader(System.in));
		String inStr = "";
		while (inStr.toLowerCase().indexOf("quit") < 0) {
			System.out.print("MIDITest: ");
			try {
				inStr = br.readLine();
			} catch (IOException e) {
				System.out.println("Error reading input! " + e);
				e.printStackTrace();
			}
		}
		
		closeAll();
	}
	
	private void loadDevices() {
		MidiDevice device = null;
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		
		for (int i = 0; i < infos.length; i++) {
			System.out.println(i + ": " + infos[i].getName());
			try {
				device = MidiSystem.getMidiDevice(infos[i]);
				System.out.println("  " + i + " class: " + device.getClass().getName());
				System.out.println("  " + i + " receivers: " + device.getMaxReceivers());
				System.out.println("  " + i + " transmitters: " + device.getMaxTransmitters());
				
				if (device.getClass().getName().equalsIgnoreCase("com.sun.media.sound.MidiInDevice")) {
					System.out.println("MIDI In Found!");
					if (!device.isOpen() && device.getMaxTransmitters() != 0) {
						device.open();
						openDevices.addLast(device);
						
						
						Transmitter t = device.getTransmitter();
						transmitters.addLast(t);
						
						receivers.add(new MIDITest_Receiver(infos[i].getName()));
						t.setReceiver(receivers.getLast());
					}
				}
		    	device = null;
			}
			catch (MidiUnavailableException e) {
				System.out.println("Error loading device " + i + " " + e);
		    }
		}
	}
	
	public void closeAll() {
		for (int i = 0; i < receivers.size(); i++)
			receivers.get(i).close();
		for (int i = 0; i < transmitters.size(); i++)
			transmitters.get(i).close();
		for (int i = 0; i < openDevices.size(); i++)
			openDevices.get(i).close();
	}
}
