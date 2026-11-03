// ffff0000907b2b0c  size=704  FUN_ffff0000907b2b0c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907b2b0c(long param_1)

{
  byte *pbVar1;
  ulong uVar2;
  byte bVar3;
  bool bVar4;
  uint uVar5;
  ulong uVar6;
  byte *pbVar7;
  ulong uVar8;
  int iVar9;
  uint uVar10;
  uint uVar11;
  ulong uVar12;
  byte *local_28;
  undefined4 local_8;
  int local_4;
  
  uVar6 = FUN_ffff0000907af524(1);
  if ((int)uVar6 != 0) {
    return uVar6;
  }
  pbVar1 = _DAT_ffff00009090cf48;
  if (_DAT_ffff00009090cf50 == 0) goto LAB_ffff0000907b2bec;
  bVar3 = _DAT_ffff00009090cf48[1];
  uVar10 = (uint)bVar3;
  if ((char)bVar3 < '\0') {
    uVar5 = uVar10 & 0x7f;
    local_4 = uVar5 + 1;
    if ((bVar3 & 0x7f) == 0) goto LAB_ffff0000907b2b50;
    uVar11 = uVar10 & 0x7f;
    uVar10 = 0;
    pbVar7 = _DAT_ffff00009090cf48 + 2;
    do {
      uVar11 = uVar11 - 1;
      uVar10 = (uint)*pbVar7 | uVar10 << 8;
      pbVar7 = pbVar7 + 1;
    } while (uVar11 != 0);
    iVar9 = uVar5 + 2;
  }
  else {
LAB_ffff0000907b2b50:
    iVar9 = 2;
  }
  if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
    uVar10 = 0;
  }
  uVar8 = (ulong)(iVar9 + uVar10);
  uVar6 = _DAT_ffff00009090cf50 - uVar8;
  if (_DAT_ffff00009090cf50 < uVar8) {
LAB_ffff0000907b2ba4:
    _DAT_ffff00009090cf48 = pbVar1;
    _DAT_ffff00009090cf50 = 0;
    uVar6 = 0x101028;
  }
  else {
    pbVar1 = _DAT_ffff00009090cf48 + uVar8;
    if (*_DAT_ffff00009090cf48 == 5) {
      if (2 < *DAT_ffff000090839ae8) {
        _DAT_ffff00009090cf48 = pbVar1;
        _DAT_ffff00009090cf50 = uVar6;
        return 0x101023;
      }
      bVar4 = _DAT_ffff00009090cf50 == uVar8;
      _DAT_ffff00009090cf50 = uVar6;
      if (bVar4) {
LAB_ffff0000907b2bec:
        _DAT_ffff00009090cf48 = pbVar1;
        FUN_ffff00009077e11c(s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
        return 0x700e;
      }
      bVar3 = pbVar1[1];
      uVar10 = (uint)bVar3;
      local_8 = 0;
      if (((char)bVar3 < '\0') && ((int)((ulong)bVar3 & 0x7f) != 0)) {
        uVar5 = bVar3 & 0x7f;
        uVar10 = 0;
        pbVar7 = _DAT_ffff00009090cf48 + uVar8 + 2;
        do {
          uVar5 = uVar5 - 1;
          uVar10 = (uint)*pbVar7 | uVar10 << 8;
          pbVar7 = pbVar7 + 1;
        } while (uVar5 != 0);
        uVar12 = ((ulong)bVar3 & 0x7f) + 2;
      }
      else {
        uVar12 = 2;
      }
      bVar3 = *pbVar1;
      uVar2 = uVar12;
      if (bVar3 == 3) {
        uVar2 = uVar12 + 1;
      }
      _DAT_ffff00009090cf50 = uVar6 - uVar2;
      if (uVar6 < uVar2) goto LAB_ffff0000907b2ba4;
      uVar5 = 0;
      if (bVar3 != 3) {
        uVar5 = 0x700f;
      }
      bVar4 = *DAT_ffff000090839ae8 * 0x80 + 0x10f != uVar10;
      local_28 = pbVar1 + uVar2;
      if (bVar4) {
        uVar5 = 0x7010;
      }
      if (bVar4) {
        _DAT_ffff00009090cf48 = local_28;
        return (ulong)uVar5;
      }
      if (bVar3 != 3) {
        _DAT_ffff00009090cf48 = local_28;
        return (ulong)uVar5;
      }
      pbVar1 = local_28;
      if (uVar6 == uVar2) goto LAB_ffff0000907b2bec;
      bVar3 = local_28[1];
      uVar10 = (uint)bVar3;
      local_4 = 1;
      if ((char)bVar3 < '\0') {
        local_4 = (bVar3 & 0x7f) + 1;
        if ((bVar3 & 0x7f) == 0) goto LAB_ffff0000907b2cc0;
        uVar5 = bVar3 & 0x7f;
        uVar10 = 0;
        pbVar7 = _DAT_ffff00009090cf48 + uVar12 + uVar8 + 3;
        do {
          uVar5 = uVar5 - 1;
          uVar10 = (uint)*pbVar7 | uVar10 << 8;
          pbVar7 = pbVar7 + 1;
        } while (uVar5 != 0);
        iVar9 = (bVar3 & 0x7f) + 2;
      }
      else {
LAB_ffff0000907b2cc0:
        iVar9 = 2;
      }
      if ((*local_28 & 0x20) != 0) {
        uVar10 = 0;
      }
      uVar6 = (ulong)(iVar9 + uVar10);
      bVar4 = _DAT_ffff00009090cf50 < uVar6;
      _DAT_ffff00009090cf50 = _DAT_ffff00009090cf50 - uVar6;
      if (bVar4) goto LAB_ffff0000907b2ba4;
      _DAT_ffff00009090cf48 = local_28 + uVar6;
      pbVar1 = _DAT_ffff00009090cf48;
      uVar6 = _DAT_ffff00009090cf50;
      if (*local_28 == 0x30) {
        local_8 = 0;
        uVar6 = FUN_ffff0000907af89c(2,&local_28,&local_4,&local_8,0);
        if ((int)uVar6 != 0) {
          return uVar6;
        }
        uVar6 = FUN_ffff0000907b2a58(local_28,param_1,0x200,param_1 + 0x200);
        if ((int)uVar6 != 0) {
          return uVar6;
        }
        local_8 = 0;
        uVar6 = FUN_ffff0000907af89c(2,&local_28,&local_4,&local_8,0);
        if ((int)uVar6 != 0) {
          return uVar6;
        }
        uVar6 = FUN_ffff0000907b2a58(local_28,param_1 + 0x204,0x200,param_1 + 0x404);
        return uVar6;
      }
    }
    _DAT_ffff00009090cf50 = uVar6;
    _DAT_ffff00009090cf48 = pbVar1;
    uVar6 = 0x700f;
  }
  return uVar6;
}


