
undefined4 FUN_ffff0000b8012700(undefined8 param_1)

{
  undefined **ppuVar1;
  int iVar2;
  undefined4 uVar3;
  undefined **ppuVar4;
  
  DAT_ffff0000b811af30 = (undefined **)0x0;
  DAT_ffff0000b811af38 = (undefined **)0x0;
  ppuVar4 = (undefined **)PTR_LOOP_ffff0000b80adf28;
  if ((undefined **)PTR_LOOP_ffff0000b80adf28 == &PTR_LOOP_ffff0000b80adf20) {
    uVar3 = 0xfffffff9;
  }
  else {
    do {
      *(undefined1 *)(ppuVar4 + 4) = 0;
      iVar2 = (*(code *)ppuVar4[6])(param_1);
      ppuVar1 = DAT_ffff0000b811af30;
      if ((-1 < iVar2) && (ppuVar1 = ppuVar4, *(char *)(ppuVar4 + 4) != '\0')) {
        DAT_ffff0000b811af38 = ppuVar4;
        *(undefined1 *)(ppuVar4 + 4) = 0;
        ppuVar1 = DAT_ffff0000b811af30;
      }
      DAT_ffff0000b811af30 = ppuVar1;
      ppuVar1 = ppuVar4 + 1;
      ppuVar4 = (undefined **)*ppuVar1;
    } while ((undefined **)*ppuVar1 != &PTR_LOOP_ffff0000b80adf20);
    uVar3 = 0xfffffff9;
    if (DAT_ffff0000b811af30 != (undefined **)0x0) {
      uVar3 = 0;
    }
  }
  return uVar3;
}


