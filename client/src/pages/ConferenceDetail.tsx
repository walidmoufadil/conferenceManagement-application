import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { conferenceService } from "@/services/conferenceService";
import { Conference, ReviewRequest } from "@/types/conference.types";
import Navbar from "@/components/Navbar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Textarea } from "@/components/ui/textarea";
import { useToast } from "@/hooks/use-toast";
import { ArrowLeft, Calendar, Clock, Users, Star, MessageSquare, Trash2 } from "lucide-react";
import { format } from "date-fns";
import { fr } from "date-fns/locale";

const ConferenceDetail = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [conference, setConference] = useState<Conference | null>(null);
  const [newReview, setNewReview] = useState("");
  const [loading, setLoading] = useState(true);
  const { toast } = useToast();

  const loadConference = async () => {
    if (!id) return;
    try {
      setLoading(true);
      const data = await conferenceService.getById(parseInt(id));
      setConference(data);
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Impossible de charger la conférence",
        variant: "destructive",
      });
      navigate("/");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadConference();
  }, [id]);

  const handleAddReview = async () => {
    if (!conference || !newReview.trim()) return;

    try {
      const review: ReviewRequest = {
        date: new Date().toISOString(),
        commentaire: newReview,
      };
      
      await conferenceService.updateReviews(conference.id, [review]);
      toast({ title: "Succès", description: "Commentaire ajouté" });
      setNewReview("");
      loadConference();
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Impossible d'ajouter le commentaire",
        variant: "destructive",
      });
    }
  };

  const handleDeleteReview = async (reviewId: number) => {
    if (!conference || !confirm("Supprimer ce commentaire ?")) return;

    try {
      await conferenceService.deleteReview(conference.id, reviewId);
      toast({ title: "Succès", description: "Commentaire supprimé" });
      loadConference();
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Impossible de supprimer le commentaire",
        variant: "destructive",
      });
    }
  };

  if (loading || !conference) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <div className="flex items-center justify-center h-[calc(100vh-4rem)]">
          <div className="animate-pulse text-muted-foreground">Chargement...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        <Button
          variant="ghost"
          onClick={() => navigate("/")}
          className="mb-6 gap-2"
        >
          <ArrowLeft className="h-4 w-4" />
          Retour
        </Button>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 space-y-6">
            <Card>
              <CardHeader>
                <div className="flex items-start justify-between gap-4">
                  <CardTitle className="text-2xl">{conference.titre}</CardTitle>
                  <Badge variant={conference.type === "Academic" ? "default" : "secondary"}>
                    {conference.type}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="flex items-center gap-2 text-muted-foreground">
                    <Calendar className="h-5 w-5" />
                    <span>{format(new Date(conference.date), "d MMMM yyyy 'à' HH:mm", { locale: fr })}</span>
                  </div>
                  <div className="flex items-center gap-2 text-muted-foreground">
                    <Clock className="h-5 w-5" />
                    <span>{conference.duree} heures</span>
                  </div>
                  <div className="flex items-center gap-2 text-muted-foreground">
                    <Users className="h-5 w-5" />
                    <span>{conference.nombreInscrits} participants</span>
                  </div>
                  <div className="flex items-center gap-2 text-muted-foreground">
                    <Star className="h-5 w-5 fill-warning text-warning" />
                    <span>{(conference.score ?? 0).toFixed(1)}/5</span>
                  </div>
                </div>
              </CardContent>
            </Card>

            {conference.keynote && (
              <Card>
                <CardHeader>
                  <CardTitle className="text-lg">Intervenant Principal</CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="flex items-start gap-4">
                    <div className="h-16 w-16 rounded-full bg-primary/10 flex items-center justify-center flex-shrink-0">
                      <span className="text-2xl font-semibold text-primary">
                        {conference.keynote.prenom.charAt(0)}{conference.keynote.nom.charAt(0)}
                      </span>
                    </div>
                    <div>
                      <h3 className="font-semibold text-lg">
                        {conference.keynote.prenom} {conference.keynote.nom}
                      </h3>
                      <p className="text-muted-foreground">{conference.keynote.fonction}</p>
                      <p className="text-sm text-muted-foreground mt-1">{conference.keynote.email}</p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            )}
          </div>

          <div className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="text-lg flex items-center gap-2">
                  <MessageSquare className="h-5 w-5" />
                  Commentaires ({conference.reviews?.length || 0})
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <Textarea
                    placeholder="Ajouter un commentaire..."
                    value={newReview}
                    onChange={(e) => setNewReview(e.target.value)}
                    rows={3}
                  />
                  <Button
                    onClick={handleAddReview}
                    disabled={!newReview.trim()}
                    className="w-full"
                  >
                    Ajouter
                  </Button>
                </div>

                <div className="space-y-3 max-h-96 overflow-y-auto">
                  {conference.reviews?.map((review) => (
                    <div key={review.id} className="p-3 bg-muted/50 rounded-lg space-y-1 group">
                      <div className="flex items-start justify-between gap-2">
                        <p className="text-sm">{review.commentaire}</p>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="opacity-0 group-hover:opacity-100 transition-opacity"
                          onClick={() => handleDeleteReview(review.id)}
                        >
                          <Trash2 className="h-3 w-3 text-destructive" />
                        </Button>
                      </div>
                      <p className="text-xs text-muted-foreground">
                        {format(new Date(review.date), "d MMM yyyy 'à' HH:mm", { locale: fr })}
                      </p>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </main>
    </div>
  );
};

export default ConferenceDetail;
