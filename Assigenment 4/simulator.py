import sqlite3

import os

dbcon = sqlite3.connect('world.db')
cursor = dbcon.cursor()
tasknotempty = True
while tasknotempty and os.path.isfile('world.db') :
    cursor.execute("SELECT * FROM tasks");
    taskslist = cursor.fetchall()
    stringworkerlist = ""*len(cursor.execute("SELECT * FROM workers"))
    tasknotempty = False
    for task in taskslist:
        tasknotempty = True
        cursor.execute("SELECT name FROM workers WHERE id=(?)", task.worker_id);
        worke = cursor.fetchall()
        if worke.status == "busy":
            if  len(stringworkerlist[worke.id]) == 0:
                if task.time_to_make == 1:
                    print(worke.name + "says: All Done!")
                    cursor.execute("DELETE FROM tasks WHERE task.id");
                    cursor.execute(" UPDATE workers SET status = idle WHERE worke.id");
                else:
                    cursor.execute(" UPDATE tasks SET time_to_make = task.time_to_make - 1 WHERE task.id");
                    print(worke.name + " is busy " + task.task_name)
                stringworkerlist[worke.id] = task.task_name
            else:
                print(worke.name + " is busy " + stringworkerlist[worke.id])
        else:
            cursor.execute(" UPDATE workers SET status = busy WHERE worke.id");
            print(worke.name + " says: work work")