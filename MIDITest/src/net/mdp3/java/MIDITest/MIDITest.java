/**
 * 
 */
package net.mdp3.java.MIDITest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

/**
 * @author Mikel
 *
 */
public class MIDITest {
	
	private BufferedReader br;
	//private LinkedList<Receiver> receivers = new LinkedList<Receiver>();
	MIDITest_Receiver mtr = new MIDITest_Receiver("MultiIn");
	private LinkedList<MidiDevice> openDevices = new LinkedList<MidiDevice>();
	private LinkedList<Transmitter> transmitters = new LinkedList<Transmitter>();
	private LinkedList<Receiver> midiOutDevices = new LinkedList<Receiver>();

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
				
				if (inStr.toLowerCase().indexOf("test") > -1 && !midiOutDevices.isEmpty()) {
					sendTestMsg();
				}
				else if (inStr.toLowerCase().indexOf("forward") > -1 && !midiOutDevices.isEmpty()) {
					forwardAllMidi();
				}
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
						
						//receivers.add(new MIDITest_Receiver(infos[i].getName()));
						//t.setReceiver(receivers.getLast());
						t.setReceiver(mtr);
					}
				}
				else if (device.getClass().getName().equalsIgnoreCase("com.sun.media.sound.MidiOutDevice")
						/* && infos[i].getName().toLowerCase().indexOf("usb") > -1*/) {
					System.out.println("USB MIDI Out Found!");
					if (!device.isOpen() && device.getMaxReceivers() != 0) {
						device.open();
						openDevices.addLast(device);
						
						Receiver r = device.getReceiver();
						midiOutDevices.add(r);
					}
				}
		    	device = null;
			}
			catch (MidiUnavailableException e) {
				System.out.println("Error loading device " + i + " " + e);
		    }
		}
	}
	
	private void sendTestMsg() {
		Receiver r = null;
		ShortMessage message = new ShortMessage();
		long timeStamp = -1;
		
		try {
			message.setMessage(ShortMessage.NOTE_ON, 2, 60, 127);
		} catch (InvalidMidiDataException e) {
			System.out.println("Error creating midi message: " + e);
			e.printStackTrace();
		}
		
		System.out.println("Sending message: " + message.getChannel() + " " + 
				message.getCommand() + " " + message.getData1() + " " + message.getData2());
		for (int i = 0; i < midiOutDevices.size(); i++) {
			r = midiOutDevices.get(i);
			
			r.send(message, timeStamp);
		}
	}
	
	private void forwardAllMidi() {
		for (int i = 0; i < midiOutDevices.size(); i++)
			mtr.addForwarder(midiOutDevices.get(i));
	}
	
	public void closeAll() {
		//for (int i = 0; i < receivers.size(); i++)
			//receivers.get(i).close();
		for (int i = 0; i < transmitters.size(); i++)
			transmitters.get(i).close();
		for (int i = 0; i < midiOutDevices.size(); i++)
			midiOutDevices.get(i).close();
		for (int i = 0; i < openDevices.size(); i++)
			openDevices.get(i).close();
	}
}
