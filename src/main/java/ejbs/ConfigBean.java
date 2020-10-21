package ejbs;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton(name = "ConfigEJB")
@Startup
public class ConfigBean {

	@EJB
	AdminBean adminBean;

	@EJB
	ClientBean clientBean;

	@EJB
	DesignerBean designerBean;

	@EJB
	ManufacturerBean manufacturerBean;

	@EJB
	MaterialBean materialBean;

	private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

	@PostConstruct
	public void populateDB() {
		try {
			//Admins
			adminBean.create("admin", "admin", "Administrador", "admin@email.com");

			//Clients
			clientBean.create("lourenco.abreu", "9GXJT2oQAC", "Gonçalo Diogo Martins de Amaral", "alves.frederico@hotmail.com", "219793927", "Travessa St. Vanessa Almeida, 21, Bloco 7 5381 Oliveira de Azeméis");
			clientBean.create("neves.nelson", "XJQAioWQjV", "António Reis", "catia74@mail.pt", "244320214", "Tv. de Freitas 4201-500 Barcelos");
			clientBean.create("constanca.batista", "YKQXcarZta", "Igor Alexandre Araújo Monteiro", "aabreu@teixeira.eu", "292489825", "Avenida Telmo Neto, 614, Bl. 3 3929-618 Vila Nova de Gaia");
			clientBean.create("lopes.lourenco", "JY3ORooMMp", "Jorge Neto de Miranda", "emanuel27@mail.pt", "260414958", "Lg. São. Joana 5691-408 Lisboa");
			clientBean.create("jtavares", "LbWHuSMDUY", "Ana Miriam Azevedo", "benedita.lopes@gmail.com", "219290205", "Tv. Magalhães, 8 1099 Ribeira Grande");
			clientBean.create("abreu.vasco", "sscleiepvu", "Érika Vieira de Antunes", "faria.luciana@yahoo.com", "285892284", "Tv. St. David Castro 8609-763 Faro");


			//Designers
			designerBean.create("diogo55", "HX6iIkymXP", "Helena Neto", "isaac.assuncao@mota.com");
			designerBean.create("bpinto", "icLMoqJM99", "Álvaro Pinheiro Paiva", "renato06@clix.pt");
			designerBean.create("artur.batista", "JFB4jLOMDK", "Jéssica Sofia Carvalho Sousa", "campos.joao@anjos.pt");


			//Manufacturer
			manufacturerBean.create("vicente03", "oJ04uCMOZ1", "Hélder Hélder Rodrigues Pinto", "bianca43@sapo.pt");
			manufacturerBean.create("fernando78", "EO8qP4ATEA", "Alice Tatiana Leite de Henriques", "hugo89@moura.pt");
			manufacturerBean.create("julia.lopes", "C6O99cQofz", "Ângelo Anjos de Leite", "mendes.sara@gmail.com");
			manufacturerBean.create("duarte.amaral", "5A7eNtHVKn", "Miguel Neto", "testeves@santos.com");


			//Material
			materialBean.create("bean", "Tom", "ab", "er");


		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}


	}

}
