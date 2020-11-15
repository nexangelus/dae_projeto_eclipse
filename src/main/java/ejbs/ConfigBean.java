package ejbs;


import entities.Profile;
import entities.Project;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.List;
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
	FamilyBean familyBean;

	@EJB
	ProfileBean profileBean;

	@EJB
	ProjectBean projectBean;

	private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

	// TODO Perguntar se é muito mau aqui chamar o ExcelUtils e lá fazer materialBean.create(...)

	@PostConstruct
	public void populateDB() {
		try {
			//region Admins
			adminBean.create("admin", "admin", "Administrador", "admin@email.com");
			//endregion

			//region Clients
			clientBean.create("client", "client", "Gonçalo Diogo Martins de Amaral", "alves.frederico@hotmail.com", "219793927", "Travessa St. Vanessa Almeida, 21, Bloco 7 5381 Oliveira de Azeméis");
			clientBean.create("neves.nelson", "XJQAioWQjV", "António Reis", "catia74@mail.pt", "244320214", "Tv. de Freitas 4201-500 Barcelos");
			clientBean.create("constanca.batista", "YKQXcarZta", "Igor Alexandre Araújo Monteiro", "aabreu@teixeira.eu", "292489825", "Avenida Telmo Neto, 614, Bl. 3 3929-618 Vila Nova de Gaia");
			clientBean.create("lopes.lourenco", "JY3ORooMMp", "Jorge Neto de Miranda", "emanuel27@mail.pt", "260414958", "Lg. São. Joana 5691-408 Lisboa");
			clientBean.create("jtavares", "LbWHuSMDUY", "Ana Miriam Azevedo", "benedita.lopes@gmail.com", "219290205", "Tv. Magalhães, 8 1099 Ribeira Grande");
			clientBean.create("abreu.vasco", "sscleiepvu", "Érika Vieira de Antunes", "faria.luciana@yahoo.com", "285892284", "Tv. St. David Castro 8609-763 Faro");
			//endregion

			//region Designers
			designerBean.create("designer", "designer", "Helena Neto", "isaac.assuncao@mota.com");
			designerBean.create("bpinto", "icLMoqJM99", "Álvaro Pinheiro Paiva", "renato06@clix.pt");
			designerBean.create("artur.batista", "JFB4jLOMDK", "Jéssica Sofia Carvalho Sousa", "campos.joao@anjos.pt");
			//endregion

			//region Manufacturer
			manufacturerBean.create("manufacturer", "manufacturer", "Hélder Hélder Rodrigues Pinto", "bianca43@sapo.pt", "Lg. Isabel Ferreira 8245 Alcobaça", "http://helder.pt", "285899583");
			manufacturerBean.create("fernando78", "EO8qP4ATEA", "Alice Tatiana Leite de Henriques", "hugo89@moura.pt", "Tv. Joaquim Soares, nº 61, 1º Dir. 7244-529 Ermesinde", "http://alice.pt", "281500760");
			manufacturerBean.create("julia.lopes", "C6O99cQofz", "Ângelo Anjos de Leite", "mendes.sara@gmail.com", "Rua de Nogueira, 2, Bl. 9 7510-449 Rio Maior", "http://angelo.pt", "246839248");
			manufacturerBean.create("duarte.amaral", "5A7eNtHVKn", "Miguel Neto", "testeves@santos.com", "Av. Pinho 5165 Espinho", "http://neto.pt", "913738624");
			//endregion


			//region Families
			familyBean.create("Section C 220 BF", "manufacturer");
			familyBean.create("Section Z 220 BF", "fernando78");
			//endregion




			//region Materials
			//region Profiles
			profileBean.create(1, "manufacturer", "C 120/50/21 x 1.5", 13846, 13846, 375, 220000);
			profileBean.create(2, "fernando78", "C 120/60/13 x 2.0", 18738, 18738, 500, 220000);

			Profile profile1 = profileBean.getProfile(3);
			profile1.addMcr_p(3.0,243.2123113);
			profile1.addMcr_p(4.0,145.238784);
			profile1.addMcr_p(5.0,99.15039028);
			profile1.addMcr_p(6.0,73.71351699);
			profile1.addMcr_p(7.0,58.07716688);
			profile1.addMcr_p(8.0,47.68885195);
			profile1.addMcr_p(9.0,40.37070843);
			profile1.addMcr_p(10.0,34.9747033);
			profile1.addMcr_p(11.0,30.84866055);
			profile1.addMcr_p(12.0,27.59984422);
			//endregion

			//region Sheets
			/*sheetBean.create("P0-272-30", "Chapa Perfilada trapezoidal com 30 mm de altura, sem nervuras de rigidez.",
					"fernando78", "P0-272-30", 0.5, 4.15, "S280GD");
			//endregion

			//region Panel
			panelBean.create("Topconver® 3", "Painel Isolante de 3 ondas para cobertura, composto por duas chapas metálicas perfiladas, unidas por um núcleo de espuma rígida de poliuretano (PUR) ou polisocianurato (PIR).",
					"fernando78", "Topconver® 3", 30, 1000, 4, 8, 7.7);*/
			//endregion
			//endregion

			projectBean.create("client", "designer", "title", "description");
			projectBean.create("jtavares", "designer", "title", "description");

			List<Project> client = clientBean.getAllClientProjects("client");
			 client = clientBean.getAllClientProjects("jtavares");
			System.out.println(client.size());

		} catch (Exception e) {
			System.out.println("ERRO");
			logger.log(Level.SEVERE, e.getMessage());
		}


	}

}
