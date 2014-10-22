#ifndef GAMEFIELD_H
#define GAMEFIELD_H

#include <QWidget>
#include "home.h"
#include <QGridLayout>
#include <QHBoxLayout>
#include <QVector>
#include <QPushButton>

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

private slots:
    void btnClicked();

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
};

#endif // GAMEFIELD_H
