#include "home.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    Home h;
    h.show();

    return a.exec();
}
