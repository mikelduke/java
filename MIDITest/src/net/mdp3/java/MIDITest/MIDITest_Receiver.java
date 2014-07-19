/**
 * 
 */
package net.mdp3.java.MIDITest;

import java.util.LinkedList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

/**
 * @author Mikel
 *
 */
public class MIDITest_Receiver implements Receiver {
	private String recName = "";
	
	private LinkedList<Receiver> receivers = new LinkedList<Receiver>();
	
	public MIDITest_Receiver(String name) {
		this.recName = name;
		System.out.println(this.recName + " new MidiTest Receiver!");
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	public void send(MidiMessage arg0, long arg1) {
		System.out.println(this.recName + " MIDIMessage: " + arg0.getMessage());
		
		if (!receivers.isEmpty()) {
			for (int i = 0; i < receivers.size(); i++)
				receivers.get(i).send(arg0, arg1);
		}
	}
	
	public void addForwarder(Receiver r) {
		receivers.addLast(r);
	}
}
