import { useState, useEffect } from "react";
import { keynoteService } from "@/services/keynoteService";
import { Keynote, KeynoteRequest } from "@/types/conference.types";
import Navbar from "@/components/Navbar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useToast } from "@/hooks/use-toast";
import { Plus, Trash2, Edit, Mail, Loader2 } from "lucide-react";
import { useForm } from "react-hook-form";

const Keynotes = () => {
  const [keynotes, setKeynotes] = useState<Keynote[]>([]);
  const [loading, setLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingKeynote, setEditingKeynote] = useState<Keynote | undefined>();
  const { toast } = useToast();
  const { register, handleSubmit, reset, formState: { errors } } = useForm<KeynoteRequest>();

  const loadKeynotes = async () => {
    try {
      setLoading(true);
      const data = await keynoteService.getAll();
      setKeynotes(data);
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Impossible de charger les keynotes",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadKeynotes();
  }, []);

  useEffect(() => {
    if (editingKeynote) {
      reset(editingKeynote);
    } else {
      reset({ nom: "", prenom: "", email: "", fonction: "" });
    }
  }, [editingKeynote, reset]);

  const onSubmit = async (data: KeynoteRequest) => {
    try {
      if (editingKeynote) {
        await keynoteService.update(editingKeynote.id, data);
        toast({ title: "Succès", description: "Keynote mis à jour" });
      } else {
        await keynoteService.create(data);
        toast({ title: "Succès", description: "Keynote créé" });
      }
      setIsDialogOpen(false);
      setEditingKeynote(undefined);
      loadKeynotes();
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Une erreur est survenue",
        variant: "destructive",
      });
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Êtes-vous sûr de vouloir supprimer ce keynote ?")) return;

    try {
      await keynoteService.delete(id);
      toast({ title: "Succès", description: "Keynote supprimé" });
      loadKeynotes();
    } catch (error) {
      toast({
        title: "Erreur",
        description: "Impossible de supprimer le keynote",
        variant: "destructive",
      });
    }
  };

  const handleEdit = (keynote: Keynote) => {
    setEditingKeynote(keynote);
    setIsDialogOpen(true);
  };

  const handleCreate = () => {
    setEditingKeynote(undefined);
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
            <h1 className="text-3xl font-bold text-foreground mb-2">Keynote Speakers</h1>
            <p className="text-muted-foreground">Gérez vos intervenants</p>
          </div>
          <Button onClick={handleCreate} className="gap-2">
            <Plus className="h-4 w-4" />
            Nouveau keynote
          </Button>
        </div>

        {keynotes.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-muted-foreground mb-4">Aucun keynote disponible</p>
            <Button onClick={handleCreate} variant="outline">
              Créer votre premier keynote
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {keynotes.map((keynote) => (
              <Card key={keynote.id} className="group hover:shadow-medium transition-all">
                <CardHeader>
                  <div className="flex items-start gap-4">
                    <div className="h-16 w-16 rounded-full bg-primary/10 flex items-center justify-center flex-shrink-0">
                      <span className="text-xl font-semibold text-primary">
                        {keynote.prenom.charAt(0)}{keynote.nom.charAt(0)}
                      </span>
                    </div>
                    <div className="flex-1 min-w-0">
                      <CardTitle className="text-lg truncate">
                        {keynote.prenom} {keynote.nom}
                      </CardTitle>
                      <p className="text-sm text-muted-foreground truncate">{keynote.fonction}</p>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <Mail className="h-4 w-4 flex-shrink-0" />
                    <span className="truncate">{keynote.email}</span>
                  </div>
                  <div className="flex gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      className="flex-1"
                      onClick={() => handleEdit(keynote)}
                    >
                      <Edit className="h-4 w-4 mr-2" />
                      Modifier
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleDelete(keynote.id)}
                    >
                      <Trash2 className="h-4 w-4 text-destructive" />
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </main>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {editingKeynote ? "Modifier le keynote" : "Nouveau keynote"}
            </DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="prenom">Prénom</Label>
                <Input
                  id="prenom"
                  {...register("prenom", { required: "Le prénom est requis" })}
                />
                {errors.prenom && (
                  <p className="text-sm text-destructive">{errors.prenom.message}</p>
                )}
              </div>
              <div className="space-y-2">
                <Label htmlFor="nom">Nom</Label>
                <Input
                  id="nom"
                  {...register("nom", { required: "Le nom est requis" })}
                />
                {errors.nom && (
                  <p className="text-sm text-destructive">{errors.nom.message}</p>
                )}
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                {...register("email", {
                  required: "L'email est requis",
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                    message: "Email invalide"
                  }
                })}
              />
              {errors.email && (
                <p className="text-sm text-destructive">{errors.email.message}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="fonction">Fonction</Label>
              <Input
                id="fonction"
                {...register("fonction", { required: "La fonction est requise" })}
              />
              {errors.fonction && (
                <p className="text-sm text-destructive">{errors.fonction.message}</p>
              )}
            </div>

            <div className="flex gap-2 pt-4">
              <Button type="submit" className="flex-1">
                {editingKeynote ? "Mettre à jour" : "Créer"}
              </Button>
              <Button
                type="button"
                variant="outline"
                onClick={() => {
                  setIsDialogOpen(false);
                  setEditingKeynote(undefined);
                }}
              >
                Annuler
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default Keynotes;
