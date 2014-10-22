#ifndef SINGLEPLAYERSETUP_H
#define SINGLEPLAYERSETUP_H

#include <QWidget>
#include "home.h"

namespace Ui {
class SingleplayerSetup;
}

class SingleplayerSetup : public QWidget
{
    Q_OBJECT

public:
    explicit SingleplayerSetup(QWidget *parent = 0);
    ~SingleplayerSetup();

    static const int EASY = 1;
    static const int NORMAL = 2;
private slots:
    void on_mainMenuButton_clicked();
    void on_startButton_clicked();

private:
    Ui::SingleplayerSetup *ui;
    Home *host;
};

#endif // SINGLEPLAYERSETUP_H
