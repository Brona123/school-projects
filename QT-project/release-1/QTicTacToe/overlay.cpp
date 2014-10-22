#include "overlay.h"
#include <QPainter>
#include <QPen>

Overlay::Overlay(QWidget *parent) :
    QWidget(parent)
{
    setPalette(Qt::transparent);
    setAttribute(Qt::WA_TransparentForMouseEvents);
}

void Overlay::paintEvent(QPaintEvent *event){
    QPainter painter(this);
    painter.setRenderHint(QPainter::Antialiasing);
    painter.setPen(QPen(Qt::red));
    painter.drawLine(QPoint(0, 0), QPoint(900, 900));
}
