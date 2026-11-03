// ffff0000907b1be4  size=1416  FUN_ffff0000907b1be4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907b1be4(long param_1,uint param_2)

{
  undefined1 uVar1;
  byte bVar2;
  bool bVar3;
  uint uVar4;
  int iVar5;
  ulong uVar6;
  undefined4 uVar7;
  ushort uVar8;
  long lVar9;
  undefined1 *puVar10;
  uint uVar11;
  ulong uVar12;
  byte *pbVar13;
  uint local_6c;
  byte *local_68;
  
  uVar4 = 0x100001;
  FUN_ffff00009077ee50(&DAT_ffff00009090cf58,0,0x408);
  local_68 = (byte *)0x0;
  _DAT_ffff00009090d158 = 0x200;
  _DAT_ffff00009090d35c = 0x200;
  uVar11 = *(uint *)(param_1 + 0x34);
  local_6c = 0;
  DAT_ffff000090837ed0 = *(int *)(param_1 + 0x3c);
  _DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)uVar11);
  _DAT_ffff00009090cc78 = (ulong)(param_2 - uVar11);
  if (param_2 < uVar11 || param_2 - uVar11 == 0) {
    uVar11 = 1;
LAB_ffff0000907b1d18:
    return (ulong)(uVar11 | 0x100000);
  }
  uVar6 = FUN_ffff0000907b05cc(&local_68,&local_6c);
  lVar9 = DAT_ffff000090839aa0;
  if ((int)uVar6 != 0) {
    return uVar6;
  }
  _DAT_ffff00009090cc78 = (ulong)local_6c;
  _DAT_ffff00009090cc68 = local_68;
  uVar6 = FUN_ffff0000907b0968(DAT_ffff000090839aa0 + 0x408,DAT_ffff000090839aa0 + 0x40c,1);
  if ((int)uVar6 != 0) {
    return uVar6;
  }
  if (DAT_ffff000090837ec8 != 1) {
    uVar7 = 0;
LAB_ffff0000907b1d44:
    _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
    _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
    *DAT_ffff000090839b30 = uVar7;
    if (_DAT_ffff00009090cf50 != 0) {
      do {
        iVar5 = FUN_ffff0000907b2b0c(&DAT_ffff00009090cf58);
        if (iVar5 == 0) break;
      } while (_DAT_ffff00009090cf50 != 0);
      _DAT_ffff00009090cf48 = _DAT_ffff00009090cc68;
      _DAT_ffff00009090cf50 = _DAT_ffff00009090cc78;
      if (iVar5 == 0) {
        while (_DAT_ffff00009090cf50 != 0) {
          while( true ) {
            iVar5 = FUN_ffff0000907af524(2);
            if (iVar5 != 0) {
              _DAT_ffff00009090cf48 = (byte *)0x0;
              _DAT_ffff00009090cf50 = 0;
              return 0x7014;
            }
            if (_DAT_ffff00009090cf50 == 0) break;
            bVar2 = _DAT_ffff00009090cf48[1];
            uVar11 = (uint)bVar2;
            if (((char)bVar2 < '\0') && ((bVar2 & 0x7f) != 0)) {
              iVar5 = (uVar11 & 0x7f) + 1;
              uVar4 = uVar11 & 0x7f;
              uVar11 = 0;
              pbVar13 = _DAT_ffff00009090cf48 + 2;
              do {
                uVar4 = uVar4 - 1;
                uVar11 = (uint)*pbVar13 | uVar11 << 8;
                pbVar13 = pbVar13 + 1;
              } while (uVar4 != 0);
            }
            else {
              iVar5 = 1;
            }
            if ((*_DAT_ffff00009090cf48 & 0x20) != 0) {
              uVar11 = 0;
            }
            uVar6 = (ulong)(iVar5 + uVar11 + 1);
            bVar3 = _DAT_ffff00009090cf50 < uVar6;
            _DAT_ffff00009090cf50 = _DAT_ffff00009090cf50 - uVar6;
            if (bVar3) {
              _DAT_ffff00009090cf48 = (byte *)0x0;
              _DAT_ffff00009090cf50 = 0;
              return 0x7014;
            }
            if (*_DAT_ffff00009090cf48 == 3) {
              bVar2 = _DAT_ffff00009090cf48[1];
              if (((char)bVar2 < '\0') && (uVar12 = (ulong)bVar2 & 0x7f, (int)uVar12 != 0)) {
                uVar11 = 0;
                lVar9 = uVar12 + 1;
                pbVar13 = _DAT_ffff00009090cf48 + 2;
                do {
                  uVar4 = (int)uVar12 - 1;
                  uVar12 = (ulong)uVar4;
                  uVar11 = (uint)*pbVar13 | uVar11 << 8;
                  pbVar13 = pbVar13 + 1;
                } while (uVar4 != 0);
              }
              else {
                uVar11 = (uint)bVar2;
                lVar9 = 1;
              }
              uVar11 = uVar11 - 1;
              if (uVar11 < 0x40d) {
                pbVar13 = _DAT_ffff00009090cf48 + lVar9 + 2;
                _DAT_ffff00009090cf48 = _DAT_ffff00009090cf48 + uVar6;
                FUN_ffff0000907ad234(&DAT_ffff00009090d360,pbVar13,uVar11);
                _DAT_ffff00009090cf48 = (byte *)0x0;
                _DAT_ffff00009090cf50 = 0;
                if (_DAT_ffff00009090d368 == 0) {
                  uVar6 = FUN_ffff0000907afff0(&DAT_ffff00009090d360,uVar11,&DAT_ffff00009090cf58);
                }
                else {
                  uVar4 = _DAT_ffff00009090d35c + 1 >> 1;
                  if (uVar4 != _DAT_ffff00009090d360) {
                    _DAT_ffff00009090cf48 = (byte *)0x0;
                    _DAT_ffff00009090cf50 = 0;
                    return 0x7015;
                  }
                  if (1 < _DAT_ffff00009090d35c + 1) {
                    lVar9 = 0;
                    do {
                      if ((_DAT_ffff00009090d35c & 1) == 0) {
                        uVar8 = *(ushort *)(&DAT_ffff00009090d15c + lVar9) >> 8 |
                                *(ushort *)(&DAT_ffff00009090d15c + lVar9) << 8;
                      }
                      else {
                        uVar8 = (ushort)DAT_ffff00009090d15c;
                        if (lVar9 != 0) {
                          uVar8 = CONCAT11((&DAT_ffff00009090d15c)[(int)lVar9 - 1],
                                           (&DAT_ffff00009090d15c)[lVar9]);
                        }
                      }
                      if (*(ushort *)(lVar9 + -0xffff6f6f2c94) != uVar8) {
                        _DAT_ffff00009090cf48 = (byte *)0x0;
                        _DAT_ffff00009090cf50 = 0;
                        return 0x7016;
                      }
                      lVar9 = lVar9 + 2;
                    } while ((ulong)uVar4 << 1 != lVar9);
                  }
                  uVar4 = _DAT_ffff00009090d158 + 1 >> 1;
                  if (uVar4 != _DAT_ffff00009090d364) {
                    _DAT_ffff00009090cf48 = (byte *)0x0;
                    _DAT_ffff00009090cf50 = 0;
                    return 0x7017;
                  }
                  if (1 < _DAT_ffff00009090d158 + 1) {
                    lVar9 = 0;
                    do {
                      if ((_DAT_ffff00009090d158 & 1) == 0) {
                        uVar8 = *(ushort *)(&DAT_ffff00009090cf58 + lVar9) >> 8 |
                                *(ushort *)(&DAT_ffff00009090cf58 + lVar9) << 8;
                      }
                      else {
                        uVar8 = (ushort)DAT_ffff00009090cf58;
                        if (lVar9 != 0) {
                          uVar8 = CONCAT11((&DAT_ffff00009090cf58)[(int)lVar9 - 1],
                                           (&DAT_ffff00009090cf58)[lVar9]);
                        }
                      }
                      if (*(ushort *)(lVar9 + -0xffff6f6f2b94) != uVar8) {
                        _DAT_ffff00009090cf48 = (byte *)0x0;
                        _DAT_ffff00009090cf50 = 0;
                        return 0x7018;
                      }
                      lVar9 = lVar9 + 2;
                    } while ((ulong)uVar4 << 1 != lVar9);
                  }
                  uVar6 = FUN_ffff0000907afb9c(&DAT_ffff00009090d360,uVar11,&DAT_ffff00009090cf58);
                }
                uVar11 = _DAT_ffff00009090d158;
                if ((int)uVar6 != 0) {
                  return uVar6;
                }
                iVar5 = FUN_ffff0000907a2b7c(_DAT_ffff00009090d158);
                if (iVar5 != 0) {
                  FUN_ffff00009077ee50(&DAT_ffff00009090ccc0,0,0x200);
                  FUN_ffff0000907ad234(&DAT_ffff00009090ccc0,&DAT_ffff00009090cf58,uVar11);
                  _DAT_ffff00009090cec0 = uVar11;
                  if (1 < uVar11) {
                    uVar6 = (ulong)(uVar11 >> 1);
                    puVar10 = &DAT_ffff00009090ccc0;
                    do {
                      uVar11 = uVar11 - 1;
                      uVar6 = uVar6 - 1;
                      uVar1 = *puVar10;
                      *puVar10 = (&DAT_ffff00009090ccc0)[uVar11];
                      (&DAT_ffff00009090ccc0)[uVar11] = uVar1;
                      puVar10 = puVar10 + 1;
                    } while (uVar6 != 0);
                  }
                  _DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)*(uint *)(param_1 + 0x34));
                  _DAT_ffff00009090cc78 = (ulong)(param_2 - *(uint *)(param_1 + 0x34));
                  uVar6 = FUN_ffff0000907b216c();
                  if ((int)uVar6 != 0) {
                    return uVar6;
                  }
                  _DAT_ffff00009090cc78 = (ulong)_DAT_ffff00009090cc80;
                  _DAT_ffff00009090cc68 = (byte *)_DAT_ffff00009090cc70;
                  uVar6 = FUN_ffff0000907b0160(param_1,param_2,0xf);
                  if ((int)uVar6 != 0) {
                    return uVar6;
                  }
                  if (DAT_ffff000090837ed0 == 0x2000001) {
                    uVar6 = FUN_ffff0000907b0160(param_1,param_2,0x10);
                    return uVar6;
                  }
                  return uVar6;
                }
                uVar11 = 0x1026;
                goto LAB_ffff0000907b1d18;
              }
            }
            _DAT_ffff00009090cf48 = _DAT_ffff00009090cf48 + uVar6;
            if (_DAT_ffff00009090cf50 == 0) {
              _DAT_ffff00009090cf48 = (byte *)0x0;
              _DAT_ffff00009090cf50 = 0;
              return 0x7014;
            }
          }
          FUN_ffff00009077e11c
                    (s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
        }
      }
    }
    _DAT_ffff00009090cf48 = (byte *)0x0;
    _DAT_ffff00009090cf50 = 0;
    return 0x7014;
  }
  uVar11 = *(uint *)(param_1 + 0x34);
  local_68 = (byte *)0x0;
  local_6c = 0;
  _DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)uVar11);
  _DAT_ffff00009090cc78 = (ulong)(param_2 - uVar11);
  if ((uVar11 <= param_2 && param_2 - uVar11 != 0) &&
     (uVar4 = FUN_ffff0000907b05cc(&local_68,&local_6c), uVar4 == 0)) {
    _DAT_ffff00009090cc78 = (ulong)local_6c;
    _DAT_ffff00009090cc68 = local_68;
    uVar4 = FUN_ffff0000907b0d14();
    if (uVar4 == 0) {
      uVar7 = *(undefined4 *)(lVar9 + 0x410);
      goto LAB_ffff0000907b1d44;
    }
  }
  return (ulong)uVar4;
}


