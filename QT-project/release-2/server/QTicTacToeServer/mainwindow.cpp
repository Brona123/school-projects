#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QMessageBox>
#include <QList>
#include <QNetworkInterface>
#include <QNetworkAccessManager>
#include <QUrl>
#include <QNetworkRequest>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    if (!server.listen()) {
        QMessageBox::critical(this, tr("QTicTacToe Server"),
                              tr("Unable to start the server: %1.")
                              .arg(server.errorString()));
        close();
        return;
    }

    QString ipAddress;
    QList<QHostAddress> ipAddressesList = QNetworkInterface::allAddresses();

    for (int i = 0; i < ipAddressesList.size(); ++i) {
        if (ipAddressesList.at(i) != QHostAddress::LocalHost &&
            ipAddressesList.at(i).toIPv4Address()) {
            ipAddress = ipAddressesList.at(i).toString();
            break;
        }
    }

    if (ipAddress.isEmpty())
        ipAddress = QHostAddress(QHostAddress::LocalHost).toString();
    ui->IPlabel->setText(tr("The server is running on\n\nIP: %1\nport: %2\n\n"
                            "Able to accept clients now.")
                         .arg(ipAddress).arg(server.serverPort()));

    storeLocationInfo(ipAddress, server.serverPort());
}

MainWindow::~MainWindow()
{
    delete ui;
}


void MainWindow::storeLocationInfo(QString ip, quint16 port){
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    QString server = "mydb.tamk.fi";
    QString db = "dbc2sjouts1";
    QString user = "c2sjouts";
    QString pass = "tJWJ1471";

    QString request = QString("http://koti.tamk.fi/~c2sjouts/QT/QTicTacToeServer/update.php")
                                + "?server=" + server
                                + "&db="     + db
                                + "&user="   + user
                                + "&pass="   + pass
                                + "&ip="     + ip
                                + "&port="   + QString::number(port);
    QUrl url(request);
    manager->get(QNetworkRequest(url));

}
