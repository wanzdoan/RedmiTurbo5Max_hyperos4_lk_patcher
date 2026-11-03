
long FUN_ffff0000be05f874(byte *param_1,undefined8 *param_2,uint param_3)

{
  long lVar1;
  int iVar2;
  byte bVar3;
  long lVar4;
  int iVar5;
  int iVar6;
  undefined4 *puVar7;
  uint uVar8;
  uint uVar9;
  uint uVar10;
  byte *pbVar11;
  uint uVar13;
  long lVar14;
  long lVar15;
  int iVar16;
  byte *pbVar12;
  
  pbVar11 = param_1;
  do {
    pbVar12 = pbVar11;
    pbVar11 = pbVar12 + 1;
    bVar3 = *pbVar12;
    iVar5 = FUN_ffff0000be05f778(bVar3);
  } while (iVar5 != 0);
  uVar13 = (uint)bVar3;
  if ((bVar3 == 0x2b) || (uVar8 = uVar13, bVar3 == 0x2d)) {
    bVar3 = *pbVar11;
    pbVar11 = pbVar12 + 2;
    uVar8 = (uint)bVar3;
  }
  if (((param_3 & 0xffffffef) == 0) && (uVar8 == 0x30)) {
    if ((*pbVar11 | 0x20) == 0x78) {
      uVar8 = (uint)pbVar11[1];
      pbVar11 = pbVar11 + 2;
      param_3 = 0x10;
    }
    else {
      uVar8 = 0x30;
      if (param_3 == 0) {
        param_3 = 8;
      }
    }
  }
  else {
    uVar9 = 8;
    if (uVar8 != 0x30) {
      uVar9 = 10;
    }
    if (param_3 == 0) {
      param_3 = uVar9;
    }
  }
  lVar1 = -0x8000000000000000;
  if (uVar13 != 0x2d) {
    lVar1 = 0x7fffffffffffffff;
  }
  lVar15 = (long)(int)param_3;
  lVar14 = 0;
  lVar4 = 0;
  if (lVar15 != 0) {
    lVar4 = lVar1 / lVar15;
  }
  iVar16 = 0;
  pbVar11 = pbVar11 + -1;
  iVar5 = (int)lVar1 - (int)lVar4 * param_3;
  uVar9 = param_3;
  if (iVar5 < 1) {
    uVar9 = 0;
  }
  uVar10 = 0;
  if (0 < iVar5) {
    uVar10 = (uint)(uVar13 == 0x2d);
  }
  lVar4 = lVar4 + (ulong)uVar10;
  iVar2 = uVar9 - iVar5;
  if (uVar13 != 0x2d) {
    iVar2 = iVar5;
  }
  do {
    iVar5 = FUN_ffff0000be05f7d4(uVar8);
    if (iVar5 == 0) {
      iVar5 = FUN_ffff0000be05f7ec(uVar8);
      if (iVar5 == 0) {
LAB_ffff0000be05fa5c:
        if (param_2 != (undefined8 *)0x0) {
          if (iVar16 != 0) {
            param_1 = pbVar11;
          }
          *param_2 = param_1;
        }
        return lVar14;
      }
      iVar6 = FUN_ffff0000be05f7bc(uVar8);
      iVar5 = -0x57;
      if (iVar6 != 0) {
        iVar5 = -0x37;
      }
      iVar5 = iVar5 + uVar8;
    }
    else {
      iVar5 = uVar8 - 0x30;
    }
    if ((int)param_3 <= iVar5) goto LAB_ffff0000be05fa5c;
    if (iVar16 < 0) {
      pbVar11 = pbVar11 + 1;
      uVar8 = (uint)*pbVar11;
      iVar16 = -1;
    }
    else if (uVar13 == 0x2d) {
      if ((lVar14 < lVar4) || ((lVar14 == lVar4 && (iVar2 < iVar5)))) {
        puVar7 = (undefined4 *)FUN_ffff0000be05f860();
        *puVar7 = 0x22;
        iVar16 = -1;
        pbVar11 = pbVar11 + 1;
        uVar8 = (uint)*pbVar11;
        lVar14 = -0x8000000000000000;
      }
      else {
        lVar14 = lVar14 * lVar15 - (long)iVar5;
LAB_ffff0000be05fa50:
        pbVar11 = pbVar11 + 1;
        uVar8 = (uint)*pbVar11;
        iVar16 = 1;
      }
    }
    else {
      if ((lVar14 <= lVar4) && ((lVar14 != lVar4 || (iVar5 <= iVar2)))) {
        lVar14 = lVar14 * lVar15 + (long)iVar5;
        goto LAB_ffff0000be05fa50;
      }
      puVar7 = (undefined4 *)FUN_ffff0000be05f860();
      *puVar7 = 0x22;
      iVar16 = -1;
      pbVar11 = pbVar11 + 1;
      uVar8 = (uint)*pbVar11;
      lVar14 = 0x7fffffffffffffff;
    }
  } while( true );
}


