import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { conferenceService } from "@/services/conferenceService";
import { keynoteService } from "@/services/keynoteService";
import { Conference, ConferenceRequest, Keynote } from "@/types/conference.types";
import Navbar from "@/components/Navbar";
import ConferenceCard from "@/components/ConferenceCard";
import ConferenceForm from "@/components/ConferenceForm";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { useToast } from "@/hooks/use-toast";
import { Plus, Loader2 } from "lucide-react";
import { Description } from "@radix-ui/react-toast";

const Conferences = () => {
  const [conferences, setConferences] = useState<Conference[]>([]);
  const [keynotes, setKeynotes] = useState<Keynote[]>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingConference, setEditingConference] = useState<Conference | undefined>();
  const { toast } = useToast();
  const navigate = useNavigate();

  const loadData = async () => {
    try {
      setLoading(true);
      const [conferencesData, keynotesData] = await Promise.all([
        conferenceService.getAll(),
        keynoteService.getAll(),
      ]);
      setConferences(conferencesData);
      setKeynotes(keynotesData);
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Impossible de charger les données",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleSubmit = async (data: ConferenceRequest) => {
    try {
      if (editingConference) {
        await conferenceService.update(editingConference.id, data);
        toast({ title: "Succès", description: "Conférence mise à jour" });
      } else {
        await conferenceService.create(data);
        toast({ title: "Succès", description: "Conférence créée" });
      }
      setIsDialogOpen(false);
      setEditingConference(undefined);
      loadData();
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Une erreur est survenue",
        variant: "destructive",
      });
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Êtes-vous sûr de vouloir supprimer cette conférence ?")) return;
    
    try {
      await conferenceService.delete(id);
      toast({ title: "Succès", description: "Conférence supprimée" });
      loadData();
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Impossible de supprimer la conférence",
        variant: "destructive",
      });
    }
  };

  const handleEdit = (conference: Conference) => {
    setEditingConference(conference);
    setIsDialogOpen(true);
  };

  const handleCreate = () => {
    setEditingConference(undefined);
    setIsDialogOpen(true);
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <div className="flex items-center justify-center h-[calc(100vh-4rem)]">
          <Loader2 className="h-8 w-8 animate-spin text-primary" />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-3xl font-bold text-foreground mb-2">Conférences</h1>
            <p className="text-muted-foreground">Gérez vos conférences et événements</p>
          </div>
          <Button onClick={handleCreate} className="gap-2">
            <Plus className="h-4 w-4" />
            Nouvelle conférence
          </Button>
        </div>

        {conferences.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-muted-foreground mb-4">Aucune conférence disponible</p>
            <Button onClick={handleCreate} variant="outline">
              Créer votre première conférence
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {conferences.map((conference) => (
              <ConferenceCard
                key={conference.id}
                conference={conference}
                onEdit={handleEdit}
                onDelete={handleDelete}
                onViewDetails={(id) => navigate(`/conferences/${id}`)}
              />
            ))}
          </div>
        )}
      </main>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>
              {editingConference ? "Modifier la conférence" : "Nouvelle conférence"}
            </DialogTitle>
          </DialogHeader>
          <DialogDescription>
            hello
          </DialogDescription>
          <ConferenceForm
            conference={editingConference}
            keynotes={keynotes}
            onSubmit={handleSubmit}
            onCancel={() => {
              setIsDialogOpen(false);
              setEditingConference(undefined);
            }}
          />
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default Conferences;
