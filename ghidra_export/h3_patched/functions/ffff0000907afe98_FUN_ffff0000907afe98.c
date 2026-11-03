// ffff0000907afe98  size=1388  FUN_ffff0000907afe98

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907afe98(long param_1,uint param_2,uint param_3)

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
  
  uVar7 = *(uint *)(param_1 + 0x34);
  _DAT_ffff00009090cba8 = (byte *)(param_1 + (ulong)uVar7);
  _DAT_ffff00009090cbb8 = (ulong)(param_2 - uVar7);
  if ((param_3 & 1) != 0) {
    if (uVar7 != param_2) {
      _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
      _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
      iVar4 = FUN_ffff0000907af25c(3);
      while (iVar4 == 0) {
        iVar4 = FUN_ffff0000907b249c(0xffff00009090c5c4);
        if ((iVar4 == 0) || (_DAT_ffff00009090ce90 == 0)) {
          if (iVar4 != 0) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          goto joined_r0xffff0000907b03fc;
        }
        iVar4 = FUN_ffff0000907af25c(3);
      }
    }
    _DAT_ffff00009090ce88 = (byte *)0x0;
    _DAT_ffff00009090ce90 = 0;
    return 0x7014;
  }
joined_r0xffff0000907b03fc:
  if (((param_3 >> 1 & 1) != 0) &&
     (uVar5 = FUN_ffff0000907b0404(&DAT_ffff00009090ca18,0xffff00009090ca1c), (int)uVar5 != 0)) {
    return uVar5;
  }
  if ((param_3 >> 2 & 1) == 0) {
joined_r0xffff0000907b00bc:
    if ((param_3 >> 3 & 1) == 0) {
joined_r0xffff0000907afef8:
      if (((param_3 >> 10 & 1) != 0) &&
         (uVar5 = FUN_ffff0000907b05b4(DAT_ffff000090839a80 + 0x408,DAT_ffff000090839a80 + 0x40c,1),
         (int)uVar5 != 0)) {
        return uVar5;
      }
      if ((param_3 >> 0xb & 1) != 0) {
        _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
        _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
        if (_DAT_ffff00009090cbb8 != 0) {
          do {
            iVar4 = FUN_ffff0000907af25c(0x10);
            if (iVar4 != 0) {
              _DAT_ffff00009090ce88 = (byte *)0x0;
              _DAT_ffff00009090ce90 = 0;
              return 0x7014;
            }
            if (_DAT_ffff00009090ce90 == 0) {
LAB_ffff0000907b03b8:
              FUN_ffff00009077de54
                        (s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20e7);
              return 0x700e;
            }
            pbVar6 = _DAT_ffff00009090ce88 + 1;
            bVar1 = *pbVar6;
            uVar7 = (uint)bVar1;
            if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
              iVar4 = (uVar7 & 0x7f) + 1;
              uVar10 = uVar7 & 0x7f;
              uVar7 = 0;
              pbVar12 = _DAT_ffff00009090ce88 + 2;
              do {
                uVar10 = uVar10 - 1;
                uVar7 = (uint)*pbVar12 | uVar7 << 8;
                pbVar12 = pbVar12 + 1;
              } while (uVar10 != 0);
            }
            else {
              iVar4 = 1;
            }
            if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
              uVar7 = 0;
            }
            uVar8 = (ulong)(iVar4 + uVar7 + 1);
            uVar2 = _DAT_ffff00009090ce90 - uVar8;
            if (_DAT_ffff00009090ce90 < uVar8) {
              _DAT_ffff00009090ce90 = 0;
              return 0x101028;
            }
            pbVar12 = _DAT_ffff00009090ce88 + uVar8;
            if (*_DAT_ffff00009090ce88 != 2) {
              _DAT_ffff00009090ce88 = pbVar12;
              _DAT_ffff00009090ce90 = uVar2;
              return 0x700f;
            }
            bVar1 = *pbVar6;
            if (((char)bVar1 < '\0') && (uVar13 = (ulong)bVar1 & 0x7f, (int)uVar13 != 0)) {
              _DAT_ffff00009090c9d8 = 0;
              lVar11 = uVar13 + 1;
              pbVar9 = _DAT_ffff00009090ce88 + 2;
              do {
                uVar7 = (int)uVar13 - 1;
                uVar13 = (ulong)uVar7;
                _DAT_ffff00009090c9d8 = (uint)*pbVar9 | _DAT_ffff00009090c9d8 << 8;
                pbVar9 = pbVar9 + 1;
              } while (uVar7 != 0);
            }
            else {
              _DAT_ffff00009090c9d8 = (uint)bVar1;
              lVar11 = 1;
            }
            pbVar6 = pbVar6 + lVar11;
            _DAT_ffff00009090ce88 = pbVar12;
            if ((*pbVar6 == 0) && ((char)pbVar6[1] < '\0')) {
              _DAT_ffff00009090c9d8 = _DAT_ffff00009090c9d8 - 1;
              if (_DAT_ffff00009090c9d8 < 5) {
                pbVar6 = pbVar6 + 1;
LAB_ffff0000907b0374:
                _DAT_ffff00009090ce90 = uVar2;
                FUN_ffff0000907acf74(&DAT_ffff00009090c9d4,pbVar6,_DAT_ffff00009090c9d8);
                _DAT_ffff00009090ce88 = (byte *)0x0;
                _DAT_ffff00009090ce90 = 0;
                goto joined_r0xffff0000907b0388;
              }
            }
            else if (_DAT_ffff00009090c9d8 < 5) goto LAB_ffff0000907b0374;
            bVar3 = _DAT_ffff00009090ce90 != uVar8;
            _DAT_ffff00009090ce90 = uVar2;
          } while (bVar3);
        }
        _DAT_ffff00009090ce88 = (byte *)0x0;
        _DAT_ffff00009090ce90 = 0;
        return 0x7014;
      }
joined_r0xffff0000907b0388:
      if ((param_3 >> 4 & 1) == 0) {
        return 0;
      }
      uVar5 = FUN_ffff0000907af738(DAT_ffff000090839a80 + 0x418,0x30,DAT_ffff000090839a80 + 0x448);
      return uVar5;
    }
    _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
    _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
    if (_DAT_ffff00009090cbb8 != 0) {
      do {
        iVar4 = FUN_ffff0000907af25c(7);
        if (iVar4 != 0) {
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          return 0x7014;
        }
        if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b03b8;
        pbVar6 = _DAT_ffff00009090ce88 + 1;
        bVar1 = *pbVar6;
        uVar7 = (uint)bVar1;
        if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
          iVar4 = (uVar7 & 0x7f) + 1;
          uVar10 = uVar7 & 0x7f;
          uVar7 = 0;
          pbVar12 = _DAT_ffff00009090ce88 + 2;
          do {
            uVar10 = uVar10 - 1;
            uVar7 = (uint)*pbVar12 | uVar7 << 8;
            pbVar12 = pbVar12 + 1;
          } while (uVar10 != 0);
        }
        else {
          iVar4 = 1;
        }
        if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
          uVar7 = 0;
        }
        uVar8 = (ulong)(iVar4 + uVar7 + 1);
        uVar2 = _DAT_ffff00009090ce90 - uVar8;
        if (_DAT_ffff00009090ce90 < uVar8) {
          _DAT_ffff00009090ce90 = 0;
          return 0x101028;
        }
        pbVar12 = _DAT_ffff00009090ce88 + uVar8;
        if (*_DAT_ffff00009090ce88 != 2) {
          _DAT_ffff00009090ce88 = pbVar12;
          _DAT_ffff00009090ce90 = uVar2;
          return 0x700f;
        }
        bVar1 = *pbVar6;
        if (((char)bVar1 < '\0') && (uVar13 = (ulong)bVar1 & 0x7f, (int)uVar13 != 0)) {
          _DAT_ffff00009090ca14 = 0;
          lVar11 = uVar13 + 1;
          pbVar9 = _DAT_ffff00009090ce88 + 2;
          do {
            uVar7 = (int)uVar13 - 1;
            uVar13 = (ulong)uVar7;
            _DAT_ffff00009090ca14 = (uint)*pbVar9 | _DAT_ffff00009090ca14 << 8;
            pbVar9 = pbVar9 + 1;
          } while (uVar7 != 0);
        }
        else {
          _DAT_ffff00009090ca14 = (uint)bVar1;
          lVar11 = 1;
        }
        pbVar6 = pbVar6 + lVar11;
        _DAT_ffff00009090ce88 = pbVar12;
        if ((*pbVar6 == 0) && ((char)pbVar6[1] < '\0')) {
          _DAT_ffff00009090ca14 = _DAT_ffff00009090ca14 - 1;
          if (_DAT_ffff00009090ca14 < 5) {
            pbVar6 = pbVar6 + 1;
LAB_ffff0000907b01f0:
            _DAT_ffff00009090ce90 = uVar2;
            FUN_ffff0000907acf74(0xffff00009090ca10,pbVar6,_DAT_ffff00009090ca14);
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            goto joined_r0xffff0000907afef8;
          }
        }
        else if (_DAT_ffff00009090ca14 < 5) goto LAB_ffff0000907b01f0;
        bVar3 = _DAT_ffff00009090ce90 != uVar8;
        _DAT_ffff00009090ce90 = uVar2;
      } while (bVar3);
    }
  }
  else {
    _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
    _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
    if (_DAT_ffff00009090cbb8 != 0) {
      do {
        iVar4 = FUN_ffff0000907af25c(0xb);
        if (iVar4 != 0) {
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          return 0x7014;
        }
        if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b03b8;
        pbVar6 = _DAT_ffff00009090ce88 + 1;
        bVar1 = *pbVar6;
        uVar7 = (uint)bVar1;
        if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
          iVar4 = (uVar7 & 0x7f) + 1;
          uVar10 = uVar7 & 0x7f;
          uVar7 = 0;
          pbVar12 = _DAT_ffff00009090ce88 + 2;
          do {
            uVar10 = uVar10 - 1;
            uVar7 = (uint)*pbVar12 | uVar7 << 8;
            pbVar12 = pbVar12 + 1;
          } while (uVar10 != 0);
        }
        else {
          iVar4 = 1;
        }
        if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
          uVar7 = 0;
        }
        uVar8 = (ulong)(iVar4 + uVar7 + 1);
        uVar2 = _DAT_ffff00009090ce90 - uVar8;
        if (_DAT_ffff00009090ce90 < uVar8) {
          _DAT_ffff00009090ce90 = 0;
          return 0x101028;
        }
        pbVar12 = _DAT_ffff00009090ce88 + uVar8;
        if (*_DAT_ffff00009090ce88 != 2) {
          _DAT_ffff00009090ce88 = pbVar12;
          _DAT_ffff00009090ce90 = uVar2;
          return 0x700f;
        }
        bVar1 = *pbVar6;
        if (((char)bVar1 < '\0') && (uVar13 = (ulong)bVar1 & 0x7f, (int)uVar13 != 0)) {
          _DAT_ffff00009090ca24 = 0;
          lVar11 = uVar13 + 1;
          pbVar9 = _DAT_ffff00009090ce88 + 2;
          do {
            uVar7 = (int)uVar13 - 1;
            uVar13 = (ulong)uVar7;
            _DAT_ffff00009090ca24 = (uint)*pbVar9 | _DAT_ffff00009090ca24 << 8;
            pbVar9 = pbVar9 + 1;
          } while (uVar7 != 0);
        }
        else {
          _DAT_ffff00009090ca24 = (uint)bVar1;
          lVar11 = 1;
        }
        pbVar6 = pbVar6 + lVar11;
        _DAT_ffff00009090ce88 = pbVar12;
        if ((*pbVar6 == 0) && ((char)pbVar6[1] < '\0')) {
          _DAT_ffff00009090ca24 = _DAT_ffff00009090ca24 - 1;
          if (_DAT_ffff00009090ca24 < 5) {
            pbVar6 = pbVar6 + 1;
LAB_ffff0000907b00a8:
            _DAT_ffff00009090ce90 = uVar2;
            FUN_ffff0000907acf74(0xffff00009090ca20,pbVar6,_DAT_ffff00009090ca24);
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            goto joined_r0xffff0000907b00bc;
          }
        }
        else if (_DAT_ffff00009090ca24 < 5) goto LAB_ffff0000907b00a8;
        bVar3 = _DAT_ffff00009090ce90 != uVar8;
        _DAT_ffff00009090ce90 = uVar2;
      } while (bVar3);
    }
  }
  _DAT_ffff00009090ce90 = 0;
  _DAT_ffff00009090ce88 = (byte *)0x0;
  return 0x7014;
}


