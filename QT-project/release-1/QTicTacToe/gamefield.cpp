#include "gamefield.h"
#include "ui_gamefield.h"
#include <QDebug>
#include <QHBoxLayout>
#include <QPushButton>
#include <QSizePolicy>
#include <QMessageBox>
#include <QFormLayout>
#include <QScrollArea>

GameField::GameField(QWidget *parent) :
    QWidget(parent)
{
    this->host = dynamic_cast<Home*>(parent);
    mainLayout = new QHBoxLayout(this);
    setLayout(mainLayout);
    playerTurn = true;
    p1mark = "X";
    p2mark = "O";
}

GameField::~GameField()
{
    delete ui;
}

int GameField::getWidth(){
    return this->width;
}

int GameField::getHeight(){
    return this->height;
}

int GameField::getVictoryCondition(){
    return this->victoryCondition;
}

void GameField::setWidth(int width){
    this->width = width;
}

void GameField::setHeight(int height){
    this->height = height;
}

void GameField::setVictoryCondition(int vc){
    this->victoryCondition = vc;
}

void GameField::showEvent(QShowEvent *event){
    btnContainer = new QVector<QPushButton*> (width * height);
    lo = new QGridLayout();
    lo->setSpacing(0);

    QPushButton *tempButton = new QPushButton(this);

    QSizePolicy sizePolicy(QSizePolicy::Minimum, QSizePolicy::Minimum);
    sizePolicy.setHorizontalStretch(0);
    sizePolicy.setVerticalStretch(0);
    sizePolicy.setHeightForWidth(tempButton->sizePolicy().hasHeightForWidth());

    for (int rows = 0; rows < height; rows++){
        for (int columns = 0; columns < width; columns++){
            qDebug() << "Cols: " << columns << " Rows: " << rows;
            QString txt = " ";
            QPushButton *btn = new QPushButton(txt, this);
            btn->setSizePolicy(sizePolicy);
            QObject::connect(btn, SIGNAL(clicked()), this, SLOT(btnClicked()));
            lo->addWidget(btn, columns, rows);
            (*btnContainer)[index(rows, columns)] = btn;
        }
    }
    mainLayout->addLayout(lo);

}

void GameField::btnClicked(){
    if (playerTurn){
        QPushButton *temp = dynamic_cast<QPushButton*>(sender());
        if (temp->text() == " "){
            temp->setText(p1mark);
            checkWinner(temp);
            playerTurn = false;
            computerTurn();
        }
    }
}

void GameField::computerTurn(){
    bool legit = false;

    while(!legit){
        int col = qrand() % height;
        int row = qrand() % width;
        qDebug() << "Col: " << col << " Row: " << row;
        qDebug() << lo->itemAtPosition(col, row);
        QLayoutItem *item = lo->itemAtPosition(col, row);
        QPushButton *btn = dynamic_cast<QPushButton*>(item->widget());
        if (btn->text() == " "){
            btn->setText(p2mark);
            checkWinner(btn);
            playerTurn = true;
            legit = true;
        }
    }
}

void GameField::checkWinner(QPushButton *btn){
    checkProximity(btn);

    bool full = true;
    for (int i = 0; i < width; ++i){
        for (int j = 0; j < height; ++j){
            if ((*btnContainer)[index(i, j)]->text() == " "){
                full = false;
            }
        }
    }
    if (full){
        endGame(-1);
    }
}

int GameField::index(int x, int y){
    return x + width * y;
}

void GameField::checkProximity(QPushButton *btn){
    int row;
    int column;

    for (int i = 0; i < width; ++i){
        for (int j = 0; j < height; ++j){
            if ((*btnContainer)[index(i, j)] == btn){
                row = i;
                column = j;
            }
        }
    }

    int hc;
    int vc;
    int dcl;
    int dcr;

    if (playerTurn){
        hc = horizontalCount(row, column, p1mark);
        vc = verticalCount(row, column, p1mark);
        dcl = diagonalCountLeftTilt(row, column, p1mark);
        dcr = diagonalCountRightTilt(row, column, p1mark);
    } else {
        hc = horizontalCount(row, column, p2mark);
        vc = verticalCount(row, column, p2mark);
        dcl = diagonalCountLeftTilt(row, column, p2mark);
        dcr = diagonalCountRightTilt(row, column, p2mark);
    }

    if (hc >= victoryCondition
        || vc >= victoryCondition
        || dcl >= victoryCondition
        || dcr >= victoryCondition){

        if (playerTurn){
            endGame(1);
        } else {
            endGame(0);
        }
    }

    qDebug() << "Horizonal count: " << hc << " Vertical count: " << vc
             << "Diagonal count left: " << dcl << "Diagonal count right: " << dcr;
}

int GameField::verticalCount(int row, int column, QString mark){
    int counter = -1;
    for (int i = column; i >= 0; --i){
        if ((*btnContainer)[index(row, i)]->text() == mark){
            counter++;
        } else {
            break;
        }
    }

    for (int i = column; i < width; ++i){
        if ((*btnContainer)[index(row, i)]->text() == mark){
            counter++;
        } else {
            break;
        }
    }

    return counter;
}

int GameField::horizontalCount(int row, int column, QString mark){
    int counter = -1;
    for (int i = row; i >= 0; --i){
        if ((*btnContainer)[index(i, column)]->text() == mark){
            counter++;
        } else {
            break;
        }
    }

    for (int i = row; i < height; ++i){
        if ((*btnContainer)[index(i, column)]->text() == mark){
            counter++;
        } else {
            break;
        }
    }

    return counter;
}

int GameField::diagonalCountLeftTilt(int row, int column, QString mark){
    int counter = -1;

    int j = column;
    for (int i = row; i >= 0; --i){
        if (j >= 0){
            if ((*btnContainer)[index(i, j)]->text() == mark){
                counter++;
            } else {
                break;
            }
            j--;
        }
    }

    j = column;
    for (int i = row; i < height; ++i){
        if (j < width){
            if ((*btnContainer)[index(i, j)]->text() == mark){
                counter++;
            } else {
                break;
            }
            j++;
        }
    }

    return counter;
}

int GameField::diagonalCountRightTilt(int row, int column, QString mark){
    int counter = -1;

    int j = column;
    for (int i = row; i >= 0; --i){
        if (j < width){
            if ((*btnContainer)[index(i, j)]->text() == mark){
                counter++;
            } else {
                break;
            }
            j++;
        }
    }

    j = column;
    for (int i = row; i < height; ++i){
        if (j >= 0){
            if ((*btnContainer)[index(i, j)]->text() == mark){
                counter++;
            } else {
                break;
            }
            j--;
        }
    }

    return counter;
}

void GameField::endGame(int winner){
    QString result = "";

    if (winner == 1){
        result = tr("Congratulations! You're winner!");
    } else if (winner == -1){
        result = tr("Draw!");
    } else {
        result = tr("You lost!");
    }

    QMessageBox msgBox;
    msgBox.setText(result);
    victory = true;
    msgBox.exec();
    exit(0);
}
