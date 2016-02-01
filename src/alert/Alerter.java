package alert;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URISyntaxException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

@Mojo(name = "sayhi")
public class Alerter extends AbstractMojo {
	
	
	JFXPanel p = new JFXPanel();
	
	
	boolean close=false;
	
	
	public void execute() throws MojoExecutionException {
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				String bip = "speech.mp3";
				String resource = null;
				try {
					resource = Alerter.class.getResource(bip).toURI().toString();
				} catch (URISyntaxException e) {
					getLog().error(e);
				}
				
				Media hit = new Media(resource);
				final MediaPlayer mediaPlayer = new MediaPlayer(hit);
				mediaPlayer.play();
				
				Runnable run = mediaPlayer.getOnStopped();
				mediaPlayer.setOnEndOfMedia(new Runnable() {
					public void run() {
						mediaPlayer.dispose();
						close=true;
						com.sun.javafx.application.PlatformImpl.tkExit();
					}
				});
			}
		});
		t2.start();
		while(!close);
	}

	public static void main(String[] args) throws MojoExecutionException {
		new Alerter().execute();
	}
}