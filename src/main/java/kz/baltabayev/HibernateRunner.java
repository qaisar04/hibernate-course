package kz.baltabayev;

import kz.baltabayev.entity.User;
import kz.baltabayev.entity.UserChat;
import kz.baltabayev.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;

import java.util.List;
import java.util.Map;

public class HibernateRunner {
    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            RootGraph<User> userGraph = session.createEntityGraph(User.class);
            userGraph.addAttributeNodes("company", "userChats");
            SubGraph<UserChat> userChatSubGraph = userGraph.addSubgraph("userChats", UserChat.class);
            userChatSubGraph.addAttributeNodes("chat");


            Map<String, Object> properties = Map.of(
//                  GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("withCompanyAndChat")
                    GraphSemantic.LOAD.getJakartaHintName(), userGraph
            );
            User user = session.find(User.class, 1L, properties);

            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());


            List<User> users = session.createQuery("select u from User u where 1 = 1", User.class)
                    .setHint(GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("withCompanyAndChat"))
                    .list();

            users.forEach(it -> System.out.println(it.getCompany().getName()));
            users.forEach(it -> System.out.println(it.getUserChats().size()));



            session.getTransaction().commit();
        }
    }
}
