import { Conference } from "@/types/conference.types";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Calendar, Clock, Users, Star, Trash2, Edit } from "lucide-react";
import { format } from "date-fns";
import { fr } from "date-fns/locale";

interface ConferenceCardProps {
  conference: Conference;
  onEdit: (conference: Conference) => void;
  onDelete: (id: number) => void;
  onViewDetails: (id: number) => void;
}

const ConferenceCard = ({ conference, onEdit, onDelete, onViewDetails }: ConferenceCardProps) => {
  return (
    <Card className="group hover:shadow-medium transition-all duration-300 cursor-pointer">
      <CardHeader>
        <div className="flex items-start justify-between gap-2">
          <CardTitle className="text-lg group-hover:text-primary transition-colors">
            {conference.titre}
          </CardTitle>
          <Badge variant={conference.type === "Academic" ? "default" : "secondary"}>
            {conference.type}
          </Badge>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="space-y-2 text-sm text-muted-foreground">
          <div className="flex items-center gap-2">
            <Calendar className="h-4 w-4" />
            <span>{format(new Date(conference.date), "d MMMM yyyy", { locale: fr })}</span>
          </div>
          <div className="flex items-center gap-2">
            <Clock className="h-4 w-4" />
            <span>{conference.duree} heures</span>
          </div>
          <div className="flex items-center gap-2">
            <Users className="h-4 w-4" />
            <span>{conference.nombreInscrits} inscrits</span>
          </div>
          <div className="flex items-center gap-2">
            <Star className="h-4 w-4 fill-warning text-warning" />
            <span>{(conference.score ?? 0).toFixed(1)}/5</span>
          </div>
        </div>

        {conference.keynote && (
          <div className="pt-3 border-t border-border">
            <p className="text-xs text-muted-foreground mb-1">Speaker</p>
            <p className="text-sm font-medium">
              {conference.keynote.prenom} {conference.keynote.nom}
            </p>
            <p className="text-xs text-muted-foreground">{conference.keynote.fonction}</p>
          </div>
        )}

        <div className="flex gap-2 pt-2">
          <Button 
            variant="outline" 
            size="sm" 
            className="flex-1"
            onClick={() => onViewDetails(conference.id)}
          >
            Détails
          </Button>
          <Button 
            variant="outline" 
            size="sm"
            onClick={(e) => {
              e.stopPropagation();
              onEdit(conference);
            }}
          >
            <Edit className="h-4 w-4" />
          </Button>
          <Button 
            variant="outline" 
            size="sm"
            onClick={(e) => {
              e.stopPropagation();
              onDelete(conference.id);
            }}
          >
            <Trash2 className="h-4 w-4 text-destructive" />
          </Button>
        </div>
      </CardContent>
    </Card>
  );
};

export default ConferenceCard;
