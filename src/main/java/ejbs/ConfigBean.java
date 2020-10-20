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
    public void populateDB(){
        try {
            //Admins
            adminBean.create("andresc3","1234","André Vicente", "andre@andre.pt");
            //Clients
            clientBean.create("andrev","1234","Andre","andre@aqui.pt","918345667","Aqui");
            //Designers
            designerBean.create("aa","1234","Lina","lina@toto.pt");
            //Manufacturer
            manufacturerBean.create("ab","1234","Bruna","luna@lady.pt");
            //Material
            materialBean.create("bean","Tom","ab","er");


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }


    }

}
