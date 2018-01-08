package com.flatirons.tasklist.service;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import com.flatirons.tasklist.model.Fields;
import com.flatirons.tasklist.model.Task;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class TaskService {
    private static volatile TaskService instance = null;
    File inputFile = new File(this.getClass().getClassLoader().getResource("db.xml").getFile());
    Document doc = null;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //private static final Log LOGGER = LogFactory.getLog(AddressService.class);

    private TaskService() {
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
        }catch(Exception exception){
        }
    }

    public static synchronized TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }


    public List<Task> getPendingTasks() {
        NodeList _tasks = findAllUncompletedTasks();
        List<Task> tasks = new ArrayList<>();
        for(int index = 0; index < _tasks.getLength(); index++) {
            Element _task = (Element) _tasks.item(index);
            Task temp = new Task();
            temp.setId(_task.getAttribute(Fields.Id.getKey()));
            temp.setName(_task.getElementsByTagName(Fields.Name.getKey()).item(0).getTextContent());
            temp.setDueDate(parseTaskDate(_task.getElementsByTagName(Fields.DueDate.getKey()).item(0).getTextContent()));
            temp.setCompleted(false);
            tasks.add(temp);
        }
        return tasks;
    }
    private Date parseTaskDate(String dateString){
        Date _date;
        try{
            _date = formatter.parse(dateString);
        }catch (ParseException exception){
            return null;
        }
        return _date;
    }

    private String formatDate(Date date){
        String formatted = "";
        formatted = formatter.format(date);
        return formatted;
    }

    public Task getTaskById(String id) {
        Task temp = new Task();
        Element _task = findElement(id);
        if(_task != null){
            Boolean status = Boolean.parseBoolean(_task.getElementsByTagName(Fields.Status.getKey()).item(0).getTextContent());
            temp.setId(id);
            temp.setName(_task.getElementsByTagName(Fields.Name.getKey()).item(0).getTextContent());
            temp.setDueDate(parseTaskDate(_task.getElementsByTagName(Fields.DueDate.getKey()).item(0).getTextContent()));
            temp.setCompleted(status);
        }
        return temp;
    }

    private Element findElement(String id){
        String xpath = "/tasks/task[@id='%s']";
        Element _result = null;
        try{
            XPath xPath = XPathFactory.newInstance().newXPath();
            _result = (Element) xPath.evaluate(String.format(xpath, id), doc.getDocumentElement(), XPathConstants.NODE);
        }catch (XPathExpressionException exception){

        }
        return _result;
    }

    private NodeList findAllUncompletedTasks(){
        String query = "/tasks/task[descendant::status[text() = 'false']]";
        NodeList result = null;
        try{
            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xPath.compile(query);
            result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        }catch (XPathExpressionException exception){

        }
        return result;
    }

    public Task saveOrUpdateAddress(Task task) {
        Element taskToUpdate = findElement(task.getId());
        if(taskToUpdate != null) {
            Node _name = taskToUpdate.getElementsByTagName(Fields.Name.getKey()).item(0);
            _name.setTextContent(task.getName());
            Node _dueDate = taskToUpdate.getElementsByTagName(Fields.DueDate.getKey()).item(0);
            _dueDate.setTextContent(formatDate(task.getDueDate()));
            Node _status = taskToUpdate.getElementsByTagName(Fields.Status.getKey()).item(0);
            _status.setTextContent(String.valueOf(task.getCompleted()));
        }else{
            Element newTask = doc.createElement("task");
            String uuid = UUID.randomUUID().toString().substring(24, 36);
            newTask.setAttribute(Fields.Id.getKey(), uuid);
            task.setId(uuid);

            Element name = doc.createElement(Fields.Name.getKey());
            name.appendChild(doc.createTextNode(task.getName()));
            newTask.appendChild(name);

            Element dueDate = doc.createElement(Fields.DueDate.getKey());
            dueDate.appendChild(doc.createTextNode(formatDate(task.getDueDate())));
            newTask.appendChild(dueDate);

            Element status = doc.createElement(Fields.Status.getKey());
            status.appendChild(doc.createTextNode("false"));
            newTask.appendChild(status);
            task.setCompleted(false);

            doc.getDocumentElement().appendChild(newTask);
        }
        write();
        return task;
    }

    private void write() {
        try {
            DOMSource source = new DOMSource(doc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(inputFile);
            transformer.transform(source, result);
        }catch (TransformerException exception){
            System.err.print(exception);
        }
    }

    public boolean deleteTask(String id) {
        Node deleted = null;
        Element taskToDelete = findElement(id);
        if(taskToDelete != null) {
            deleted = doc.getDocumentElement().removeChild(taskToDelete);
        }
        return deleted != null;
    }

    /*public void deleteAllAddress() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        if(transaction.getCompleted().equals(TransactionStatus.NOT_ACTIVE))
            LOGGER.debug(" >>> Transaction close.");
        session.createQuery("delete from Address").executeUpdate();
        transaction.commit();
    }*/
}
