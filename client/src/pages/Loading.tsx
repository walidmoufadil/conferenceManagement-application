import { Loader2 } from "lucide-react";

const Loading = () => {
  return (
    <div className="min-h-screen bg-background flex items-center justify-center">
      <div className="text-center space-y-4">
        <Loader2 className="h-12 w-12 animate-spin text-primary mx-auto" />
        <p className="text-muted-foreground">Authentification en cours...</p>
      </div>
    </div>
  );
};

export default Loading;
