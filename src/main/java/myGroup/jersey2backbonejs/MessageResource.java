package myGroup.jersey2backbonejs;

import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

@Path("message")
public class MessageResource {
	private DataSource ds;

	public MessageResource(@Context ServletContext ctx) throws NamingException {
		InitialContext initCon = new InitialContext();
		ds = (DataSource) initCon.lookup("java:comp/env/MySQL_JDBC");
		if (ds == null) {
			ctx.log("The DataSource(MySQL_JDBC) couldn't be got.");
			throw new InternalServerErrorException();
		}
	}

	private static final String SQL_SELECT_LIST = "SELECT m.id, m.content, m.createTime FROM message AS m";
	private static final String SQL_SELECT_ID = "SELECT m.id, m.content, m.createTime FROM message AS m WHERE id = ?";
	private static final String SQL_SELECT_LAST = "SELECT m.id, m.content, m.createTime FROM message AS m WHERE id = (SELECT MAX(id) FROM message)";
	private static final String SQL_INSERT = "INSERT INTO message(content, createTime) VALUES(?, now())";
	private static final String SQL_UPDATE = "UPDATE message SET content = ? WHERE id = ?";
	private static final String SQL_DELETE = "DELETE FROM message WHERE id = ?";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessages(@Context ServletContext ctx) {
		ctx.log("GET ()");
		QueryRunner qr = new QueryRunner(ds);
		ResultSetHandler<List<Message>> h = new BeanListHandler<Message>(
				Message.class);
		try {
			return qr.query(SQL_SELECT_LIST, h);
		} catch (SQLException e) {
			ctx.log(e.getMessage());
			throw new InternalServerErrorException();
		}
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Message getMessage(@Context ServletContext ctx,
			@PathParam("id") Integer id) {
		ctx.log("GET (" + id + ")");
		QueryRunner qr = new QueryRunner(ds);
		ResultSetHandler<Message> h = new BeanHandler<Message>(Message.class);
		try {
			Message m = qr.query(SQL_SELECT_ID, h, id);
			if (m != null)
				return m;
			else
				throw new NotFoundException();
		} catch (SQLException e) {
			ctx.log(e.getMessage());
			throw new InternalServerErrorException();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message postMessage(@Context ServletContext ctx, Message message) {
		ctx.log("POST (" + message + ")");
		QueryRunner qr = new QueryRunner(ds);
		ResultSetHandler<Message> h = new BeanHandler<Message>(Message.class);
		try {
			int n = qr.update(SQL_INSERT, message.getContent());
			if (n == 0)
				throw new NotFoundException();
			Message m = qr.query(SQL_SELECT_LAST, h);
			if (m != null)
				return m;
			else
				throw new InternalServerErrorException();
		} catch (SQLException e) {
			ctx.log(e.getMessage());
			throw new InternalServerErrorException();
		}
	}

	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Message putMessage(@Context ServletContext ctx,
			@PathParam("id") Integer id, Message message) {
		ctx.log("PUT (" + id + ", " + message + ")");
		QueryRunner qr = new QueryRunner(ds);
		ResultSetHandler<Message> h = new BeanHandler<Message>(Message.class);
		try {
			int n = qr.update(SQL_UPDATE, message.getContent(), id);
			if (n == 0)
				throw new NotFoundException();
			Message m = qr.query(SQL_SELECT_ID, h, id);
			if (m != null)
				return m;
			else
				throw new InternalServerErrorException();
		} catch (SQLException e) {
			ctx.log(e.getMessage());
			throw new InternalServerErrorException();
		}
	}

	@Path("{id}")
	@DELETE
	public void deleteMessage(@Context ServletContext ctx,
			@PathParam("id") Integer id) {
		ctx.log("DELETE (" + id + ")");
		QueryRunner qr = new QueryRunner(ds);
		try {
			int n = qr.update(SQL_DELETE, id);
			if (n > 0)
				return;
			else
				throw new NotFoundException();
		} catch (SQLException e) {
			ctx.log(e.getMessage());
			throw new InternalServerErrorException();
		}
	}
}
