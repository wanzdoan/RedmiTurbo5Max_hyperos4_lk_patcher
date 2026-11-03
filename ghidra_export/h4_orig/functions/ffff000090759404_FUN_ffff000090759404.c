// ffff000090759404  size=116  FUN_ffff000090759404

void FUN_ffff000090759404(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff00009081f3fc;
  if (param_1 != 7) {
    if (param_1 == 0xb) {
      FUN_ffff000090759478(&DAT_ffff00009081fd5c);
      puVar1 = (undefined4 *)&DAT_ffff00009081ffb4;
    }
    else if (param_1 == 0x11) {
      FUN_ffff000090759478(&DAT_ffff00009081f3fc);
      puVar1 = (undefined4 *)&DAT_ffff00009081f8ac;
    }
    else {
      puVar1 = &DAT_ffff00009081f3fc + (ulong)param_1 * 0x4b;
    }
  }
  FUN_ffff000090759478(puVar1);
  return;
}


