import org.springframework.boot.MyBootStartTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


public class MyBootStartConfigTest {
	public static void main(String[] args) {
		SpringApplication.run(MyBootStartTest.class);
		SpringApplication application=new SpringApplication(MyBootStartTest.class);
		ApplicationContext applicationContext=application.run(args);
		//applicationContext.publishEvent();
	}
}
