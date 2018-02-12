import sqlite3

import os

import sys

databaseexisted = os.path.isfile('world.db')
if not databaseexisted:
    dbcon = sqlite3.connect('world.db')
    with dbcon:
        cursor = dbcon.cursor()
        cursor.execute("CREATE TABLE tasks(id INTEGER PRIMARY KEY,task_name TEXT NOT NULL,worker_id INTEGER REFERENCES workers(id),time_to_make INTEGER NOT NULL,resource_name TEXT NOT NULL,resource_amount INTEGER NOT NULL)") # create table tasks
        cursor.execute(
            "CREATE TABLE workers(id INTEGER PRIMARY KEY,name TEXT NOT NULL,status TEXT NOT NULL)")  # create table workers
        cursor.execute(
            "CREATE TABLE resources(name TEXT PRIMARY KEY,amount INTEGER NOT NULL)")  # create table resources

    config_file  = open(sys.argv[1])

    numberoftasks = 1
    for line in config_file:
        line = line.replace('\n','')
        config_line = line.split(",")
        if len(config_line) == 2:
            resourcename = config_line[0]
            amount = int(config_line[1])
            cursor.execute("INSERT INTO resources VALUES(?,?)", (resourcename, amount));
        elif len(config_line) == 3:
            workername = config_line[2]
            workerid = int(config_line[1])
            cursor.execute("INSERT INTO workers VALUES(?,?,?)", (workerid, workername ,'idle'));
        elif len(config_line) == 5:
            taskname = config_line[0]
            workerid = config_line[1]
            resourcename = config_line[2]
            resourceamount = config_line[3]
            time = config_line[4]
            cursor.execute("INSERT INTO tasks VALUES(?,?,?,?,?,?)", (numberoftasks, taskname, workerid ,time,resourcename,resourceamount));
            numberoftasks = numberoftasks + 1

    dbcon.commit()
else:
    print("Error the world as already exist")
