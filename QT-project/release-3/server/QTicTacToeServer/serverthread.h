#ifndef SERVERTHREAD_H
#define SERVERTHREAD_H

#include <QThread>
#include <QTcpSocket>
#include <QVector>

class ServerThread : public QThread
{
    Q_OBJECT
public:
    explicit ServerThread(int socketDescriptor, QObject *parent = 0);

    void run();
    void sendText(QByteArray text);
signals:
    void error(QTcpSocket::SocketError socketError);

public slots:

private slots:
    void textRead();
    void disconnected();

private:
    int socketDescriptor;
    QTcpSocket *tcpSocket;
    QObject *parent;
    QThread *clientThread;
};

#endif // SERVERTHREAD_H
