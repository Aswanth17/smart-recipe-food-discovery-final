import { Outlet } from "react-router-dom";
import Navbar from "../components/Navbar";

function MainLayout() {
  return (
    <div className="app-shell">

      <Navbar />

      <main className="main-content">
        <Outlet />
      </main>

      <footer className="footer">
        Smart Recipe & Food Discovery System
      </footer>

    </div>
  );
}

export default MainLayout;