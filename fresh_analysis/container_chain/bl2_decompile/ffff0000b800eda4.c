
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000b800eda4(undefined8 param_1)

{
  uint uVar1;
  int iVar2;
  undefined **ppuVar3;
  
  DAT_ffff0000b811ae80 = 0;
  _DAT_ffff0000b811ae88 = 0;
  if ((undefined **)PTR_LOOP_ffff0000b80add38 == &PTR_LOOP_ffff0000b80add30) {
    iVar2 = 0;
  }
  else {
    iVar2 = 0;
    ppuVar3 = (undefined **)PTR_LOOP_ffff0000b80add38;
    do {
      uVar1 = (**(code **)ppuVar3[3])(param_1);
      if (uVar1 < 2) {
        iVar2 = iVar2 + 1;
        (&DAT_ffff0000b811ae80)[uVar1] = ppuVar3;
      }
      ppuVar3 = (undefined **)ppuVar3[1];
    } while (ppuVar3 != &PTR_LOOP_ffff0000b80add30);
  }
  return iVar2;
}


