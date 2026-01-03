import { Button } from "@/components/ui/button";
import { logout, getUsername } from "@/lib/keycloak";
import { LogOut, Users, Calendar } from "lucide-react";
import { Link, useLocation } from "react-router-dom";

const Navbar = () => {
  const username = getUsername();
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  return (
    <nav className="sticky top-0 z-50 w-full border-b border-border bg-card/80 backdrop-blur-lg shadow-soft">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        <div className="flex items-center gap-8">
          <Link to="/" className="flex items-center gap-2">
            <div className="h-8 w-8 rounded-lg bg-gradient-primary flex items-center justify-center">
              <Calendar className="h-5 w-5 text-primary-foreground" />
            </div>
            <span className="text-xl font-bold text-foreground">ConferenceHub</span>
          </Link>
          
          <div className="hidden md:flex items-center gap-4">
            <Link to="/">
              <Button 
                variant={isActive("/") ? "default" : "ghost"}
                size="sm"
                className="gap-2"
              >
                <Calendar className="h-4 w-4" />
                Conférences
              </Button>
            </Link>
            <Link to="/keynotes">
              <Button 
                variant={isActive("/keynotes") ? "default" : "ghost"}
                size="sm"
                className="gap-2"
              >
                <Users className="h-4 w-4" />
                Keynotes
              </Button>
            </Link>
          </div>
        </div>

        <div className="flex items-center gap-4">
          <div className="hidden sm:flex items-center gap-2 text-sm text-muted-foreground">
            <div className="h-8 w-8 rounded-full bg-primary/10 flex items-center justify-center">
              <span className="text-xs font-semibold text-primary">
                {username.charAt(0).toUpperCase()}
              </span>
            </div>
            <span>{username}</span>
          </div>
          <Button
            variant="outline"
            size="sm"
            onClick={() => logout()}
            className="gap-2"
          >
            <LogOut className="h-4 w-4" />
            <span className="hidden sm:inline">Déconnexion</span>
          </Button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
