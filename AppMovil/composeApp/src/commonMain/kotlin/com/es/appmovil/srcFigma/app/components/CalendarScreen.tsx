import { useState } from 'react';

interface CalendarDay {
  day: number;
  month: number;
  year: number;
  hours: number;
  isCurrentMonth: boolean;
  isToday: boolean;
}

export function CalendarScreen() {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [selectedDay, setSelectedDay] = useState<CalendarDay | null>(null);

  // Datos de ejemplo de horas trabajadas por día
  const hoursData: { [key: string]: number } = {
    '2026-3-20': 8,
    '2026-3-21': 7.5,
    '2026-3-22': 0,
    '2026-3-23': 0,
    '2026-3-24': 8,
    '2026-3-25': 6.5,
  };

  const getMonthName = (date: Date) => {
    return date.toLocaleDateString('es-ES', { month: 'long', year: 'numeric' });
  };

  const getDaysInMonth = (date: Date): CalendarDay[] => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDayOfWeek = firstDay.getDay();
    const today = new Date();

    const days: CalendarDay[] = [];

    // Días del mes anterior
    const prevMonthLastDay = new Date(year, month, 0).getDate();
    for (let i = startingDayOfWeek - 1; i >= 0; i--) {
      const day = prevMonthLastDay - i;
      const prevMonth = month === 0 ? 11 : month - 1;
      const prevYear = month === 0 ? year - 1 : year;
      const key = `${prevYear}-${prevMonth + 1}-${day}`;
      days.push({
        day,
        month: prevMonth,
        year: prevYear,
        hours: hoursData[key] || 0,
        isCurrentMonth: false,
        isToday: false,
      });
    }

    // Días del mes actual
    for (let day = 1; day <= daysInMonth; day++) {
      const key = `${year}-${month + 1}-${day}`;
      const isToday = day === today.getDate() && month === today.getMonth() && year === today.getFullYear();
      days.push({
        day,
        month,
        year,
        hours: hoursData[key] || 0,
        isCurrentMonth: true,
        isToday,
      });
    }

    // Días del mes siguiente
    const remainingDays = 42 - days.length;
    for (let day = 1; day <= remainingDays; day++) {
      const nextMonth = month === 11 ? 0 : month + 1;
      const nextYear = month === 11 ? year + 1 : year;
      const key = `${nextYear}-${nextMonth + 1}-${day}`;
      days.push({
        day,
        month: nextMonth,
        year: nextYear,
        hours: hoursData[key] || 0,
        isCurrentMonth: false,
        isToday: false,
      });
    }

    return days;
  };

  const previousMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1));
  };

  const nextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1));
  };

  const days = getDaysInMonth(currentDate);
  const weekDays = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];

  const getColorForHours = (hours: number) => {
    if (hours === 0) return 'bg-muted';
    if (hours < 6) return 'bg-yellow-500/30';
    if (hours < 8) return 'bg-green-500/30';
    return 'bg-green-500/50';
  };

  return (
    <div className="flex-1 overflow-auto p-6 pb-24">
      <div className="mb-6">
        <h1 className="mb-6">Calendario</h1>

        {/* Navegación del mes */}
        <div className="flex items-center justify-between mb-6">
          <button
            onClick={previousMonth}
            className="p-2 hover:bg-muted rounded-[--radius-lg] transition-colors"
          >
            ←
          </button>
          <h3 className="capitalize">{getMonthName(currentDate)}</h3>
          <button
            onClick={nextMonth}
            className="p-2 hover:bg-muted rounded-[--radius-lg] transition-colors"
          >
            →
          </button>
        </div>

        {/* Días de la semana */}
        <div className="grid grid-cols-7 gap-2 mb-2">
          {weekDays.map(day => (
            <div key={day} className="text-center text-muted-foreground text-sm p-2">
              {day}
            </div>
          ))}
        </div>

        {/* Calendario */}
        <div className="grid grid-cols-7 gap-2">
          {days.map((dayData, idx) => (
            <button
              key={idx}
              onClick={() => setSelectedDay(dayData)}
              className={`aspect-square p-2 rounded-[--radius-md] border transition-all ${
                dayData.isToday
                  ? 'border-primary bg-primary/10'
                  : 'border-border'
              } ${
                dayData.isCurrentMonth
                  ? 'text-foreground'
                  : 'text-muted-foreground'
              } ${
                selectedDay?.day === dayData.day && selectedDay?.month === dayData.month
                  ? 'ring-2 ring-primary'
                  : ''
              } hover:bg-muted`}
            >
              <div className="flex flex-col items-center justify-center h-full">
                <span className="text-sm">{dayData.day}</span>
                {dayData.hours > 0 && (
                  <div className={`w-1.5 h-1.5 rounded-full mt-1 ${getColorForHours(dayData.hours).replace('/30', '').replace('/50', '')}`} />
                )}
              </div>
            </button>
          ))}
        </div>
      </div>

      {/* Información del día seleccionado */}
      {selectedDay && (
        <div className="mt-6 bg-card rounded-[--radius-lg] p-6 border border-border">
          <h3 className="mb-4">
            {selectedDay.day} de {new Date(selectedDay.year, selectedDay.month).toLocaleDateString('es-ES', { month: 'long' })}
          </h3>
          <div className="space-y-3">
            <div className="flex justify-between">
              <span className="text-muted-foreground">Horas trabajadas</span>
              <span>{selectedDay.hours}h</span>
            </div>
            {selectedDay.hours > 0 && (
              <>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Entrada</span>
                  <span>09:00</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Salida</span>
                  <span>{`${9 + Math.floor(selectedDay.hours)}:${(selectedDay.hours % 1) * 60 || '00'}`}</span>
                </div>
              </>
            )}
          </div>
        </div>
      )}

      {/* Leyenda */}
      <div className="mt-6 bg-card rounded-[--radius-lg] p-4 border border-border">
        <h4 className="mb-3">Leyenda</h4>
        <div className="space-y-2">
          <div className="flex items-center gap-3">
            <div className="w-4 h-4 rounded-full bg-muted" />
            <span className="text-sm text-muted-foreground">Sin horas</span>
          </div>
          <div className="flex items-center gap-3">
            <div className="w-4 h-4 rounded-full bg-yellow-500" />
            <span className="text-sm text-muted-foreground">Menos de 6h</span>
          </div>
          <div className="flex items-center gap-3">
            <div className="w-4 h-4 rounded-full bg-green-500/60" />
            <span className="text-sm text-muted-foreground">6-8h</span>
          </div>
          <div className="flex items-center gap-3">
            <div className="w-4 h-4 rounded-full bg-green-500" />
            <span className="text-sm text-muted-foreground">8h o más</span>
          </div>
        </div>
      </div>
    </div>
  );
}
