#!/bin/bash
set -e

export THREADS_FLAG=native

export ORACLE_BASE=/usr/lib/oracle
export ORACLE_HOME=$ORACLE_BASE/19.8/client64
export TNS_ADMIN=$ORACLE_HOME/network/admin
export ORACLE_TERM=vt100

export LD_LIBRARY_PATH=$ORACLE_HOME/lib:/lib:/usr/lib
export CLASSPATH=$ORACLE_HOME/jre:$ORACLE_HOME/jlib:$ORACLE_HOME/rdbms/jlib
export PATH=$PATH:/usr/sbin:$ORACLE_HOME/bin

sqlplus nathanlamont/a2347@oracle.aloe @build_tables.sql >> build_tables.log 2>&1

# Base tables with no foreign keys
sqlplus nathanlamont/a2347@oracle.aloe @insert_property.sql >> build_tables.log 2>&1
sqlplus nathanlamont/a2347@oracle.aloe @insert_equipment.sql >> build_tables.log 2>&1
sqlplus nathanlamont/a2347@oracle.aloe @insert_trails.sql >> build_tables.log 2>&1
sqlplus nathanlamont/a2347@oracle.aloe @insert_lift.sql >> build_tables.log 2>&1
sqlplus nathanlamont/a2347@oracle.aloe @insert_member.sql >> build_tables.log 2>&1

# Tables that depend on previously inserted ones
sqlplus nathanlamont/a2347@oracle.aloe @insert_trailLift.sql >> build_tables.log 2>&1       # needs Trail + Lift
sqlplus nathanlamont/a2347@oracle.aloe @insert_pass.sql >> build_tables.log 2>&1            # needs Member
sqlplus nathanlamont/a2347@oracle.aloe @insert_employee.sql >> build_tables.log 2>&1        # needs Property
sqlplus nathanlamont/a2347@oracle.aloe @insert_shops.sql >> build_tables.log 2>&1           # needs Property
sqlplus nathanlamont/a2347@oracle.aloe @insert_lesson_offering.sql >> build_tables.log 2>&1 # needs Employee
sqlplus nathanlamont/a2347@oracle.aloe @insert_lessonPurchase.sql >> build_tables.log 2>&1  # needs LessonOffering + Member
sqlplus nathanlamont/a2347@oracle.aloe @insert_rentals.sql >> build_tables.log 2>&1         # needs Pass + Equipment
sqlplus nathanlamont/a2347@oracle.aloe @insert_liftlog.sql >> build_tables.log 2>&1         # needs Pass + Lift
sqlplus nathanlamont/a2347@oracle.aloe @insert_lessonLog.sql >> build_tables.log 2>&1       # needs LessonPurchase

echo "All done thank you"
