// ffff0000907c3368  size=252  FUN_ffff0000907c3368

long FUN_ffff0000907c3368(undefined8 param_1,undefined1 *param_2,long param_3)

{
  undefined **ppuVar1;
  bool bVar2;
  undefined8 uVar3;
  ulong uVar4;
  long lVar5;
  undefined **ppuVar6;
  uint uVar7;
  
  uVar3 = daif;
  uVar7 = (uint)uVar3;
  lVar5 = param_3;
  if ((undefined **)PTR_LOOP_ffff000090838460 == &PTR_LOOP_ffff000090838458)
  goto joined_r0xffff0000907c3408;
  ppuVar6 = (undefined **)PTR_LOOP_ffff000090838460;
  if ((uVar7 >> 7 & 1) == 0) {
    uVar3 = daif;
    bVar2 = ((uint)uVar3 >> 7 & 1) == 0;
    if (bVar2) {
      uVar4 = daif;
      daif = uVar4 | 0x80;
    }
    if ((undefined **)PTR_LOOP_ffff000090838460 != &PTR_LOOP_ffff000090838458)
    goto LAB_ffff0000907c33e0;
  }
  else {
    bVar2 = false;
LAB_ffff0000907c33e0:
    do {
      if ((code *)ppuVar6[2] != (code *)0x0) {
        (*(code *)ppuVar6[2])(ppuVar6,param_2,param_3);
      }
      ppuVar1 = ppuVar6 + 1;
      ppuVar6 = (undefined **)*ppuVar1;
    } while ((undefined **)*ppuVar1 != &PTR_LOOP_ffff000090838458);
    if ((uVar7 >> 7 & 1) != 0) goto joined_r0xffff0000907c3408;
  }
  if (bVar2) {
    uVar4 = daif;
    daif = uVar4 & 0xffffffffffffff7f;
  }
joined_r0xffff0000907c3408:
  for (; lVar5 != 0; lVar5 = lVar5 + -1) {
    if (((uVar7 >> 7 & 1) == 0) && (uVar3 = daif, ((uint)uVar3 >> 7 & 1) == 0)) {
      uVar4 = daif;
      daif = uVar4 | 0x80;
      FUN_ffff000090722694(*param_2);
      uVar4 = daif;
      daif = uVar4 & 0xffffffffffffff7f;
    }
    else {
      FUN_ffff000090722694(*param_2);
    }
    param_2 = param_2 + 1;
  }
  return param_3;
}


