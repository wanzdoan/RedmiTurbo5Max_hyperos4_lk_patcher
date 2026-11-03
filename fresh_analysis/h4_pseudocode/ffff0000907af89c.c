
undefined4
FUN_ffff0000907af89c(byte param_1,undefined8 *param_2,int *param_3,uint *param_4,int param_5)

{
  uint uVar1;
  byte bVar2;
  long lVar3;
  byte *pbVar4;
  uint uVar5;
  ulong uVar6;
  undefined4 uVar7;
  long lVar8;
  
  if (DAT_ffff00009090cf50 == 0) {
    FUN_ffff00009077e11c(s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
    return 0x700e;
  }
  uVar1 = *param_4;
  *param_4 = 0;
  pbVar4 = DAT_ffff00009090cf48;
  *param_3 = 1;
  bVar2 = pbVar4[1];
  if ((char)bVar2 < '\0') {
    *param_3 = (bVar2 & 0x7f) + 1;
    if ((bVar2 & 0x7f) == 0) {
      bVar2 = pbVar4[1];
      goto LAB_ffff0000907af8d8;
    }
    lVar8 = 0;
    uVar5 = *param_4;
    do {
      lVar3 = lVar8 + 2;
      lVar8 = lVar8 + 1;
      uVar5 = (uint)pbVar4[lVar3] | uVar5 << 8;
      *param_4 = uVar5;
    } while ((uint)lVar8 < *param_3 - 1U);
  }
  else {
LAB_ffff0000907af8d8:
    uVar5 = (uint)bVar2;
    *param_4 = uVar5;
  }
  bVar2 = *pbVar4;
  uVar7 = 0;
  if (bVar2 != param_1) {
    uVar7 = 0x700f;
  }
  if (uVar1 != uVar5 && uVar1 != 0) {
    uVar7 = 0x7010;
  }
  if (param_5 == 2) {
    uVar1 = *param_3 + uVar5 + 1;
LAB_ffff0000907af998:
    uVar6 = (ulong)uVar1;
  }
  else if (param_5 == 1) {
    uVar6 = (ulong)(*param_3 + 1);
    if (bVar2 == 3) {
      uVar6 = uVar6 + 1;
    }
  }
  else {
    if (param_5 != 0) {
      uVar6 = 0;
      uVar7 = 0x7011;
      goto LAB_ffff0000907af9d4;
    }
    uVar1 = *param_3 + 1;
    if ((bVar2 >> 5 & 1) != 0) goto LAB_ffff0000907af998;
    uVar6 = (ulong)(uVar1 + uVar5);
  }
  if (DAT_ffff00009090cf50 < uVar6) {
    DAT_ffff00009090cf50 = 0;
    return 0x101028;
  }
LAB_ffff0000907af9d4:
  *param_2 = pbVar4;
  DAT_ffff00009090cf50 = DAT_ffff00009090cf50 - uVar6;
  DAT_ffff00009090cf48 = DAT_ffff00009090cf48 + uVar6;
  return uVar7;
}


