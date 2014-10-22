#ifndef MULTIPLAYERSETUP_H
#define MULTIPLAYERSETUP_H

#include <QWidget>
#include <QTcpSocket>
#include <QNetworkSession>
#include <QNetworkReply>
#include "home.h"

namespace Ui {
class MultiplayerSetup;
}

class MultiplayerSetup : public QWidget
{
    Q_OBJECT

public:
    explicit MultiplayerSetup(QWidget *parent = 0);
    ~MultiplayerSetup();
    void showEvent(QShowEvent *);

private slots:
    void displayError(QAbstractSocket::SocketError);
    void on_pushButton_clicked();
    void requestFinished(QNetworkReply*);
    void clientConnected();

private:
    Ui::MultiplayerSetup *ui;
    QTcpSocket *tcpSocket;
    QNetworkSession *networkSession;
    Home *host;
};

#endif // MULTIPLAYERSETUP_H
