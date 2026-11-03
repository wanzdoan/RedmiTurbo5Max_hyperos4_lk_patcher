
undefined8 FUN_ffff0000be08a9ac(undefined8 param_1,uint *param_2)

{
  byte bVar1;
  bool bVar2;
  int iVar3;
  byte *pbVar4;
  byte *pbVar5;
  uint uVar6;
  ulong uVar7;
  long lVar8;
  uint uVar9;
  byte *pbVar10;
  
  DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
  DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
  do {
    while( true ) {
      if ((DAT_ffff0000be1b7410 == 0) || (iVar3 = FUN_ffff0000be089718(6), iVar3 != 0)) {
        DAT_ffff0000be1b7408 = (byte *)0x0;
        DAT_ffff0000be1b7410 = 0;
        return 0x7014;
      }
      if (DAT_ffff0000be1b7410 == 0) {
        FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
        return 0x700e;
      }
      pbVar4 = DAT_ffff0000be1b7408 + 1;
      bVar1 = *pbVar4;
      uVar6 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar3 = (uVar6 & 0x7f) + 1;
        uVar9 = uVar6 & 0x7f;
        uVar6 = 0;
        pbVar10 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar9 = uVar9 - 1;
          uVar6 = (uint)*pbVar10 | uVar6 << 8;
          pbVar10 = pbVar10 + 1;
        } while (uVar9 != 0);
      }
      else {
        iVar3 = 1;
      }
      if ((*DAT_ffff0000be1b7408 & 0x20) != 0) {
        uVar6 = 0;
      }
      uVar7 = (ulong)(iVar3 + uVar6 + 1);
      bVar2 = DAT_ffff0000be1b7410 < uVar7;
      DAT_ffff0000be1b7410 = DAT_ffff0000be1b7410 - uVar7;
      if (bVar2) {
        DAT_ffff0000be1b7410 = 0;
        return 0x101028;
      }
      pbVar10 = DAT_ffff0000be1b7408 + uVar7;
      if (*DAT_ffff0000be1b7408 != 2) {
        DAT_ffff0000be1b7408 = pbVar10;
        return 0x700f;
      }
      bVar1 = *pbVar4;
      if (((char)bVar1 < '\0') && (uVar7 = (ulong)bVar1 & 0x7f, (int)uVar7 != 0)) {
        uVar6 = 0;
        lVar8 = uVar7 + 1;
        pbVar5 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar9 = (int)uVar7 - 1;
          uVar7 = (ulong)uVar9;
          uVar6 = (uint)*pbVar5 | uVar6 << 8;
          pbVar5 = pbVar5 + 1;
        } while (uVar9 != 0);
      }
      else {
        uVar6 = (uint)bVar1;
        lVar8 = 1;
      }
      pbVar4 = pbVar4 + lVar8;
      DAT_ffff0000be1b7408 = pbVar10;
      if ((*pbVar4 == 0) && ((char)pbVar4[1] < '\0')) break;
      *param_2 = uVar6;
      if (uVar6 < 5) goto LAB_ffff0000be08aaf8;
    }
    uVar6 = uVar6 - 1;
    *param_2 = uVar6;
  } while (4 < uVar6);
  pbVar4 = pbVar4 + 1;
LAB_ffff0000be08aaf8:
  FUN_ffff0000be087428(param_1,pbVar4,uVar6);
  DAT_ffff0000be1b7410 = 0;
  DAT_ffff0000be1b7408 = (byte *)0x0;
  return 0;
}


