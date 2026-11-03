// ffff00009075916c  size=116  FUN_ffff00009075916c

void FUN_ffff00009075916c(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff00009081f3dc;
  if (param_1 != 7) {
    if (param_1 == 0xb) {
      FUN_ffff0000907591e0(&DAT_ffff00009081fd3c);
      puVar1 = (undefined4 *)&DAT_ffff00009081ff94;
    }
    else if (param_1 == 0x11) {
      FUN_ffff0000907591e0(&DAT_ffff00009081f3dc);
      puVar1 = (undefined4 *)&DAT_ffff00009081f88c;
    }
    else {
      puVar1 = &DAT_ffff00009081f3dc + (ulong)param_1 * 0x4b;
    }
  }
  FUN_ffff0000907591e0(puVar1);
  return;
}


