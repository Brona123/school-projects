#ifndef MULTIPLAYERSETTINGS_H
#define MULTIPLAYERSETTINGS_H

#include <QWidget>
#include <QTcpSocket>
#include "home.h"

namespace Ui {
class MultiplayerSettings;
}

class MultiplayerSettings : public QWidget
{
    Q_OBJECT

public:
    explicit MultiplayerSettings(QWidget *parent = 0);
    ~MultiplayerSettings();
    void setSocket(QTcpSocket*);
    void setPlayers(QString, QString);
    static const int PLAYER_ONE = 1;
    static const int PLAYER_TWO = 2;
    void setID(int);
    void showEvent(QShowEvent *);
    void hideEvent(QHideEvent *);
signals:
    void settingsChanged();

private slots:
    void on_widthBox_currentTextChanged(const QString &arg1);
    void on_heightBox_currentTextChanged(const QString &arg1);
    void on_vcBox_currentTextChanged(const QString &arg1);
    void sendNewSettings();
    void textRead();
    void on_lineEdit_returnPressed();
    void on_goButton_clicked();

private:
    Ui::MultiplayerSettings *ui;
    QTcpSocket *tcpSocket;
    void changeSettings(QString);
    void startGame();
    Home *host;
    QString player1;
    QString player2;
    int myID;
};

#endif // MULTIPLAYERSETTINGS_H
