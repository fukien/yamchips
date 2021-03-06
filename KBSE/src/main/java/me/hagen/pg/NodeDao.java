package me.hagen.pg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NodeDao {

	public void add(Connection conn, Node n) throws SQLException{
		String sql = "insert into node(id,name,pscore,pscore2,diverse,wsum,wsum2) values(?,?,?,?,?)";
		PreparedStatement psmt = conn.prepareStatement(sql);
		psmt.setString(1, n.getId());
		psmt.setString(2, n.getName());
		psmt.setDouble(3, n.getPscore());
		psmt.setDouble(4, n.getPscore2());
		psmt.setDouble(5, n.getDiverse());
		psmt.setDouble(6, n.getWsum());
		psmt.setDouble(7, n.getWsum2());
		psmt.executeUpdate();
		psmt.close();
	}
	public void remove(Connection conn, Node n) throws SQLException{
		String sql = "delete from node where id = ?";
		PreparedStatement psmt = conn.prepareStatement(sql);
		psmt.setString(1, n.getId());
		psmt.executeUpdate();
		psmt.close();
	}
	public void updatePrank(Connection conn,String id, double prank) throws SQLException{
		PreparedStatement psmt = conn.prepareStatement("update node set pscore=? where id =?");
		psmt.setDouble(1, prank);
		psmt.setString(2, id);
		psmt.executeUpdate();
	}
	public void updateDiversePrank(Connection conn,String id, double prank) throws SQLException{
		PreparedStatement psmt = conn.prepareStatement("update node set pscore2=? where id =?");
		psmt.setDouble(1, prank);
		psmt.setString(2, id);
		psmt.executeUpdate();
	}
	public Node map(ResultSet rs) throws SQLException{
		Node n = new Node();
		n.setId(rs.getString("id"));
		n.setName(rs.getString("name"));
		n.setPscore(rs.getDouble("pscore"));
		n.setPscore2(rs.getDouble("pscore2"));
		n.setDiverse(rs.getDouble("diverse"));
		n.setWsum(rs.getDouble("wsum"));
		n.setWsum2(rs.getDouble("wsum2"));
		return n;
	}
	public Node get(Connection conn, String id){
		String sql = "select id,name,pscore,pscore2,diverse,wsum,wsum2 from node where id = ?";
		try(PreparedStatement psmt = conn.prepareStatement(sql)){
			psmt.setString(1, id);
			try(ResultSet rs = psmt.executeQuery()){
				while(rs.next()){
					return map(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<Node> prefixMatch(Connection conn,String name,int top) throws SQLException{
		String sql = "select id,name,pscore,pscore2,diverse,wsum,wsum2 from node where name like '"+name+"%' order by pscore desc limit "+top;
		List<Node> nodeList = new ArrayList<Node>();
		try(PreparedStatement psmt = conn.prepareStatement(sql)){
			try(ResultSet rs = psmt.executeQuery()){
				while(rs.next()){
					nodeList.add(map(rs));
				}
			}
		}
		return nodeList;
	}
}
