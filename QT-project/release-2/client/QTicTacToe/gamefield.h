#ifndef GAMEFIELD_H
#define GAMEFIELD_H

#include <QWidget>
#include "home.h"
#include <QGridLayout>
#include <QHBoxLayout>
#include <QVector>
#include <QPushButton>
#include <QLineEdit>
#include <QTextBrowser>
#include <QListWidget>
#include <QTcpSocket>

namespace Ui {
class GameField;
}

class GameField : public QWidget
{
    Q_OBJECT

public:
    explicit GameField(QWidget *parent = 0);
    ~GameField();
    int getWidth();
    int getHeight();
    int getVictoryCondition();
    void setWidth(int);
    void setHeight(int);
    void setVictoryCondition(int);
    void showEvent(QShowEvent *);
    void hideEvent(QHideEvent *);
    void setDifficulty(int);
    void setMultiplayerMode(bool);
    QPoint* getButtonLocation(QPushButton*);

    //Multiplayer components
    void setSocket(QTcpSocket*);
    void setPlayers(QString, QString);
    void setPlayerID(int ID);
private slots:
    void btnClicked();
    void textSent();

    //Multiplayer components
    void textRead();

private:
    Ui::GameField *ui;
    Home *host;
    int width;
    int height;
    int victoryCondition;
    QHBoxLayout *mainLayout;
    QGridLayout *lo;
    bool playerTurn;
    void computerTurn();
    void checkWinner(QPushButton*);
    QVector<QPushButton*> *btnContainer;
    int index(int, int);
    void checkProximity(QPushButton*);
    void endGame(int);
    bool victory;
    int horizontalCount(int, int, QString);
    int verticalCount(int, int, QString);
    int diagonalCountLeftTilt(int, int, QString);
    int diagonalCountRightTilt(int, int, QString);
    QString p1mark;
    QString p2mark;
    int difficulty;
    QVector<QPoint*> *AIbtnContainer;
    int validateRow(int);
    int validateCol(int);
    void setVictoryStyle(QVector<QPushButton*>);
    QPoint getEasyPoint();
    QPoint getNormalPoint(int);
    bool multiplayerMode;
    void changeTurn(bool clicked);
    void init();
    void checkSPVictoryConditions(int row, int column);
    void clearLayout(QLayout*);

    // Multiplayer components
    void createChatComponents();
    QLineEdit *textLine;
    QTextBrowser *chatView;
    QListWidget *playerList;
    QTcpSocket *tcpSocket;
    QString player1;
    QString player2;
    int myTurn;
    int playerID;
    void sendMarkLocation(QPoint*);
    void checkMPVictoryConditions(int row, int column);


};

#endif // GAMEFIELD_H
