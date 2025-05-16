# WeatherApp

A simple client-side weather application that fetches current conditions and a 5-day forecast from the OpenWeatherMap API.

---

## Features

- Search by city name
- Displays:
  - Current temperature, humidity, wind speed
  - Weather icon and description
  - Five-day forecast with daily highs / lows
- Temperature toggle (°C / °F)
- Responsive layout for mobile and desktop

---

## Tech Stack

| Layer      | Details                              |
|------------|--------------------------------------|
| Front-end  | HTML, CSS, JavaScript (ES6)          |
| API        | OpenWeatherMap REST endpoints        |
| Build      | No build step (pure static files)    |
| Hosting    | Works with any static file host      |

---

## Prerequisites

- A free OpenWeatherMap API key  
  Sign up at <https://openweathermap.org/api>

---

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/connorcoop0/WeatherApp.git
   cd WeatherApp
   ```

2. Create a file named `.env` in the project root (same level as `index.html`) and add:

   ```
   OPENWEATHER_API_KEY=your_api_key_here
   ```

3. Start a simple static server (examples):

   ```bash
   # Python 3
   python -m http.server 8000

   # or with npm (serve package)
   npx serve .
   ```

4. Visit `http://localhost:8000` (or the port shown) in a browser.

---

## Usage

1. Enter a city name in the search box.
2. Press **Enter** or click **Search**.
3. Current weather and the 5-day forecast appear below the search bar.
4. Click the °C / °F toggle to switch units.

---

## Folder Structure

```
WeatherApp/
 ├─ css/
 │   └─ style.css
 ├─ js/
 │   └─ app.js
 ├─ images/            # weather icons (optional)
 ├─ .env               # API key (not committed)
 └─ index.html
```

---

## Roadmap

- Geolocation: auto-detect user’s location on load
- Hour-by-hour forecast view
- Dark / light theme toggle
- LocalStorage cache to reduce API calls

---

## License

MIT
