#ifndef LOBBY_H
#define LOBBY_H

#include <QWidget>
#include <QTcpSocket>
#include "home.h"

namespace Ui {
class Lobby;
class multiplayersetup;
}

class Lobby : public QWidget
{
    Q_OBJECT

public:
    explicit Lobby(QWidget *parent = 0);
    ~Lobby();
    void setSocket(QTcpSocket*);
    void setPlayer(QString);
    void showEvent(QShowEvent *);
    void hideEvent(QHideEvent *);
private:
    Ui::Lobby *ui;
    Home *host;
    QTcpSocket *tcpSocket;
    QString player;
    QString challengedPlayer;
    void setLobbyPlayerList(QString, QString);
    void announceDisconnection(QString, QString);
    void challengeReceived(QString, QString);
    void createSettingsMenu(QString, int ID);
private slots:
    void textRead();
    void on_lineEdit_returnPressed();
    void on_challengeButton_clicked();
    void on_pushButton_clicked();
};

#endif // LOBBY_H
