// ffff0000907b0ec4  size=1272  FUN_ffff0000907b0ec4

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907b0ec4(long param_1,uint param_2,uint param_3)

{
  byte bVar1;
  bool bVar2;
  int iVar3;
  uint uVar4;
  ulong uVar5;
  byte *pbVar6;
  ulong uVar7;
  byte *pbVar8;
  uint uVar9;
  long lVar10;
  ulong uVar11;
  byte *pbVar12;
  uint local_4c;
  byte *local_48;
  
  uVar4 = *(uint *)(param_1 + 0x34);
  local_48 = (byte *)0x0;
  local_4c = 0;
  _DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)uVar4);
  _DAT_ffff00009090cc78 = (ulong)(param_2 - uVar4);
  if (uVar4 <= param_2 && param_2 - uVar4 != 0) {
    uVar5 = FUN_ffff0000907b05cc(&local_48,&local_4c);
    if ((int)uVar5 != 0) {
      return uVar5;
    }
    _DAT_ffff00009090cc78 = (ulong)local_4c;
    _DAT_ffff00009090cc68 = local_48;
    if ((param_3 >> 5 & 1) == 0) {
joined_r0xffff0000907b1248:
      if ((param_3 >> 6 & 1) == 0) {
joined_r0xffff0000907b0f34:
        if (((param_3 >> 1 & 1) != 0) &&
           (uVar5 = FUN_ffff0000907b07b8(&DAT_ffff00009090cb58,&DAT_ffff00009090cb5c),
           (int)uVar5 != 0)) {
          return uVar5;
        }
        if ((param_3 >> 7 & 1) != 0) {
          _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
          _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
          if (_DAT_ffff00009090cc78 != 0) {
            do {
              iVar3 = FUN_ffff0000907af524(9);
              if (iVar3 != 0) {
                _DAT_ffff00009090cf48 = (byte *)0x0;
                _DAT_ffff00009090cf50 = 0;
                return 0x7014;
              }
              if (_DAT_ffff00009090cf50 == 0) {
LAB_ffff0000907b1360:
                FUN_ffff00009077e11c
                          (s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
                return 0x700e;
              }
              pbVar6 = _DAT_ffff00009090cf48 + 1;
              bVar1 = *pbVar6;
              uVar4 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar4 & 0x7f) + 1;
                uVar9 = uVar4 & 0x7f;
                uVar4 = 0;
                pbVar12 = _DAT_ffff00009090cf48 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar4 = (uint)*pbVar12 | uVar4 << 8;
                  pbVar12 = pbVar12 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
                uVar4 = 0;
              }
              uVar7 = (ulong)(iVar3 + uVar4 + 1);
              uVar5 = _DAT_ffff00009090cf50 - uVar7;
              if (_DAT_ffff00009090cf50 < uVar7) {
                _DAT_ffff00009090cf50 = 0;
                return 0x101028;
              }
              pbVar12 = _DAT_ffff00009090cf48 + uVar7;
              if (*_DAT_ffff00009090cf48 != 2) {
                _DAT_ffff00009090cf48 = pbVar12;
                _DAT_ffff00009090cf50 = uVar5;
                return 0x700f;
              }
              bVar1 = *pbVar6;
              if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
                _DAT_ffff00009090cb64 = 0;
                lVar10 = uVar11 + 1;
                pbVar8 = _DAT_ffff00009090cf48 + 2;
                do {
                  uVar4 = (int)uVar11 - 1;
                  uVar11 = (ulong)uVar4;
                  _DAT_ffff00009090cb64 = (uint)*pbVar8 | _DAT_ffff00009090cb64 << 8;
                  pbVar8 = pbVar8 + 1;
                } while (uVar4 != 0);
              }
              else {
                _DAT_ffff00009090cb64 = (uint)bVar1;
                lVar10 = 1;
              }
              pbVar6 = pbVar6 + lVar10;
              _DAT_ffff00009090cf48 = pbVar12;
              if ((*pbVar6 == 0) && ((char)pbVar6[1] < '\0')) {
                _DAT_ffff00009090cb64 = _DAT_ffff00009090cb64 - 1;
                if (_DAT_ffff00009090cb64 < 5) {
                  pbVar6 = pbVar6 + 1;
LAB_ffff0000907b1204:
                  _DAT_ffff00009090cf50 = uVar5;
                  FUN_ffff0000907ad234(0xffff00009090cb60,pbVar6,_DAT_ffff00009090cb64);
                  _DAT_ffff00009090cf48 = (byte *)0x0;
                  _DAT_ffff00009090cf50 = 0;
                  goto joined_r0xffff0000907b1218;
                }
              }
              else if (_DAT_ffff00009090cb64 < 5) goto LAB_ffff0000907b1204;
              bVar2 = _DAT_ffff00009090cf50 != uVar7;
              _DAT_ffff00009090cf50 = uVar5;
            } while (bVar2);
          }
          _DAT_ffff00009090cf48 = (byte *)0x0;
          _DAT_ffff00009090cf50 = 0;
          return 0x7014;
        }
joined_r0xffff0000907b1218:
        if (((param_3 >> 10 & 1) != 0) &&
           (uVar5 = FUN_ffff0000907b0968(DAT_ffff000090839ab0,DAT_ffff000090839ab0 + 4,2),
           (int)uVar5 != 0)) {
          return uVar5;
        }
        if (((param_3 >> 0xc & 1) != 0) && (uVar5 = FUN_ffff0000907b13bc(), (int)uVar5 != 0)) {
          return uVar5;
        }
        if ((param_3 >> 0xd & 1) != 0) {
          FUN_ffff0000907b157c();
        }
        if (((param_3 >> 0xe & 1) != 0) && (uVar5 = FUN_ffff0000907b171c(), (int)uVar5 != 0)) {
          return uVar5;
        }
        if (((param_3 >> 0xf & 1) != 0) && (uVar5 = FUN_ffff0000907b18dc(), (int)uVar5 != 0)) {
          return uVar5;
        }
        if (((param_3 >> 0x10 & 1) != 0) && (uVar5 = FUN_ffff0000907b1a60(), (int)uVar5 != 0)) {
          return uVar5;
        }
        return 0;
      }
      _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
      _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
      if (_DAT_ffff00009090cc78 != 0) {
        do {
          iVar3 = FUN_ffff0000907af524(5);
          if (iVar3 != 0) {
            _DAT_ffff00009090cf48 = (byte *)0x0;
            _DAT_ffff00009090cf50 = 0;
            return 0x7014;
          }
          if (_DAT_ffff00009090cf50 == 0) goto LAB_ffff0000907b1360;
          bVar1 = _DAT_ffff00009090cf48[1];
          uVar4 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar4 & 0x7f) + 1;
            uVar9 = uVar4 & 0x7f;
            uVar4 = 0;
            pbVar6 = _DAT_ffff00009090cf48 + 2;
            do {
              uVar9 = uVar9 - 1;
              uVar4 = (uint)*pbVar6 | uVar4 << 8;
              pbVar6 = pbVar6 + 1;
            } while (uVar9 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
            uVar4 = 0;
          }
          uVar7 = (ulong)(iVar3 + uVar4 + 1);
          uVar5 = _DAT_ffff00009090cf50 - uVar7;
          if (_DAT_ffff00009090cf50 < uVar7) goto LAB_ffff0000907b1384;
          pbVar6 = _DAT_ffff00009090cf48 + uVar7;
          if (*_DAT_ffff00009090cf48 != 3) {
            _DAT_ffff00009090cf48 = pbVar6;
            _DAT_ffff00009090cf50 = uVar5;
            return 0x700f;
          }
          bVar1 = _DAT_ffff00009090cf48[1];
          if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
            _DAT_ffff00009090cb20 = 0;
            lVar10 = uVar11 + 1;
            pbVar12 = _DAT_ffff00009090cf48 + 2;
            do {
              uVar4 = (int)uVar11 - 1;
              uVar11 = (ulong)uVar4;
              _DAT_ffff00009090cb20 = (uint)*pbVar12 | _DAT_ffff00009090cb20 << 8;
              pbVar12 = pbVar12 + 1;
            } while (uVar4 != 0);
          }
          else {
            _DAT_ffff00009090cb20 = (uint)bVar1;
            lVar10 = 1;
          }
          _DAT_ffff00009090cb20 = _DAT_ffff00009090cb20 - 1;
          if (_DAT_ffff00009090cb20 < 0x31) {
            pbVar12 = _DAT_ffff00009090cf48 + lVar10 + 2;
            _DAT_ffff00009090cf48 = pbVar6;
            _DAT_ffff00009090cf50 = uVar5;
            FUN_ffff0000907ad234(0xffff00009090caf0,pbVar12);
            _DAT_ffff00009090cf48 = (byte *)0x0;
            _DAT_ffff00009090cf50 = 0;
            goto joined_r0xffff0000907b0f34;
          }
          bVar2 = _DAT_ffff00009090cf50 != uVar7;
          _DAT_ffff00009090cf48 = pbVar6;
          _DAT_ffff00009090cf50 = uVar5;
        } while (bVar2);
      }
    }
    else {
      _DAT_ffff00009090cf48 = local_48;
      _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
      if (local_4c != 0) {
        do {
          iVar3 = FUN_ffff0000907af524(10);
          if (iVar3 != 0) {
            _DAT_ffff00009090cf48 = (byte *)0x0;
            _DAT_ffff00009090cf50 = 0;
            return 0x7014;
          }
          if (_DAT_ffff00009090cf50 == 0) goto LAB_ffff0000907b1360;
          bVar1 = _DAT_ffff00009090cf48[1];
          uVar4 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar4 & 0x7f) + 1;
            uVar9 = uVar4 & 0x7f;
            uVar4 = 0;
            pbVar6 = _DAT_ffff00009090cf48 + 2;
            do {
              uVar9 = uVar9 - 1;
              uVar4 = (uint)*pbVar6 | uVar4 << 8;
              pbVar6 = pbVar6 + 1;
            } while (uVar9 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
            uVar4 = 0;
          }
          uVar7 = (ulong)(iVar3 + uVar4 + 1);
          uVar5 = _DAT_ffff00009090cf50 - uVar7;
          if (_DAT_ffff00009090cf50 < uVar7) goto LAB_ffff0000907b1384;
          pbVar6 = _DAT_ffff00009090cf48 + uVar7;
          if (*_DAT_ffff00009090cf48 != 3) {
            _DAT_ffff00009090cf48 = pbVar6;
            _DAT_ffff00009090cf50 = uVar5;
            return 0x700f;
          }
          bVar1 = _DAT_ffff00009090cf48[1];
          if (((char)bVar1 < '\0') && (uVar11 = (ulong)bVar1 & 0x7f, (int)uVar11 != 0)) {
            _DAT_ffff00009090cb54 = 0;
            lVar10 = uVar11 + 1;
            pbVar12 = _DAT_ffff00009090cf48 + 2;
            do {
              uVar4 = (int)uVar11 - 1;
              uVar11 = (ulong)uVar4;
              _DAT_ffff00009090cb54 = (uint)*pbVar12 | _DAT_ffff00009090cb54 << 8;
              pbVar12 = pbVar12 + 1;
            } while (uVar4 != 0);
          }
          else {
            _DAT_ffff00009090cb54 = (uint)bVar1;
            lVar10 = 1;
          }
          _DAT_ffff00009090cb54 = _DAT_ffff00009090cb54 - 1;
          if (_DAT_ffff00009090cb54 < 0x31) {
            pbVar12 = _DAT_ffff00009090cf48 + lVar10 + 2;
            _DAT_ffff00009090cf48 = pbVar6;
            _DAT_ffff00009090cf50 = uVar5;
            FUN_ffff0000907ad234(0xffff00009090cb24,pbVar12);
            _DAT_ffff00009090cf48 = (byte *)0x0;
            _DAT_ffff00009090cf50 = 0;
            goto joined_r0xffff0000907b1248;
          }
          bVar2 = _DAT_ffff00009090cf50 != uVar7;
          _DAT_ffff00009090cf48 = pbVar6;
          _DAT_ffff00009090cf50 = uVar5;
        } while (bVar2);
      }
    }
    _DAT_ffff00009090cf48 = (byte *)0x0;
    _DAT_ffff00009090cf50 = 0;
    return 0x7014;
  }
  uVar4 = 1;
LAB_ffff0000907b0fb4:
  return (ulong)(uVar4 | 0x100000);
LAB_ffff0000907b1384:
  uVar4 = 0x1028;
  _DAT_ffff00009090cf50 = 0;
  goto LAB_ffff0000907b0fb4;
}


