// ffff00009075995c  size=44  FUN_ffff00009075995c

void FUN_ffff00009075995c(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff00009081f3fc;
  if (param_1 != 7) {
    if (param_1 == 0x12) {
      FUN_ffff0000907599bc(&DAT_ffff000090820914);
      FUN_ffff0000907599bc(&DAT_ffff000090820a40);
      return;
    }
    puVar1 = &DAT_ffff00009081f3fc + (ulong)param_1 * 0x4b;
  }
  FUN_ffff0000907599bc(puVar1);
  return;
}


