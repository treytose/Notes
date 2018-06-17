import pyautogui as pag
import time

pag.FAILSAFE = True

mode = input('Play or Record? r/p')
if mode == 'r':
    f = open('record.txt', 'w+')
    while True:
        f.write(str(pag.position()[0]) + ',' + str(pag.position()[1]) + '\n')
        time.sleep(0.01)

elif mode == 'p':
    f = open('record.txt', 'r')
    for line in f.readlines():
        coords = line.split(',')
        pag.moveTo(int(coords[0]), int(coords[1]))
