package com.example.tecto.bgcard.data;

import android.util.Log;

import com.example.tecto.bgcard.data.model.Card;
import com.example.tecto.bgcard.data.model.CardTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static android.content.ContentValues.TAG;

public class MySQLAccess {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultCardHistory = null;
    private ResultSet resultCardHistoryAPI = null;
    private ResultSet resultCardTrue = null;

    public MySQLAccess() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties info = new Properties();
            info.put("user", "root");
            info.put("password", "");

            connect = DriverManager
                    .getConnection("jdbc:mysql://10.0.2.2:3306/shopdoithe_new",
                            info);
            statement = connect.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Card> readCardHistory() throws Exception {
        try {
            resultCardHistory = statement
                    .executeQuery("select * from card_history where status = 69");
            return writeResultSetCardHistory(resultCardHistory);
        } finally {
            close();
        }

    }

    public List<Card> readCardHistoryApi() throws Exception {
        try {
            resultCardHistoryAPI = statement
                    .executeQuery("select * from card_history_api where status = 69");
            return writeResultSetCardHistoryApi(resultCardHistoryAPI);
        } finally {
            close();
        }

    }

    public List<CardTrue> readCardTrue() throws Exception {
        try {
            resultCardTrue = statement
                    .executeQuery("select * from card_true where status = 1");
            return writeResultSetCardTrue(resultCardTrue);
        } finally {
            close();
        }

    }

    public void updateCardHistory(int status, int status_nap, int status_app, int card_history_id) {
        try {
            preparedStatement = connect.prepareStatement("update card_history set status = ?, status_nap = ?, status_app = ? where card_history_id = ?");
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(1, status_app);
            preparedStatement.setInt(1, status_nap);
            preparedStatement.setInt(1, card_history_id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCardHistoryApi(int status, int status_nap, int status_app, int card_history_id) {
        try {
            preparedStatement = connect.prepareStatement("update card_history_api set status = ?, status_nap = ?, status_app = ? where card_history_id = ?");
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(1, status_app);
            preparedStatement.setInt(1, status_nap);
            preparedStatement.setInt(1, card_history_id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCardTrue(int status, int date_check, int card_true_id) {
        try {
            preparedStatement = connect.prepareStatement("update card_true set status = ?, date_check = ? where card_true_id = ?");
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(1, date_check);
            preparedStatement.setInt(1, card_true_id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Card> writeResultSetCardHistory(ResultSet resultCardHistory) throws SQLException {
        List<Card> cardList = new ArrayList<>();
        while (resultCardHistory.next()) {
            Card card = new Card();
            card.setCard_history_id(resultCardHistory.getInt("card_history_id"));
            card.setCardnumber(resultCardHistory.getString("cardnumber"));
            card.setStatus(resultCardHistory.getInt("status"));
            card.setStatus_nap(resultCardHistory.getInt("status_nap"));
            card.setStatus_app(resultCardHistory.getInt("status_app"));
            cardList.add(card);
            Log.e(TAG, "writeResultSet: " + card.getCardnumber());
        }
        return cardList;
    }

    private List<Card> writeResultSetCardHistoryApi(ResultSet resultCardHistoryApi) throws SQLException {
        List<Card> cardList = new ArrayList<>();
        while (resultCardHistoryApi.next()) {
            Card card = new Card();
            card.setCard_history_id(resultCardHistoryApi.getInt("card_history_id"));
            card.setCardnumber(resultCardHistoryApi.getString("cardnumber"));
            card.setStatus(resultCardHistoryApi.getInt("status"));
            card.setStatus_nap(resultCardHistoryApi.getInt("status_nap"));
            card.setStatus_app(resultCardHistoryApi.getInt("status_app"));
            cardList.add(card);
            Log.e(TAG, "writeResultSet: " + card.getCardnumber());
        }
        return cardList;
    }

    private List<CardTrue> writeResultSetCardTrue(ResultSet resultCardTrue) throws SQLException {
        List<CardTrue> cardList = new ArrayList<>();
        while (resultCardTrue.next()) {
            CardTrue card = new CardTrue();
            card.setCard_true_id(resultCardTrue.getInt("card_true_id"));
            card.setCardnumber(resultCardTrue.getString("cardnumber"));
            card.setDate_check(resultCardTrue.getInt("date_check"));
            card.setStatus(resultCardTrue.getInt("status"));
            cardList.add(card);
            Log.e(TAG, "writeResultSet: " + card.getCardnumber());
        }
        return cardList;
    }

    private void close() {
        try {
            if (resultCardHistory != null) {
                resultCardHistory.close();
            }

            if (resultCardHistoryAPI != null) {
                resultCardHistoryAPI.close();
            }

            if (resultCardTrue != null) {
                resultCardTrue.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "close: " + e.getMessage());
        }
    }
}
