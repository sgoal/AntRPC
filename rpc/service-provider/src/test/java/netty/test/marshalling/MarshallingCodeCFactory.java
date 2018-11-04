package netty.test.marshalling;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingCodeCFactory {
	
	private static final int VERSION = 5;
	
	public static MarshallingDecoder buildingMarshallingDecoder() {
		MarshallerFactory factory = 
				Marshalling.getProvidedMarshallerFactory("serial");
		MarshallingConfiguration configuration = 
				new MarshallingConfiguration();
		configuration.setVersion(VERSION);
		
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(
				factory, configuration);
		
		MarshallingDecoder decoder = new MarshallingDecoder(provider);
		return decoder;
		
	}
	
	public static MarshallingEncoder buildingMarshallingEncoder() {
		MarshallerFactory factory = 
				Marshalling.getProvidedMarshallerFactory("serial");
		MarshallingConfiguration configuration = 
				new MarshallingConfiguration();
		configuration.setVersion(VERSION);
		
		MarshallerProvider provider = new DefaultMarshallerProvider(
				factory, configuration);
		
		MarshallingEncoder encoder = new MarshallingEncoder(provider);
		return encoder;
		
	}
}
