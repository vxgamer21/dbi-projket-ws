import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend, Counter } from 'k6/metrics';

// Custom Metrics
const errorRate = new Rate('errors');
const requestDuration = new Trend('request_duration');
const successfulRequests = new Counter('successful_requests');

// Test Configuration
export const options = {
    stages: [
        { duration: '30s', target: 10 },  // Warm-up: 10 VUs for 30s
        { duration: '1m', target: 50 },   // Load: 50 VUs for 1m
        { duration: '2m', target: 100 },  // Peak: 100 VUs for 2m
        { duration: '1m', target: 50 },   // Scale down
        { duration: '30s', target: 0 },   // Cool down
    ],
    thresholds: {
        http_req_duration: ['p(95)<500', 'p(99)<1000'], // 95% requests < 500ms, 99% < 1s
        errors: ['rate<0.1'],                            // Error rate < 10%
        successful_requests: ['count>1000'],             // At least 1000 successful requests
    },
};

const BASE_URL = 'http://localhost:8080';

// Test Data
const testArzt = {
    name: 'Dr. Test Arzt',
    gebDatum: '1975-05-15',
    svnr: 1234567890,
    fachgebiet: 'ORTHOPAEDIE',
    email: { mail: `test${Date.now()}@medical.at` },
    adresse: {
        strasse: 'Teststraße',
        hausNr: '42',
        stadt: 'Wien',
        plz: '1010'
    },
    telefonNummer: {
        lkennzahl: '043',
        okennzahl: '1234',
        rufnummer: '56789012',
        art: 'BUSINESS'
    }
};

const testPatient = {
    name: 'Max Mustermann',
    gebDatum: '1985-03-20',
    svnr: 9876543210,
    versicherungsart: 'PRIVAT',
    adresse: {
        strasse: 'Hauptstraße',
        hausNr: '1',
        stadt: 'Wien',
        plz: '1010'
    },
    telefonNummer: {
        lkennzahl: '043',
        okennzahl: '5678',
        rufnummer: '12345678',
        art: 'MOBIL'
    }
};

// Setup: Runs once at the beginning
export function setup() {
    console.log('='.repeat(80));
    console.log('K6 Performance Test Setup - MongoDB Backend');
    console.log('='.repeat(80));

    // Create some initial test data
    const setupData = {
        arztIds: [],
        patientIds: []
    };

    // Create 10 Ärzte
    for (let i = 0; i < 10; i++) {
        const arzt = { ...testArzt, email: { mail: `arzt${i}@medical.at` } };
        const res = http.post(`${BASE_URL}/api/aerzte`, JSON.stringify(arzt), {
            headers: { 'Content-Type': 'application/json' }
        });
        if (res.status === 201) {
            const arztId = JSON.parse(res.body).id;
            setupData.arztIds.push(arztId);
        }
    }

    // Create 10 Patienten
    for (let i = 0; i < 10; i++) {
        const patient = { ...testPatient, name: `Patient ${i}` };
        const res = http.post(`${BASE_URL}/api/patienten`, JSON.stringify(patient), {
            headers: { 'Content-Type': 'application/json' }
        });
        if (res.status === 201) {
            const patientId = JSON.parse(res.body).id;
            setupData.patientIds.push(patientId);
        }
    }

    console.log(`Setup completed: ${setupData.arztIds.length} Ärzte, ${setupData.patientIds.length} Patienten`);
    return setupData;
}

// Main Test Scenario
export default function(data) {
    const scenario = Math.random();

    if (scenario < 0.4) {
        // 40% READ operations - Aerzte
        testReadAerzte();
    } else if (scenario < 0.7) {
        // 30% READ operations - Patienten
        testReadPatienten();
    } else if (scenario < 0.85) {
        // 15% WRITE operations - Create
        testCreateArzt();
    } else if (scenario < 0.95) {
        // 10% UPDATE operations
        if (data.arztIds.length > 0) {
            testUpdateArzt(data.arztIds);
        }
    } else {
        // 5% DELETE operations
        if (data.patientIds.length > 0) {
            testDeletePatient(data.patientIds);
        }
    }

    sleep(1); // Think time
}

// Test Functions

function testReadAerzte() {
    const startTime = Date.now();
    const res = http.get(`${BASE_URL}/api/aerzte`);
    const duration = Date.now() - startTime;

    const success = check(res, {
        'GET Aerzte status 200': (r) => r.status === 200,
        'GET Aerzte has body': (r) => r.body.length > 0,
    });

    errorRate.add(!success);
    requestDuration.add(duration);
    if (success) successfulRequests.add(1);
}

function testReadPatienten() {
    const startTime = Date.now();
    const res = http.get(`${BASE_URL}/api/patienten`);
    const duration = Date.now() - startTime;

    const success = check(res, {
        'GET Patienten status 200': (r) => r.status === 200,
        'GET Patienten has body': (r) => r.body.length > 0,
    });

    errorRate.add(!success);
    requestDuration.add(duration);
    if (success) successfulRequests.add(1);
}

function testCreateArzt() {
    const arzt = {
        ...testArzt,
        email: { mail: `arzt${Date.now()}@medical.at` }
    };

    const startTime = Date.now();
    const res = http.post(`${BASE_URL}/api/aerzte`, JSON.stringify(arzt), {
        headers: { 'Content-Type': 'application/json' }
    });
    const duration = Date.now() - startTime;

    const success = check(res, {
        'POST Arzt status 201': (r) => r.status === 201,
        'POST Arzt has ID': (r) => {
            try {
                const body = JSON.parse(r.body);
                return body.id !== undefined;
            } catch {
                return false;
            }
        },
    });

    errorRate.add(!success);
    requestDuration.add(duration);
    if (success) successfulRequests.add(1);
}

function testUpdateArzt(arztIds) {
    if (arztIds.length === 0) return;

    const randomId = arztIds[Math.floor(Math.random() * arztIds.length)];
    const update = {
        fachgebiet: 'CHIRURGIE'
    };

    const startTime = Date.now();
    const res = http.patch(`${BASE_URL}/api/aerzte/${randomId}`, JSON.stringify(update), {
        headers: { 'Content-Type': 'application/json' }
    });
    const duration = Date.now() - startTime;

    const success = check(res, {
        'PATCH Arzt status 200': (r) => r.status === 200 || r.status === 204,
    });

    errorRate.add(!success);
    requestDuration.add(duration);
    if (success) successfulRequests.add(1);
}

function testDeletePatient(patientIds) {
    if (patientIds.length === 0) return;

    const randomId = patientIds[Math.floor(Math.random() * patientIds.length)];

    const startTime = Date.now();
    const res = http.del(`${BASE_URL}/api/patienten/${randomId}`);
    const duration = Date.now() - startTime;

    const success = check(res, {
        'DELETE Patient status 200/204': (r) => r.status === 200 || r.status === 204,
    });

    errorRate.add(!success);
    requestDuration.add(duration);
    if (success) successfulRequests.add(1);
}

// Teardown: Runs once at the end
export function teardown(data) {
    console.log('='.repeat(80));
    console.log('K6 Performance Test Teardown');
    console.log('='.repeat(80));
    console.log('Test completed. Check results above.');
}

// Summary Handler
export function handleSummary(data) {
    return {
        'k6-tests/results/summary.json': JSON.stringify(data),
        stdout: textSummary(data, { indent: ' ', enableColors: true }),
    };
}

function textSummary(data, options) {
    const indent = options.indent || '';
    const enableColors = options.enableColors || false;

    let summary = '\n';
    summary += '='.repeat(80) + '\n';
    summary += 'K6 PERFORMANCE TEST RESULTS - MongoDB Backend\n';
    summary += '='.repeat(80) + '\n\n';

    // Requests
    summary += `${indent}Total Requests: ${data.metrics.http_reqs.values.count}\n`;
    summary += `${indent}Successful Requests: ${data.metrics.successful_requests?.values?.count || 0}\n`;
    summary += `${indent}Error Rate: ${(data.metrics.errors.values.rate * 100).toFixed(2)}%\n\n`;

    // Response Times
    summary += `${indent}Response Times:\n`;
    summary += `${indent}  Average: ${data.metrics.http_req_duration.values.avg.toFixed(2)}ms\n`;
    summary += `${indent}  Median:  ${data.metrics.http_req_duration.values.med.toFixed(2)}ms\n`;
    summary += `${indent}  P95:     ${data.metrics.http_req_duration.values['p(95)'].toFixed(2)}ms\n`;
    summary += `${indent}  P99:     ${data.metrics.http_req_duration.values['p(99)'].toFixed(2)}ms\n`;
    summary += `${indent}  Max:     ${data.metrics.http_req_duration.values.max.toFixed(2)}ms\n\n`;

    // Throughput
    summary += `${indent}Throughput: ${(data.metrics.http_reqs.values.rate).toFixed(2)} req/s\n\n`;

    summary += '='.repeat(80) + '\n';

    return summary;
}

