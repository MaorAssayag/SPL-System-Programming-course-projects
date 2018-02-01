import sqlite3

import os

def max(arr):
    i = 0
    for temp in arr:
        if i < temp[0]:
            i = temp[0]
    return i
dbcon = sqlite3.connect('world.db')
cursor = dbcon.cursor()
cursor.execute("SELECT id FROM workers")
numberOfWorker = max(cursor.fetchall())+1
tasknotempty = True
while tasknotempty and os.path.isfile('world.db') :
    tasknotempty = False
    ListOfWorker = [""]*numberOfWorker
    cursor.execute("""
                SELECT *
                FROM tasks
                JOIN workers ON tasks.worker_id = workers.id
                JOIN resources ON resources.name = tasks.resource_name""");
    TaskList = cursor.fetchall()
    ListOfPrintend = []
    ListOfidleWorker = []
    for Task in TaskList:
        tasknotempty = True
        if Task[8] == 'idle' and len(ListOfWorker[Task[2]]) == 0:
            cursor.execute(" UPDATE resources SET amount = ? WHERE name = ?", (Task[10]-Task[5],Task[4] ));
            cursor.execute(" UPDATE workers SET status = ? WHERE id = ?", ('busy', Task[2]));
            ListOfWorker[Task[2]] = Task[1]
            print(Task[7] + " says: work work")
        elif len(ListOfWorker[Task[2]]) == 0:
            if Task[3] == 1:
                print(Task[7] + " is busy " + Task[1] + "...")
                ListOfPrintend.insert(len(ListOfPrintend), Task[7] + " says: All Done!")
                ListOfidleWorker.insert(len(ListOfidleWorker),Task[6])
                ListOfWorker[Task[2]] = Task[1]
                cursor.execute("DELETE FROM tasks WHERE id = ?", (Task[0],));
            elif Task[3] > 1:
                cursor.execute(" UPDATE tasks SET time_to_make = ? WHERE id = ?", (Task[3] - 1, Task[0]));
                ListOfWorker[Task[2]] = Task[1]
                print(Task[7] + " is busy " + Task[1] + "...")

    for toptint in ListOfPrintend:
        print(toptint)
    for idleWorker in ListOfidleWorker:
        cursor.execute(" UPDATE workers SET status = ? WHERE id = ?", ('idle', idleWorker));
    dbcon.commit()

    # cursor.execute("SELECT * FROM tasks");
    # taskslist = c
    # cursor.execute("SELECT * FROM workers")
    # workeslist = cursor.fetchall()
    # numberofworker = len(workeslist)
    # tasknotempty = False
    # stringworkerlist = [""] * numberofworker
    # lisofIDsworkerprint = []
    # lisofIDsworker = []
    # lisofIDstasks = []
    # for task in taskslist:
    #     tasknotempty = True
    #     tasks_id = int(task[0])
    #     task_time_to_make = int(task[3])
    #     task_name = task[1]
    #     task_resource_name = task[4]
    #     task_resource_amount = int(task[5])
    #     task_worker_id = int(task[2])
    #     cursor.execute("SELECT * FROM workers WHERE id=(?)",(task_worker_id,) );
    #     worke = cursor.fetchall()
    #     worke = worke[0]
    #     worke_id = worke[0]
    #     worke_name = worke[1]
    #     if worke[2] == "busy":
    #         if  len(stringworkerlist[worke_id-1]) == 0:
    #             if task_time_to_make > 0:
    #                 cursor.execute(" UPDATE tasks SET time_to_make = ? WHERE id = ?",(task_time_to_make - 1,tasks_id));
    #                 print(worke_name + " is busy " + task_name +"...")
    #                 stringworkerlist[worke_id-1] = task_name
    #     else:
    #         cursor.execute("SELECT amount FROM resources WHERE name=(?)", (task_resource_name,));
    #         resource = cursor.fetchall()[0][0]
    #         newamount = resource - task_resource_amount
    #         cursor.execute(" UPDATE resources SET amount = ? WHERE name = ?",(newamount,task_resource_name));
    #         cursor.execute(" UPDATE workers SET status = ? WHERE id = ?", ('busy', worke_id));
    #         print(worke_name + " says: work work")
    #         stringworkerlist[worke_id - 1] = task_name
    #     if task_time_to_make == 0:
    #         lisofIDsworkerprint.insert(len(lisofIDsworkerprint), worke_name + " says: All Done!")
    #         lisofIDstasks.insert(len(lisofIDstasks),tasks_id)
    #         lisofIDsworker.insert(len(lisofIDsworker), worke_id)
    #         # cursor.execute(" UPDATE workers SET status = ? WHERE id = ?",('idle',worke_id));
    # if len(lisofIDsworkerprint) != 0:
    #     for prints in lisofIDsworkerprint:
    #         print(prints)
    #     for id in lisofIDsworker:
    #         cursor.execute(" UPDATE workers SET status = ? WHERE id = ?", ('idle', worke_id));
    #     for taskIDs in lisofIDstasks:
    #         cursor.execute("DELETE FROM tasks WHERE id = ?", (taskIDs,));
    # dbcon.commit()

