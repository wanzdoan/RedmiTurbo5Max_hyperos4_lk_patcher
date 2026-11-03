// ffff0000907b0960  size=2840  FUN_ffff0000907b0960

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907b0960(long param_1,uint param_2,uint param_3)

{
  byte bVar1;
  bool bVar2;
  int iVar3;
  ulong uVar4;
  byte *pbVar5;
  ulong uVar6;
  long lVar7;
  byte *pbVar8;
  uint uVar9;
  uint uVar10;
  byte *pbVar11;
  ulong uVar12;
  
  uVar10 = *(uint *)(param_1 + 0x34);
  _DAT_ffff00009090cba8 = (byte *)(param_1 + (ulong)uVar10);
  _DAT_ffff00009090cbb8 = (ulong)(param_2 - uVar10);
  if ((param_3 >> 5 & 1) == 0) {
joined_r0xffff0000907b0d48:
    if ((param_3 >> 6 & 1) == 0) {
joined_r0xffff0000907b0fb4:
      if (((param_3 >> 1 & 1) != 0) &&
         (uVar4 = FUN_ffff0000907b0404(&DAT_ffff00009090ca98,&DAT_ffff00009090ca9c), (int)uVar4 != 0
         )) {
        return uVar4;
      }
      if ((param_3 >> 7 & 1) == 0) {
joined_r0xffff0000907b09c0:
        if (((param_3 >> 10 & 1) != 0) &&
           (uVar4 = FUN_ffff0000907b05b4(DAT_ffff000090839a90,DAT_ffff000090839a90 + 4,2),
           (int)uVar4 != 0)) {
          return uVar4;
        }
        if ((param_3 >> 0xc & 1) != 0) {
          _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
          _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
          if (_DAT_ffff00009090cbb8 == 0) {
            uVar4 = 0;
          }
          else {
            while (uVar4 = FUN_ffff0000907af25c(0x11), (int)uVar4 == 0) {
              if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b1428;
              bVar1 = _DAT_ffff00009090ce88[1];
              uVar10 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar10 & 0x7f) + 1;
                uVar9 = uVar10 & 0x7f;
                uVar10 = 0;
                pbVar5 = _DAT_ffff00009090ce88 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = (uint)*pbVar5 | uVar10 << 8;
                  pbVar5 = pbVar5 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
                uVar10 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar10 + 1);
              uVar4 = _DAT_ffff00009090ce90 - uVar6;
              if (_DAT_ffff00009090ce90 < uVar6) {
                _DAT_ffff00009090ce90 = 0;
                return 0x101028;
              }
              pbVar5 = _DAT_ffff00009090ce88 + uVar6;
              if (*_DAT_ffff00009090ce88 != 3) {
                _DAT_ffff00009090ce88 = pbVar5;
                _DAT_ffff00009090ce90 = uVar4;
                return 0x700f;
              }
              bVar1 = _DAT_ffff00009090ce88[1];
              if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
                _DAT_ffff00009090cb14 = 0;
                lVar7 = uVar12 + 1;
                pbVar11 = _DAT_ffff00009090ce88 + 2;
                do {
                  uVar10 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar10;
                  _DAT_ffff00009090cb14 = (uint)*pbVar11 | _DAT_ffff00009090cb14 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar10 != 0);
              }
              else {
                _DAT_ffff00009090cb14 = (uint)bVar1;
                lVar7 = 1;
              }
              _DAT_ffff00009090cb14 = _DAT_ffff00009090cb14 - 1;
              if (_DAT_ffff00009090cb14 < 0x21) {
                pbVar11 = _DAT_ffff00009090ce88 + lVar7 + 2;
                _DAT_ffff00009090ce88 = pbVar5;
                _DAT_ffff00009090ce90 = uVar4;
                FUN_ffff0000907acf74(0xffff00009090caf4,pbVar11);
                _DAT_ffff00009090ce88 = (byte *)0x0;
                _DAT_ffff00009090ce90 = 0;
                goto LAB_ffff0000907b09e0;
              }
              bVar2 = _DAT_ffff00009090ce90 == uVar6;
              _DAT_ffff00009090ce88 = pbVar5;
              _DAT_ffff00009090ce90 = uVar4;
              if (bVar2) {
                uVar10 = 0x101028;
                if (*DAT_ffff000090839ab8 != 0) {
                  uVar10 = 0x7014;
                }
                _DAT_ffff00009090ce88 = (byte *)0x0;
                _DAT_ffff00009090ce90 = 0;
                return (ulong)uVar10;
              }
            }
          }
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          if (_DAT_ffff00009090c518 != 0) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          if ((int)uVar4 != 0) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return uVar4;
          }
        }
LAB_ffff0000907b09e0:
        if ((param_3 >> 0xd & 1) != 0) {
          _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
          _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
          pbVar5 = _DAT_ffff00009090ce88;
          uVar4 = _DAT_ffff00009090ce90;
          if (_DAT_ffff00009090cbb8 != 0) {
            do {
              iVar3 = FUN_ffff0000907af25c(0x12);
              pbVar5 = _DAT_ffff00009090ce88;
              uVar4 = _DAT_ffff00009090ce90;
              if (iVar3 != 0) break;
              if (_DAT_ffff00009090ce90 == 0) {
                FUN_ffff00009077de54
                          (s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20e7);
                pbVar5 = _DAT_ffff00009090ce88;
                uVar4 = _DAT_ffff00009090ce90;
                break;
              }
              bVar1 = _DAT_ffff00009090ce88[1];
              uVar10 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar10 & 0x7f) + 1;
                uVar9 = uVar10 & 0x7f;
                uVar10 = 0;
                pbVar11 = _DAT_ffff00009090ce88 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = (uint)*pbVar11 | uVar10 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
                uVar10 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar10 + 1);
              uVar4 = _DAT_ffff00009090ce90 - uVar6;
              if (_DAT_ffff00009090ce90 < uVar6) {
                _DAT_ffff00009090ce90 = 0;
                uVar4 = _DAT_ffff00009090ce90;
                break;
              }
              pbVar5 = _DAT_ffff00009090ce88 + uVar6;
              if (*_DAT_ffff00009090ce88 != 3) break;
              bVar1 = _DAT_ffff00009090ce88[1];
              if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
                uVar10 = 0;
                lVar7 = uVar12 + 1;
                pbVar11 = _DAT_ffff00009090ce88 + 2;
                do {
                  uVar9 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar9;
                  uVar10 = (uint)*pbVar11 | uVar10 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar9 != 0);
              }
              else {
                uVar10 = (uint)bVar1;
                lVar7 = 1;
              }
              if (uVar10 - 1 < 0x41) {
                pbVar11 = _DAT_ffff00009090ce88 + lVar7 + 2;
                _DAT_ffff00009090ce88 = pbVar5;
                _DAT_ffff00009090ce90 = uVar4;
                FUN_ffff0000907acf74(0xffff00009090cb18,pbVar11);
                goto LAB_ffff0000907b105c;
              }
              bVar2 = _DAT_ffff00009090ce90 != uVar6;
              _DAT_ffff00009090ce88 = pbVar5;
              _DAT_ffff00009090ce90 = uVar4;
            } while (bVar2);
          }
          _DAT_ffff00009090ce90 = uVar4;
          _DAT_ffff00009090ce88 = pbVar5;
          if (_DAT_ffff00009090c518 != 0) {
            FUN_ffff00009077eb88(DAT_ffff000090839a90 + 0xf0,0,0x40);
            FUN_ffff00009077de54
                      (s___s__Decode_checksum_lost__ffff0000907e069a,&DAT_ffff0000907f20e7);
          }
LAB_ffff0000907b105c:
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
        }
        if ((param_3 >> 0xe & 1) != 0) {
          _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
          _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
          if (_DAT_ffff00009090cbb8 != 0) {
            do {
              iVar3 = FUN_ffff0000907af25c(0x13);
              if (iVar3 != 0) break;
              if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b1428;
              bVar1 = _DAT_ffff00009090ce88[1];
              uVar10 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar10 & 0x7f) + 1;
                uVar9 = uVar10 & 0x7f;
                uVar10 = 0;
                pbVar5 = _DAT_ffff00009090ce88 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = (uint)*pbVar5 | uVar10 << 8;
                  pbVar5 = pbVar5 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
                uVar10 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar10 + 1);
              uVar4 = _DAT_ffff00009090ce90 - uVar6;
              if (_DAT_ffff00009090ce90 < uVar6) {
                _DAT_ffff00009090ce90 = 0;
                return 0x101028;
              }
              pbVar5 = _DAT_ffff00009090ce88 + uVar6;
              if (*_DAT_ffff00009090ce88 != 3) {
                _DAT_ffff00009090ce88 = pbVar5;
                _DAT_ffff00009090ce90 = uVar4;
                return 0x700f;
              }
              bVar1 = _DAT_ffff00009090ce88[1];
              if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
                _DAT_ffff00009090cb78 = 0;
                lVar7 = uVar12 + 1;
                pbVar11 = _DAT_ffff00009090ce88 + 2;
                do {
                  uVar10 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar10;
                  _DAT_ffff00009090cb78 = (uint)*pbVar11 | _DAT_ffff00009090cb78 << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar10 != 0);
              }
              else {
                _DAT_ffff00009090cb78 = (uint)bVar1;
                lVar7 = 1;
              }
              _DAT_ffff00009090cb78 = _DAT_ffff00009090cb78 - 1;
              if (_DAT_ffff00009090cb78 < 0x21) {
                pbVar11 = _DAT_ffff00009090ce88 + lVar7 + 2;
                _DAT_ffff00009090ce88 = pbVar5;
                _DAT_ffff00009090ce90 = uVar4;
                FUN_ffff0000907acf74(0xffff00009090cb58,pbVar11);
                goto LAB_ffff0000907b11d8;
              }
              bVar2 = _DAT_ffff00009090ce90 != uVar6;
              _DAT_ffff00009090ce88 = pbVar5;
              _DAT_ffff00009090ce90 = uVar4;
            } while (bVar2);
          }
          if (_DAT_ffff00009090c518 != 0) {
            FUN_ffff00009077eb88(DAT_ffff000090839a90 + 0x130,0,0x20);
            FUN_ffff00009077de54
                      (s___s__FE_without_cust_wrapped__ffff0000907e3d18,&DAT_ffff0000907f20e7);
          }
LAB_ffff0000907b11d8:
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
        }
        if ((param_3 >> 0xf & 1) != 0) {
          _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
          _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
          if (_DAT_ffff00009090cbb8 != 0) {
            do {
              iVar3 = FUN_ffff0000907af25c(0x14);
              if (iVar3 != 0) break;
              if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b1428;
              bVar1 = _DAT_ffff00009090ce88[1];
              uVar10 = (uint)bVar1;
              if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
                iVar3 = (uVar10 & 0x7f) + 1;
                uVar9 = uVar10 & 0x7f;
                uVar10 = 0;
                pbVar5 = _DAT_ffff00009090ce88 + 2;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = (uint)*pbVar5 | uVar10 << 8;
                  pbVar5 = pbVar5 + 1;
                } while (uVar9 != 0);
              }
              else {
                iVar3 = 1;
              }
              if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
                uVar10 = 0;
              }
              uVar6 = (ulong)(iVar3 + uVar10 + 1);
              uVar4 = _DAT_ffff00009090ce90 - uVar6;
              if (_DAT_ffff00009090ce90 < uVar6) {
                _DAT_ffff00009090ce90 = 0;
                return 0x101028;
              }
              pbVar5 = _DAT_ffff00009090ce88 + uVar6;
              if (*_DAT_ffff00009090ce88 != 3) {
                _DAT_ffff00009090ce88 = pbVar5;
                _DAT_ffff00009090ce90 = uVar4;
                return 0x700f;
              }
              bVar1 = _DAT_ffff00009090ce88[1];
              if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
                _DAT_ffff00009090cb8c = 0;
                lVar7 = uVar12 + 1;
                pbVar11 = _DAT_ffff00009090ce88 + 2;
                do {
                  uVar10 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar10;
                  _DAT_ffff00009090cb8c = (uint)*pbVar11 | _DAT_ffff00009090cb8c << 8;
                  pbVar11 = pbVar11 + 1;
                } while (uVar10 != 0);
              }
              else {
                _DAT_ffff00009090cb8c = (uint)bVar1;
                lVar7 = 1;
              }
              _DAT_ffff00009090cb8c = _DAT_ffff00009090cb8c - 1;
              if (_DAT_ffff00009090cb8c < 0x11) {
                pbVar11 = _DAT_ffff00009090ce88 + lVar7 + 2;
                _DAT_ffff00009090ce88 = pbVar5;
                _DAT_ffff00009090ce90 = uVar4;
                FUN_ffff0000907acf74(0xffff00009090cb7c,pbVar11);
                break;
              }
              bVar2 = _DAT_ffff00009090ce90 != uVar6;
              _DAT_ffff00009090ce88 = pbVar5;
              _DAT_ffff00009090ce90 = uVar4;
            } while (bVar2);
          }
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
        }
        if ((param_3 >> 0x10 & 1) == 0) {
          return 0;
        }
        _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
        _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
        if (_DAT_ffff00009090cbb8 != 0) {
          do {
            iVar3 = FUN_ffff0000907af25c(0x15);
            if (iVar3 != 0) {
              _DAT_ffff00009090ce88 = (byte *)0x0;
              _DAT_ffff00009090ce90 = 0;
              return 0;
            }
            if (_DAT_ffff00009090ce90 == 0) {
LAB_ffff0000907b1428:
              FUN_ffff00009077de54
                        (s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20e7);
              return 0x700e;
            }
            bVar1 = _DAT_ffff00009090ce88[1];
            uVar10 = (uint)bVar1;
            if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
              iVar3 = (uVar10 & 0x7f) + 1;
              uVar9 = uVar10 & 0x7f;
              uVar10 = 0;
              pbVar5 = _DAT_ffff00009090ce88 + 2;
              do {
                uVar9 = uVar9 - 1;
                uVar10 = (uint)*pbVar5 | uVar10 << 8;
                pbVar5 = pbVar5 + 1;
              } while (uVar9 != 0);
            }
            else {
              iVar3 = 1;
            }
            if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
              uVar10 = 0;
            }
            uVar6 = (ulong)(iVar3 + uVar10 + 1);
            uVar4 = _DAT_ffff00009090ce90 - uVar6;
            if (_DAT_ffff00009090ce90 < uVar6) {
              _DAT_ffff00009090ce90 = 0;
              return 0x101028;
            }
            pbVar5 = _DAT_ffff00009090ce88 + uVar6;
            if (*_DAT_ffff00009090ce88 != 3) {
              _DAT_ffff00009090ce90 = uVar4;
              _DAT_ffff00009090ce88 = pbVar5;
              return 0x700f;
            }
            bVar1 = _DAT_ffff00009090ce88[1];
            if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
              _DAT_ffff00009090cba0 = 0;
              lVar7 = uVar12 + 1;
              pbVar11 = _DAT_ffff00009090ce88 + 2;
              do {
                uVar10 = (int)uVar12 - 1;
                uVar12 = (ulong)uVar10;
                _DAT_ffff00009090cba0 = (uint)*pbVar11 | _DAT_ffff00009090cba0 << 8;
                pbVar11 = pbVar11 + 1;
              } while (uVar10 != 0);
            }
            else {
              _DAT_ffff00009090cba0 = (uint)bVar1;
              lVar7 = 1;
            }
            _DAT_ffff00009090cba0 = _DAT_ffff00009090cba0 - 1;
            if (_DAT_ffff00009090cba0 < 0x11) {
              pbVar11 = _DAT_ffff00009090ce88 + lVar7 + 2;
              _DAT_ffff00009090ce88 = pbVar5;
              _DAT_ffff00009090ce90 = uVar4;
              FUN_ffff0000907acf74(0xffff00009090cb90,pbVar11);
              _DAT_ffff00009090ce88 = (byte *)0x0;
              _DAT_ffff00009090ce90 = 0;
              return 0;
            }
            bVar2 = _DAT_ffff00009090ce90 != uVar6;
            _DAT_ffff00009090ce88 = pbVar5;
            _DAT_ffff00009090ce90 = uVar4;
          } while (bVar2);
        }
        _DAT_ffff00009090ce88 = (byte *)0x0;
        _DAT_ffff00009090ce90 = 0;
        return 0;
      }
      _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
      _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
      if (_DAT_ffff00009090cbb8 != 0) {
        do {
          iVar3 = FUN_ffff0000907af25c(9);
          if (iVar3 != 0) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b1428;
          pbVar5 = _DAT_ffff00009090ce88 + 1;
          bVar1 = *pbVar5;
          uVar10 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar10 & 0x7f) + 1;
            uVar9 = uVar10 & 0x7f;
            uVar10 = 0;
            pbVar11 = _DAT_ffff00009090ce88 + 2;
            do {
              uVar9 = uVar9 - 1;
              uVar10 = (uint)*pbVar11 | uVar10 << 8;
              pbVar11 = pbVar11 + 1;
            } while (uVar9 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
            uVar10 = 0;
          }
          uVar6 = (ulong)(iVar3 + uVar10 + 1);
          uVar4 = _DAT_ffff00009090ce90 - uVar6;
          if (_DAT_ffff00009090ce90 < uVar6) {
            _DAT_ffff00009090ce90 = 0;
            return 0x101028;
          }
          pbVar11 = _DAT_ffff00009090ce88 + uVar6;
          if (*_DAT_ffff00009090ce88 != 2) {
            _DAT_ffff00009090ce88 = pbVar11;
            _DAT_ffff00009090ce90 = uVar4;
            return 0x700f;
          }
          bVar1 = *pbVar5;
          if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
            _DAT_ffff00009090caa4 = 0;
            lVar7 = uVar12 + 1;
            pbVar8 = _DAT_ffff00009090ce88 + 2;
            do {
              uVar10 = (int)uVar12 - 1;
              uVar12 = (ulong)uVar10;
              _DAT_ffff00009090caa4 = (uint)*pbVar8 | _DAT_ffff00009090caa4 << 8;
              pbVar8 = pbVar8 + 1;
            } while (uVar10 != 0);
          }
          else {
            _DAT_ffff00009090caa4 = (uint)bVar1;
            lVar7 = 1;
          }
          pbVar5 = pbVar5 + lVar7;
          _DAT_ffff00009090ce88 = pbVar11;
          if ((*pbVar5 == 0) && ((char)pbVar5[1] < '\0')) {
            _DAT_ffff00009090caa4 = _DAT_ffff00009090caa4 - 1;
            if (_DAT_ffff00009090caa4 < 5) {
              pbVar5 = pbVar5 + 1;
LAB_ffff0000907b0d14:
              _DAT_ffff00009090ce90 = uVar4;
              FUN_ffff0000907acf74(0xffff00009090caa0,pbVar5,_DAT_ffff00009090caa4);
              _DAT_ffff00009090ce88 = (byte *)0x0;
              _DAT_ffff00009090ce90 = 0;
              goto joined_r0xffff0000907b09c0;
            }
          }
          else if (_DAT_ffff00009090caa4 < 5) goto LAB_ffff0000907b0d14;
          bVar2 = _DAT_ffff00009090ce90 != uVar6;
          _DAT_ffff00009090ce90 = uVar4;
        } while (bVar2);
      }
    }
    else {
      _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
      _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
      if (_DAT_ffff00009090cbb8 != 0) {
        do {
          iVar3 = FUN_ffff0000907af25c(5);
          if (iVar3 != 0) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b1428;
          bVar1 = _DAT_ffff00009090ce88[1];
          uVar10 = (uint)bVar1;
          if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
            iVar3 = (uVar10 & 0x7f) + 1;
            uVar9 = uVar10 & 0x7f;
            uVar10 = 0;
            pbVar5 = _DAT_ffff00009090ce88 + 2;
            do {
              uVar9 = uVar9 - 1;
              uVar10 = (uint)*pbVar5 | uVar10 << 8;
              pbVar5 = pbVar5 + 1;
            } while (uVar9 != 0);
          }
          else {
            iVar3 = 1;
          }
          if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
            uVar10 = 0;
          }
          uVar6 = (ulong)(iVar3 + uVar10 + 1);
          uVar4 = _DAT_ffff00009090ce90 - uVar6;
          if (_DAT_ffff00009090ce90 < uVar6) {
            _DAT_ffff00009090ce90 = 0;
            return 0x101028;
          }
          pbVar5 = _DAT_ffff00009090ce88 + uVar6;
          if (*_DAT_ffff00009090ce88 != 3) {
            _DAT_ffff00009090ce88 = pbVar5;
            _DAT_ffff00009090ce90 = uVar4;
            return 0x700f;
          }
          bVar1 = _DAT_ffff00009090ce88[1];
          if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
            _DAT_ffff00009090ca60 = 0;
            lVar7 = uVar12 + 1;
            pbVar11 = _DAT_ffff00009090ce88 + 2;
            do {
              uVar10 = (int)uVar12 - 1;
              uVar12 = (ulong)uVar10;
              _DAT_ffff00009090ca60 = (uint)*pbVar11 | _DAT_ffff00009090ca60 << 8;
              pbVar11 = pbVar11 + 1;
            } while (uVar10 != 0);
          }
          else {
            _DAT_ffff00009090ca60 = (uint)bVar1;
            lVar7 = 1;
          }
          _DAT_ffff00009090ca60 = _DAT_ffff00009090ca60 - 1;
          if (_DAT_ffff00009090ca60 < 0x31) {
            pbVar11 = _DAT_ffff00009090ce88 + lVar7 + 2;
            _DAT_ffff00009090ce88 = pbVar5;
            _DAT_ffff00009090ce90 = uVar4;
            FUN_ffff0000907acf74(0xffff00009090ca30,pbVar11);
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            goto joined_r0xffff0000907b0fb4;
          }
          bVar2 = _DAT_ffff00009090ce90 != uVar6;
          _DAT_ffff00009090ce88 = pbVar5;
          _DAT_ffff00009090ce90 = uVar4;
        } while (bVar2);
      }
    }
  }
  else {
    _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
    _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
    if (uVar10 != param_2) {
      do {
        iVar3 = FUN_ffff0000907af25c(10);
        if (iVar3 != 0) {
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          return 0x7014;
        }
        if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b1428;
        bVar1 = _DAT_ffff00009090ce88[1];
        uVar10 = (uint)bVar1;
        if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
          iVar3 = (uVar10 & 0x7f) + 1;
          uVar9 = uVar10 & 0x7f;
          uVar10 = 0;
          pbVar5 = _DAT_ffff00009090ce88 + 2;
          do {
            uVar9 = uVar9 - 1;
            uVar10 = (uint)*pbVar5 | uVar10 << 8;
            pbVar5 = pbVar5 + 1;
          } while (uVar9 != 0);
        }
        else {
          iVar3 = 1;
        }
        if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
          uVar10 = 0;
        }
        uVar6 = (ulong)(iVar3 + uVar10 + 1);
        uVar4 = _DAT_ffff00009090ce90 - uVar6;
        if (_DAT_ffff00009090ce90 < uVar6) {
          _DAT_ffff00009090ce90 = 0;
          return 0x101028;
        }
        pbVar5 = _DAT_ffff00009090ce88 + uVar6;
        if (*_DAT_ffff00009090ce88 != 3) {
          _DAT_ffff00009090ce88 = pbVar5;
          _DAT_ffff00009090ce90 = uVar4;
          return 0x700f;
        }
        bVar1 = _DAT_ffff00009090ce88[1];
        if (((char)bVar1 < '\0') && (uVar12 = (ulong)bVar1 & 0x7f, (int)uVar12 != 0)) {
          _DAT_ffff00009090ca94 = 0;
          lVar7 = uVar12 + 1;
          pbVar11 = _DAT_ffff00009090ce88 + 2;
          do {
            uVar10 = (int)uVar12 - 1;
            uVar12 = (ulong)uVar10;
            _DAT_ffff00009090ca94 = (uint)*pbVar11 | _DAT_ffff00009090ca94 << 8;
            pbVar11 = pbVar11 + 1;
          } while (uVar10 != 0);
        }
        else {
          _DAT_ffff00009090ca94 = (uint)bVar1;
          lVar7 = 1;
        }
        _DAT_ffff00009090ca94 = _DAT_ffff00009090ca94 - 1;
        if (_DAT_ffff00009090ca94 < 0x31) {
          pbVar11 = _DAT_ffff00009090ce88 + lVar7 + 2;
          _DAT_ffff00009090ce88 = pbVar5;
          _DAT_ffff00009090ce90 = uVar4;
          FUN_ffff0000907acf74(0xffff00009090ca64,pbVar11);
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          goto joined_r0xffff0000907b0d48;
        }
        bVar2 = _DAT_ffff00009090ce90 != uVar6;
        _DAT_ffff00009090ce88 = pbVar5;
        _DAT_ffff00009090ce90 = uVar4;
      } while (bVar2);
    }
  }
  _DAT_ffff00009090ce88 = (byte *)0x0;
  _DAT_ffff00009090ce90 = 0;
  return 0x7014;
}


