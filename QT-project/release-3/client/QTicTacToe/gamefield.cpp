#include "gamefield.h"
#include "ui_gamefield.h"
#include <QHBoxLayout>
#include <QPushButton>
#include <QSizePolicy>
#include <QMessageBox>
#include <QFormLayout>
#include <QScrollArea>
#include <QTime>
#include <QSplitter>
#include "singleplayersetup.h"

GameField::GameField(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::GameField)
{
    this->host = dynamic_cast<Home*>(parent);
    QTime time = QTime::currentTime();
    qsrand((uint)time.msec());
    ui->setupUi(this);
    multiplayerMode = false;
    tcpSocket = NULL;
}

GameField::~GameField()
{
    delete ui;
}

void GameField::init(){
    victory = false;
    playerTurn = true;
    p1mark = "X";
    p2mark = "O";
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

void GameField::showEvent(QShowEvent*){
    init();
    clearLayout(ui->gridLayout);
    btnContainer = new QVector<QPushButton*>;
    btnContainer->clear();
    ui->buttonLayout->setSpacing(0);

    QPushButton *tempButton = new QPushButton(this);

    QSizePolicy sizePolicy(QSizePolicy::Minimum, QSizePolicy::Minimum);
    sizePolicy.setHorizontalStretch(0);
    sizePolicy.setVerticalStretch(0);
    sizePolicy.setHeightForWidth(tempButton->sizePolicy().hasHeightForWidth());


    for (int rows = 0; rows < height; ++rows){
        for (int columns = 0; columns < width; ++columns){
            QPushButton *btn = new QPushButton(" ", this);
            btn->setSizePolicy(sizePolicy);
            QObject::connect(btn, SIGNAL(clicked()), this, SLOT(btnClicked()));
            ui->buttonLayout->addWidget(btn, rows, columns);
            btnContainer->push_back(btn);
        }
    }
    ui->gridLayout->addLayout(ui->buttonLayout, 0, 0);

    if (multiplayerMode){
        createChatComponents();
        myTurn = 1;
    }
}

void GameField::hideEvent(QHideEvent*){
    if (tcpSocket != NULL){
        tcpSocket->disconnect(tcpSocket, SIGNAL(readyRead()),
                              this,      SLOT(textRead()));
    }
}

void GameField::clearLayout(QLayout *layout){
    QLayoutItem *child;
    while ((child = layout->takeAt(0)) != 0) {
        QWidget* widget;
        if (widget = child->widget()){
            delete widget;
        }

        if (QLayout *childLayout = child->layout()){
            clearLayout(childLayout);
        }

    }
}

void GameField::btnClicked(){
    if (multiplayerMode){
        if (myTurn == playerID){
            QPushButton *temp = dynamic_cast<QPushButton*>(sender());

            if (temp->text() == " "){
                QString mark;
                playerID == 1 ? mark = p1mark : mark = p2mark;
                temp->setText(mark);
                sendMarkLocation(getButtonLocation(temp));
                checkWinner(temp);
                changeTurn();
            }
        }
    } else {
        AIbtnContainer = new QVector<QPoint*>();

        if (playerTurn){
            QPushButton *temp = dynamic_cast<QPushButton*>(sender());
            if (temp->text() == " "){
                temp->setText(p1mark);
                QPoint *btnLocation = getButtonLocation(temp);
                AIbtnContainer->append(btnLocation);
                checkWinner(temp);
                playerTurn = false;

                if (!victory){
                    computerTurn();
                }
            }
        }
    }
}

void GameField::sendMarkLocation(QPoint *p){
    QString locationPrefix = "/Location::";
    tcpSocket->write(locationPrefix.toLatin1()
                     + player2.toLatin1()
                     + "|"
                     + QString::number(p->x()).toLatin1()
                     + "|"
                     + QString::number(p->y()).toLatin1());
}

void GameField::changeTurn(){
    if (myTurn == 1){
        myTurn = 2;
    } else {
        myTurn = 1;
    }
}

QPoint* GameField::getButtonLocation(QPushButton *btn){
    for (int i = 0; i < height; ++i){
        for (int j = 0; j < width; ++j){
            if ((*btnContainer)[index(i, j)] == btn){
                return new QPoint(i, j);
            }
        }
    }
    return NULL;
}

void GameField::computerTurn(){
    bool legit = false;

    int AIradiusLevel = 1;  // Defines how large is the range of randomizable locations for AI.
    int cycleAmount = 0;    // How many times the while loop has repeated.
    const int THRESHOLD = 9; // Defines how many times to loop before increasing the radius.
    while(!legit){

        QLayoutItem *item;
        QPushButton *btn;
        QPoint randomizedPoint;
        if (difficulty == SingleplayerSetup::EASY){
            randomizedPoint = getEasyPoint();
        } else if (difficulty == SingleplayerSetup::NORMAL){
            randomizedPoint = getNormalPoint(AIradiusLevel);
        }
        item = ui->buttonLayout->itemAtPosition(randomizedPoint.x(),
                                                randomizedPoint.y());
        btn = dynamic_cast<QPushButton*>(item->widget());

        if (btn->text() == " "){
            btn->setText(p2mark);
            checkWinner(btn);
            playerTurn = true;
            legit = true;
        }
        cycleAmount++;
        if (cycleAmount == THRESHOLD){
            AIradiusLevel++;
            cycleAmount = 0;
        }
    }
}

QPoint GameField::getNormalPoint(int AIradiusLevel){
    int x = AIbtnContainer->at(0)->x();
    int col = ((qrand() % (3 * AIradiusLevel)) - (1 * AIradiusLevel));

    int y = AIbtnContainer->at(0)->y();
    int row = ((qrand() % (3 * AIradiusLevel)) - (1 * AIradiusLevel));

    return QPoint(validateRow(x + col),
                  validateCol(y + row));
}

QPoint GameField::getEasyPoint(){
    return QPoint(qrand() % height,
                  qrand() % width);
}

int GameField::validateRow(int x){
    if (x < 0){
        return 0;
    } else if (x >= height){
        return height-1;
    } else {
        return x;
    }
}

int GameField::validateCol(int y){
    if (y < 0){
        return 0;
    } else if (y >= width){
        return width-1;
    } else {
        return y;
    }
}

void GameField::checkWinner(QPushButton *btn){
    checkProximity(btn);

    if (!victory){
        bool full = true;
        for (int i = 0; i < height; ++i){
            for (int j = 0; j < width; ++j){
                if ((*btnContainer)[index(i, j)]->text() == " "){
                    full = false;
                }
            }
        }

        if (full){
            endGame(-1);
        }
    }
}

int GameField::index(int y, int x){
    return x + width * y;
}

void GameField::checkProximity(QPushButton *btn){
    int row;
    int column;

    for (int i = 0; i < height; ++i){
        for (int j = 0; j < width; ++j){
            if ((*btnContainer)[index(i, j)] == btn){
                row = i;
                column = j;
            }
        }
    }

    if (!multiplayerMode){
        checkSPVictoryConditions(row, column);
    } else {
        checkMPVictoryConditions(row, column);
    }
}

void GameField::checkSPVictoryConditions(int row, int column){
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
}

void GameField::checkMPVictoryConditions(int row, int column){
    int hc;
    int vc;
    int dcl;
    int dcr;

    if (playerID == 1){
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

        if (playerID == myTurn){
            endGame(1);
        } else {
            endGame(0);
        }
    }
}

int GameField::horizontalCount(int row, int column, QString mark){
    int counter = -1;

    QVector<QPushButton*> tempContainer;
    for (int i = column; i >= 0; --i){
        if ((*btnContainer)[index(row, i)]->text() == mark){
            counter++;
            tempContainer.push_back((*btnContainer)[index(row, i)]);
        } else {
            break;
        }
    }

    for (int i = column; i < width; ++i){
        if ((*btnContainer)[index(row, i)]->text() == mark){
            counter++;
            tempContainer.push_back((*btnContainer)[index(row, i)]);
        } else {
            break;
        }
    }

    if (counter >= victoryCondition){
        setVictoryStyle(tempContainer);
    }

    return counter;
}

void GameField::setVictoryStyle(QVector<QPushButton *> cont){
    for (int i = 0; i < cont.size(); ++i){
        cont.at(i)->setStyleSheet("color: rgb(255, 0, 0);");
    }
}

int GameField::verticalCount(int row, int column, QString mark){
    int counter = -1;
    QVector<QPushButton*> tempContainer;
    for (int i = row; i >= 0; --i){
        if ((*btnContainer)[index(i, column)]->text() == mark){
            counter++;
            tempContainer.push_back((*btnContainer)[index(i, column)]);
        } else {
            break;
        }
    }

    for (int i = row; i < height; ++i){
        if ((*btnContainer)[index(i, column)]->text() == mark){
            counter++;
            tempContainer.push_back((*btnContainer)[index(i, column)]);
        } else {
            break;
        }
    }

    if (counter >= victoryCondition){
        setVictoryStyle(tempContainer);
    }
    return counter;
}

int GameField::diagonalCountLeftTilt(int row, int column, QString mark){
    int counter = -1;

    QVector<QPushButton*> tempContainer;
    int j = column;
    for (int i = row; i >= 0; --i){
        if (j >= 0){
            if ((*btnContainer)[index(i, j)]->text() == mark){
                counter++;
                tempContainer.push_back((*btnContainer)[index(i, j)]);
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
                tempContainer.push_back((*btnContainer)[index(i, j)]);
            } else {
                break;
            }
            j++;
        }
    }

    if (counter >= victoryCondition){
        setVictoryStyle(tempContainer);
    }
    return counter;
}

int GameField::diagonalCountRightTilt(int row, int column, QString mark){
    int counter = -1;

    QVector<QPushButton*> tempContainer;
    int j = column;
    for (int i = row; i >= 0; --i){
        if (j < width){
            if ((*btnContainer)[index(i, j)]->text() == mark){
                counter++;
                tempContainer.push_back((*btnContainer)[index(i, j)]);
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
                tempContainer.push_back((*btnContainer)[index(i, j)]);
            } else {
                break;
            }
            j--;
        }
    }

    if (counter >= victoryCondition){
        setVictoryStyle(tempContainer);
    }
    return counter;
}

void GameField::endGame(int winner){
    QString result = "";

    if (winner == 1){
        result = tr("Congratulations! You're winner!");
        if (multiplayerMode){
            tcpSocket->write("/Winner::" + QString::number(playerID).toLatin1());
        }
    } else if (winner == -1){
        result = tr("Draw!");
    } else {
        result = tr("You lost!");
    }

    QMessageBox::information(this
                             , "Game Over"
                             , result);
    victory = true;
    if (multiplayerMode){
        host->setWidget(Home::LOBBY_WINDOW);
    } else {
        host->setWidget(Home::MAIN_MENU_WINDOW);
    }
}

void GameField::setDifficulty(int diff){
    this->difficulty = diff;
}

void GameField::setMultiplayerMode(bool flag){
    multiplayerMode = flag;
}

void GameField::createChatComponents(){
    textLine = new QLineEdit();
    chatView = new QTextBrowser();
    playerList = new QListWidget();

    playerList->addItem(player1);
    playerList->item(0)->setForeground(QColor(0, 0, 220));
    playerList->addItem(player2);

    connect(textLine, SIGNAL(returnPressed()),
            this,     SLOT(textSent()));

    QSplitter *verticalSplitter = new QSplitter();
    verticalSplitter->setOrientation(Qt::Vertical);
    verticalSplitter->addWidget(chatView);
    verticalSplitter->addWidget(textLine);


    QSplitter *horizontalSplitter = new QSplitter(this);
    horizontalSplitter->addWidget(verticalSplitter);
    horizontalSplitter->addWidget(playerList);

    ui->gridLayout->addWidget(horizontalSplitter);
}

void GameField::textSent(){
    QString multiplayerMatchPrefix = "/MultiplayerMatch::";
    QString line = textLine->text();

    if (!line.startsWith(("/"))){
        chatView->append("<" + player1.toLatin1() + "> " + line);
        tcpSocket->write(multiplayerMatchPrefix.toLatin1()
                         + QString::number(player2.length()).toLatin1()
                         + player2.toLatin1()
                         + "<"
                         + player1.toLatin1()
                         + "> "
                         + textLine->text().toLatin1());
        textLine->clear();
    }
}

void GameField::setSocket(QTcpSocket *socket){
    this->tcpSocket = socket;

    connect(tcpSocket, SIGNAL(readyRead()),
            this,      SLOT(textRead()));
}

void GameField::textRead(){
    QString text = tcpSocket->readAll();
    QString locationPrefix = "/Location::";

    if (text.startsWith(locationPrefix)){
        QStringRef values(&text, locationPrefix.length(), text.length() - locationPrefix.length());
        QStringList valueArray = values.toString().split("|");
        QString name = valueArray[0];

        if (name == playerList->item(0)->text()){
            changeTurn();
            QLayoutItem *item = ui->buttonLayout->itemAtPosition(valueArray[1].toInt(),
                                                                 valueArray[2].toInt());
            QPushButton *btn = dynamic_cast<QPushButton*>(item->widget());

            if (playerID == 1){
                btn->setText(p2mark);
            } else {
                btn->setText(p1mark);
            }
        }
        QString victoryPrefix = "/Winner::";
        if (text.contains(victoryPrefix)){
            QString winner = text.at(text.length()-1);
            if (winner.toInt() != playerID){
                endGame(0);
            }
        }
    } else {
        chatView->append(text);
    }
}

void GameField::setPlayers(QString p1, QString p2){
    player1 = p1;
    player2 = p2;
}

void GameField::setPlayerID(int ID){
    this->playerID = ID;
}
