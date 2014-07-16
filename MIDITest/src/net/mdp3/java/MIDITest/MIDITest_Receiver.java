/**
 * 
 */
package net.mdp3.java.MIDITest;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * @author Mikel
 *
 */
public class MIDITest_Receiver implements Receiver {
	private String recName = "";
	
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
		// TODO Auto-generated method stub
		System.out.println(this.recName + " MIDIMessage: " + arg0 + " Time: " + arg1);
	}
}
