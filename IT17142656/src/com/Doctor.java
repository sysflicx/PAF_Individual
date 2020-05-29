/*IT17142656 Madushanka R.M.J
*/
package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Doctor {

	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Dcotor?serverTimezone=UTC", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	public String readDoctor() {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Can not connect to the database.";
			}

			// Prepare the html table to be displayed
			output = "<table border='2'>" + "<tr><th>Full Name</th>" + "<th>Department</th>"
					+ "<th>Address</th>" + "<th>Phone Number</th>" + "<th>Update</th>" + "<th>Remove</th></tr>";

			String query = "select * from Doctor";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			// iterate through the rows in the result set
			while (rs.next()) {
				String docotorID = Integer.toString(rs.getInt("doctorID"));
				String name = rs.getString("name");
				String dep = rs.getString("dep");
				String address = rs.getString("address");
				String number = rs.getString("number");

				// Add into the html table
				output += "<tr><td><input id='hidItemIDUpdate'name='hidItemIDUpdate' type='hidden' value='" + docotorID
						+ "'>" + name + "</td>";
				output += "<td>" + dep + "</td>";
				output += "<td>" + address + "</td>";
				output += "<td>" + number + "</td>";

				// buttons
				output += "<td><input name='btnUpdate'type='button' "
						+ "value='Update'class='btnUpdate btn btn-secondary'></td>"
						+ "<td><input name='btnRemove'type='button' "
						+ "value='Remove'class='btnRemove btn btn-danger'data-itemid='" + docotorID + "'>"
						+ "</td></tr>";
			}

			con.close();

			// Complete the html table
			output += "</table>";

		} catch (Exception e) {
			output = "Error while reading the Doctor Info.";
			System.err.println(e.getMessage());
		}

		return output;
	}

	
	public String insertDoctor(String name, String dep, String address, String number) {
		String output = "";
		try {
			Connection con = connect();

			if (con == null) {
				return "Can not connect to the database for inserting info.";
			}

			// create a prepared statement
			String query = " insert into doctor(`doctorID`,`name`,`dep`,`address`,`number`) values (?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, name);
			preparedStmt.setString(3, dep);
			preparedStmt.setString(4, address);
			preparedStmt.setString(5, number);

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newItems = readDoctor();
			output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Can not insert the doctor info.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	
	public String updateDoctor(String doctorID, String name, String dep, String address, String number) {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Can not connect to the database for update payment info.";
			}

			// create a prepared statement
			String query = "UPDATE doctor SET name=?,dep=?,address=?,cvc=? WHERE DoctorID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setString(1, name);
			preparedStmt.setString(2, dep);
			preparedStmt.setString(3, address);
			preparedStmt.setString(4, number);
			preparedStmt.setInt(5, Integer.parseInt(doctorID));

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newItems = readDoctor();
			output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Can not update the docotor info.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	
	public String deletedoctor(String doctorID) {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Can not connect to the database for delete doctor info.";
			}

			// create a prepared statement
			String query = "delete from doctor where doctorID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, Integer.parseInt(doctorID));

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newItems = readDoctor();
			output = "{\"status\":\"success\", \"data\": \"" + newItems + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Can not delete the doctor info.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}
}
