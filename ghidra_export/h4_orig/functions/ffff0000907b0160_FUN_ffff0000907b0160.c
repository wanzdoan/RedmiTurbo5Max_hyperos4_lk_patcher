// ffff0000907b0160  size=1132  FUN_ffff0000907b0160

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b0160(long param_1,uint param_2,uint param_3)

{
  byte bVar1;
  ulong uVar2;
  bool bVar3;
  int iVar4;
  undefined8 uVar5;
  byte *pbVar6;
  uint uVar7;
  ulong uVar8;
  byte *pbVar9;
  uint uVar10;
  long lVar11;
  byte *pbVar12;
  ulong uVar13;
  uint local_4c;
  byte *local_48;
  
  uVar7 = *(uint *)(param_1 + 0x34);
  local_48 = (byte *)0x0;
  local_4c = 0;
  _DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)uVar7);
  _DAT_ffff00009090cc78 = (ulong)(param_2 - uVar7);
  if (param_2 < uVar7 || param_2 - uVar7 == 0) {
    return 0x100001;
  }
  uVar5 = FUN_ffff0000907b05cc(&local_48,&local_4c);
  if ((int)uVar5 != 0) {
    return uVar5;
  }
  _DAT_ffff00009090cc78 = (ulong)local_4c;
  _DAT_ffff00009090cc68 = local_48;
  if ((param_3 & 1) != 0) {
    _DAT_ffff00009090cf48 = local_48;
    if (local_4c != 0) {
      _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
      iVar4 = FUN_ffff0000907af524(3);
      while (iVar4 == 0) {
        iVar4 = FUN_ffff0000907b2b0c(0xffff00009090c684);
        if ((iVar4 == 0) || (_DAT_ffff00009090cf50 == 0)) {
          if (iVar4 != 0) {
            _DAT_ffff00009090cf48 = (byte *)0x0;
            _DAT_ffff00009090cf50 = 0;
            return 0x7014;
          }
          _DAT_ffff00009090cf48 = (byte *)0x0;
          _DAT_ffff00009090cf50 = 0;
          goto joined_r0xffff0000907b05c4;
        }
        iVar4 = FUN_ffff0000907af524(3);
      }
    }
    _DAT_ffff00009090cf48 = (byte *)0x0;
    _DAT_ffff00009090cf50 = 0;
    return 0x7014;
  }
joined_r0xffff0000907b05c4:
  if (((param_3 >> 1 & 1) != 0) &&
     (uVar5 = FUN_ffff0000907b07b8(&DAT_ffff00009090cad8,0xffff00009090cadc), (int)uVar5 != 0)) {
    return uVar5;
  }
  if ((param_3 >> 2 & 1) == 0) {
joined_r0xffff0000907b03dc:
    if ((param_3 >> 3 & 1) == 0) {
joined_r0xffff0000907b01f0:
      if (((param_3 >> 10 & 1) != 0) &&
         (uVar5 = FUN_ffff0000907b0968(DAT_ffff000090839aa0 + 0x408,DAT_ffff000090839aa0 + 0x40c,1),
         (int)uVar5 != 0)) {
        return uVar5;
      }
      if (((param_3 >> 0xb & 1) != 0) && (uVar5 = FUN_ffff0000907b0d14(), (int)uVar5 != 0)) {
        return uVar5;
      }
      if ((param_3 >> 4 & 1) != 0) {
        uVar5 = FUN_ffff0000907afa00(DAT_ffff000090839aa0 + 0x418,0x30,DAT_ffff000090839aa0 + 0x448)
        ;
        return uVar5;
      }
      return 0;
    }
    _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
    _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
    if (_DAT_ffff00009090cc78 != 0) {
      do {
        iVar4 = FUN_ffff0000907af524(7);
        if (iVar4 != 0) {
          _DAT_ffff00009090cf48 = (byte *)0x0;
          _DAT_ffff00009090cf50 = 0;
          return 0x7014;
        }
        if (_DAT_ffff00009090cf50 == 0) {
LAB_ffff0000907b0558:
          FUN_ffff00009077e11c
                    (s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
          return 0x700e;
        }
        pbVar6 = _DAT_ffff00009090cf48 + 1;
        bVar1 = *pbVar6;
        uVar7 = (uint)bVar1;
        if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
          iVar4 = (uVar7 & 0x7f) + 1;
          uVar10 = uVar7 & 0x7f;
          uVar7 = 0;
          pbVar12 = _DAT_ffff00009090cf48 + 2;
          do {
            uVar10 = uVar10 - 1;
            uVar7 = (uint)*pbVar12 | uVar7 << 8;
            pbVar12 = pbVar12 + 1;
          } while (uVar10 != 0);
        }
        else {
          iVar4 = 1;
        }
        if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
          uVar7 = 0;
        }
        uVar8 = (ulong)(iVar4 + uVar7 + 1);
        uVar2 = _DAT_ffff00009090cf50 - uVar8;
        if (_DAT_ffff00009090cf50 < uVar8) {
          _DAT_ffff00009090cf50 = 0;
          return 0x101028;
        }
        pbVar12 = _DAT_ffff00009090cf48 + uVar8;
        if (*_DAT_ffff00009090cf48 != 2) {
          _DAT_ffff00009090cf48 = pbVar12;
          _DAT_ffff00009090cf50 = uVar2;
          return 0x700f;
        }
        bVar1 = *pbVar6;
        if (((char)bVar1 < '\0') && (uVar13 = (ulong)bVar1 & 0x7f, (int)uVar13 != 0)) {
          _DAT_ffff00009090cad4 = 0;
          lVar11 = uVar13 + 1;
          pbVar9 = _DAT_ffff00009090cf48 + 2;
          do {
            uVar7 = (int)uVar13 - 1;
            uVar13 = (ulong)uVar7;
            _DAT_ffff00009090cad4 = (uint)*pbVar9 | _DAT_ffff00009090cad4 << 8;
            pbVar9 = pbVar9 + 1;
          } while (uVar7 != 0);
        }
        else {
          _DAT_ffff00009090cad4 = (uint)bVar1;
          lVar11 = 1;
        }
        pbVar6 = pbVar6 + lVar11;
        _DAT_ffff00009090cf48 = pbVar12;
        if ((*pbVar6 == 0) && ((char)pbVar6[1] < '\0')) {
          _DAT_ffff00009090cad4 = _DAT_ffff00009090cad4 - 1;
          if (_DAT_ffff00009090cad4 < 5) {
            pbVar6 = pbVar6 + 1;
LAB_ffff0000907b0510:
            _DAT_ffff00009090cf50 = uVar2;
            FUN_ffff0000907ad234(0xffff00009090cad0,pbVar6,_DAT_ffff00009090cad4);
            _DAT_ffff00009090cf48 = (byte *)0x0;
            _DAT_ffff00009090cf50 = 0;
            goto joined_r0xffff0000907b01f0;
          }
        }
        else if (_DAT_ffff00009090cad4 < 5) goto LAB_ffff0000907b0510;
        bVar3 = _DAT_ffff00009090cf50 != uVar8;
        _DAT_ffff00009090cf50 = uVar2;
      } while (bVar3);
    }
  }
  else {
    _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
    _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
    if (_DAT_ffff00009090cc78 != 0) {
      do {
        iVar4 = FUN_ffff0000907af524(0xb);
        if (iVar4 != 0) {
          _DAT_ffff00009090cf48 = (byte *)0x0;
          _DAT_ffff00009090cf50 = 0;
          return 0x7014;
        }
        if (_DAT_ffff00009090cf50 == 0) goto LAB_ffff0000907b0558;
        pbVar6 = _DAT_ffff00009090cf48 + 1;
        bVar1 = *pbVar6;
        uVar7 = (uint)bVar1;
        if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
          iVar4 = (uVar7 & 0x7f) + 1;
          uVar10 = uVar7 & 0x7f;
          uVar7 = 0;
          pbVar12 = _DAT_ffff00009090cf48 + 2;
          do {
            uVar10 = uVar10 - 1;
            uVar7 = (uint)*pbVar12 | uVar7 << 8;
            pbVar12 = pbVar12 + 1;
          } while (uVar10 != 0);
        }
        else {
          iVar4 = 1;
        }
        if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
          uVar7 = 0;
        }
        uVar8 = (ulong)(iVar4 + uVar7 + 1);
        uVar2 = _DAT_ffff00009090cf50 - uVar8;
        if (_DAT_ffff00009090cf50 < uVar8) {
          _DAT_ffff00009090cf50 = 0;
          return 0x101028;
        }
        pbVar12 = _DAT_ffff00009090cf48 + uVar8;
        if (*_DAT_ffff00009090cf48 != 2) {
          _DAT_ffff00009090cf48 = pbVar12;
          _DAT_ffff00009090cf50 = uVar2;
          return 0x700f;
        }
        bVar1 = *pbVar6;
        if (((char)bVar1 < '\0') && (uVar13 = (ulong)bVar1 & 0x7f, (int)uVar13 != 0)) {
          _DAT_ffff00009090cae4 = 0;
          lVar11 = uVar13 + 1;
          pbVar9 = _DAT_ffff00009090cf48 + 2;
          do {
            uVar7 = (int)uVar13 - 1;
            uVar13 = (ulong)uVar7;
            _DAT_ffff00009090cae4 = (uint)*pbVar9 | _DAT_ffff00009090cae4 << 8;
            pbVar9 = pbVar9 + 1;
          } while (uVar7 != 0);
        }
        else {
          _DAT_ffff00009090cae4 = (uint)bVar1;
          lVar11 = 1;
        }
        pbVar6 = pbVar6 + lVar11;
        _DAT_ffff00009090cf48 = pbVar12;
        if ((*pbVar6 == 0) && ((char)pbVar6[1] < '\0')) {
          _DAT_ffff00009090cae4 = _DAT_ffff00009090cae4 - 1;
          if (_DAT_ffff00009090cae4 < 5) {
            pbVar6 = pbVar6 + 1;
LAB_ffff0000907b03c8:
            _DAT_ffff00009090cf50 = uVar2;
            FUN_ffff0000907ad234(0xffff00009090cae0,pbVar6,_DAT_ffff00009090cae4);
            _DAT_ffff00009090cf48 = (byte *)0x0;
            _DAT_ffff00009090cf50 = 0;
            goto joined_r0xffff0000907b03dc;
          }
        }
        else if (_DAT_ffff00009090cae4 < 5) goto LAB_ffff0000907b03c8;
        bVar3 = _DAT_ffff00009090cf50 != uVar8;
        _DAT_ffff00009090cf50 = uVar2;
      } while (bVar3);
    }
  }
  _DAT_ffff00009090cf50 = 0;
  _DAT_ffff00009090cf48 = (byte *)0x0;
  return 0x7014;
}


