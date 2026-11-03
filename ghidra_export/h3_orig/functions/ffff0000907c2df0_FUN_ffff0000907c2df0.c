// ffff0000907c2df0  size=160  FUN_ffff0000907c2df0

void FUN_ffff0000907c2df0(void)

{
  undefined **ppuVar1;
  bool bVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  ulong uVar5;
  undefined **ppuVar6;
  
  uVar3 = daif;
  if ((undefined **)PTR_LOOP_ffff000090838440 == &PTR_LOOP_ffff000090838438) {
    return;
  }
  ppuVar6 = (undefined **)PTR_LOOP_ffff000090838440;
  if (((uint)uVar3 >> 7 & 1) == 0) {
    uVar4 = daif;
    bVar2 = ((uint)uVar4 >> 7 & 1) == 0;
    if (bVar2) {
      uVar5 = daif;
      daif = uVar5 | 0x80;
    }
    if ((undefined **)PTR_LOOP_ffff000090838440 == &PTR_LOOP_ffff000090838438)
    goto LAB_ffff0000907c2e74;
  }
  else {
    bVar2 = false;
  }
  do {
    if ((code *)ppuVar6[3] != (code *)0x0) {
      (*(code *)ppuVar6[3])(ppuVar6);
    }
    ppuVar1 = ppuVar6 + 1;
    ppuVar6 = (undefined **)*ppuVar1;
  } while ((undefined **)*ppuVar1 != &PTR_LOOP_ffff000090838438);
  if (((uint)uVar3 >> 7 & 1) != 0) {
    return;
  }
LAB_ffff0000907c2e74:
  if (bVar2) {
    uVar5 = daif;
    daif = uVar5 & 0xffffffffffffff7f;
  }
  return;
}


