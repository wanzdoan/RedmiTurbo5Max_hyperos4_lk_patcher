// ffff00009071aec4  size=208  FUN_ffff00009071aec4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff00009071aec4(void)

{
  long *plVar1;
  long *plVar2;
  long lVar3;
  
  FUN_ffff0000907175f4(&LAB_ffff0000908181f8,0xffffffff);
  plVar1 = (long *)(_DAT_ffff000090850c50 + 8);
  plVar2 = plVar1 + (ulong)(_DAT_ffff00009085ac90 - 1) * 3;
  if (plVar2 < plVar1) {
    lVar3 = 0;
  }
  else {
    lVar3 = 0;
    do {
      if (*plVar1 != 0) {
        lVar3 = plVar1[1] + lVar3;
      }
      plVar1 = plVar1 + 3;
    } while (plVar1 <= plVar2);
  }
  if (_DAT_ffff00009085b8a0 != 0) {
    plVar1 = (long *)(_DAT_ffff000090850c50 + 0xc18);
    do {
      if (*plVar1 != 0) {
        lVar3 = plVar1[1] + lVar3;
      }
      plVar1 = plVar1 + 0x13;
    } while (plVar1 <= (long *)(_DAT_ffff000090850c50 + 0x5780U));
  }
  FUN_ffff00009071768c(&LAB_ffff0000908181f8);
  return lVar3;
}


