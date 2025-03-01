const express = require('express');
const mysql = require('mysql');
const app = express();
const port = 3000;

// MySQL connection
const db = mysql.createConnection({
    host: 'seklys.ila.lt',
    user: 'stud',
    password: 'vLXCDmSG6EpEnhXX',
    database: 'LDB'
});

// Connect to MySQL database
db.connect(err => {
    if (err) {
        console.error('Error connecting to MySQL:', err);
        return;
    }
    console.log('Connected to MySQL');
});

// Root route to verify server is running
app.get('/', (req, res) => {
    res.send('Welcome to My Node.js API!');
});

// Route to get signal strength data from the stiprumai table
app.get('/api/signalStrength', (req, res) => {
    const query = 'SELECT id, matavimas, stiprumas, sensorius FROM stiprumai';
    db.query(query, (err, results) => {
        if (err) {
            console.error('Error executing query:', err);
            res.status(500).json({ error: 'Database query failed' });
            return;
        }
        console.log('Fetched signal strength data:', results);  // Debugging log
        res.json(results);
    });
});

// Route to get all coordinates data from the matavimai table
app.get('/api/allCoordinates', (req, res) => {
    const query = 'SELECT matavimas, x, y, atstumas FROM matavimai';
    db.query(query, (err, results) => {
        if (err) {
            console.error('Error executing query:', err);
            res.status(500).json({ error: 'Database query failed' });
            return;
        }
        console.log('Fetched all coordinates data:', results);  // Debugging log
        res.json(results);
    });
});

// Route to get coordinates data from the matavimai table by matavimas ID
app.get('/api/getCoordinates/:metavimasId', (req, res) => {
    const { metavimasId } = req.params;
    const query = 'SELECT x, y, atstumas FROM matavimai WHERE matavimas = ?';
    db.query(query, [metavimasId], (err, results) => {
        if (err) {
            console.error('Error executing query:', err);
            res.status(500).json({ error: 'Database query failed' });
            return;
        }
        if (results.length > 0) {
            console.log(`Fetched coordinates for matavimasId ${metavimasId}:`, results[0]);  // Debugging log
            res.json(results[0]);
        } else {
            console.warn(`No coordinates found for matavimasId ${metavimasId}`);  // Warning log for debugging
            res.status(404).json({ error: 'No coordinates found for the given metavimasId' });
        }
    });
});

// Start the server
app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
