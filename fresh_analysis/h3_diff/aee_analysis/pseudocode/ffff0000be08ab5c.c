
undefined4 FUN_ffff0000be08ab5c(byte *param_1,uint *param_2,int param_3)

{
  byte bVar1;
  ushort uVar2;
  uint3 uVar3;
  undefined *puVar4;
  undefined *puVar5;
  undefined *puVar6;
  bool bVar7;
  int iVar8;
  undefined4 uVar9;
  byte *pbVar10;
  uint uVar11;
  undefined4 uVar12;
  byte *pbVar13;
  uint uVar14;
  ulong uVar15;
  long lVar16;
  undefined *puVar17;
  byte *pbVar18;
  
  DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
  DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
  pbVar18 = DAT_ffff0000be1b7408;
  if (DAT_ffff0000be1b7170 == 0) {
LAB_ffff0000be08acd8:
    DAT_ffff0000be1b7408 = pbVar18;
    bVar7 = false;
  }
  else {
    do {
      iVar8 = FUN_ffff0000be089718(0xf);
      pbVar18 = DAT_ffff0000be1b7408;
      if (iVar8 != 0) goto LAB_ffff0000be08acd8;
      if (DAT_ffff0000be1b7410 == 0) {
        FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
        pbVar18 = DAT_ffff0000be1b7408;
        goto LAB_ffff0000be08acd8;
      }
      pbVar10 = DAT_ffff0000be1b7408 + 1;
      bVar1 = *pbVar10;
      uVar14 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar8 = (uVar14 & 0x7f) + 1;
        uVar11 = uVar14 & 0x7f;
        uVar14 = 0;
        pbVar18 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar11 = uVar11 - 1;
          uVar14 = (uint)*pbVar18 | uVar14 << 8;
          pbVar18 = pbVar18 + 1;
        } while (uVar11 != 0);
      }
      else {
        iVar8 = 1;
      }
      if ((*DAT_ffff0000be1b7408 & 0x20) != 0) {
        uVar14 = 0;
      }
      uVar15 = (ulong)(iVar8 + uVar14 + 1);
      bVar7 = DAT_ffff0000be1b7410 < uVar15;
      DAT_ffff0000be1b7410 = DAT_ffff0000be1b7410 - uVar15;
      if (bVar7) {
        bVar7 = false;
        DAT_ffff0000be1b7410 = 0;
        goto LAB_ffff0000be08acdc;
      }
      pbVar18 = DAT_ffff0000be1b7408 + uVar15;
      if (*DAT_ffff0000be1b7408 != 2) goto LAB_ffff0000be08acd8;
      bVar1 = *pbVar10;
      if (((char)bVar1 < '\0') && (uVar15 = (ulong)bVar1 & 0x7f, (int)uVar15 != 0)) {
        uVar14 = 0;
        lVar16 = uVar15 + 1;
        pbVar13 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar11 = (int)uVar15 - 1;
          uVar15 = (ulong)uVar11;
          uVar14 = (uint)*pbVar13 | uVar14 << 8;
          pbVar13 = pbVar13 + 1;
        } while (uVar11 != 0);
      }
      else {
        uVar14 = (uint)bVar1;
        lVar16 = 1;
      }
      pbVar10 = pbVar10 + lVar16;
      DAT_ffff0000be1b7408 = pbVar18;
      if ((*pbVar10 == 0) && ((char)pbVar10[1] < '\0')) {
        uVar14 = uVar14 - 1;
        *param_2 = uVar14;
        if (uVar14 < 5) {
          pbVar10 = pbVar10 + 1;
LAB_ffff0000be08acb0:
          FUN_ffff0000be087428(param_1,pbVar10,uVar14);
          bVar7 = true;
          goto LAB_ffff0000be08acdc;
        }
      }
      else {
        *param_2 = uVar14;
        if (uVar14 < 5) goto LAB_ffff0000be08acb0;
      }
    } while (DAT_ffff0000be1b7410 != 0);
    bVar7 = false;
  }
LAB_ffff0000be08acdc:
  puVar17 = PTR_DAT_ffff0000be0f5750;
  puVar5 = PTR_DAT_ffff0000be0f5748;
  puVar4 = PTR_DAT_ffff0000be0f5740;
  uVar14 = *param_2;
  if (4 < uVar14) {
    return 0x101025;
  }
  if (uVar14 == 0) {
    uVar11 = 0;
  }
  else {
    uVar11 = (uint)*param_1;
    if (uVar14 != 1) {
      uVar2 = CONCAT11(*param_1,param_1[1]);
      uVar11 = (uint)uVar2;
      if (uVar14 != 2) {
        uVar3 = CONCAT21(uVar2,param_1[2]);
        uVar11 = (uint)uVar3;
        if (uVar14 != 3) {
          uVar11 = CONCAT31(uVar3,param_1[3]);
        }
      }
    }
  }
  if (bVar7) {
    if (param_3 != 1) {
      *(uint *)PTR_DAT_ffff0000be0f5740 = uVar11 >> 0x10 & 0xf;
      *(uint *)puVar5 = uVar11 >> 0x14 & 0xf;
      DAT_ffff0000be0f40a0 = 0xffffffff;
      DAT_ffff0000be0f40a4 = 0xffffffff;
      DAT_ffff0000be1b7408 = (byte *)0x0;
      DAT_ffff0000be1b7410 = 0;
      return 0;
    }
    uVar14 = uVar11 & 0xf;
    DAT_ffff0000be0f40a0 = 1;
    *(uint *)PTR_DAT_ffff0000be0f57a0 = uVar11 & 0x700;
    *(uint *)puVar17 = uVar14;
  }
  else {
    if (param_3 != 1) {
      *(undefined4 *)PTR_DAT_ffff0000be0f5748 = 0;
      *(undefined4 *)puVar4 = 0;
      DAT_ffff0000be0f40a0 = 0xffffffff;
      DAT_ffff0000be0f40a4 = 0xffffffff;
      DAT_ffff0000be1b7408 = (byte *)0x0;
      DAT_ffff0000be1b7410 = 0;
      return 0;
    }
    uVar14 = 0;
    DAT_ffff0000be0f40a0 = 0;
    *(undefined4 *)PTR_DAT_ffff0000be0f5750 = 0;
  }
  FUN_ffff0000be05fd98(s___s__g_sec_level____u_ffff0000be0bef6c,&DAT_ffff0000be0bb811,uVar14);
  puVar5 = PTR_DAT_ffff0000be0f5790;
  puVar4 = PTR_DAT_ffff0000be0f5788;
  iVar8 = *(int *)PTR_DAT_ffff0000be0f5750;
  if (iVar8 == 2) {
    uVar12 = 0x200;
    *(undefined4 *)PTR_DAT_ffff0000be0f5780 = 0x200;
    uVar9 = 0x30;
    puVar17 = PTR_DAT_ffff0000be0f5768;
  }
  else if (iVar8 == 1) {
    uVar12 = 0x180;
    *(undefined4 *)PTR_DAT_ffff0000be0f5780 = 0x180;
    uVar9 = 0x30;
    puVar17 = PTR_DAT_ffff0000be0f5770;
  }
  else {
    if (iVar8 != 0) {
      return 0x101023;
    }
    uVar12 = 0x100;
    *(undefined4 *)PTR_DAT_ffff0000be0f5780 = 0x100;
    uVar9 = 0x20;
    puVar17 = PTR_DAT_ffff0000be0f5758;
  }
  puVar6 = PTR_DAT_ffff0000be0f5760;
  *(undefined4 *)puVar4 = uVar12;
  *(undefined4 *)puVar5 = uVar9;
  *(undefined **)puVar6 = puVar17;
  DAT_ffff0000be1b7410 = 0;
  DAT_ffff0000be1b7408 = (byte *)0x0;
  return 0;
}


