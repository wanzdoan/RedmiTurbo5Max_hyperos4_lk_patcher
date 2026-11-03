// ffff0000907596c8  size=44  FUN_ffff0000907596c8

void FUN_ffff0000907596c8(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff00009081f3fc;
  if (param_1 != 7) {
    if (param_1 == 0xb) {
      FUN_ffff000090759728(&DAT_ffff00009081fd5c);
      FUN_ffff000090759728(&DAT_ffff00009081ffb4);
      return;
    }
    puVar1 = &DAT_ffff00009081f3fc + (ulong)param_1 * 0x4b;
  }
  FUN_ffff000090759728(puVar1);
  return;
}


