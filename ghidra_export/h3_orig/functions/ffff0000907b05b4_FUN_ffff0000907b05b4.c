// ffff0000907b05b4  size=940  FUN_ffff0000907b05b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907b05b4(byte *param_1,uint *param_2,int param_3)

{
  byte bVar1;
  ushort uVar2;
  uint3 uVar3;
  uint *puVar4;
  uint *puVar5;
  uint *puVar6;
  undefined8 *puVar7;
  undefined *puVar8;
  undefined *puVar9;
  bool bVar10;
  int iVar11;
  undefined4 uVar12;
  byte *pbVar13;
  uint uVar14;
  undefined4 uVar15;
  byte *pbVar16;
  uint uVar17;
  ulong uVar18;
  long lVar19;
  undefined8 uVar20;
  byte *pbVar21;
  
  _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
  _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
  pbVar21 = _DAT_ffff00009090ce88;
  if (_DAT_ffff00009090cbb8 == 0) {
LAB_ffff0000907b0730:
    _DAT_ffff00009090ce88 = pbVar21;
    bVar10 = false;
  }
  else {
    do {
      iVar11 = FUN_ffff0000907af25c(0xf);
      pbVar21 = _DAT_ffff00009090ce88;
      if (iVar11 != 0) goto LAB_ffff0000907b0730;
      if (_DAT_ffff00009090ce90 == 0) {
        FUN_ffff00009077de54(s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20fe);
        pbVar21 = _DAT_ffff00009090ce88;
        goto LAB_ffff0000907b0730;
      }
      pbVar13 = _DAT_ffff00009090ce88 + 1;
      bVar1 = *pbVar13;
      uVar17 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar11 = (uVar17 & 0x7f) + 1;
        uVar14 = uVar17 & 0x7f;
        uVar17 = 0;
        pbVar21 = _DAT_ffff00009090ce88 + 2;
        do {
          uVar14 = uVar14 - 1;
          uVar17 = (uint)*pbVar21 | uVar17 << 8;
          pbVar21 = pbVar21 + 1;
        } while (uVar14 != 0);
      }
      else {
        iVar11 = 1;
      }
      if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
        uVar17 = 0;
      }
      uVar18 = (ulong)(iVar11 + uVar17 + 1);
      bVar10 = _DAT_ffff00009090ce90 < uVar18;
      _DAT_ffff00009090ce90 = _DAT_ffff00009090ce90 - uVar18;
      if (bVar10) {
        bVar10 = false;
        _DAT_ffff00009090ce90 = 0;
        goto LAB_ffff0000907b0734;
      }
      pbVar21 = _DAT_ffff00009090ce88 + uVar18;
      if (*_DAT_ffff00009090ce88 != 2) goto LAB_ffff0000907b0730;
      bVar1 = *pbVar13;
      if (((char)bVar1 < '\0') && (uVar18 = (ulong)bVar1 & 0x7f, (int)uVar18 != 0)) {
        uVar17 = 0;
        lVar19 = uVar18 + 1;
        pbVar16 = _DAT_ffff00009090ce88 + 2;
        do {
          uVar14 = (int)uVar18 - 1;
          uVar18 = (ulong)uVar14;
          uVar17 = (uint)*pbVar16 | uVar17 << 8;
          pbVar16 = pbVar16 + 1;
        } while (uVar14 != 0);
      }
      else {
        uVar17 = (uint)bVar1;
        lVar19 = 1;
      }
      pbVar13 = pbVar13 + lVar19;
      _DAT_ffff00009090ce88 = pbVar21;
      if ((*pbVar13 == 0) && ((char)pbVar13[1] < '\0')) {
        uVar17 = uVar17 - 1;
        *param_2 = uVar17;
        if (uVar17 < 5) {
          pbVar13 = pbVar13 + 1;
LAB_ffff0000907b0708:
          FUN_ffff0000907acf74(param_1,pbVar13,uVar17);
          bVar10 = true;
          goto LAB_ffff0000907b0734;
        }
      }
      else {
        *param_2 = uVar17;
        if (uVar17 < 5) goto LAB_ffff0000907b0708;
      }
    } while (_DAT_ffff00009090ce90 != 0);
    bVar10 = false;
  }
LAB_ffff0000907b0734:
  puVar6 = DAT_ffff000090839ac8;
  puVar5 = DAT_ffff000090839ac0;
  puVar4 = DAT_ffff000090839ab8;
  uVar17 = *param_2;
  if (4 < uVar17) {
    return 0x101025;
  }
  if (uVar17 == 0) {
    uVar14 = 0;
  }
  else {
    uVar14 = (uint)*param_1;
    if (uVar17 != 1) {
      uVar2 = CONCAT11(*param_1,param_1[1]);
      uVar14 = (uint)uVar2;
      if (uVar17 != 2) {
        uVar3 = CONCAT21(uVar2,param_1[2]);
        uVar14 = (uint)uVar3;
        if (uVar17 != 3) {
          uVar14 = CONCAT31(uVar3,param_1[3]);
        }
      }
    }
  }
  if (bVar10) {
    if (param_3 != 1) {
      *DAT_ffff000090839ab8 = uVar14 >> 0x10 & 0xf;
      *puVar5 = uVar14 >> 0x14 & 0xf;
      DAT_ffff000090837ea8 = 0xffffffff;
      DAT_ffff000090837eac = 0xffffffff;
      _DAT_ffff00009090ce88 = (byte *)0x0;
      _DAT_ffff00009090ce90 = 0;
      return 0;
    }
    uVar17 = uVar14 & 0xf;
    DAT_ffff000090837ea8 = 1;
    *DAT_ffff000090839b18 = uVar14 & 0x700;
    *puVar6 = uVar17;
  }
  else {
    if (param_3 != 1) {
      *DAT_ffff000090839ac0 = 0;
      *puVar4 = 0;
      DAT_ffff000090837ea8 = 0xffffffff;
      DAT_ffff000090837eac = 0xffffffff;
      _DAT_ffff00009090ce88 = (byte *)0x0;
      _DAT_ffff00009090ce90 = 0;
      return 0;
    }
    uVar17 = 0;
    DAT_ffff000090837ea8 = 0;
    *DAT_ffff000090839ac8 = 0;
  }
  FUN_ffff00009077de54(s___s__g_sec_level____u_ffff0000907f745b,&DAT_ffff0000907f20fe,uVar17);
  puVar9 = PTR_DAT_ffff000090839b08;
  puVar8 = PTR_DAT_ffff000090839b00;
  uVar17 = *DAT_ffff000090839ac8;
  if (uVar17 == 2) {
    uVar15 = 0x200;
    *(undefined4 *)PTR_DAT_ffff000090839af8 = 0x200;
    uVar12 = 0x30;
    uVar20 = DAT_ffff000090839ae0;
  }
  else if (uVar17 == 1) {
    uVar15 = 0x180;
    *(undefined4 *)PTR_DAT_ffff000090839af8 = 0x180;
    uVar12 = 0x30;
    uVar20 = DAT_ffff000090839ae8;
  }
  else {
    if (uVar17 != 0) {
      return 0x101023;
    }
    uVar15 = 0x100;
    *(undefined4 *)PTR_DAT_ffff000090839af8 = 0x100;
    uVar12 = 0x20;
    uVar20 = DAT_ffff000090839ad0;
  }
  puVar7 = DAT_ffff000090839ad8;
  *(undefined4 *)puVar8 = uVar15;
  *(undefined4 *)puVar9 = uVar12;
  *puVar7 = uVar20;
  _DAT_ffff00009090ce90 = 0;
  _DAT_ffff00009090ce88 = (byte *)0x0;
  return 0;
}


