-- Section 1: Ensure Database and Table exist
USE master;
GO

IF DB_ID('LoginSystemDB') IS NULL
BEGIN
    PRINT 'Database LoginSystemDB does not exist. Creating it...';
    CREATE DATABASE LoginSystemDB;
    PRINT 'Database LoginSystemDB created.';
END
ELSE
BEGIN
    PRINT 'Database LoginSystemDB already exists.';
END
GO

USE LoginSystemDB;
GO

-- Drop existing AppUsers table if it exists, to ensure a clean slate with the new user_type column and new users
IF OBJECT_ID('AppUsers', 'U') IS NOT NULL
BEGIN
    PRINT 'Table AppUsers exists. Dropping it to recreate with specified users...';
    DROP TABLE AppUsers;
END
GO

PRINT 'Creating table AppUsers with user_type column...';
CREATE TABLE AppUsers (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL, -- For a real app, ALWAYS HASH PASSWORDS!
    user_type VARCHAR(10) NOT NULL CHECK (user_type IN ('admin', 'passenger'))
);
PRINT 'Table AppUsers created.';
GO

-- Insert NEW sample application users with user_type
PRINT 'Inserting new sample users into AppUsers...';
INSERT INTO AppUsers (username, password, user_type) VALUES ('admin', 'admin123', 'admin');
INSERT INTO AppUsers (username, password, user_type) VALUES ('waleed', 'waleed123', 'passenger');
INSERT INTO AppUsers (username, password, user_type) VALUES ('ayaz', 'ayaz123', 'passenger');
INSERT INTO AppUsers (username, password, user_type) VALUES ('zakria', 'zakria123', 'passenger');
PRINT 'New sample users inserted.';
GO

-- Section 2: Create/Recreate SQL Server Login 'waleed_user' (for DB connection, not application login)
USE master; 
GO

IF EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'waleed_user')
BEGIN
    PRINT 'Login waleed_user exists. Attempting to drop it (might fail if in use)...';
    -- Note: This DROP might fail if the login is actively in use. 
    -- The CREATE LOGIN will also fail if the drop fails.
    -- If errors occur here, ensure no other connections are using 'waleed_user' then re-run,
    -- or manually run ALTER LOGIN to set password if CREATE fails.
    BEGIN TRY
        DROP LOGIN waleed_user;
        PRINT 'Login waleed_user dropped.';
    END TRY
    BEGIN CATCH
        PRINT 'Could not drop login waleed_user. It might be in use. Error: ' + ERROR_MESSAGE();
    END CATCH
END
GO

IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'waleed_user')
BEGIN
    PRINT 'Creating SQL Server Login: waleed_user...';
    CREATE LOGIN waleed_user WITH PASSWORD = 'Need4Speed'; 
END
ELSE
BEGIN
    PRINT 'Login waleed_user already exists. Ensuring password is correct and login is enabled.';
    ALTER LOGIN waleed_user WITH PASSWORD = 'Need4Speed';
END
GO

PRINT 'Enabling login waleed_user...';
ALTER LOGIN waleed_user ENABLE;
GO

-- Section 3: Create Database User in LoginSystemDB and Grant Permissions
USE LoginSystemDB; 
GO

IF EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'waleed_user')
BEGIN
    PRINT 'Database user waleed_user exists in LoginSystemDB. Dropping it to recreate cleanly...';
    DROP USER waleed_user;
END
GO

PRINT 'Creating Database User waleed_user in LoginSystemDB...';
CREATE USER waleed_user FOR LOGIN waleed_user;
GO

PRINT 'Granting SELECT, INSERT permissions to database user waleed_user on AppUsers table...';
GRANT SELECT ON AppUsers TO waleed_user;
GRANT INSERT ON AppUsers TO waleed_user;
GO

PRINT 'Setup for database, table, login waleed_user, and database user completed.';
GO

-- Section 4: Verification (Optional, but recommended)
PRINT '--- Verification ---';
PRINT 'Server Login Details for waleed_user (DB connection user):';
SELECT name AS login_name, type_desc AS login_type, is_disabled 
FROM sys.server_principals 
WHERE name = 'waleed_user';

PRINT 'Database User Details for waleed_user in LoginSystemDB:';
USE LoginSystemDB;
SELECT u.name AS database_user_name, u.type_desc AS user_type, default_schema_name
FROM sys.database_principals u
WHERE u.name = 'waleed_user';

PRINT 'Permissions for database user waleed_user on AppUsers table:';
SELECT p.permission_name, p.state_desc AS permission_state
FROM sys.database_permissions p
INNER JOIN sys.database_principals u ON p.grantee_principal_id = u.principal_id
INNER JOIN sys.objects o ON p.major_id = o.object_id
WHERE u.name = 'waleed_user' AND o.name = 'AppUsers';

PRINT 'Current data from AppUsers (application login users):';
SELECT TOP 5 id, username, password, user_type FROM AppUsers;
GO
select * from AppUsers;