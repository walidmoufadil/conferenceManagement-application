import { useForm } from "react-hook-form";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Conference, ConferenceRequest, Keynote } from "@/types/conference.types";
import { useEffect } from "react";

interface ConferenceFormProps {
  conference?: Conference;
  keynotes: Keynote[];
  onSubmit: (data: ConferenceRequest) => void;
  onCancel: () => void;
}

const ConferenceForm = ({ conference, keynotes, onSubmit, onCancel }: ConferenceFormProps) => {
  const { register, handleSubmit, setValue, watch, formState: { errors } } = useForm<ConferenceRequest>({
    defaultValues: conference ? {
      titre: conference.titre,
      type: conference.type,
      date: new Date(conference.date).toISOString().slice(0, 16),
      duree: conference.duree,
      nombreInscrits: conference.nombreInscrits,
      score: conference.score,
      keynoteId: conference.keynoteId,
    } : {
      titre: "",
      type: "Academic",
      date: new Date().toISOString().slice(0, 16),
      duree: 2,
      nombreInscrits: 0,
      score: 0,
      keynoteId: 0,
    }
  });

  const typeValue = watch("type");

  useEffect(() => {
    if (conference) {
      setValue("type", conference.type);
    }
  }, [conference, setValue]);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="titre">Titre</Label>
        <Input
          id="titre"
          {...register("titre", { required: "Le titre est requis" })}
          placeholder="Titre de la conférence"
        />
        {errors.titre && <p className="text-sm text-destructive">{errors.titre.message}</p>}
      </div>

      <div className="space-y-2">
        <Label htmlFor="type">Type</Label>
        <Select
          value={typeValue}
          onValueChange={(value) => setValue("type", value as "Academic" | "commercial")}
        >
          <SelectTrigger>
            <SelectValue placeholder="Sélectionnez un type" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="Academic">Academic</SelectItem>
            <SelectItem value="commercial">Commercial</SelectItem>
          </SelectContent>
        </Select>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="date">Date et heure</Label>
          <Input
            id="date"
            type="datetime-local"
            {...register("date", { required: "La date est requise" })}
          />
          {errors.date && <p className="text-sm text-destructive">{errors.date.message}</p>}
        </div>

        <div className="space-y-2">
          <Label htmlFor="duree">Durée (heures)</Label>
          <Input
            id="duree"
            type="number"
            step="0.5"
            {...register("duree", { required: "La durée est requise", min: 0.5 })}
          />
          {errors.duree && <p className="text-sm text-destructive">{errors.duree.message}</p>}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="nombreInscrits">Nombre d'inscrits</Label>
          <Input
            id="nombreInscrits"
            type="number"
            {...register("nombreInscrits", { required: "Le nombre d'inscrits est requis", min: 0 })}
          />
        </div>

        <div className="space-y-2">
          <Label htmlFor="score">Score</Label>
          <Input
            id="score"
            type="number"
            step="0.1"
            {...register("score", { required: "Le score est requis", min: 0, max: 5 })}
          />
        </div>
      </div>

      <div className="space-y-2">
        <Label htmlFor="keynoteId">Keynote Speaker</Label>
        <Select
          value={watch("keynoteId")?.toString()}
          onValueChange={(value) => setValue("keynoteId", parseInt(value))}
        >
          <SelectTrigger>
            <SelectValue placeholder="Sélectionnez un keynote" />
          </SelectTrigger>
          <SelectContent>
            {keynotes.map((keynote) => (
              <SelectItem key={keynote.id} value={keynote.id.toString()}>
                {keynote.prenom} {keynote.nom} - {keynote.fonction}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div className="flex gap-2 pt-4">
        <Button type="submit" className="flex-1">
          {conference ? "Mettre à jour" : "Créer"}
        </Button>
        <Button type="button" variant="outline" onClick={onCancel}>
          Annuler
        </Button>
      </div>
    </form>
  );
};

export default ConferenceForm;
