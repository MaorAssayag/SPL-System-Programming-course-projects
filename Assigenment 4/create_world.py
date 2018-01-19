import sqlite3

import os


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


