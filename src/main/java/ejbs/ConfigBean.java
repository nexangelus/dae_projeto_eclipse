package ejbs;


import entities.Panel;

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
	ProfileBean profileBean;

	@EJB
	SheetBean sheetBean;

	@EJB
	PanelBean panelBean;

	private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

	@PostConstruct
	public void populateDB() {
		try {
			//region Admins
			adminBean.create("admin", "admin", "Administrador", "admin@email.com");
			//endregion

			//region Clients
			clientBean.create("lourenco.abreu", "9GXJT2oQAC", "Gonçalo Diogo Martins de Amaral", "alves.frederico@hotmail.com", "219793927", "Travessa St. Vanessa Almeida, 21, Bloco 7 5381 Oliveira de Azeméis");
			clientBean.create("neves.nelson", "XJQAioWQjV", "António Reis", "catia74@mail.pt", "244320214", "Tv. de Freitas 4201-500 Barcelos");
			clientBean.create("constanca.batista", "YKQXcarZta", "Igor Alexandre Araújo Monteiro", "aabreu@teixeira.eu", "292489825", "Avenida Telmo Neto, 614, Bl. 3 3929-618 Vila Nova de Gaia");
			clientBean.create("lopes.lourenco", "JY3ORooMMp", "Jorge Neto de Miranda", "emanuel27@mail.pt", "260414958", "Lg. São. Joana 5691-408 Lisboa");
			clientBean.create("jtavares", "LbWHuSMDUY", "Ana Miriam Azevedo", "benedita.lopes@gmail.com", "219290205", "Tv. Magalhães, 8 1099 Ribeira Grande");
			clientBean.create("abreu.vasco", "sscleiepvu", "Érika Vieira de Antunes", "faria.luciana@yahoo.com", "285892284", "Tv. St. David Castro 8609-763 Faro");
			//endregion

			//region Designers
			designerBean.create("diogo55", "HX6iIkymXP", "Helena Neto", "isaac.assuncao@mota.com");
			designerBean.create("bpinto", "icLMoqJM99", "Álvaro Pinheiro Paiva", "renato06@clix.pt");
			designerBean.create("artur.batista", "JFB4jLOMDK", "Jéssica Sofia Carvalho Sousa", "campos.joao@anjos.pt");
			//endregion

			//region Manufacturer
			manufacturerBean.create("vicente03", "oJ04uCMOZ1", "Hélder Hélder Rodrigues Pinto", "bianca43@sapo.pt", "Lg. Isabel Ferreira 8245 Alcobaça", "http://helder.pt", "285899583");
			manufacturerBean.create("fernando78", "EO8qP4ATEA", "Alice Tatiana Leite de Henriques", "hugo89@moura.pt", "Tv. Joaquim Soares, nº 61, 1º Dir. 7244-529 Ermesinde", "http://alice.pt", "281500760");
			manufacturerBean.create("julia.lopes", "C6O99cQofz", "Ângelo Anjos de Leite", "mendes.sara@gmail.com", "Rua de Nogueira, 2, Bl. 9 7510-449 Rio Maior", "http://angelo.pt", "246839248");
			manufacturerBean.create("duarte.amaral", "5A7eNtHVKn", "Miguel Neto", "testeves@santos.com", "Av. Pinho 5165 Espinho", "http://neto.pt", "913738624");
			//endregion

			//region Materials
			//region Profiles
			profileBean.create("Superomega® 80 x 1.0", "O Superomega ® é o resultado de 2 anos de investimento em I&D numa parceria entre O FELIZ e a Universidade de Coimbra para a conceção de uma solução inovadora em perfis de aço leve enformados a frio.",
					"fernando78", "Ω", 80, 176, 2.39, 0.608, "S280GD");
			//endregion

			//region Sheets
			sheetBean.create("P0-272-30", "Chapa Perfilada trapezoidal com 30 mm de altura, sem nervuras de rigidez.",
					"fernando78", "P0-272-30", 0.5, 4.15, "S280GD");
			//endregion

			//region Panel
			panelBean.create("Topconver® 3", "Painel Isolante de 3 ondas para cobertura, composto por duas chapas metálicas perfiladas, unidas por um núcleo de espuma rígida de poliuretano (PUR) ou polisocianurato (PIR).",
					"fernando78", "Topconver® 3", 30, 1000, 4, 8, 7.7);
			//endregion
			//endregion
			

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}


	}

}
