import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.*;   // import JMF classes
import javax.swing.JFileChooser;
 
/**
*
* @author BUDDHIMA
*/
 
public class SimpleAudioPlayer {
 
private Player audioPlayer = null;
 
public SimpleAudioPlayer(URL url) {
 
try {
 
//MediaLocator ml=new MediaLocator(url);
 
audioPlayer = Manager.createPlayer(url);
 
} catch (Exception ex) {
 
System.out.println(ex);
 
}
 
}
 
public SimpleAudioPlayer(File file) throws MalformedURLException {
 
this(file.toURI().toURL());
 
}
 
public void play() {
 
audioPlayer.start(); // start playing
 
}
 
public void stop() {
 
audioPlayer.stop();  //stop playing
 
audioPlayer.close();
 
}
 
public static void main(String[] args) {
 
try {
 
// TODO code application logic here
 
JFileChooser fc = new JFileChooser();
 
fc.showOpenDialog(null);
 
File file = fc.getSelectedFile();
 
SimpleAduioPlayer sap = new SimpleAduioPlayer(file);
 
sap.play();
 
//sap.stop();
 
} catch (MalformedURLException ex) {
 
System.out.println(ex);
 
}
 
}
 
}
