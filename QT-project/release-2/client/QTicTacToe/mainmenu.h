#ifndef MAINMENU_H
#define MAINMENU_H

#include <QMainWindow>
#include "home.h"

namespace Ui {
class MainMenu;
}

class MainMenu : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainMenu(QWidget *parent = 0);
    ~MainMenu();

private slots:
    void on_exitButton_clicked();

    void on_spButton_clicked();

    void on_mpButton_clicked();

private:
    Ui::MainMenu *ui;
    Home *host;
};

#endif // MAINMENU_H
