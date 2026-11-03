
undefined8 FUN_ffff0000be08a7c0(long *param_1,int *param_2)

{
  byte bVar1;
  byte *pbVar2;
  byte *pbVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  ulong uVar8;
  int iVar9;
  ulong uVar10;
  uint uVar11;
  
  pbVar3 = DAT_ffff0000be1b7160;
  uVar8 = DAT_ffff0000be1b7170;
  do {
    do {
      while( true ) {
        pbVar2 = pbVar3;
        if (uVar8 == 0) {
          DAT_ffff0000be1b7408 = (byte *)0x0;
          DAT_ffff0000be1b7410 = 0;
          return 0x7014;
        }
        bVar1 = pbVar2[1];
        uVar5 = (uint)bVar1;
        if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
          iVar4 = (uVar5 & 0x7f) + 1;
          uVar7 = uVar5 & 0x7f;
          uVar5 = 0;
          pbVar3 = pbVar2 + 2;
          do {
            uVar7 = uVar7 - 1;
            uVar5 = (uint)*pbVar3 | uVar5 << 8;
            pbVar3 = pbVar3 + 1;
          } while (uVar7 != 0);
        }
        else {
          iVar4 = 1;
        }
        if ((*pbVar2 & 0x20) != 0) {
          uVar5 = 0;
        }
        uVar10 = (ulong)(iVar4 + uVar5 + 1);
        DAT_ffff0000be1b7410 = uVar8 - uVar10;
        DAT_ffff0000be1b7408 = pbVar2;
        if (uVar10 <= uVar8) break;
LAB_ffff0000be08a818:
        uVar8 = 0;
        pbVar3 = DAT_ffff0000be1b7408;
      }
      DAT_ffff0000be1b7408 = pbVar2 + uVar10;
      pbVar3 = DAT_ffff0000be1b7408;
      uVar8 = DAT_ffff0000be1b7410;
    } while (*pbVar2 != 0x30);
    if (DAT_ffff0000be1b7410 == 0) {
      FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
      pbVar3 = DAT_ffff0000be1b7408;
      uVar8 = DAT_ffff0000be1b7410;
    }
    else {
      bVar1 = DAT_ffff0000be1b7408[1];
      uVar7 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        uVar11 = uVar7 & 0x7f;
        iVar9 = (uVar7 & 0x7f) + 1;
        uVar7 = 0;
        pbVar3 = pbVar2 + uVar10 + 2;
        do {
          uVar11 = uVar11 - 1;
          uVar7 = (uint)*pbVar3 | uVar7 << 8;
          pbVar3 = pbVar3 + 1;
        } while (uVar11 != 0);
      }
      else {
        iVar9 = 1;
      }
      uVar10 = (ulong)(iVar9 + uVar7 + 1);
      uVar8 = DAT_ffff0000be1b7410 - uVar10;
      if (DAT_ffff0000be1b7410 < uVar10) goto LAB_ffff0000be08a818;
      pbVar3 = DAT_ffff0000be1b7408 + uVar10;
      if (*DAT_ffff0000be1b7408 == 0x30) {
        bVar1 = DAT_ffff0000be1b7408[1];
        uVar7 = (uint)bVar1;
        if (((char)bVar1 < '\0') && (uVar11 = uVar7 & 0x7f, (bVar1 & 0x7f) != 0)) {
          uVar6 = uVar7 & 0x7f;
          uVar7 = 0;
          pbVar3 = pbVar2 + (ulong)(uVar5 + iVar4 + 1) + 2;
          do {
            uVar6 = uVar6 - 1;
            uVar7 = (uint)*pbVar3 | uVar7 << 8;
            pbVar3 = pbVar3 + 1;
          } while (uVar6 != 0);
          iVar4 = uVar11 + 2;
        }
        else {
          iVar4 = 2;
        }
        *param_1 = (long)DAT_ffff0000be1b7408;
        *param_2 = uVar7 + iVar4;
        DAT_ffff0000be1b7408 = (byte *)0x0;
        DAT_ffff0000be1b7410 = 0;
        return 0;
      }
    }
  } while( true );
}


