#ifndef SERVER_H
#define SERVER_H

#include <QTcpServer>
#include <QList>
#include <QTcpSocket>

class Server : public QTcpServer
{
    Q_OBJECT
public:
    explicit Server(QObject *parent = 0);
    void broadcastToAllThreads(QByteArray);
signals:
    void textReceived(QByteArray);
public slots:
    void disconnected();
    void textRead();
    void connectionReceived();
protected:

private:
    QList<QTcpSocket*> *clientContainer;
    QList<QString> *nickContainer;
    void sendClientList();
    void sendChallenge(QTcpSocket*, QStringRef);
    void sendChallengeAccepted(QStringRef);
    void sendSettingsData(QString);
    void sendStartMessage(QStringRef);
    void sendTurnChangeMessage(QStringRef);
    void sendLocationMessage(QStringRef);
    QString isAvailable(QString);
    void sendPrivateMessage(QString, QString);
};

#endif // SERVER_H
